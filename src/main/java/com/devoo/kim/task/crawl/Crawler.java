package com.devoo.kim.task.crawl;

import com.devoo.kim.schedule.TaskScheduler;
import com.devoo.kim.storage.StorageLoader;
import com.devoo.kim.storage.data.WebPage;
import com.devoo.kim.task.Task;
import com.devoo.kim.task.TaskGenerator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by devoo-kim on 16. 10. 14.
 */
public class Crawler {
    String[] inputPath;
    String[] outputPath;
    TaskScheduler<WebPage> taskScheduler;
    TaskGenerator taskGen;
    private BlockingQueue<Task> taskQueue = new LinkedBlockingQueue<>(16); // TODO: 16. 10. 17  Configure capacity of TaskQueue to balance task load
    StorageLoader storageLoader = new StorageLoader();

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

    public static void main(String[] args){
        try {
            Files.createFile(Paths.get("createFile"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
