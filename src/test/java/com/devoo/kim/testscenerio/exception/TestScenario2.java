package com.devoo.kim.testscenerio.exception;

import com.devoo.kim.context.Contexts;
import com.devoo.kim.storage.Storage;
import com.devoo.kim.storage.data.CrawlData;
import com.devoo.kim.storage.data.WebPage;
import com.devoo.kim.storage.exception.InvaildStorageException;
import org.junit.BeforeClass;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by devoo-kim on 16. 10. 31.
 */
public class TestScenario2 {

    private static final String BASE_PATH ="src/test/resources/scenario2/";
    private static final String LOCAL_STORAGE_PATH_1 ="src/test/resources/scenario2/localstorage1/";
    public static Storage<File> LOCAL_STORAGE_1;
    public static final HashMap<String, Storage> STORAGES = new HashMap<>(4);

    private final String testFileName ="testUrls.url";
    private final File testUrlFile = new File(LOCAL_STORAGE_PATH_1 +testFileName);
    private final Path testUrlPath = testUrlFile.toPath();

    public final int TOTAL_URLS = 1000;
    public CrawlData[] testCrawlData = new CrawlData[TOTAL_URLS];

    static {
        try {
            LOCAL_STORAGE_1 =
                    Contexts.generateStorageConnection("file", LOCAL_STORAGE_PATH_1);
            STORAGES.put(LOCAL_STORAGE_PATH_1, LOCAL_STORAGE_1);
        } catch (InvaildStorageException e) {}
    }


//    @BeforeClass
    public void setTestUrlFile() throws IOException {
        if (!testUrlFile.exists()||
                (testUrlFile.exists() && !testUrlFile.isFile())){
            if (testUrlFile.exists()) testUrlFile.delete();
            testUrlFile.createNewFile();

            BufferedWriter writer = new BufferedWriter(new FileWriter(testUrlFile));
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i< TOTAL_URLS; i++){
                builder.append("http://www.scenario2.testurl").append(i)
                        .append(".com/\n");
            }
            writer.write(builder.toString());
            writer.close();
            testUrlFile.setReadOnly();
        }
    }
    private void setStorages(){
        STORAGES.put(LOCAL_STORAGE_PATH_1, LOCAL_STORAGE_1);
    }

    private void generateTestCrawlDatas() throws IOException {
        Iterator<String> urls = Files.readAllLines(testUrlPath).iterator();

        for (CrawlData crawlData : testCrawlData){
            crawlData = new WebPage(urls.next());
        }
    }


    public static void main(String[] args) throws IOException {
    }
}
