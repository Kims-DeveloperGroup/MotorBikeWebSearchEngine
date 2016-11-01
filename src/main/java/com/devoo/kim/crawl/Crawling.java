package com.devoo.kim.crawl;

import com.devoo.kim.crawl.exception.CrawlingTaskException;
import com.devoo.kim.crawl.exception.NoCrawlingTargetException;
import com.devoo.kim.storage.data.CrawlData;
import com.devoo.kim.task.Task;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by devoo-kim on 16. 10. 12.
 */
abstract public class Crawling <SubTypeOfCrawlData extends CrawlData> extends Task {
    private AtomicInteger status =new AtomicInteger(Crawler.NOT_INITIALLZED); // TODO: 16. 10. 20 Find out whether 'AtomicInterger' is appropriate to guarantee a consistent monitor of status for CrawlingGenerator.
    /**NOTE: final does not guarantee consistency of attr in 'CrawlData'**/
//    private SubTypeOfCrawlData crawlData; // TODO: 16. 10. 21 Required to fix CrawlData is better to be managed in sub-class.

    /**By using 'Generic type', different sub-classes are offered to use their own type of 'CrawlData'.
     * But they share the common interface.
     **/



    @Override
    abstract public SubTypeOfCrawlData call() throws CrawlingTaskException;

    /***
     * By calling this method, outside objects monitor the status of this.
     * @return the current status of this 'CrawlingGenerator'
     */

 /**   public void setCrawlData(SubTypeOfCrawlData crawldata) throws CrawlingTaskException {
        if (crawlData!=null) throw new CrawlingTaskException();
        crawlData =crawldata;
    }
    public SubTypeOfCrawlData getCrawlData() throws NoCrawlingTargetException {
        if (crawlData==null) throw new NoCrawlingTargetException();
        return crawlData;
    }
  **/

    public int getStatus(){
        return status.get();
    }
    public void setStatus(int newstatus){
        status.set(newstatus);
    }

}
