package com.devoo.kim.schedule;

import com.devoo.kim.task.Task;
import com.devoo.kim.task.TaskGenerator;
import com.devoo.kim.task.crawl.Crawler;

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
    BlockingQueue<Future> submissions;
    BlockingQueue<Task> taskQueue;
    Crawler crawler; //Caller of this

    public TaskScheduler(Crawler crawler, BlockingQueue<Task> taskQueue, int threads){        this.startTime = System.currentTimeMillis();
        this.threads = Runtime.getRuntime().availableProcessors();
        this.executorService = Executors.newFixedThreadPool(threads);
        this.taskQueue =taskQueue;
        this.crawler=crawler;
        submissions = new LinkedBlockingQueue<>(threads+2); //// TODO: 16. 10. 17 Find out the appropriate number of tasks to be submitted.
    }

    public TaskScheduler(Crawler crawler, BlockingQueue<Task> taskQueue){
        this(crawler, taskQueue, Runtime.getRuntime().availableProcessors());
    }
    
    public void submitTask(TaskGenerator taskGenerator){
        // TODO: 16. 10. 17 Being provided with tasks from task Generator. 
    }

    public void submitTasks(){ /**Incomplete yet***/
        Callable task;
        Future future;
        while (true){
            try {
                task =taskQueue.take(); /**Possibly Blocked**/
                future=executorService.submit(task);
                submissions.put(future); /**Possibly Blocked**/
                // TODO: 16. 10. 17 Monitor Future instances in submissions whether it is complete or not. If it's complete, remove and process
                // TODO: 16. 10. 17 Implement a module to take complete future from submission in order to make space to add.
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean canSubmit(){

    }

    public void shutdown(){
        //TODO: Terminate this and kill working thread properly.(Revised to be tested)
        try {
            if (!executorService.awaitTermination(10L, TimeUnit.SECONDS))
                executorService.shutdownNow();
            if (!executorService.awaitTermination(10L, TimeUnit.SECONDS)) //Double Check of ThreadPool and Tasks being terminated at all.
                executorService.shutdownNow();
        } catch (InterruptedException e) {
            executorService.shutdownNow(); //To handle the case of Thread  interruption.
        }
        endTime =System.currentTimeMillis();
        totalWorkingTime = endTime -startTime;
        System.out.println("Total Working Time(/sec): " + totalWorkingTime/1000+ "(sec)");// TODO: 16. 10. 15 Log
    }

    public int getThreadNumber() {
        return threads;
    }
}
