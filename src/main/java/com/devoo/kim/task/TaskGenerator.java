package com.devoo.kim.task;

import com.devoo.kim.schedule.TaskScheduler;
import com.devoo.kim.storage.Storage;
import com.devoo.kim.storage.StorageManager;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by devoo-kim on 16. 10. 14.
 */
public class TaskGenerator<T1> extends Thread {// TODO: Handle Multi-Thread Issue
    int maxTasks=-1;
    private TaskScheduler<T1> taskScheduler; //In order to monitor the status of taskScheduler receiving tasks from this.
    private BlockingQueue<Task<T1>> taskQueue = new LinkedBlockingQueue<>();
    private StorageManager storageManager;
    private Storage[] sources;

    public TaskGenerator(StorageManager sources,int maxTasks) throws Exception { //Edit Ver.
//        this(taskScheduler, maxTasks);
        this.maxTasks= maxTasks;
        this.sources=storageManager.getStorages();
    }
    public TaskGenerator(StorageManager sources, TaskScheduler taksScheduler) throws Exception { //Edit Ver.
        this.sources=storageManager.getStorages();
        this.taskScheduler = taksScheduler;
    }
    public TaskGenerator(TaskScheduler<T1> taskScheduler, int maxTasks, Storage... sources){
        this(taskScheduler, maxTasks);
        this.sources=sources;
    }

    public TaskGenerator(TaskScheduler<T1> taskScheduler, int maxTasks){
        this.taskScheduler = taskScheduler;
        this.maxTasks = maxTasks;
    }

    public TaskGenerator(TaskScheduler<T1> taskScheduler){ // TODO: 16. 10. 17 Think again whether Scheduler needs to be passed as param here 
        this.taskScheduler = taskScheduler;                // TODO: 16. 10. 17 Better pass a TaskGenerator to Scheduler. The scheduler dequeue tasks from taskQuque. 
        this.maxTasks = taskScheduler.getThreadNumber();
    }

    @Override
    public void run() {
        // TODO: 16. 10. 15 Retrieve urls to be processed from Sources(Storage Instances)
        // TODO: 16. 10. 15 Make them tasks
        // TODO: 16. 10. 15 Enqueue them to provide Tasks to be run by TaskScheduler
    }

    public void addTask(Task<T1> task){ /**Blocked**/
        try {
            taskQueue.put(task);
        } catch (InterruptedException e) {}
    }

    public void submitTask(){ /**Blocked**/
        try {
            taskScheduler.submitTask(taskQueue.take()); // TODO: Implements 'DI of TaskScheduler'
        } catch (InterruptedException e) {}
    }
}
