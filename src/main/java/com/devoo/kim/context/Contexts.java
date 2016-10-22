package com.devoo.kim.context;

import com.devoo.kim.crawl.Crawling;
import com.devoo.kim.storage.Storage;
import com.devoo.kim.storage.data.CrawlData;
import com.devoo.kim.task.Task;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by devoo-kim on 16. 10. 17.
 */
public final class Contexts {

    //// TODO:  /** Make Consistent API of SpringContainer **/
    public static final ApplicationContext STORAGES = new ClassPathXmlApplicationContext("contexts/storageBeans.xml");
    public static final ApplicationContext TASKS = new ClassPathXmlApplicationContext("contexts/taskBeans.xml");
    public static Storage getStorageBean(String scheme, String path){
        return (Storage) STORAGES.getBean(scheme, path);
    }

    public static Crawling getCrawlingTask(CrawlData crawlData){
        return (Crawling) TASKS.getBean(crawlData.getDataType(), crawlData);
    }
}
