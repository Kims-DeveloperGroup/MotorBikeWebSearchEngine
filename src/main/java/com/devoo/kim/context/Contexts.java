package com.devoo.kim.context;

import com.devoo.kim.storage.Storage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by devoo-kim on 16. 10. 17.
 */
public class Contexts {
    public static final ApplicationContext STORAGES = new ClassPathXmlApplicationContext("storageBeans.xml");

    public static final Storage getStorageBean(String scheme, String path){
        return (Storage) STORAGES.getBean(scheme, path);
    }
}
