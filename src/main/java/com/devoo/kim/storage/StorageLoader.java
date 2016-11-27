package com.devoo.kim.storage;

import com.devoo.kim.context.Contexts;
import com.devoo.kim.storage.exception.InvaildStorageException;
import com.devoo.kim.storage.exception.InvalidStorageLoaderException;
import com.devoo.kim.storage.exception.LoaderInitializationFailureException;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;

/**
 * @Responsibility: Loads Abstraction 'Storage-s' from given paths
 * @Behavior: StorageLoader instantiates several types of storage instances with given protocols. And Initialize the map of 'Storage-s"
 * @Instantiation: By calling 'static StorageLoader initialize(String... paths)'
 * @Note:
 * 1) Once initialized, The instance does not allow to change.(If you try to re-initialize, That throws an exception)
 * 2) If modification is required, new instantiation is required.
 * 
 */
/**@Thread-safe*/
public class StorageLoader {
    long initTime;
    public final int storageCount;
    private boolean isInitialized=false;
    private HashMap<String, Storage> storages; /**@Must:Immutable*/

    private StorageLoader(HashMap<String, Storage> storages) throws LoaderInitializationFailureException {
        if (isInitialized) throw new LoaderInitializationFailureException(); // TODO: 16. 10. 17 Define Custom InitializedException to prevent change.
        this.storages = storages;
        isInitialized= true;
        initTime=System.currentTimeMillis();
        storageCount=storages.size();
    }

    /**
     * Initializes this 'StorageLoader' by connecting 'Storage-s' with given paths.
     * Once this is called, the same 'StorageLoader' is not allowed to call this twice.
     * @param paths : paths of storage to be loaded.
     * @return an instance of 'StorageLoader' if any of 'Storage-s' are added and ready to load 'CrawlData-s'/'CrawlDataFile-s'
     */
    public static StorageLoader initialize(String... paths) throws LoaderInitializationFailureException {// TODO: 16. 10. 17 Not Nomalize uri yet.
        StorageLoader loader;
        URI uri;
        HashMap<String, Storage> tempStorages =new HashMap<>();
        Storage storage;

        for (String path: paths){
            try{
                uri =new URI(path);
                storage= Contexts.generateStorageConnection(uri.getScheme(), path);
                tempStorages.putIfAbsent(path, storage);
                // TODO: 16. 10. 17 Log metadata of Storage Instances.
            }catch (URISyntaxException e) {continue;}
            catch (InvaildStorageException e) {continue;}
        }
        if (tempStorages.isEmpty()) new LoaderInitializationFailureException();
        loader = new StorageLoader(tempStorages);
        return loader;
    }

    public HashMap<String, Storage> getStorages() throws InvalidStorageLoaderException {
        if (!isInitialized) throw new InvalidStorageLoaderException();
        return this.storages;
    }

    @Override
    public String toString() {
        return null; // TODO: 16. 10. 17 Log (1) the time of initailizing 'Storage-s' (2) Storage Path (3) Status of 'Storage-s' 
    }
}
