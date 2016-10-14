package com.devoo.kim.schedul;

import com.devoo.kim.task.Task;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by devoo-kim on 16. 10. 14.
 */
public class Scheduler<T1> { // TODO: Hadle Multi-Thread Issue
    private int threads=-1;
    AtomicInteger currTasks = new AtomicInteger(0);
    AtomicInteger totalTasks = new AtomicInteger(0);
    private final long startTime;
    private long endTime;
    private long timeout;
    ExecutorService executorService;//// TODO: NOT Thread-Safe

    public Scheduler(){
        this(Runtime.getRuntime().availableProcessors());
    }

    public Scheduler(int threads){
        this.threads = threads;
        startTime = System.currentTimeMillis();
        executorService = Executors.newFixedThreadPool(this.threads);
    }

    public Future<T1> submitTask(Callable<T1> task){
        currTasks.incrementAndGet();
        totalTasks.incrementAndGet();
        //TODO: Required to think about whether to return complete result or to return a pending result of the task.
        return executorService.submit(task);
    }

    public void terminate(){
        //TODO: Terminate this and kill working thread at all.
    }
}
