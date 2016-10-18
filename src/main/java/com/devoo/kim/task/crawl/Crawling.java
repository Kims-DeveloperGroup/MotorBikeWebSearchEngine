package com.devoo.kim.task.crawl;

import com.devoo.kim.storage.data.CrawlData;
import com.devoo.kim.task.Task;

/**
 * Created by devoo-kim on 16. 10. 12.
 */
abstract public class Crawling extends Task {

    private byte[] content;
    private CrawlData crawlData;

    public Crawling(CrawlData crawlData){
        this.crawlData =crawlData;
    }

}
