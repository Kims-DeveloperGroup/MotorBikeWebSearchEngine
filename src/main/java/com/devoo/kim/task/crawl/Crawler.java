package com.devoo.kim.task.crawl;

import com.devoo.kim.schedule.TaskScheduler;
import com.devoo.kim.storage.StorageManager;
import com.devoo.kim.storage.data.WebPage;
import com.devoo.kim.task.TaskGenerator;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Future;

/**
 * Created by devoo-kim on 16. 10. 14.
 */
public class Crawler {
    String inputPath;
    String outputPath;
    TaskScheduler<WebPage> taskScheduler;

    public Crawler(String inputPath, String outputPath){
        this.inputPath = inputPath;
        this.outputPath= outputPath;
        // TODO: Required to Set Cofiguration of Crawler and Crawling TaskGenerator
        taskScheduler = new TaskScheduler();
    }

    public void run(){
        List<String> seedUrls;
        Path path = Paths.get(inputPath);
        File input;

        try {
            // TODO: 16. 10. 16 Read input sources to generate tasks.
//            readInputSources(paths);
//            seedUrls = Files.readAllLines(path);
//            String url;
//            Iterator<String> it = seedUrls.iterator();
//            while (it.hasNext()){
//                url =it.next();
//                 TODO: 16. 10. 15 Generates tasks through TaskGenerator.class & Submit them to TaskScheduler(com.devoo.kim.schedule)
//                Future<WebPage> result = taskScheduler.submitTask(new WebCrawling("Crawling "+url, url));
//                result.get();
//            }
            TaskGenerator tasks = new TaskGenerator(readInputSources("path"), taskScheduler);
            taskScheduler
            tasks.run();


            taskScheduler.shutdown(); //be here

        } catch (Exception e) { return;}
    }
    
    private StorageManager readInputSources(String... paths){
        // TODO: 16. 10. 16 Instantiate Storage Instances(com.devoo.kim.storage) to load physical storage on memory.

        for (String path :paths){
            // TODO: 16. 10. 17 By using com.devoo.kim.storage.StorageManagers and TaskGenerator(com.devoo.kim.task)
        }
        return null;
    }

    public static void main(String[] args){
        Crawler crawler = new Crawler("seed.txt", "output.txt");
        crawler.run();
        return;
    }
}
