package com.devoo.kim.crawl;

import com.devoo.kim.crawl.event.TaskListener;
import com.devoo.kim.crawl.schedule.CrawlingScheduler;
import com.devoo.kim.storage.StorageLoader;
import com.devoo.kim.storage.data.CrawlData;
import com.devoo.kim.storage.data.WebPage;
import com.devoo.kim.storage.fs.CrawlDataFile;
import com.devoo.kim.task.Task;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by devoo-kim on 16. 10. 14.
 */
public class Crawler implements TaskListener{

    public static final int NOT_INITIALLZED=0;
    public static final int READY=1;
    public static final int RUNNING=2;
    public static final int WAITING=3;
    public static final int DELAYED=4;
    public static final int STOPED=5;
    public static final int COMPLETE=6;
    public static final int FAIL=7;
    String[] inputPath;
    String outputPath;
    StorageLoader storageLoader = new StorageLoader();
    int statusofLoader;
    CrawlingScheduler<WebPage> crawlingScheduler;
    int statusOfScheduler;
    CrawlingGenerator crawlingGenerator;
    int statusOfGenerator;
    private BlockingQueue<Task> taskQueue = new LinkedBlockingQueue<>(16); // TODO: 16. 10. 17  Configure capacity of TaskQueue to balance task load
    List<CrawlDataFile> outputFiles = new LinkedList<>();
    BlockingQueue<CrawlData> results = new LinkedBlockingQueue(100);



    ;

    public Crawler(String[] inputPath, String outputPath){
        this.inputPath = inputPath;
        this.outputPath= outputPath;
        // TODO: Required to Set Cofiguration of Crawler and Crawling CrawlingGenerator
    }

    public void run(){
        try {
            storageLoader.initialize(inputPath);
            crawlingGenerator = new CrawlingGenerator(storageLoader.getStorages());
            crawlingGenerator.run();
            crawlingScheduler = new CrawlingScheduler(this, crawlingGenerator);
            crawlingScheduler.submitTasks();
        } catch (Exception e) { return;}
    }

    /**
     *
     * @param result
     */


    @Override
    public void completeTask(CrawlData result) {
        Collection collection =null;
        try {
            if (results.remainingCapacity()==0){
                long uniqueNo = new Date().getTime();
                StringBuilder pathBuilder = new StringBuilder(outputPath)
                        .append(File.separatorChar).append("crawl-").append(uniqueNo);
                CrawlDataFile crawlDataFile =new CrawlDataFile(pathBuilder.toString());
                results.drainTo(collection);
                crawlDataFile.add(collection);
            }else
            results.put(result);
        } catch (InterruptedException e) {}
        catch (Exception e) {
            results.addAll(collection);
        }
    }

    public static void main(String[] args){}
}
