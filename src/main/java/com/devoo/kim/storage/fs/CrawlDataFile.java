package com.devoo.kim.storage.fs;

import com.devoo.kim.storage.data.CrawlData;
import com.devoo.kim.storage.data.WebPage;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

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


    public CrawlDataFile(Path path) throws Exception {
        this(path.toString());
    }

    public CrawlDataFile(String pathStr) throws Exception {
        if (!pathStr.toLowerCase().endsWith(EXTENSION)
                || !pathStr.toLowerCase().endsWith(EXTENSION_URL)) throw new Exception();

        this.file = new File(pathStr).getAbsoluteFile();
        this.absolutePath =pathStr;
        if (pathStr.endsWith(EXTENSION_URL)){
            //Read Lines of urls written in the file.
            List<String> urls =Files.readAllLines(Paths.get(absolutePath));
            //Instantiates CrawlData with list of urls.
            convertUrlToCrawlData(urls);
            //Create a New 'CrawlDataFile'
            String newPathStr = Paths.get(absolutePath).getParent().toString()+EXTENSION;
            //Set the path of newly created 'CrawlDataFile(this)' and Create a File('.crawl').
            file = new File(newPathStr).getAbsoluteFile();
            absolutePath = file.getAbsolutePath();
            createNewFile();
        }
    }

    /**
     * Only called in Constructor
     * @throws Exception if the 'CrawlDataFile' is not new, and has been written already.
     */
    private void convertUrlToCrawlData(Collection<String> urls) throws Exception {
        if (this.written) throw  new Exception();
        for (String url : urls){
            add(new WebPage(url));
        }
        this.written =true;
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
    public void add(CrawlData crawlData) throws Exception {
        if (written) throw new Exception();
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
