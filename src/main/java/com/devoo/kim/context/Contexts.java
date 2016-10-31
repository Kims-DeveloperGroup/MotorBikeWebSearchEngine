package com.devoo.kim.context;

import com.devoo.kim.crawl.Crawling;
import com.devoo.kim.crawl.exception.CrawlingTaskException;
import com.devoo.kim.storage.Storage;
import com.devoo.kim.storage.data.CrawlData;
import com.devoo.kim.storage.exception.InvaildStorageException;
import com.sun.nio.sctp.InvalidStreamException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * Created by devoo-kim on 16. 10. 17.
 */
public final class Contexts {

    //// TODO:  /** Make Consistent API of SpringContainer **/
    public static final ApplicationContext STORAGES = new FileSystemXmlApplicationContext("/contexts/storageBeans.xml");
    public static final ApplicationContext TASKS = new FileSystemXmlApplicationContext("/contexts/crawlingBeans.xml");

    public static Storage generateStorageConnection(String scheme, String  path) throws InvaildStorageException {
        if (scheme==null || scheme.isEmpty()) scheme= "file";
        Storage storage = (Storage) STORAGES.getBean(scheme);
        storage.connect(path);
        return storage;
    }

    // TODO: 16. 10. 31 BeanCreationException: Error creating bean with name 'http' 
    public static Crawling generateCrawling(CrawlData crawlData) throws CrawlingTaskException {
        Crawling crawling =(Crawling) TASKS.getBean("http", crawlData);
        crawling.setCrawlData(crawlData);
        return crawling;
    }

    public static void main(String[] args){}
}
