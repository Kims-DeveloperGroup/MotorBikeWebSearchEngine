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

    final String basePath ="src/test/resources/scenario2/";
    final String localStoragePath1 ="src/test/resources/scenario2/localstorage1/";
    final public Storage<File> localStorage1;
    public HashMap<String, Storage> storages = new HashMap<>(4);
    final String testFileName ="testUrls.url";
    final File testUrlFile = new File(localStoragePath1+testFileName);
    final Path testUrlPath = testUrlFile.toPath();
    public final int numberOfTestUrls = 1000;
    public CrawlData[] testCrawlData = new CrawlData[numberOfTestUrls];

    public TestScenario2() throws InvaildStorageException {
        localStorage1 = Contexts.generateStorageConnection("file", localStoragePath1);
        storages.put(localStoragePath1, localStorage1);
    }

//    @BeforeClass
    public void setTestUrlFile() throws IOException {
        if (!testUrlFile.exists()||
                (testUrlFile.exists() && !testUrlFile.isFile())){
            if (testUrlFile.exists()) testUrlFile.delete();
            testUrlFile.createNewFile();

            BufferedWriter writer = new BufferedWriter(new FileWriter(testUrlFile));
            StringBuilder builder = new StringBuilder();
            for (int i =0; i<numberOfTestUrls; i++){
                builder.append("http://www.scenario2.testurl").append(i)
                        .append(".com/\n");
            }
            writer.write(builder.toString());
            writer.close();
            testUrlFile.setReadOnly();
        }
    }
    private void setStorages(){
        storages.put(localStoragePath1,localStorage1);
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
