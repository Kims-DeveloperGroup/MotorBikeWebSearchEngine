package com.devoo.kim.task;

import com.devoo.kim.schedul.Scheduler;
import com.devoo.kim.storage.Storage;

import java.io.File;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by devoo-kim on 16. 10. 14.
 */
public class TaskGenerator<T1> extends Thread {// TODO: Handle Multi-Thread Issue
    int maxTasks;
    private Scheduler<T1> scheduler;
    private BlockingQueue<Task<T1>> taskQueue = new LinkedBlockingQueue<>();
    private Storage source;


    public TaskGenerator(Scheduler<T1> scheduler, Storage source, int maxTasks){
        this(scheduler, maxTasks);
        this.source=source;
    }

    public TaskGenerator(Scheduler<T1> scheduler, int maxTasks){
        this.scheduler =scheduler;
        this.maxTasks = maxTasks;
    }

    public TaskGenerator(Scheduler<T1> scheduler){
        this.scheduler = scheduler;
        this.maxTasks = scheduler.getThreadNumber();
    }

    @Override
    public void run() {
        // TODO: 16. 10. 15 Retrieve urls to be processed from Sources
        // TODO: 16. 10. 15 Make them tasks
        // TODO: 16. 10. 15 Enqueue them to provide Tasks to be run by Scheduler
    }

    public void addTask(Task<T1> task){ /**Blocked**/
        try {
            taskQueue.put(task);
        } catch (InterruptedException e) {}
    }

    public void submitTask(){ /**Blocked**/
        try {
            scheduler.submitTask(taskQueue.take()); // TODO: Implements 'DI of Scheduler'
        } catch (InterruptedException e) {}
    }
}
