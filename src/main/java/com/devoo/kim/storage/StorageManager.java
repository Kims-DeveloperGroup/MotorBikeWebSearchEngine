package com.devoo.kim.storage;

import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @Role: load several types of storage instances by given protocols
 * 
 * @Note: Once initialized, The instance is immutable. If modification is required, newly instantiation is required.
 */
public class StorageManager {
    /**
     * Normalization
     * Resolution
     * Relativization
     */
    private boolean isInitialized =false;
    Storage[] storages;
    URI uri;

    public Storage[] getStorages() throws Exception {
        if (!isInitialized) throw new Exception();// TODO: 16. 10. 17 Define Custom InitializedException to prevent change.
        return storages;
    }

    public void load(String... paths) throws Exception {
        if (isInitialized) throw new Exception(); // TODO: 16. 10. 17 Define Custom InitializedException to prevent change. 
        isInitialized= true;
        // TODO: 16. 10. 17 (Use Spring Container)
        // TODO: 16. 10. 16 Instantiate Storage-s with a protocol
        // TODO: 16. 10. 16 Parsing paths and protocol & Determine which type of storage is loaded.
    }
}
