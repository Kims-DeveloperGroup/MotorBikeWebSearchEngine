package com.devoo.kim.storage.fs;

import com.devoo.kim.storage.data.CrawlData;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @NOTE: CopyOnWrite Operation is employed.
 */
public class CrawlDataFile implements Serializable{ // TODO: Handle Overwrite && Update Operation. 


    public static final String EXTENSION =".crawl";
    File file;
    String pathStr;
    private LinkedList<CrawlData> listOfCrawlData = new LinkedList<>();
    transient Path path;


    public CrawlDataFile(Path path) throws Exception {
        this(path.toString());
    }

    public CrawlDataFile(String pathname) throws Exception {
        if (!pathname.toLowerCase().endsWith(EXTENSION)) this.pathStr =pathname+EXTENSION;
        this.path=Paths.get(this.pathStr);
        this.file = path.toFile();
    }

    /**
     *
     * @return a clone of 'Iterator<CrawlData>' for 'CopyOnWrite'
     */

    public Iterator<CrawlData> iterateCrawlData(){
        LinkedList<CrawlData> clone = (LinkedList<CrawlData>) listOfCrawlData.clone();
        return clone.iterator();
    }

    public void add(CrawlData crawlData){
        listOfCrawlData.add(crawlData);
    }

    private void loadFile() throws IOException, ClassNotFoundException {
        CrawlDataFile instance;
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
        instance =(CrawlDataFile)in.readObject();
        this.listOfCrawlData =instance.getListOfCrawlData();
        in.close();
    }

    // TODO: 16. 10. 19 Whether to overwrite or not is flaged here.
    public void createNewFile(boolean overwrite) throws IOException {
        if (overwrite &&file.exists()) Files.delete(this.path);
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
        out.writeObject(this);
    }

    /***
     *
     * @return a clone of 'LinkedList<CrawlData>' for 'CopyOnWrite' Todo: Think about using 'Immutable Collection' instead.
     */
    public LinkedList<CrawlData> getListOfCrawlData() {
        return (LinkedList<CrawlData>)listOfCrawlData.clone();
    }
}
