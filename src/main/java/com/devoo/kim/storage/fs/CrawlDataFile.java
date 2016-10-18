package com.devoo.kim.storage.fs;

import com.devoo.kim.storage.data.CrawlData;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

import static java.lang.System.in;

/**
 * Created by devoo-kim on 16. 10. 18.
 */
public class CrawlDataFile implements Serializable{ // TODO: Handle Overwrite && Update Operation. 


    public static final String EXTENSION =".crawl";
    File file;
    String pathStr;
    private List<CrawlData> listOfCrawlData = new LinkedList<>();
    transient Path path;


    public CrawlDataFile(Path path, boolean overwrite) throws Exception {
        this(path.toString(), overwrite);
    }

    public CrawlDataFile(String pathname, boolean overwrite) throws Exception {
        if (!pathname.toLowerCase().endsWith(EXTENSION)) this.pathStr =pathname+EXTENSION;
        this.path=Paths.get(this.pathStr);
        this.file = path.toFile();
        if (this.file.exists() && !overwrite) loadFile();
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

    public void createNewFile(boolean overwrite) throws IOException {
        if (overwrite &&file.exists()) Files.delete(this.path);
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
        out.writeObject(this);
    }

    public List<CrawlData> getListOfCrawlData() {
        return listOfCrawlData;
    }
}
