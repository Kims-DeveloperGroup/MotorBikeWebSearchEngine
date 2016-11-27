package com.devoo.kim.testscenerio;

import com.devoo.kim.crawl.Crawling;
import com.devoo.kim.crawl.WebCrawling;
import com.devoo.kim.storage.data.WebPage;
import com.devoo.kim.storage.exception.InvaildStorageException;

import java.net.MalformedURLException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by devoo-kim on 16. 11. 23.
 */
public class TestCase3_1kWebCrawlingTasks {
    public static String STORAGE_PATH ="src/test/resources/testcase3/";

    /**
     * Generates temporary crawling tasks with non-existing urls.
     * @param size of tasks to be generated into a queue.
     * @return a queue containing a given size of crawling tasks.
     *
     * @throws InvaildStorageException
     * @throws MalformedURLException
     * @throws InterruptedException
     */

    public BlockingQueue<Crawling> generateTestCrawlingTasks(int size)
            throws InvaildStorageException, MalformedURLException, InterruptedException {
        BlockingQueue<Crawling> queue =new LinkedBlockingQueue<>(size);
        String prefixUrl ="http://www.testcase3.com/";
        StringBuilder url =new StringBuilder(prefixUrl);
        int last = prefixUrl.length();

        for (int i=0; i<size; i++){
            url.replace(last, url.length(), String.valueOf(i));
            WebCrawling crawling = new WebCrawling(new WebPage(
                    url.toString()));
            if(!queue.offer(crawling, 1L, TimeUnit.SECONDS)) break;
        }
        return queue;
    }
}
