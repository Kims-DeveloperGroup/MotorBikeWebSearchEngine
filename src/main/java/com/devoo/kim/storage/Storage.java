package com.devoo.kim.storage;

import java.io.Closeable;
import java.io.File;
import java.io.FileReader;

/**
 *@Responsibility: An abstraction of storage loaded on memory. e.g) Local File System, HDFS, etc.
 *
 * @Behavior: This interface is not a concrete, but offers interface to implement concrete behaviors.
 */
public interface Storage<T1> extends Closeable{

    static final String LOCAL_FILE_PROTOCOL = "file://";
    static final String URL_PROTOCOL ="http://";

    public boolean connect();
    public boolean isValid();
    public T1 load() throws Exception;
    // TODO: 16. 10. 17 Think how to implement close();
}
