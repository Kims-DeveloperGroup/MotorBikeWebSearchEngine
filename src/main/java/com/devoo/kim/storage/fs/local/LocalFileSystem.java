package com.devoo.kim.storage.fs.local;

import com.devoo.kim.storage.Storage;
import com.devoo.kim.storage.data.CrawlData;
import com.devoo.kim.storage.exception.InvaildStorageException;
import com.devoo.kim.storage.exception.StorageLoadException;
import com.devoo.kim.storage.fs.CrawlDataFile;
import com.devoo.kim.storage.fs.exception.CrawlDataFileException;
import com.devoo.kim.storage.fs.exception.InvalidCrawlDataFileException;

import java.io.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * LocalFileSystem implements Storage<File[]>
 *
 * @Responsibility: An abstraction of Local File System loaded on memory
 * @Behavior: This class offers an access to Local File System, which contains crawlDataFiles of 'CrawlData-s'
 * @Instantiation: By calling constructor and 'public boolean connect()' todo: inconsistent instantiation: find a better way.
 * @NOTE: When loading 'CrawlDataFile-s' from File System to generate 'CrawlData-s',
 *        too large size or too many of 'CrawlData-s' may cause 'OutOfMemoryError', or long blocking time(Delay).
 *        Also that could reduce performance of the application.
 */
public class LocalFileSystem implements Storage<File> {

    private static class CrawlDataFilter implements FilenameFilter{
        public final String CRAWL_DATA_EXTENSION = CrawlDataFile.EXTENSION;//'.crawl'
        public final String URLS_EXTENSION =".url";

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
     * Load 'File-s' with '.crawl' extension and read them into 'CrawlDataFile-s'.
     * if 'CrawlDataFile-s' have been loaded, nothing is loaded.
     * @throws InvaildStorageException  when the path is an invalid storage.
     */
    @Override
    public void load() throws StorageLoadException {
        if (!isValid()) throw new StorageLoadException();
        File[] files;
        if (this.crawlDataFiles ==null){
            files =rootDir.listFiles(fileFilter);
            this.crawlDataFiles = new CrawlDataFile[files.length];
            readCrawlDataFiles(files, this.crawlDataFiles);
        }
    }

    /**
     * Reload Files from Local File System.
     * if it had not been loaded before, an exception is thrown.
     * @throws StorageLoadException
     */
    public void reload() throws StorageLoadException {
        CrawlDataFile[] temp =crawlDataFiles;
        loaded = false;
        try {
            load();
        } catch (StorageLoadException e) {
            crawlDataFiles =temp;
            this.loaded =true;
            throw new StorageLoadException();
        }finally {
            /****/
        }
    }

    /**
     * Parse an array of files('.crawl'/'.url') from the specified path into 'CrawlDataFile-s'
     * @Note: Ignores a 'File' that throws an exception by handling the exception in this method
     * @param fromFiles
     * @param toCrawlFiles
     */
    private void readCrawlDataFiles(File[] fromFiles, CrawlDataFile[] toCrawlFiles){
        ObjectInputStream in = null;
        for (int i=0; i<fromFiles.length; i++){
            File fromFile = fromFiles[i];
            String fileName = fromFile.getName();

            try {
                if(fileName.toLowerCase().endsWith(CrawlDataFile.EXTENSION_URL)){
                    toCrawlFiles[i]= parseUrlFile(fromFile);
                }else {
                    //Process '.crawl' files
                    toCrawlFiles[i] = parseCrawlFile(fromFile);
                }
            } catch (CrawlDataFileException e) { continue; }//Handle CrawlDataFileException, FileNotFoundException, ClassNotFoundException
        }
        // TODO: Is it a proper way of closing stream. 
        try {
            if (in!=null) in.close();
        } catch (IOException e) {}
    }

    /**
     * Parses 'UrlFile(.url)' as a CrawlDataFile instance.
     * @param urlFile, which has '.url' extension.
     * @return an instance of 'CrawlDataFile' parsed from a passed 'UrlFile(.url)'
     * @throws CrawlDataFileException if instantiation of 'CrawlDataFile' fails.
     */
    private CrawlDataFile parseUrlFile(File urlFile) throws CrawlDataFileException {
        if (!urlFile.getName().toLowerCase().endsWith(CrawlDataFile.EXTENSION_URL))
            throw new InvalidCrawlDataFileException();
        CrawlDataFile crawlDataFile = new CrawlDataFile(urlFile.toPath());
        return crawlDataFile;
    }

    /**
     * Parses 'CrawlFile(.crawl)' as a CrawlDataFile instance.
     * @param crawlFile, which has '.crawl' extension.
     * @return an instance of 'CrawlDataFile' parsed from a passed 'CrawlFile(.crawl)'
     * @throws CrawlDataFileException if instantiation of 'CrawlDataFile' fails.
     */

    private  CrawlDataFile parseCrawlFile(File crawlFile) throws CrawlDataFileException{
        if (!crawlFile.getName().toLowerCase().endsWith(CrawlDataFile.EXTENSION_URL))
            throw new InvalidCrawlDataFileException();
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(crawlFile));
            CrawlDataFile crawlDataFile = (CrawlDataFile) in.readObject();
            return crawlDataFile;
        } catch (IOException e) {throw new CrawlDataFileException();}
          catch (ClassNotFoundException e) {throw new CrawlDataFileException();}
    }



    /***
     * Gathers 'CrawlData-s' from 'CrawDataFile-s' in a list.
     * @return an instance of 'Iterator<CrawlData>'
     */
    @Override
    public List<CrawlData> getCrawlData() throws StorageLoadException { // TODO: Extract the method into independent interface.
        if (!loaded) {
            load();
            loaded = true;
        }
        LinkedList<CrawlData> crawlDatas = new LinkedList<>();
        for (CrawlDataFile crawlFile : this.crawlDataFiles){
            crawlDatas.addAll(crawlFile.getCrawlData());
        }
        return crawlDatas;
    }

    @Override
    public File getRoot() {
        return rootDir;
    }

    @Override
    public void close() throws IOException {}

    public static void main(String[] args){}
}
