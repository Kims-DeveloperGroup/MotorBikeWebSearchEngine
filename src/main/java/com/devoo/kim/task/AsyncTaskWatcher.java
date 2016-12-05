package com.devoo.kim.task;

import com.devoo.kim.task.listener.TaskListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @Responsibility: Watches if any future is complete or canceled.
 * @Behavior: Passes complete 'Future-s' to 'TaskListener', or removes canceled 'Future-s' from the watch list.
 */
public class AsyncTaskWatcher extends Thread{
    Logger logger = LoggerFactory.getLogger(AsyncTaskWatcher.class);
    private long period = 500L;
    private TaskListener listener;
    private BlockingQueue<Future> futures;
    private int total =0;
    private int completed=0;
    private int canceled=0;

    private final int READY =0;
    private final int RUNNING =1;
    private final int WATING_FOR_TERMINATION=2;
    private final int TERMINATED =3;
    private int status =READY;


    public AsyncTaskWatcher(TaskListener listener){
        this.listener =listener;
        this.futures = new LinkedBlockingQueue<>();
    }

    public AsyncTaskWatcher(TaskListener listener, int capacity){
        this.listener =listener;
        this.futures = new LinkedBlockingQueue<>(capacity);
    }

    public AsyncTaskWatcher(TaskListener listener, int capacity, long cycleInMills){
        this(listener, capacity);
        period =cycleInMills;
    }

    /***
     * Puts an instance of 'Future' to be watched.
     *@NOTE: Being blocked until any space is available to put.
     */
    public void put(Future future) throws InterruptedException {
        futures.put(future);
        total++;
    }
    // TODO: Possibility of Deadlock : Calling outside method,
    // which might request for a lock, or wait until some condition.

    /**
     * Watches instances of 'Future-s'.
     * completed task are passed to listener and canceled are removed from the queue.
     */
    @Override
    public void run() {
        status = RUNNING;
        // TODO: 16. 11. 23 set time-out of task to prevent infinite waiting.
        try {
            while (isRunnable()) { // TODO: 16. 10. 25 How to exit of loop?
                Thread.sleep(period);
                futures.forEach(future -> {
                    watch(future);
                });
            }
        }catch (InterruptedException e){
        }finally {
            status= TERMINATED;
            logger.info("Current Running Task: {}, Completed: {}, Canceled: {}, Remaining: {}",
                    total, completed, canceled, futures.size());
        }
    }

    /**
     * Checks if a given instance of 'Future' is completed or canceled,
     * and handles it.
     * @param future, to be checked.
     */
    private void watch(Future future){
        if (future.isDone()){
            if (listener!=null) listener.completeTask(future); // TODO: Being blocked until the method returns.
            futures.remove(future);
            completed++;
        }else if (future.isCancelled()){
            futures.remove(future);
            canceled++;
        }
    }

    /**
     * Check the Watcher is runnable or not.
     * @return true if it is, otherwise false if it is terminated.
     */
    private boolean isRunnable(){
        if ((status==WATING_FOR_TERMINATION && isEmpty())
                || status== TERMINATED) return false;
        return true;
    }
    /**
     * Terminates this Watcher after the 'Future-s' being watched are completed or canceled.
     * @NOTE:
     * This does not guarantee termination of running state,
     * but only set a flag in case that no more instance exists to be watched.
     */
    public void terminates(){
        status =WATING_FOR_TERMINATION;
    }

    public void terminatesNow(){
        status =TERMINATED;
    }

    public boolean isEmpty(){
        return futures.isEmpty();
    }

    public static void main(String[] args){
        AsyncTaskWatcher watcher = new AsyncTaskWatcher(new TaskListener() {
            @Override
            public void completeTask(Future result) {

            }
        });
        watcher.start();
        watcher.interrupt();
        System.out.println("Try to interrupt");
        AsyncTaskWatcher watcher1 = new AsyncTaskWatcher(new TaskListener() {
            @Override
            public void completeTask(Future result) {

            }
        });
        watcher1.start();

    }

}
