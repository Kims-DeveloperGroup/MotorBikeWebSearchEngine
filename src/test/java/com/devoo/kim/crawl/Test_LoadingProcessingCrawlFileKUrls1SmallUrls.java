package com.devoo.kim.crawl;

import com.devoo.kim.storage.Storage;
import com.devoo.kim.storage.data.CrawlData;
import com.devoo.kim.storage.exception.InvaildStorageException;
import com.devoo.kim.storage.exception.InvalidStorageLoaderException;
import com.devoo.kim.storage.exception.StorageLoadException;
import com.devoo.kim.testscenerio.TestCase1_FourLocalStorages_SmallUrls;
import org.junit.Test;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by devoo-kim on 16. 10. 30.
 */
public class Test_LoadingProcessingCrawlFileKUrls1SmallUrls extends TestCase1_FourLocalStorages_SmallUrls {

    @Test
    public void TestLoadCrawlingDataFileFromLocalFileSystem1_Positive() throws StorageLoadException, InvaildStorageException {
        Storage<File> localStorage1 = loadTestLocalFileSystem1();
        List<CrawlData> crawlDatas= localStorage1.getCrawlData();
        assertTrue(crawlDatas.size()==totalCountOfUrlsFrom1);
    }

    @Test
    public void TestLoadCrawlingDataFileFromLocalFileSystem1_Negative() throws StorageLoadException, InvaildStorageException {
        Storage<File> localStorage1 = loadTestLocalFileSystem1();
        List<CrawlData> crawlDatas= localStorage1.getCrawlData();
        assertFalse(crawlDatas.size()!=totalCountOfUrlsFrom1);
    }

    @Test
    public void TestLoadCrawlingDataFileFromLocalFileSystem1234_Positive() throws InvaildStorageException, InvalidStorageLoaderException {
        HashMap<String, Storage> storages =loadTestLocalFileSystem1234();
    }


    public static void main(String[] args) throws InvaildStorageException {
    }
}
