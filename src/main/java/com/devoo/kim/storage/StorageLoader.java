package com.devoo.kim.storage;

import com.devoo.kim.context.Contexts;
import org.springframework.context.ApplicationContext;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;

/**
 * @Responsibility: Loads Abstraction 'Storage-s' from given paths
 * @Behavior: StorageLoader instantiates several types of storage instances with given protocols. And Initialize the map of 'Storage-s"
 * @Note:
 * 1) Once initialized, The instance does not allow to change.(If you try to re-initialize, That throws an exception)
 * 2) If modification is required, new instantiation is required.
 * 
 */
/**@Thread-safe*/
public class StorageLoader {
    long initTime;
    private boolean isInitialized =false;
    private HashMap<String, Storage> storages;/**@Must:Immutable*/

    public HashMap<String, Storage> getStorages() throws Exception {
        if (!isInitialized) throw new Exception();// TODO: 16. 10. 17 Define Custom InitializedException to prevent change.
        return storages;
    }

    /**
     * Initializes this 'StorageLoader' by loading 'Storage-s' with given paths.
     * Once this is called, the same 'StorageLoader' is not allowed to call this twice.
     * @param paths : paths of storage to access (MUST: Absolute path with scheme/protocol)
     * @throws Exception
     */

    public void initialize(String... paths) throws Exception {// TODO: 16. 10. 17 Not Nomalize uri yet. 
        if (isInitialized) throw new Exception(); // TODO: 16. 10. 17 Define Custom InitializedException to prevent change. 
        isInitialized= true;
        initTime=System.currentTimeMillis();
        ApplicationContext context =Contexts.STORAGES;
        URI uri;
        for (String path: paths){
            try{
                uri =new URI(path);
                if (!uri.isAbsolute()) throw new Exception();
                storages.putIfAbsent(path, Contexts.getStorageBean(uri.getScheme(), path));
            }catch (URISyntaxException e){
                isInitialized=false;
                storages=null;
                throw new Exception(); // TODO: 16. 10. 17 Define Initialization Failuer Exception.
            }
            // TODO: 16. 10. 17 Log metadata of Storage Instances.
        }
    }

    @Override
    public String toString() {
        return null; // TODO: 16. 10. 17 Log (1) the time of initailizing 'Storage-s' (2) Storage Path (3) Status of 'Storage-s' 
    }
}
