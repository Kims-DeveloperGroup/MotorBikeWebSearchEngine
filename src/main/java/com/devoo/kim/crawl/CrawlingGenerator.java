package com.devoo.kim.crawl;

import com.devoo.kim.context.Contexts;
import com.devoo.kim.storage.Storage;
import com.devoo.kim.storage.data.CrawlData;
import com.devoo.kim.storage.exception.StorageException;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;


/**
 * @Responsibility: Generates Crawling tasks(extends com.devoo.kim.task.craw.Crawling) by reading 'CrawlDataFile-s' from 'Storage'
 * @NOTE: This 'CrawlingGenerator' generates only 'Crawling' tasks, but not parsing and injection.
 */
public class CrawlingGenerator extends Thread {// TODO: Handle Multi-Thread Issue (not supported yet)
    private Map<String, Storage> storages;
    private BlockingQueue<Crawling> crawlingQueue;
    private static int capacity =1000;
    // TODO: Log:  Count total size of output and Running Time.

    /**
     * @param storages ,from which instances of 'CrawlData' are loaded.
     * @param size ,of which tasks wait in the 'CrawlingQueue'
     */
    public CrawlingGenerator(HashMap<String, Storage> storages, int size){
        this.storages =storages;
        this.crawlingQueue = new LinkedBlockingDeque<>(size);
    }

    public CrawlingGenerator(HashMap<String, Storage> storages){
        this(storages, capacity);
    }

    /**
     * Generates 'CrawlingTask-s' and enqueue them into 'TaskQueue'. 'CrawlingScheduler' will take 'Crawling' tasks from 'crawlingQueue' and will run tasks.
     * The inputs of Tasks, 'CrawlData-s' are loaded from Storage, which possibly causes delay to load data/files from physical storage.
     */
    @Override
    public void run() {
        String key;
        Storage storage;
        Iterator<CrawlData> crawlDatas;// iteration 'CrawlData-s' from a loaded 'Storage'
        CrawlData crawlData;
        Crawling crawlTask;
        for (Map.Entry<String, Storage> storageEntry : storages.entrySet()){
            key = storageEntry.getKey();
            storage = storageEntry.getValue();
            try{
                crawlDatas=storage.getCrawlData().iterator();
                while (crawlDatas.hasNext()){
                    crawlData = crawlDatas.next();
                    crawlTask = Contexts.generateCrawling(crawlData);
                    crawlingQueue.put(crawlTask);
                }
            }catch (StorageException e) {continue;}
             catch (InterruptedException e) {}
        }
        System.out.println("All generated!");

    }

    public BlockingQueue<Crawling> getCrawlingQueue(){
        return this.crawlingQueue;
    }
    public boolean isEmpty(){
        return crawlingQueue.isEmpty();
    }
    /**
     * @return the current count of generated Crawling-s.
     */
    public int size(){return crawlingQueue.size();}

    /**
     * @return total capacity of Crawling Queue
     */
    public int getCapacity(){return capacity;}

    /***
     * By calling this method, outside objects monitor the status of this.
     * @return the current status of this 'CrawlingGenerator'
     */
}
