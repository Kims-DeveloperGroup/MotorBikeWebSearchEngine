package com.devoo.kim.storage;

import java.io.File;
import java.io.FileReader;

/**
 * Created by devoo-kim on 16. 10. 14.
 */
public interface Storage {

    static final String LOCAL_FILE_PROTOCOL = "file://";
    static final String URL_PROTOCOL ="http://";

    boolean save(String path, boolean overwrite);
    boolean update(String path);
    boolean load(String path);
}
