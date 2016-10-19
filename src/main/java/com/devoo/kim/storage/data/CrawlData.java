package com.devoo.kim.storage.data;

/**
 * Created by devoo-kim on 16. 10. 12.
 */
abstract public class CrawlData {

    public final String PROTOCOL = "undefined";
    final long initDate;
    long updatedDate;

    public CrawlData(){
        initDate =System.currentTimeMillis();
    }

    public void update(){
        updatedDate = System.currentTimeMillis();
    }

}
