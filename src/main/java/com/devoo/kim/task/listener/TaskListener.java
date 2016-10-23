package com.devoo.kim.task.listener;

import java.util.concurrent.Future;

/**
 * Created by devoo-kim on 16. 10. 22.
 */
public interface TaskListener <T1> {

    public void completeTask(Future<T1> result);
}
