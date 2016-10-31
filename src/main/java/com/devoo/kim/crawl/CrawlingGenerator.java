package com.devoo.kim.crawl;

import com.devoo.kim.context.Contexts;
import com.devoo.kim.crawl.exception.CrawlingTaskException;
import com.devoo.kim.storage.Storage;
import com.devoo.kim.storage.data.CrawlData;
import com.devoo.kim.storage.exception.StorageException;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * @Responsibility: Generates Crawling tasks(extends com.devoo.kim.task.craw.Crawling) by reading 'CrawlDataFile-s' from 'Storage'
 * @NOTE: This 'CrawlingGenerator' generates only 'Crawling' tasks, but not parsing and injection.
 */
public class CrawlingGenerator extends Thread {// TODO: Handle Multi-Thread Issue (not supported yet)
    int maxTasks =-1;
    private AtomicInteger status =new AtomicInteger(Crawler.NOT_INITIALLZED); // TODO: 16. 10. 20 Find out whether 'AtomicInterger' is appropriate to guarantee a consistent monitor of status for CrawlingGenerator.
    private Map<String, Storage> storages;
    private BlockingQueue<Crawling> crawlingQueue;
    // TODO: Log:  Count total size of output and Running Time.

    /**
     *
     * @param storages ,from which instances of 'CrawlData' are loaded.
     * @param maxTasks ,of which tasks wait in the 'CrawlingQueue'
     */

    public CrawlingGenerator(HashMap<String, Storage> storages, int maxTasks){
        this(storages);
        this.maxTasks = maxTasks;
    }

    public CrawlingGenerator(HashMap<String, Storage> storages){
        this.storages =storages;
        this.crawlingQueue = new LinkedBlockingDeque<>();
        this.status.set(Crawler.READY);/**#READY**/
    }

    /**
     * Generates 'CrawlingTask-s' and enqueue them into 'TaskQueue'. 'CrawlingScheduler' will take 'Crawling' tasks from 'crawlingQueue' and will run tasks.
     * The inputs of Tasks, 'CrawlData-s' are loaded from Storage, which possibly causes delay to load data/files from physical storage.
     */
    @Override
    public synchronized void start() {
        String key;
        Storage storage;
        Iterator<CrawlData> crawlDatas;// iteration 'CrawlData-s' from a loaded 'Storage'
        CrawlData crawlData;
        Crawling crawlTask;
        for (Map.Entry<String, Storage> storageEntry : storages.entrySet()){
            this.status.set(Crawler.RUNNING); /**#RUNNING**/
            key = storageEntry.getKey();
            storage = storageEntry.getValue();
            try{
                this.status.set(Crawler.DELAYED);/**#DELAYED(for loading 'CrawlData-s')**/
                crawlDatas=storage.getCrawlData().iterator();
                while (crawlDatas.hasNext()){
                    this.status.set(Crawler.RUNNING); /**#RUNNING**/
                    try {
                        crawlData = crawlDatas.next();
                        crawlTask = Contexts.generateCrawling(crawlData);
                        this.status.set(Crawler.WAITING);
                        crawlingQueue.put(crawlTask);
                        /**#WAITING (Being blocked until crawlingQueue is available to put).
                         Possibly Deadlock (crawlingQueue is being taken && crawlingQueue)**/
                    }catch (CrawlingTaskException e){continue;}
                }
            }catch (StorageException e) {continue;}
             catch (InterruptedException e) {}

        }
        this.status.set(Crawler.COMPLETE);/**#COMPLETE**/
    }

    public BlockingQueue<Crawling> getCrawlingQueue(){
        return this.crawlingQueue;
    }

    /***
     * By calling this method, outside objects monitor the status of this.
     * @return the current status of this 'CrawlingGenerator'
     */
    public AtomicInteger getStatus(){
        return this.status;
    }
}
