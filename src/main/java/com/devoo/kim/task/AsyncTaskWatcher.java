package com.devoo.kim.task;

import com.devoo.kim.task.listener.TaskListener;

import java.util.Collection;
import java.util.concurrent.Future;

/**
 * Created by devoo-kim on 16. 10. 24.
 */
public class AsyncTaskWatcher extends Thread{

    Collection<Future> watchedFutures;
    TaskListener listener;

    public AsyncTaskWatcher(Collection<Future> futures, TaskListener listener){
        watchedFutures =futures;
    }

    @Override
    public synchronized void start() { // TODO: Possibility of Deadlock : Calling outside method, which might request for a lock, or wait until some condition. 
        while (true){
            try {
                Thread.sleep(1000);
                watchedFutures.forEach(future -> { // TODO: 16. 10. 24 Set Timeout for traversal of Collection<Future> to avoid deadlock.
                    if (future.isDone()){
                        listener.completeTask(future);
                    }else if (future.isCancelled()){
                        watchedFutures.remove(future);
                    }
                });

            } catch (InterruptedException e) {
                break;
            }
        }
    }
}
