package com.devoo.kim.crawl;

import com.devoo.kim.storage.exception.InvaildStorageException;
import com.devoo.kim.testscenerio.exception.TestException;
import com.devoo.kim.testscenerio.exception.TestScenario2;
import org.junit.Test;

import java.util.concurrent.BlockingQueue;

import static org.junit.Assert.assertTrue;

/**
 * Created by devoo-kim on 16. 10. 31.
 */
public class TestCrawlingTaskGeneration extends TestScenario2{

    public TestCrawlingTaskGeneration() throws InvaildStorageException {
        super();
    }

    @Test
    public void TestCrawlTaskGenerationWithInput() throws TestException, InterruptedException {
        int count=0;
        CrawlingGenerator crawlingGenerator = new CrawlingGenerator(storages);
        crawlingGenerator.start();
//        crawlingGenerator.run();
        BlockingQueue<Crawling> crawlings = crawlingGenerator.getCrawlingQueue();
        crawlingGenerator.join();

        assertTrue(count == numberOfTestUrls);
    }

    private int takeItems(BlockingQueue<Crawling> crawlings) throws TestException {
        int totalTaken =0;
        while (totalTaken < numberOfTestUrls){
            try {
                crawlings.take();
                totalTaken++;
            } catch (InterruptedException e) {
                throw new TestException();
            }
        }
        return totalTaken;
    }

    public static void main(String[] args) {
        TestCrawlingTaskGeneration test = null;
        try {
            test = new TestCrawlingTaskGeneration();
        } catch (InvaildStorageException e) {
            e.printStackTrace();
        }
        try {
            test.TestCrawlTaskGenerationWithInput();
        } catch (TestException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
