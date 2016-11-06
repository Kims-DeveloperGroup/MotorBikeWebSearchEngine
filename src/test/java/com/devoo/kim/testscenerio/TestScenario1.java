package com.devoo.kim.testscenerio;

import com.devoo.kim.context.Contexts;
import com.devoo.kim.storage.Storage;
import com.devoo.kim.storage.StorageLoader;
import com.devoo.kim.storage.exception.InvaildStorageException;
import com.devoo.kim.storage.exception.InvalidStorageLoaderException;
import com.devoo.kim.testscenerio.exception.InvalidTestInputFailureException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;

/**
 * Created by devoo-kim on 16. 10. 31.
 */
abstract public class TestScenario1 {
    // TODO: 16. 10. 31 Check validity test resources before running a test.

    public static final String localPath1 = "/home/devoo-kim/IdeaProjects/MotorBikeWebSearchEngine/src/test/resources/scenario1/localstorage1";
    public static final String localPath2 = "/home/devoo-kim/IdeaProjects/MotorBikeWebSearchEngine/src/test/resources/scenario1/localstorage2";
    public static final String localPath3 = "/home/devoo-kim/IdeaProjects/MotorBikeWebSearchEngine/src/test/resources/scenario1/localstorage3";
    public static final String localPath4 = "/home/devoo-kim/IdeaProjects/MotorBikeWebSearchEngine/src/test/resources/scenario1/localstorage4";
    public File expectedLocalStorage1;
    public File expectedLocalStorage2;
    public File expectedLocalStorage3;
    public File expectedLocalStorage4;
    public static final int totalCountOfUrlsFrom1234 =16*4;
    public static final int totalCountOfUrlsFrom1 =16;
    public static final int totalCountOfUrlsFrom2 =16;
    public static final int totalCountOfUrlsFrom3 =16;
    public static final int totalCountOfUrlsFrom4 =16;


//    @BeforeClass
    public final void preloadExpectedLocalStorage1234(){
        expectedLocalStorage1 = new File(localPath1);
        expectedLocalStorage2 = new File(localPath2);
        expectedLocalStorage3 = new File(localPath3);
        expectedLocalStorage4 = new File(localPath4);
        /**Count Urls From each local storage***/
    }

    private void countCrawlDataFromLocalStorage1234(){

    }

    private int countUrlsFromOneLocalStorage(Storage localStorage){return 0;}

    private int countUrls(File urlFile) throws InvalidTestInputFailureException, IOException {
        if (!urlFile.getName().toLowerCase().endsWith(".url"))
            throw new InvalidTestInputFailureException();
        List<String> urls =Files.readAllLines(urlFile.toPath());
        return urls.size();
    }



    /******/

    public final Storage<File> loadTestLocalFileSystem1() throws InvaildStorageException {
        Storage<File> localStorage1 = Contexts.generateStorageConnection("file",localPath1);
        return localStorage1;
    }

    public final Storage<File> loadTestLocalFileSystem2() throws InvaildStorageException {
        Storage<File> localStorage1 = Contexts.generateStorageConnection("file",localPath2);
        return localStorage1;
    }

    public final Storage<File> loadTestLocalFileSystem3() throws InvaildStorageException {
        Storage<File> localStorage1 = Contexts.generateStorageConnection("file",localPath3);
        return localStorage1;
    }

    public final Storage<File> loadTestLocalFileSystem4() throws InvaildStorageException {
        Storage<File> localStorage1 = Contexts.generateStorageConnection("file",localPath4);
        return localStorage1;
    }

    public final HashMap<String, Storage> loadTestLocalFileSystem1234() throws InvalidStorageLoaderException {
        StorageLoader loader= StorageLoader.initialize(localPath1,localPath2,localPath3,localPath4);
        HashMap<String, Storage> storages =loader.getStorages();
        return storages;
    }
}
