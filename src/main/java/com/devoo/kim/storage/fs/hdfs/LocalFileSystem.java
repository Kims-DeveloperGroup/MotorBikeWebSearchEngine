package com.devoo.kim.storage.fs.hdfs;

import com.devoo.kim.storage.Storage;
import com.devoo.kim.storage.data.CrawlData;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * LocalFileSystem implements Storage<File[]>
 *
 *@Responsibility: An abstraction of Local File System loaded on memory
 *
 * @Behavior: This class offers an access to Local File System, which contains files of 'CrawlData-s'
 */
public class LocalFileSystem implements Storage<File[]> {

    private static class CrawlDataFilter implements FilenameFilter{
        public final String CRAWL_DATA_EXTENSION =".crawl";
        public final String URLS_EXTENSION =".urls";

        @Override
        public boolean accept(File dir, String name) {
            return name.toLowerCase().endsWith(CRAWL_DATA_EXTENSION) ||name.toLowerCase().endsWith(URLS_EXTENSION);
        }
    }

    File rootDir;
    File[] files;
    private static CrawlDataFilter fileFilter = new CrawlDataFilter();
    public LocalFileSystem(String path) throws Exception {
        rootDir = new  File(path);
        if(!isValid()){
            throw new Exception();
        }
    }
    
    public LocalFileSystem() throws Exception {
        throw new Exception(); // TODO: 16. 10. 17 Define user-defined Exception. 
    }

    @Override
    public boolean connect() {
        if (!isValid()) return false;
        return true;
    }

    @Override
    public boolean isValid() {
        return (rootDir.exists()&& rootDir.isDirectory() &&
                rootDir.canExecute() && rootDir.canRead());
    }

    @Override
    public File[] load() throws Exception {
        if (!isValid()) throw new Exception();
        if (files==null)files =rootDir.listFiles(fileFilter);
        return files.clone(); //CopyOnWrite
    }

    public void convertURLStoCrawlData(File urlsFile){
        if(!urlsFile.getName().endsWith(fileFilter.URLS_EXTENSION)) return;
        try {
            Files.readAllLines(urlsFile.toPath());
        } catch (IOException e) {}

    }

    @Override
    public CrawlData readCrawlData() throws Exception {
        if (files==null) throw new Exception(); // TODO: 16. 10. 18 Define UserDefined Exception (CrawlDataLoadException)
        return null;
    }

    public File[] reload() throws Exception {
        if (!isValid()) throw new Exception();// TODO: 16. 10. 17 User-define Exception for failure of file load
        files = rootDir.listFiles(fileFilter);
        return files.clone(); //CopyOnWrite
    }

    @Override
    public void close() throws IOException {}
}
