package com.devoo.kim.crawl;

import com.devoo.kim.storage.CrawlData;
import com.devoo.kim.task.Task;

import java.util.concurrent.Callable;

/**
 * Created by devoo-kim on 16. 10. 12.
 */
abstract public class CrawlTask extends Task {

    private byte[] content;

    public CrawlTask(String taskId){
        super(taskId);
    }




}
