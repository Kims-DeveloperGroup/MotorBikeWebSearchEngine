package com.devoo.kim.crawl;

import com.devoo.kim.testscenerio.exception.TestFailureException;
import com.devoo.kim.testscenerio.TestCase2_TwoLocalStorages_1kUrls;
import org.junit.Test;

import java.lang.Thread.State;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.junit.Assert.assertTrue;

/**
 * Created by devoo-kim on 16. 10. 31.
 */
public class Test_CrawlingTaskGeneration_WithoutExceptionTwoLocalStorages1kUrls extends TestCase2_TwoLocalStorages_1kUrls {
    Logger logger = LoggerFactory.getLogger(Test_CrawlingTaskGeneration_WithoutExceptionTwoLocalStorages1kUrls.class);



    @Test
    public void Test_Unlimited_QueueCapacity() throws TestFailureException, InterruptedException {
        int acutal=0;
        int expected = TOTAL_URLS;
        CrawlingGenerator generator = new CrawlingGenerator(STORAGES);
        generator.start();
        BlockingQueue<Crawling> crawlQue = generator.getCrawlingQueue();
        generator.join();
        acutal =crawlQue.size();
        assertTrue(acutal == TOTAL_URLS);
    }

    @Test
    public void Test_Limited_QueueCapacity() throws TestFailureException, InterruptedException {
        int count=0;
        int capacity =500;
        State acutal;
        State expected=State.WAITING;
        CrawlingGenerator generator = new CrawlingGenerator(STORAGES, 500);
        BlockingQueue<Crawling> crawlQue =generator.getCrawlingQueue();
        generator.start();
        while (crawlQue.size() <500){
            Thread.sleep(100);
        }
        count =crawlQue.size();
        acutal = generator.getState();
        assertTrue( acutal == expected);
        generator.interrupt();
    }

    @Test
    public void Test_BeingConsumed()throws TestFailureException, InterruptedException {
        int capacity =500;
        int actual=0;
        int expected = TOTAL_URLS;
        CrawlingGenerator generator = new CrawlingGenerator(STORAGES, capacity);
        BlockingQueue<Crawling> crawlQue = generator.getCrawlingQueue();
        generator.start();
        actual =consumesCrawlingTasks(generator);
        assertTrue(actual == expected);
    }

/***********************************************************************************************************************/

    /**
     * Consumes crawling tasks from a given queue.
     * @param generator
     * @return
     * @throws TestFailureException
     */
    private int consumesCrawlingTasks(CrawlingGenerator generator) throws TestFailureException {
        BlockingQueue<Crawling> crawlQue =generator.getCrawlingQueue();
        int consumption =0;
        int waiting =0; //Micro Second
        while (generator.isAlive() || !generator.isEmpty()){
            Crawling crawling;
            try {
                System.out.println(generator.getState().name());
                crawling =crawlQue.poll(1, TimeUnit.MICROSECONDS);//Re-check state of generator
                if (crawling!=null) {
                    logger.info("Consuming {} items", consumption);
                    consumption++;
                    waiting=0;
                }
                else waiting++;
            } catch (InterruptedException e) {
                throw new TestFailureException();
            }
        }
        logger.info("{} items were consumed!", consumption);
        return consumption;
    }

    public static void main(String[] args){
        Test_CrawlingTaskGeneration_WithoutExceptionTwoLocalStorages1kUrls test
                = new Test_CrawlingTaskGeneration_WithoutExceptionTwoLocalStorages1kUrls();
        try {
            test.Test_BeingConsumed();
        } catch (TestFailureException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
