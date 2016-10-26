package com.devoo.kim.storage.fs.local;

import com.devoo.kim.storage.Storage;
import com.devoo.kim.storage.data.CrawlData;
import com.devoo.kim.storage.exception.InvaildStorageException;
import com.devoo.kim.storage.exception.StorageLoadException;
import com.devoo.kim.storage.fs.CrawlDataFile;

import java.io.*;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * LocalFileSystem implements Storage<File[]>
 *
 *@Responsibility: An abstraction of Local File System loaded on memory
 *
 * @Behavior: This class offers an access to Local File System, which contains crawlDataFiles of 'CrawlData-s'
 * @NOTE: When loading 'CrawlDataFile-s' from File System to generate 'CrawlData-s',
 *        too large size or too many of 'CrawlData-s' may cause 'OutOfMemoryError', or long blocking time(Delay).
 *        Also that could reduce performance of the application.
 */
public class LocalFileSystem implements Storage<File> {

    private static class CrawlDataFilter implements FilenameFilter{
        public final String CRAWL_DATA_EXTENSION = CrawlDataFile.EXTENSION;//'.crawl'
        public final String URLS_EXTENSION =".urls";

        @Override
        public boolean accept(File dir, String name) {
            return name.toLowerCase().endsWith(CRAWL_DATA_EXTENSION) ||name.toLowerCase().endsWith(URLS_EXTENSION);
        }
    }
    private static CrawlDataFilter fileFilter = new CrawlDataFilter();

    private File rootDir;
    CrawlDataFile[] crawlDataFiles;
    boolean loaded =false;

    @Override
    public boolean connect(String path) throws InvaildStorageException {
        rootDir= new File(path);
        if (!isValid()) throw new InvaildStorageException();
        return true;
    }

    /**
     * Check if the 'Storage' is valid or invalid.
     * @return true if the 'Storage' is valid.
     */
    @Override
    public boolean isValid() {
        return (rootDir.exists()&& rootDir.isDirectory() && rootDir.canRead());
    }

    /**
     * Load 'File-s' with '.crawl' extension and Store them in a type of 'CrawlDataFile'.
     * if 'CrawlDataFile-s' have been loaded, nothing is loaded.
     * @throws InvaildStorageException  when the path is an invalid storage.
     */
    @Override
    public void load(String path) throws InvaildStorageException {
        if (loaded) return;
        rootDir = new File(path);
        if (!isValid()) throw new InvaildStorageException();
        File[] files;
        if (this.crawlDataFiles ==null){
            files =rootDir.listFiles(fileFilter);
            this.crawlDataFiles = new CrawlDataFile[crawlDataFiles.length];
            castFileToCrawlDataFile(files, this.crawlDataFiles);
        }
    }

    public void load(File rootDir) throws InvaildStorageException {
        load(rootDir.getPath());
    }

    /**
     * Reload Files from Local File System.
     * if it had not been loaded before, an exception is thrown.
     * @throws StorageLoadException
     */

    public void reload() throws StorageLoadException {
        CrawlDataFile[] temp =this.crawlDataFiles;
        this.loaded = false;

        try {
            load(rootDir);
        } catch (InvaildStorageException e) {
            this.crawlDataFiles =temp;
            throw new StorageLoadException();
        }
    }

    /**
     * Read objects from an array of 'File' from local file system into another array of 'CrawlDataFile'
     * @Note: Ignores a 'File' that throws an exception by handling the exception in this method
     * @param fromFiles
     * @param toCrawlFiles
     */
    private void castFileToCrawlDataFile(File[] fromFiles, CrawlDataFile[] toCrawlFiles){
        ObjectInputStream in = null;
        for (int i=0; i<fromFiles.length; i++){
            try {
                in = new ObjectInputStream(new FileInputStream(fromFiles[i]));
                toCrawlFiles[i] =(CrawlDataFile) in.readObject();
            } catch (Exception e) {continue;}
        }
        try {
            if (in!=null) in.close();
        } catch (IOException e) {}
    }

    /***
     * Gathers 'CrawlData-s' from 'CrawDataFile-s' in a list.
     * @return an instance of 'Iterator<CrawlData>'
     */
    @Override
    public Iterator<CrawlData> iterateCrawlData(){ // TODO: Extract the method into independent interface.
        LinkedList<CrawlData> crawlDatas = new LinkedList<>();
        for (CrawlDataFile crawlFile : this.crawlDataFiles){
            if (!crawlFile.isValid()) continue; // TODO: Is it required to be called ? 
            crawlDatas.addAll(crawlFile.getListOfCrawlData());
        }
        return crawlDatas.iterator();
    }

    @Override
    public File getRoot() {
        return rootDir;
    }

    @Override
    public void close() throws IOException {}

    public static void main(String[] args){}
}
