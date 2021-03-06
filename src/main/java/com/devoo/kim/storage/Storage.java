package com.devoo.kim.storage;

import com.devoo.kim.storage.data.CrawlData;
import com.devoo.kim.storage.exception.InvaildStorageException;
import com.devoo.kim.storage.exception.StorageLoadException;

import java.io.Closeable;
import java.util.Iterator;
import java.util.List;

/**
 *@Responsibility: An abstraction of storage loaded on memory. e.g) Local File System, HDFS, etc.
 *
 * @Behavior: This interface is not a concrete, but offers interface to implement concrete behaviors.
 */
public interface Storage<TypeOfStorage> extends Closeable{

    /**
     * Connect/Access to this Storage
     * @return: true if the storage is connected/accessed with valid condition.
     */
    public boolean connect(String path) throws InvaildStorageException;
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
    public void load() throws Exception; // TODO: 16. 10. 20 Define StorageLoadFailure Exception.

    /**
     * Tries to reload data/files on memory from physical storage.
     * And updates the current data.
     * @return
     * @throws Exception
     */
    public void reload() throws Exception; // TODO: Define StorageLoadFailure Exception.

    /***
     * Gets 'CrawlData-s' from loaded 'CrawlDataFile-s'
     * @return instances of 'CrawData-s'
     */
    public List<CrawlData> getCrawlData() throws InvaildStorageException, StorageLoadException;
    // TODO: 16. 10. 17 Think how to implement close();

    public TypeOfStorage getRoot();

}
