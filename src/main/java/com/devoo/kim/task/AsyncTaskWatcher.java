package com.devoo.kim.task;

import com.devoo.kim.task.listener.TaskListener;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @Responsibility: Watches if any future is complete or canceled.
 * @Behavior: Passes complete 'Future-s' to 'TaskListener', or removes canceled 'Future-s' from the watch list.
 */
public class AsyncTaskWatcher extends Thread{

    private TaskListener listener;
    private BlockingQueue<Future> futures;


    public AsyncTaskWatcher(TaskListener listener){
        this.listener =listener;
        this.futures = new LinkedBlockingQueue<>();
    }

    public AsyncTaskWatcher(TaskListener listener, int capacity){
        this.listener =listener;
        this.futures = new LinkedBlockingQueue<>(capacity);
    }

    /***
     * Being blocked until any space is available to put.
     */
    public void put(Future future) throws InterruptedException {
        futures.put(future);
    }

    @Override
    public synchronized void start() { // TODO: Possibility of Deadlock : Calling outside method, which might request for a lock, or wait until some condition. 
        System.out.println("Started");
        while (true){ // TODO: 16. 10. 25 How to exit of loop? 
            try { // TODO: 16. 10. 24 Should be tested for possibility of Read and Write Conflict
                System.out.println("Running");
                Thread.sleep(1000);
                futures.forEach(future -> { // TODO: 16. 10. 24 Set Timeout for traversal of Collection<Future> to avoid deadlock.
                    watch(future);
                });
            }catch (InterruptedException e) {
                System.out.println("Interrupted");
                break;
            }
        }
    }

    private void watch(Future future){
        if (future.isDone()){
            listener.completeTask(future);
            futures.remove(future);
        }else if (future.isCancelled()){
            futures.remove(future);
        }
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
