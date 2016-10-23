package com.devoo.kim.storage.data;

/**
 * Created by devoo-kim on 16. 10. 12.
 */
abstract public class CrawlData {

    public long initDate;
    public long updatedDate;


    public void update(){
        updatedDate = System.currentTimeMillis();
    }

    /**
     * Protocol defines a type of CrawlData such as WebPage, etc,.
     * @return the type of protocol. e.g)http, file
     */
    abstract public String getDataType();

}
