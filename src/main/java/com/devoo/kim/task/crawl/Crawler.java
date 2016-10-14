package com.devoo.kim.task.crawl;

import com.devoo.kim.schedul.Scheduler;
import com.devoo.kim.storage.data.WebPage;

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
    Scheduler<WebPage> scheduler;

    public Crawler(String inputPath, String outputPath){
        this.inputPath = inputPath;
        this.outputPath= outputPath;
        // TODO: Required to Set Cofiguration of Crawler and Crawling Tasks
        scheduler = new Scheduler();
    }

    public void run(){
        List<String> seedUrls;
        Path path = Paths.get(inputPath);
        File input;
        try {
            seedUrls = Files.readAllLines(path);
            String url;
            Iterator<String> it = seedUrls.iterator();
            while ((url =it.next())!=null){
                Future<WebPage> result =scheduler.submitTask(new WebCrawling("Crawling "+url, url));
//                WebPage page =result.get();
            }
        } catch (Exception e) { return;}
    }

    public static void main(String[] args){
        Crawler crawler = new Crawler("seed.txt", "output.txt");
        crawler.run();
    }
}
