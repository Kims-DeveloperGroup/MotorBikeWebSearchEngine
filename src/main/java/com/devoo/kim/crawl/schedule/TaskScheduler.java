package com.devoo.kim.crawl.schedule;

import com.devoo.kim.crawl.Crawler;
import com.devoo.kim.crawl.event.TaskListener;
import com.devoo.kim.task.Task;

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
    TaskListener taskListener;

    public TaskScheduler(TaskListener taskListener, BlockingQueue<Task> taskQueue, int threads){        this.startTime = System.currentTimeMillis();
        this.threads = Runtime.getRuntime().availableProcessors();
        this.executorService = Executors.newFixedThreadPool(threads);
        this.taskQueue =taskQueue;
        this.taskListener=taskListener;
        submissions = new LinkedBlockingQueue<>(threads+2); //// TODO: 16. 10. 17 Find out the appropriate number of tasks to be submitted.
    }

    public TaskScheduler(TaskListener listener, BlockingQueue<Task> taskQueue){
        this(listener, taskQueue, Runtime.getRuntime().availableProcessors());
    }


    public void submitTasks(){ /**Incomplete yet***/
        Callable task;
        Future future;
        while (true){
            try {
                task =taskQueue.take(); /**Blocked until the taskQueue in available. (Deadlock if taskQue isEmpty && No more being put)**/
                future=executorService.submit(task);
                submissions.put(future); /**Possibly Blocked (=> Limit the number of running task)**/
                // TODO: 16. 10. 17 Monitor Future instances in submissions whether it is complete or not. If it's complete, remove and process
                // TODO: 16. 10. 17 Implement a module to take complete future from submission in order to make space to add.
            } catch (InterruptedException e) {}
        }
    }

    public void shutdown(){
        //TODO: Terminate this and kill working thread properly.(Revised to be tested)
        //TODO: is called when 'Submissions'.isEmpty() && taskQueue.isEmpty() && TaskGen.status== 'Complete/Terminate'
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

    /***
     * Check whether this 'TaskScheduler' is shutdown or not.
     *
     * @return true if 'TaskScheduler' is shut down.
     */
    public void monitorTask(){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                /***
                 * Monitor 'Submissions' if there exists any completed 'Future(submitted Task)'.
                 * If it does, call listener.completeTask(Future result) and pass the result of a Task.
                 */
            }
        };

    }

    public int getNumberOfThreads() {
        return threads;
    }
}
