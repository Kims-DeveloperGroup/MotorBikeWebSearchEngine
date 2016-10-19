package com.devoo.kim.task.crawl;

import com.devoo.kim.storage.data.CrawlData;
import com.devoo.kim.task.Task;

/**
 * Created by devoo-kim on 16. 10. 12.
 */
abstract public class Crawling <SubTypeOfCrawlData extends CrawlData> extends Task {

    /**NOTE: Not guarantee consistency of attr in 'CrawlData'**/
    public final SubTypeOfCrawlData crawlData;
    public final String TASKTYPE ="CRAWLING";

    /**By using 'Generic type', different sub-classes are offered to use their own type of 'CrawlData'.
     * But they share the common interface.
     *  **/
    public Crawling(SubTypeOfCrawlData crawlData){
        this.crawlData =crawlData;
    }

    @Override
    abstract public SubTypeOfCrawlData call() throws Exception;
}
