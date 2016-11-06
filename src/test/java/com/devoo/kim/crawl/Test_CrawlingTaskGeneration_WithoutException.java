package com.devoo.kim.crawl;

import com.devoo.kim.testscenerio.exception.TestFailureException;
import com.devoo.kim.testscenerio.exception.TestScenario2;
import org.junit.Test;

import java.lang.Thread.State;
import java.util.concurrent.BlockingQueue;

import static org.junit.Assert.assertTrue;

/**
 * Created by devoo-kim on 16. 10. 31.
 */
public class Test_CrawlingTaskGeneration_WithoutException extends TestScenario2{


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
        System.out.println(generator.getState().name());
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
        while (generator.isAlive() || !generator.isEmpty()){
            try {
                System.out.println(consumption);
                System.out.println(generator.getState().name());
                crawlQue.take();//Possibly Blocekd.
                consumption++;
            } catch (InterruptedException e) {
                throw new TestFailureException();
            }
        }
        System.out.println("All consumed!");

        return consumption;
    }

    public static void main(String[] args){
        Test_CrawlingTaskGeneration_WithoutException test = new Test_CrawlingTaskGeneration_WithoutException();
        try {
            test.Test_BeingConsumed();
        } catch (TestFailureException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
