package com.devoo.kim.crawl;

import com.devoo.kim.crawl.schedule.CrawlingScheduler;
import com.devoo.kim.storage.exception.InvaildStorageException;
import com.devoo.kim.task.listener.TaskListener;
import com.devoo.kim.testscenerio.TestCase3_1kWebCrawlingTasks;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by devoo-kim on 16. 11. 23.
 */
public class Test_WebPageCrawling extends TestCase3_1kWebCrawlingTasks{
    Logger logger = LoggerFactory.getLogger(Test_WebPageCrawling.class);

    @Test
    public void Test_Async_CrawlingSchedule()
            throws InvaildStorageException, MalformedURLException, InterruptedException {
        int expected =10;
        int actual;
        final AtomicInteger count =new AtomicInteger(0);

        CrawlingScheduler scheduler = new CrawlingScheduler(new TaskListener(){

            @Override
            public void completeTask(Future result) {
                count.incrementAndGet();
                logger.info("{} are completed.", count.intValue());
            }
        }, generateTestCrawlingTasks(expected));

        scheduler.submitTasks();
        scheduler.shutdown();
        actual= count.get();

        Assert.assertTrue(expected == actual);
    }

    public static void main(String[] args) throws InvaildStorageException, InterruptedException, MalformedURLException {
        Test_WebPageCrawling test = new Test_WebPageCrawling();
        test.Test_Async_CrawlingSchedule();
    }

}
