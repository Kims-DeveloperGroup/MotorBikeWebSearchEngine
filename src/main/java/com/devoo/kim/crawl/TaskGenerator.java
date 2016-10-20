package com.devoo.kim.crawl;

import com.devoo.kim.context.Contexts;
import com.devoo.kim.storage.Storage;
import com.devoo.kim.storage.data.CrawlData;
import com.devoo.kim.task.Task;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * @Responsibility: Generates Crawling tasks(extends com.devoo.kim.task.craw.Crawling) by reading 'CrawlDataFile-s' from 'Storage'
 * @NOTE: This 'TaskGenerator' generates only 'Crawling' tasks, but not parsing and injection.
 */
public class TaskGenerator<T1> extends Thread {// TODO: Handle Multi-Thread Issue (not supported yet)
    int threads =-1;
    private AtomicInteger status =new AtomicInteger(Crawler.NOT_INITIALLZED); // TODO: 16. 10. 20 Find out whether 'AtomicInterger' is appropriate to guarantee a consistent monitor of status for TaskGenerator.
    private Map<String, Storage> storages;
    private BlockingQueue<Task> taskQueue;
    // TODO: Log:  Count total size of output and Running Time.

    public TaskGenerator(HashMap<String, Storage> storages, BlockingQueue<Task> taskQueue, int threads){
        this(storages, taskQueue);
        this.threads = threads;
    }

    public TaskGenerator(HashMap<String, Storage> storages, BlockingQueue<Task> taskQueue){
        this.storages =storages;
        this.taskQueue = taskQueue;
        this.status.set(Crawler.READY);/**#READY**/
    }

    /***
     * Generates 'CrawlingTask-s' and enqueue them into 'TaskQueue'. 'TaskScheduler' will take 'Crawling' tasks from 'taskQueue' and will run tasks.
     * The inputs of Tasks, 'CrawlData-s' are loaded from Storage, which possibly causes delay to load data/files from physical storage.
     */
    @Override
    public void run() {
        String key;
        Storage storage;
        Iterator<CrawlData> iterator;// iteration 'CrawlData-s' from a loaded 'Storage'
        CrawlData crawlData;
        Task crawlTask;
        for (Map.Entry<String, Storage> storageEntry : storages.entrySet()){
            this.status.set(Crawler.RUNNING); /**#RUNNING**/
            key = storageEntry.getKey();
            storage = storageEntry.getValue();
            if (!storage.isValid()){
                continue; // TODO: 16. 10. 18 Log: Metadata of Invalid Storage
            }
            try{
                this.status.set(Crawler.DELAYED);/**#DELAYED(for loading 'CrawlData-s')**/
                iterator=storage.load().iterateCrawlData();
                while (iterator.hasNext()){
                    this.status.set(Crawler.RUNNING); /**#RUNNING**/
                    crawlData= iterator.next();
                    crawlTask =Contexts.getTask(crawlData);
                    this.status.set(Crawler.WAITING);
                    taskQueue.put(crawlTask);/**#WAITING (Being blocked until taskQueue is available to put).
                     Possibly Deadlock (taskQueue is being taken && taskQueue)**/
                }
            }catch (Exception e){}
        }
        this.status.set(Crawler.COMPLETE);/**#COMPLETE**/
    }

    /***
     * By calling this method, outside objects monitor the status of this.
     * @return the current status of this 'TaskGenerator'
     */
    public AtomicInteger getStatus(){
        return this.status;
    }
}
