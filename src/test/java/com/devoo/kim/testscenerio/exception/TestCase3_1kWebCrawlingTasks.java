package com.devoo.kim.testscenerio.exception;

import com.devoo.kim.crawl.CrawlingGenerator;
import com.devoo.kim.crawl.schedule.CrawlingScheduler;
import com.devoo.kim.storage.Storage;
import com.devoo.kim.storage.StorageLoader;
import com.devoo.kim.storage.exception.InvaildStorageException;
import com.devoo.kim.storage.fs.local.LocalFileSystem;
import com.devoo.kim.task.listener.TaskListener;
import org.junit.Test;

import java.util.HashMap;
import java.util.concurrent.Future;

/**
 * Created by devoo-kim on 16. 11. 23.
 */
public class TestCase3_1kWebCrawlingTasks {
    public static String STORAGE_PATH ="src/test/resources/testcase3/";

    @Test
    public void Test_Async_CrawlingSchedule() throws InvaildStorageException {
        int a =0;
        CrawlingScheduler scheduler = new CrawlingScheduler(new TaskListener() {
            @Override
            public void completeTask(Future result) {
                //Process completed or canceled tasks.
            }

        }, generateCrawlingTasks());
    }

    public CrawlingGenerator generateCrawlingTasks() throws InvaildStorageException {
        HashMap<String,Storage> storages = new HashMap<>(1);
        Storage storage = new LocalFileSystem();
        storage.connect(STORAGE_PATH);
        storages.put(STORAGE_PATH, storage);
        CrawlingGenerator gen = new CrawlingGenerator(storages);
        return gen;
    }


}
