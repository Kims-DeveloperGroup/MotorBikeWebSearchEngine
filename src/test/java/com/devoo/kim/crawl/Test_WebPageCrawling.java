package com.devoo.kim.crawl;

import com.devoo.kim.crawl.schedule.CrawlingScheduler;
import com.devoo.kim.storage.exception.InvaildStorageException;
import com.devoo.kim.task.listener.TaskListener;
import com.devoo.kim.testscenerio.TestCase3_1kWebCrawlingTasks;
import org.junit.Assert;
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

    /*******************************/

//    @Test
    public void Test_Async_CrawlingSchedule()
            throws InvaildStorageException, MalformedURLException, InterruptedException {
        int expected =100;
        int acutal;
        final AtomicInteger count =new AtomicInteger(0);

        CrawlingScheduler scheduler = new CrawlingScheduler(new TaskListener() {
            @Override
            public void completeTask(Future result) {

                count.incrementAndGet();
            }

        }, generateTestCrawlingTasks(expected));
        scheduler.submitTasks();
        scheduler.shutdown();
        acutal= count.get();

        Assert.assertTrue(expected == acutal);
    }

    public static void main(String[] args) throws InvaildStorageException, InterruptedException, MalformedURLException {
        Test_WebPageCrawling test = new Test_WebPageCrawling();
        test.Test_Async_CrawlingSchedule();
    }

}
