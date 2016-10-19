package com.devoo.kim.task.crawl;

import com.devoo.kim.task.crawl.schedule.TaskScheduler;
import com.devoo.kim.storage.StorageLoader;
import com.devoo.kim.storage.data.WebPage;
import com.devoo.kim.task.Task;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by devoo-kim on 16. 10. 14.
 */
public class Crawler {
    public static final RUNNING=;
    public static final DELAYED=;
    public static final WAITING=;
    public static final STOPED=;
    public static final READY=;
    public static final BLOCKED=;
    public static final COMPLETE=;
    String[] inputPath;
    String[] outputPath;
    StorageLoader storageLoader = new StorageLoader();
    int statusofLoader;
    TaskScheduler<WebPage> taskScheduler;
    int statusOfScheduler;
    TaskGenerator taskGen;
    int statusOfGenerator;
    private BlockingQueue<Task> taskQueue = new LinkedBlockingQueue<>(16); // TODO: 16. 10. 17  Configure capacity of TaskQueue to balance task load
;

    public Crawler(String[] inputPath, String[] outputPath){
        this.inputPath = inputPath;
        this.outputPath= outputPath;
        // TODO: Required to Set Cofiguration of Crawler and Crawling TaskGenerator
    }

    public void run(){
        try {
            storageLoader.initialize(inputPath);
            taskGen = new TaskGenerator(storageLoader.getStorages(), taskQueue);
            taskGen.run();
            taskScheduler = new TaskScheduler(this, taskQueue);
            taskScheduler.submitTasks();
        } catch (Exception e) { return;}
    }

    public static void main(String[] args){}
}
