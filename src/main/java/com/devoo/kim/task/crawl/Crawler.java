package com.devoo.kim.task.crawl;

import com.devoo.kim.schedule.TaskScheduler;
import com.devoo.kim.storage.StorageLoader;
import com.devoo.kim.storage.data.WebPage;
import com.devoo.kim.task.Task;
import com.devoo.kim.task.TaskGenerator;

import java.io.File;
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
    TaskGenerator taskGenerator;
    private BlockingQueue<Task> taskQueue = new LinkedBlockingQueue<>(16); // TODO: 16. 10. 17  Configure capacity of TaskQueue to balance task load
    StorageLoader storageLoader = new StorageLoader();

    public Crawler(String[] inputPath, String[] outputPath){
        this.inputPath = inputPath;
        this.outputPath= outputPath;
        // TODO: Required to Set Cofiguration of Crawler and Crawling TaskGenerator
        taskScheduler = new TaskScheduler();
    }

    public void run(){


        try {
            // TODO: 16. 10. 16 Read input sources to generate tasks.
            storageLoader.initialize(inputPath);
//             TODO: 16. 10. 15 Generates tasks through TaskGenerator.class & Submit them to TaskScheduler(com.devoo.kim.schedule)
            TaskGenerator tasks = new TaskGenerator(storageLoader.getStorages(), taskQueue);
            tasks.run();
        } catch (Exception e) { return;}
    }
    
    private StorageLoader readInputSources(String... paths){
        // TODO: 16. 10. 16 Instantiate Storage Instances(com.devoo.kim.storage) to initialize physical storage on memory.

        for (String path :paths){
            // TODO: 16. 10. 17 By using com.devoo.kim.storage.StorageManagers and TaskGenerator(com.devoo.kim.task)
        }
        return null;
    }

    public static void main(String[] args){
//        Crawler crawler = new Crawler({"seed.txt"}, {"output.txt"});
//        crawler.run();
        return;
    }
}
