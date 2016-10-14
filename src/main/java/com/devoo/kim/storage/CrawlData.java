package com.devoo.kim.storage;

/**
 * Created by devoo-kim on 16. 10. 12.
 */
abstract public class CrawlData {
    long initDate;
    long updatedDate;

    public CrawlData(){
        initDate =System.currentTimeMillis();
    }

    public void update(){
        updatedDate = System.currentTimeMillis();
    }

}
