package com.devoo.kim.storage.fs;

import com.devoo.kim.storage.data.CrawlData;
import com.devoo.kim.storage.data.WebPage;
import com.devoo.kim.storage.fs.exception.CrawlDataFileException;
import com.devoo.kim.storage.fs.exception.CrawlDataFileReadOnlyExcepation;
import com.devoo.kim.storage.fs.exception.InvalidCrawlDataFileException;

import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * @NOTE:
 * 1. CopyOnWrite Operation is employed when providing list of 'CrawlData' in the file.
 * 2. On written, the file cannot be modified(Read-only).
 */
public class CrawlDataFile implements Serializable{


    public static final String EXTENSION =".crawl";
    public static final String EXTENSION_URL =".url";
    private boolean written =false;
    private File file;
    private String absolutePath;
    private LinkedList<CrawlData> listOfCrawlData = new LinkedList<>();


    public CrawlDataFile(Path path) throws CrawlDataFileException {
        this(path.toString());
    }

    public CrawlDataFile(String pathStr) throws CrawlDataFileException {
        if (!pathStr.toLowerCase().endsWith(EXTENSION)
                && !pathStr.toLowerCase().endsWith(EXTENSION_URL)) throw new InvalidCrawlDataFileException();

        file = new File(pathStr).getAbsoluteFile();
        absolutePath = file.getAbsolutePath();

        if (file.getName().toLowerCase().endsWith(EXTENSION_URL)){
            convertUrlFileToCrawlDataFile(file);
        }
    }

    private void convertUrlFileToCrawlDataFile(File urlFile) throws CrawlDataFileException {
        if (!urlFile.getName().toLowerCase().endsWith(CrawlDataFile.EXTENSION_URL)
                || !urlFile.exists())
            throw new InvalidCrawlDataFileException();
        Path currentPath = urlFile.toPath();
        String currentName = urlFile.getName();
        String parent =currentPath.getParent().toString();
        try {
            String newFileName = currentName.replaceFirst(EXTENSION_URL, EXTENSION);
            Path newPath = Paths.get(parent, newFileName).toAbsolutePath();
            List<String> urls = Files.readAllLines(currentPath);
            //Format a CrawlDataFile with '.url' file
            file = new File(newPath.toString()).getAbsoluteFile();
            absolutePath = file.getAbsolutePath();
            addUrlAsCrawlData(urls);
        } catch (IOException e) {
            throw new CrawlDataFileException();
        }
    }

    /**
     * Only called in Constructor
     * @throws CrawlDataFileReadOnlyExcepation if the 'CrawlDataFile' is not readable, and has been written already.
     */
    private void addUrlAsCrawlData(Collection<String> urls) throws CrawlDataFileReadOnlyExcepation {
        // TODO: 16. 10. 31 URL Normalization Required
        for (String url : urls){
            try {
                add(new WebPage(url));
            } catch (MalformedURLException e) {continue;}
        }
    }

    /***
     * Creates a new file in a given path and write data of 'CrawlDataFile'
     * it throws exception if
     * @throws IOException
     */
    public void createNewFile() throws IOException {
        if (this.written) throw new IOException();
        if (file.createNewFile()){
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
            out.writeObject(this);
            file.setReadOnly();
            written =true;
        }
    }

    /**
     * Add a unit of 'CrawlData' into newly created 'CrawlDataFile'.
     * If the 'CrawlDataFile' has been already written before, it throws an exception.
     * @NOTE: Addtition is only allowed to a new 'CrawlDataFile'.
     *
     * @param crawlData
     * @throws Exception
     */
    public void add(CrawlData crawlData) throws CrawlDataFileReadOnlyExcepation {
        if (written) throw new CrawlDataFileReadOnlyExcepation();
        listOfCrawlData.add(crawlData);
    }

    public void add(Collection<? extends CrawlData> crawlData) throws Exception {
        if (written) throw new Exception();
        listOfCrawlData.addAll(crawlData);
    }

    public boolean isValid(){
        if (!this.file.exists() || !file.canRead()) return false;
        return true;
    }

    /***
     *
     * @return a clone of 'LinkedList<CrawlData>' for 'CopyOnWrite' Todo: Think about using 'Immutable Collection' instead.
     */
    public LinkedList<CrawlData> getListOfCrawlData() {
        return (LinkedList<CrawlData>)listOfCrawlData.clone();
    }
}
