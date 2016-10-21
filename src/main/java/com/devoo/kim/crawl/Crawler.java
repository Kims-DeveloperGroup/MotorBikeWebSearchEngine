package com.devoo.kim.crawl;

import com.devoo.kim.storage.StorageLoader;
import com.devoo.kim.storage.data.WebPage;
import com.devoo.kim.task.Task;
import com.devoo.kim.crawl.schedule.TaskScheduler;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by devoo-kim on 16. 10. 14.
 */
public class Crawler {

    public static final int NOT_INITIALLZED=0;
    public static final int READY=1;
    public static final int RUNNING=2;
    public static final int WAITING=3;
    public static final int DELAYED=4;
    public static final int STOPED=5;
    public static final int COMPLETE=6;
    public static final int FAIL=7;
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
//        while ()
    }

    public static void main(String[] args){}
}
