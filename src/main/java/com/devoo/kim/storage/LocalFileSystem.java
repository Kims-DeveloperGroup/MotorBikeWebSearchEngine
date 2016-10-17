package com.devoo.kim.storage;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

/**
 * LocalFileSystem implements Storage<File[]>
 *
 *@Responsibility: An abstraction of Local File System loaded on memory
 *
 * @Behavior: This class offers an access to Local File System, which contains files of 'CrawlData-s'
 */
public class LocalFileSystem implements Storage<File[]> {

    private static class CrawlDataFilter implements FilenameFilter{
        public final String EXTENSION =".crawl";

        @Override
        public boolean accept(File dir, String name) {
            return name.toLowerCase().endsWith(EXTENSION);
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

    public File[] reload() throws Exception {
        if (!isValid()) throw new Exception();// TODO: 16. 10. 17 User-define Exception for failure of file load
        files = rootDir.listFiles(fileFilter);
        return files.clone(); //CopyOnWrite
    }

    @Override
    public void close() throws IOException {}
}
