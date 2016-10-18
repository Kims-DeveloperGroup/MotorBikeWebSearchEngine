package com.devoo.kim.task;

import com.devoo.kim.storage.Storage;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

/**
 * Created by devoo-kim on 16. 10. 14.
 */
public class TaskGenerator<T1> extends Thread {// TODO: Handle Multi-Thread Issue
    int threads =-1;
    private Map<String, Storage> storages;
    private BlockingQueue<Task> taskQueue;


    public TaskGenerator(HashMap<String, Storage> storages, BlockingQueue<Task> taskQueue, int threads){
        this(storages, taskQueue);
        this.threads = threads;
    }

    public TaskGenerator(HashMap<String, Storage> storages, BlockingQueue<Task> taskQueue){
        this.storages =storages;
        this.taskQueue = taskQueue;
    }

    @Override
    public void run() {

        for (Map.Entry<String, Storage> storageEntry : storages.entrySet()){
            String key = storageEntry.getKey();
            Storage storage = storageEntry.getValue();
            if (!storage.isValid()){
                // TODO: 16. 10. 18 Log: Metadata of Invalid Storage
                continue;
            }
            try{
                storage.load();
                while (true){

                    Task newTask;

                }
            }catch (Exception e){}

        }
        // TODO: 16. 10. 15 Retrieve urls to be processed from Sources(Storage Instances)
        // TODO: 16. 10. 15 Make them tasks
        // TODO: 16. 10. 15 Enqueue them to provide Tasks to be run by TaskScheduler
    }
}
