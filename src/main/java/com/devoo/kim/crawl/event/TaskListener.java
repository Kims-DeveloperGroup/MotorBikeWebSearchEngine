package com.devoo.kim.crawl.event;

/**
 * Created by devoo-kim on 16. 10. 22.
 */
public interface TaskListener<T1> {

    public void completeTask(T1 result);
}
