package com.devoo.kim.storage.fs.local;

import com.devoo.kim.storage.Storage;
import com.devoo.kim.storage.data.CrawlData;
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
public class LocalFileSystem implements Storage {

    private static class CrawlDataFilter implements FilenameFilter{
        public final String CRAWL_DATA_EXTENSION = CrawlDataFile.EXTENSION;//'.crawl'
        public final String URLS_EXTENSION =".urls";

        @Override
        public boolean accept(File dir, String name) {
            return name.toLowerCase().endsWith(CRAWL_DATA_EXTENSION) ||name.toLowerCase().endsWith(URLS_EXTENSION);
        }
    }

    File rootDir;
    CrawlDataFile[] crawlDataFiles;
    private static CrawlDataFilter fileFilter = new CrawlDataFilter();

    public LocalFileSystem(String path) throws Exception {
        rootDir = new  File(path);
        if(!isValid()){
            throw new Exception();
        }
    }
    public LocalFileSystem() throws Exception {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean connect() {
        if (!isValid()) return false;
        return true;
    }

    /**
     * Check if the 'Storage' is valid or invalid.
     * @return true if the 'Storage' is valid.
     */
    @Override
    public boolean isValid() {
        return (rootDir.exists()&& rootDir.isDirectory() &&
                rootDir.canExecute() && rootDir.canRead());
    }

    /**
     * Load 'File-s' with '.crawl' extension and Store them in a type of 'CrawlDataFile'.
     * if 'CrawlDataFile-s' have been loaded, the previously loaded are returned.
     * @NOTE: The returned is a clone.
     * @return
     * @throws Exception
     */
    @Override
    public Storage load() throws Exception {
        if (!isValid()) throw new Exception();
        File[] files;
        if (this.crawlDataFiles ==null){
            files =rootDir.listFiles(fileFilter);
            this.crawlDataFiles = new CrawlDataFile[crawlDataFiles.length];
            castFileToCrawlDataFile(files, this.crawlDataFiles);
        }
        return this;
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

    /**
     * Reload Files from Local File System.
     * @NOTE: The returned is a clone.
     * @return
     * @throws Exception
     */

    public Storage reload() throws Exception {
        CrawlDataFile[] temp =this.crawlDataFiles;
        this.crawlDataFiles =null;
        load();
        if (crawlDataFiles==null){
            this.crawlDataFiles =temp;
            throw new Exception();
        }
        return this;
    }

    @Override
    public void close() throws IOException {}

    public static void main(String[] args){
        try {
            LocalFileSystem localFileSystem = new LocalFileSystem();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
