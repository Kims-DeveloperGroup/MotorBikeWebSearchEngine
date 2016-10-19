package com.devoo.kim.task.crawl;

import com.devoo.kim.context.Contexts;
import com.devoo.kim.storage.Storage;
import com.devoo.kim.storage.data.CrawlData;
import com.devoo.kim.task.Task;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

/**
 * Created by devoo-kim on 16. 10. 14.
 */
public class TaskGenerator<T1> extends Thread {// TODO: Handle Multi-Thread Issue
    int threads =-1;
    int status;
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
        /**#READY**/
    }

    /***
     * Generates 'CrawlingTask-s' and enqueue them into 'TaskQueue'
     * The inputs of Tasks, 'CrawlData-s' are loaded from Storage
     * , which possibly causes delay to laod data/files from physical storage.
     */
    @Override
    public void run() { /**#RUNNING**/
        String key;
        Storage storage;
        Iterator<CrawlData> iterator;// iteration 'CrawlData-s' from a loaded 'Storage'
        CrawlData crawlData;
        Task crawlTask;
        for (Map.Entry<String, Storage> storageEntry : storages.entrySet()){
            key = storageEntry.getKey();
            storage = storageEntry.getValue();
            if (!storage.isValid()){
                continue; // TODO: 16. 10. 18 Log: Metadata of Invalid Storage
            }
            try{
                iterator=storage.load().iterateCrawlData();/**Possibly Delayed (#WAITING)**/
                while (iterator.hasNext()){
                    crawlData= iterator.next();
                    crawlTask =Contexts.getTask(crawlData);
                    taskQueue.put(crawlTask);/**BLOCKED (#WAITING)**/
                }
            }catch (Exception e){}
        }
        /**#COMPLETE**/
    }
}
