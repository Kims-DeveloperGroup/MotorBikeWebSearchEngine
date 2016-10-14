package com.devoo.kim.task;

import com.devoo.kim.schedul.Scheduler;
import com.devoo.kim.storage.Storage;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by devoo-kim on 16. 10. 14.
 */
public class Tasks<T1> {// TODO: Handle Multi-Thread Issue
    int maxTasks;
    private Scheduler<T1> scheduler;
    private BlockingQueue<Task<T1>> taskQueue = new LinkedBlockingQueue<>();
    private Storage source;

    public Tasks(Scheduler<T1> scheduler,int maxTasks){
        this.scheduler = scheduler;
        this.maxTasks = maxTasks;
    }
    public Tasks(Scheduler<T1> scheduler, Storage source, int maxTasks){
        this.scheduler = scheduler;
        this.maxTasks = maxTasks;
        this.source=source;
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
