package com.devoo.kim.crawl.schedule;

import com.devoo.kim.crawl.Crawling;
import com.devoo.kim.crawl.CrawlingGenerator;
import com.devoo.kim.task.AsyncTaskWatcher;
import com.devoo.kim.task.listener.TaskListener;
import com.devoo.kim.storage.data.CrawlData;
import com.devoo.kim.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 */
public class CrawlingScheduler { // TODO: Handle Multi-Thread Issue
    Logger logger = LoggerFactory.getLogger(CrawlingScheduler.class);

    private int threads=-1;
    AtomicInteger currTasks = new AtomicInteger(0);
    AtomicInteger totalTasks = new AtomicInteger(0);
    private long totalWorkingTime;
    private long startTime;
    private long endTime;
    private long timeout; //todo:
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

    public CrawlingScheduler(BlockingQueue<Crawling> crawlingQueue){
        this(null, crawlingQueue, Runtime.getRuntime().availableProcessors());
    }

    @Deprecated
    public CrawlingScheduler(TaskListener listener, CrawlingGenerator crawlingGenerator){
        this(listener, crawlingGenerator.getCrawlingQueue(), Runtime.getRuntime().availableProcessors());
        this.crawlingGenerator =crawlingGenerator;
    }

    /**
     * Submits tasks(type of WebCrawling) asynchronously.
     * That Keeps polling crawling tasks from a given queue, and waiting for an item.
     *
     * @Note: if the queue is still empty even after time-out, the method is returned.
     */
    public void submitTasks(){
        Crawling crawling;
        Future<CrawlData> future;
        taskWatcher.start();

        while (true){
            try { // TODO: 16. 11. 24 Find the better to handle exception.
                if (!taskWatcher.isAlive()) { //Restarts taskWatcher if it's not running.
                    taskWatcher = new AsyncTaskWatcher(taskListener, threads);
                    taskWatcher.start();
                    weakTaskWatcher = new WeakReference(taskWatcher);
                }
                crawling = crawlingQueue.poll(10L, TimeUnit.MILLISECONDS);
                if (crawling == null) break;

                future = executorService.submit(crawling);//exception point #1
                totalTasks.incrementAndGet();
                /*** Possibly Blocked (=> Limit the number of running crawling) ***/
                taskWatcher.put(future); //exception point #2
            }catch (InterruptedException e){}
        }
        try {
            // TODO: 16. 11. 24 Find the better to handle exception.
            taskWatcher.terminates();
            taskWatcher.join(); //exception point #3
        }catch (InterruptedException e){}
    }

    /**
     * Check whether this 'CrawlingScheduler' is shutdown or not.
     * @return true if 'CrawlingScheduler' is shut down.
     */
    public void shutdown(){
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(3L, TimeUnit.SECONDS))
                executorService.shutdownNow();
        } catch (InterruptedException e) {
        }finally {
            executorService.shutdownNow(); //To handle the case of Thread  interruption.
            endTime =System.currentTimeMillis();
            totalWorkingTime = endTime -startTime;
            logger.info("Total Running Time: {}(/sec)", totalWorkingTime/1000);
            logger.info("Total Submitted Tasks: {}", totalTasks);
        }
    }

    /**
     * Monitor 'Submissions' if there exists any completed 'Future(submitted Task)'.
     * If it does, call listener.completeTask(Future result) and pass the result of a Task.
     */

    public int getNumberOfThreads() {
        return threads;
    }
}
