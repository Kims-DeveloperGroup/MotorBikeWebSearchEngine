package com.devoo.kim.storage;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by devoo-kim on 16. 10. 15.
 */
public class LocalFileSystem implements Storage {
    // TODO: 16. 10. 15 Think about possibilty that the list might be modified while be read. 
    List<File> storagePaths = new LinkedList<>();

    public LocalFileSystem(String... paths){
        addPaths(paths);
    }

    public void addPaths(String... storagePaths){
        String[] paths = storagePaths;
        for (String path: paths){
            this.storagePaths.add(new File(path));
        }
    }

    @Override
    public boolean save(String path, boolean overwrite) {
        return false;
    }

    @Override
    public boolean update(String path) {
        return false;
    }

    @Override
    public boolean load(String path) {
        return false;
    }
}
