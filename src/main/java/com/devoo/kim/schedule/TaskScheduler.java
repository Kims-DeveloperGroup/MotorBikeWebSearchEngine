package com.devoo.kim.schedule;

import com.devoo.kim.task.Task;
import com.devoo.kim.task.TaskGenerator;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by devoo-kim on 16. 10. 14.
 */
public class TaskScheduler<T1> { // TODO: Hadle Multi-Thread Issue

    private int threads=-1;
    AtomicInteger currTasks = new AtomicInteger(0);
    AtomicInteger totalTasks = new AtomicInteger(0);
    private long totalWorkingTime;
    private long startTime;
    private long endTime;
    private long timeout;
    ExecutorService executorService;//// TODO: NOT Thread-Safe

    public TaskScheduler(){
        this(Runtime.getRuntime().availableProcessors());
    }

    public TaskScheduler(int threads){
        this.threads = threads;
        startTime = System.currentTimeMillis();
        executorService = Executors.newFixedThreadPool(this.threads);
    }
    
    public void submitTask(TaskGenerator taskGenerator){
        // TODO: 16. 10. 17 Being provided with tasks from task Generator. 
    }

    public Future<T1> submitTask(Callable<T1> task){
        currTasks.incrementAndGet();
        totalTasks.incrementAndGet();
        //TODO: Required to think about whether to return complete result or to return a pending result of the task.
        return executorService.submit(task);
    }

    public void shutdown(){
        //TODO: Terminate this and kill working thread properly.(Current Problem)
        try {
            if (executorService.awaitTermination(20L, TimeUnit.SECONDS))
                System.out.println("All TaskGenerator are executed.");
            endTime =System.currentTimeMillis();
            totalWorkingTime = endTime -startTime;
            System.out.println("Total Working Time(/sec): " + totalWorkingTime/1000+ "(sec)");// TODO: 16. 10. 15 Log
            executorService.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public int getThreadNumber() {
        return threads;
    }
}
