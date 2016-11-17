package com.devoo.kim.crawl;

import com.devoo.kim.context.Contexts;
import com.devoo.kim.storage.Storage;
import com.devoo.kim.storage.StorageLoader;
import com.devoo.kim.storage.exception.InvaildStorageException;
import com.devoo.kim.storage.exception.InvalidStorageLoaderException;
import com.devoo.kim.storage.exception.LoaderInitializationFailureException;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by devoo-kim on 16. 10. 24.
 */

// TODO: 16. 10. 30 Make 'Domain API for test' and 'Given-When-Then Pattern'
    //Refactor by extending 'TestCase1_FourLocalStorages_SmallUrls'
public class Test_Storage {
    @Test
    public void TestAccessOneLocalFileSystem1_Positive() throws InvaildStorageException, IOException {
        String localPath1 = "/home/devoo-kim/IdeaProjects/MotorBikeWebSearchEngine/src/test/resources/localstorage1";
        File expected = new File(localPath1);
        Storage<File> actual = Contexts.generateStorageConnection("file", localPath1);
        boolean areSameFile =Files.isSameFile(expected.toPath(), actual.getRoot().toPath());
        assertTrue(areSameFile);
    }

    @Test
    public void TestAccessOneLocalFileSystem1_Negative() throws InvaildStorageException, IOException {
        String localPath1 = "/home/devoo-kim/IdeaProjects/MotorBikeWebSearchEngine/src/test/resources/localstorage1";
        String localPath2 = "/home/devoo-kim/IdeaProjects/MotorBikeWebSearchEngine/src/test/resources/localstorage2";
        File expected = new File(localPath1);
        Storage<File> actual = Contexts.generateStorageConnection("file", localPath2);
        boolean areSameFile =Files.isSameFile(expected.toPath(), actual.getRoot().toPath());
        assertFalse(areSameFile);
    }

    @Test
    public void TestAccessOneLocalFileSystem2_Positive() throws InvaildStorageException, IOException {
        String localPath = "/home/devoo-kim/IdeaProjects/MotorBikeWebSearchEngine/src/test/resources/localstorage2";
        File expected = new File(localPath);
        Storage<File> actual = Contexts.generateStorageConnection("file", localPath);
        boolean areSameFile =Files.isSameFile(expected.toPath(), actual.getRoot().toPath());
        assertTrue(areSameFile);
    }
    @Test
    public void TestAccessOneLocalFileSystem2_Negative() throws InvaildStorageException, IOException {
        String localPath1 = "/home/devoo-kim/IdeaProjects/MotorBikeWebSearchEngine/src/test/resources/localstorage1";
        String localPath2 = "/home/devoo-kim/IdeaProjects/MotorBikeWebSearchEngine/src/test/resources/localstorage2";
        File expected = new File(localPath1);
        Storage<File> actual = Contexts.generateStorageConnection("file", localPath2);
        boolean areSameFile =Files.isSameFile(expected.toPath(), actual.getRoot().toPath());
        assertFalse(areSameFile);
    }

    @Test
    public void TestAccessMultipleStorages1_Positive() throws InvalidStorageLoaderException, IOException {
        String localPath1 = "/home/devoo-kim/IdeaProjects/MotorBikeWebSearchEngine/src/test/resources/localstorage1";
        String localPath2 = "/home/devoo-kim/IdeaProjects/MotorBikeWebSearchEngine/src/test/resources/localstorage2";
        String localPath3 = "/home/devoo-kim/IdeaProjects/MotorBikeWebSearchEngine/src/test/resources/localstorage3";
        String localPath4 = "/home/devoo-kim/IdeaProjects/MotorBikeWebSearchEngine/src/test/resources/localstorage4";

        File expected1 = new File(localPath1);
        File expected2 = new File(localPath2);
        File expected3 = new File(localPath3);
        File expected4 = new File(localPath4);
        StorageLoader loader= StorageLoader.initialize(localPath1,localPath2,localPath3,localPath4);
        HashMap<String, Storage> storages =loader.getStorages();
        File actual1 = (File) storages.get(localPath1).getRoot();
        File actual2 = (File) storages.get(localPath2).getRoot();
        File actual3 = (File) storages.get(localPath3).getRoot();
        File actual4 = (File) storages.get(localPath4).getRoot();

        boolean areSameFile1 =Files.isSameFile(expected1.toPath(), actual1.toPath());
        boolean areSameFile2 =Files.isSameFile(expected2.toPath(), actual2.toPath());
        boolean areSameFile3 =Files.isSameFile(expected3.toPath(), actual3.toPath());
        boolean areSameFile4 =Files.isSameFile(expected4.toPath(), actual4.toPath());
        assertTrue(areSameFile1 &&
                    areSameFile2 &&
                        areSameFile3 &&
                            areSameFile4);
    }

    @Test
    public void TestAccessMultipleStorages1_Negative() throws InvalidStorageLoaderException, IOException {
        String localPath1 = "/home/devoo-kim/IdeaProjects/MotorBikeWebSearchEngine/src/test/resources/localstorage1";
        String localPath2 = "/home/devoo-kim/IdeaProjects/MotorBikeWebSearchEngine/src/test/resources/localstorage2";
        String localPath3 = "/home/devoo-kim/IdeaProjects/MotorBikeWebSearchEngine/src/test/resources/localstorage3";
        String localPath4 = "/home/devoo-kim/IdeaProjects/MotorBikeWebSearchEngine/src/test/resources/localstorage4";

        File expected1 = new File(localPath1);
        File expected2 = new File(localPath2);
        File expected3 = new File(localPath3);
        File expected4 = new File(localPath4);
        StorageLoader loader= StorageLoader.initialize(localPath1,localPath2,localPath3,localPath4);
        HashMap<String, Storage> storages =loader.getStorages();
        File actual1 = (File) storages.get(localPath1).getRoot();
        File actual2 = (File) storages.get(localPath2).getRoot();
        File actual3 = (File) storages.get(localPath3).getRoot();
        File actual4 = (File) storages.get(localPath4).getRoot();

        boolean areSameFile1 =Files.isSameFile(expected1.toPath(), actual1.toPath());
        boolean areSameFile2 =Files.isSameFile(expected2.toPath(), actual1.toPath());
        boolean areSameFile3 =Files.isSameFile(expected3.toPath(), actual3.toPath());
        boolean areSameFile4 =Files.isSameFile(expected4.toPath(), actual4.toPath());
        assertFalse(areSameFile1 &&
                areSameFile2 &&
                areSameFile3 &&
                areSameFile4);
    }

    @Test
    public void TestIfAnyStorageNotExists() throws LoaderInitializationFailureException {
        String localPath1 = "/home/devoo-kim/IdeaProjects/MotorBikeWebSearchEngine/src/test/resources/localstorage1";
        String localPath2 = "/home/devoo-kim/IdeaProjects/MotorBikeWebSearchEngine/src/test/resources/localstorage2";
        String localPath3 = "/home/devoo-kim/IdeaProjects/MotorBikeWebSearchEngine/src/test/resources/localstorage3";
        String noExists = "/home/devoo-kim/IdeaProjects/MotorBikeWebSearchEngine/src/test/resources/noexists";

        StorageLoader loader = StorageLoader.initialize(localPath1, localPath2, localPath3, noExists);
        int expectedStorageCount =3;
        int actualStorageCount= loader.storageCount;
        assertTrue(expectedStorageCount== actualStorageCount);
    }

    public static void main(String[] args){
    }
}
