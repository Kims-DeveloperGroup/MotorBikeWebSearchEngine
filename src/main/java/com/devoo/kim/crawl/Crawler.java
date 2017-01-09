package com.devoo.kim.crawl;

import com.devoo.kim.task.listener.TaskListener;
import com.devoo.kim.crawl.schedule.CrawlingScheduler;
import com.devoo.kim.storage.StorageLoader;
import com.devoo.kim.storage.data.CrawlData;
import com.devoo.kim.storage.data.WebPage;
import com.devoo.kim.storage.fs.CrawlDataFile;
import com.devoo.kim.task.Task;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by devoo-kim on 16. 10. 14.
 */
public class Crawler implements TaskListener<CrawlData>{

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
    StorageLoader storageLoader;
    int statusofLoader;
    CrawlingScheduler crawlingScheduler;
    int statusOfScheduler;
    CrawlingGenerator crawlingGenerator;
    int statusOfGenerator;
    private BlockingQueue<Task> taskQueue = new LinkedBlockingQueue<>(16); // TODO: 16. 10. 17  Configure capacity of TaskQueue to balance task load
    List<CrawlDataFile> outputFiles = new LinkedList<>();
    BlockingQueue<CrawlData> results = new LinkedBlockingQueue(100);

    public Crawler(String[] inputPath, String outputPath){
        this.inputPath = inputPath;
        this.outputPath= outputPath;
        // TODO: Required to Set Configuration of Crawler and Crawling CrawlingGenerator
    }

    public void run(){
        try {
            storageLoader = StorageLoader.initialize(inputPath);
            crawlingGenerator = new CrawlingGenerator(storageLoader.getStorages());
            crawlingGenerator.start();
            // TODO: 17. 1. 9 Make an independant module of a listener
            crawlingScheduler = new CrawlingScheduler(this, crawlingGenerator);
            crawlingScheduler.submitTasks();
                // TODO: Re-create CrawlingGen & CrawlingScheduler if they are not alive.
                // TODO: Find exit a exit point of loop if CrawlGen is not alive and CrawlingScheduler is terminated.
                // TODO: if CrawlGen is alive and CrawlingScheduler is terminated, restart it.
        } catch (Exception e) { return;}
    }

    /***
     *
     * @param future passed from AsyncTaskWatcher
     */
    @Override
    public void completeTask(Future<CrawlData> future) {
        try {
            Collection collection =null;
            CrawlData result = future.get();
            if (results.remainingCapacity()==0){
                long uniqueNo = new Date().getTime();
                StringBuilder pathBuilder = new StringBuilder(outputPath)
                        .append(File.separatorChar).append("crawl-").append(uniqueNo);
                CrawlDataFile crawlDataFile =new CrawlDataFile(pathBuilder.toString());
                results.drainTo(collection);
                crawlDataFile.add(collection);
                crawlDataFile.createNewFile();
            }else
                results.put(result);
        }catch (IOException io){  // TODO: 16. 10. 24 Handle Exception and aggregate Exception Catch Block as small as possible
//            results.addAll(collection);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param args
     */
    public static void main(String[] args){}
}
