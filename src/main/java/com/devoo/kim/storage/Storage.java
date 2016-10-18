package com.devoo.kim.storage;

import com.devoo.kim.storage.data.CrawlData;

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

    /**
     * Connect/Access to this Storage
     * @return: true if the storage is connected/accessed with valid condition.
     */
    public boolean connect();
    /**
     *
     * @return true if the storage has valid condition. Otherwise, false.
     * e.g) existence, executable, permission, network connection, etc,.
     */
    public boolean isValid();

    /**
     * Load data/files on memory from physical storage.
     * @Note: Not offers reload, but the previous loaded data will be returned.
     * @return Loaded data set/files
     * @throws Exception in case of failure
     */
    public T1 load() throws Exception;

    /**
     * Tries to reload data/files on memory from physical storage.
     * And updates the current data.
     * @return
     * @throws Exception
     */
    public T1 reload() throws Exception;

    /***
     * Iterates 'CrawlData-s' from the beginning of loaded data,
     * and generates 'Task' with 'CrawlData'
     * @return an instance of 'Task'
     */
    public CrawlData readCrawlData() throws Exception;
    // TODO: 16. 10. 17 Think how to implement close();
}
