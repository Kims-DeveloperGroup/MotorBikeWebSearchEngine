package com.devoo.kim.crawl.schedule;

import com.devoo.kim.crawl.Crawling;
import com.devoo.kim.crawl.CrawlingGenerator;
import com.devoo.kim.task.AsyncTaskWatcher;
import com.devoo.kim.task.listener.TaskListener;
import com.devoo.kim.storage.data.CrawlData;
import com.devoo.kim.task.Task;

import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 */
public class CrawlingScheduler<T1> { // TODO: Handle Multi-Thread Issue

    private int threads=-1;
    AtomicInteger currTasks = new AtomicInteger(0);
    AtomicInteger totalTasks = new AtomicInteger(0);
    private long totalWorkingTime;
    private long startTime;
    private long endTime;
    private long timeout;
    ExecutorService executorService;//// TODO: NOT Thread-Safe(?)
    WeakReference<AsyncTaskWatcher> weakTaskWatcher;
    AsyncTaskWatcher taskWatcher;
    CrawlingGenerator crawlingGenerator;
    BlockingQueue<Crawling> crawlingQueue;
    TaskListener taskListener;

    public CrawlingScheduler(TaskListener taskListener, BlockingQueue<Crawling> crawlingQueue, int threads){
        this.startTime = System.currentTimeMillis();
        this.threads = Runtime.getRuntime().availableProcessors();
        this.executorService = Executors.newFixedThreadPool(threads);
        this.crawlingQueue = crawlingQueue;
        this.taskListener=taskListener;
        taskWatcher = new AsyncTaskWatcher(taskListener, this.threads);// TODO: 16. 10. 17 Find out the appropriate number of tasks to be submitted.
    }

    public CrawlingScheduler(TaskListener listener, BlockingQueue<Crawling> crawlingQueue){
        this(listener, crawlingQueue, Runtime.getRuntime().availableProcessors());
    }

    public CrawlingScheduler(TaskListener listener, CrawlingGenerator crawlingGenerator){
        this(listener, crawlingGenerator.getCrawlingQueue(), Runtime.getRuntime().availableProcessors());
        this.crawlingGenerator =crawlingGenerator;
    }

    public void submitTasks(){
        Task task;
        Future<CrawlData> future;
        if (!crawlingGenerator.isAlive()) {
            // TODO: 16. 11. 17 Handle a Dead Generator.
        }
        taskWatcher.start();
        while (crawlingGenerator.isAlive() || !crawlingQueue.isEmpty()){
            try {
                if (!taskWatcher.isAlive()){ //Restarts taskWatcher if it's not running.
                    taskWatcher=new AsyncTaskWatcher(taskListener,threads);
                    weakTaskWatcher= new WeakReference(taskWatcher);
                }
                task = crawlingQueue.poll(1L, TimeUnit.MILLISECONDS);
                if (task == null) continue;

                future=executorService.submit(task);
                taskWatcher.put(future); /**Possibly Blocked (=> Limit the number of running task)**/
            } catch (InterruptedException e) {}
        }
        shutdown();
    }

    /***
     * Check whether this 'CrawlingScheduler' is shutdown or not.
     *
     * @return true if 'CrawlingScheduler' is shut down.
     */
    public void shutdown(){
        //TODO: Terminate this and kill working thread properly.(Not guarantee yet.)
        //TODO: is called when 'Submissions'.isEmpty() && crawlingQueue.isEmpty() && TaskGen.status== 'Complete/Terminate'
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

    /**
     * Monitor 'Submissions' if there exists any completed 'Future(submitted Task)'.
     * If it does, call listener.completeTask(Future result) and pass the result of a Task.
     */

    public int getNumberOfThreads() {
        return threads;
    }
}
