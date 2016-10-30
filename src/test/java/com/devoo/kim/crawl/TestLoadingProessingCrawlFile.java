package com.devoo.kim.crawl;

import com.devoo.kim.context.Contexts;
import com.devoo.kim.storage.Storage;
import com.devoo.kim.storage.data.CrawlData;
import com.devoo.kim.storage.exception.InvaildStorageException;
import org.junit.Test;

import java.io.File;
import java.util.Iterator;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by devoo-kim on 16. 10. 30.
 */
public class TestLoadingProessingCrawlFile {

    @Test
    public void TestCrawlingDataFileFromLocalFileSystem() throws InvaildStorageException {
        Storage<File> localStorage1 = loadTestLocalFileSystem();
        Iterator<CrawlData> crawlDatas= localStorage1.iterateCrawlData();
        assertTrue(crawlDatas.hasNext());
    }



    public Storage<File> loadTestLocalFileSystem() throws InvaildStorageException {
        String localPath1= "/home/devoo-kim/IdeaProjects/MotorBikeWebSearchEngine/src/test/resources/localstorage1";
        Storage<File> localStorage1 = Contexts.generateStorageConnection("file",localPath1);
        return localStorage1;
    }

    public static void main(String[] args) throws InvaildStorageException {
        TestLoadingProessingCrawlFile testCases = new TestLoadingProessingCrawlFile();
        testCases.TestCrawlingDataFileFromLocalFileSystem();
    }
}
