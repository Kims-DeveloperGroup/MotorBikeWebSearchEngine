package com.devoo.kim.storage;

import java.io.File;
import java.io.FileReader;

/**
 * Created by devoo-kim on 16. 10. 14.
 */
public abstract class Storage {
    String path;
    String id;

    public Storage(String path){
        this.path =path;

    }

    public void load(String path){

    }
    abstract void save(boolean overwrite);
    public void update(){
        save(false);
    }
}
