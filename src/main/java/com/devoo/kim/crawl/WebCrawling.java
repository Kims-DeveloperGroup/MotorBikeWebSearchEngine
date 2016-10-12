package com.devoo.kim.crawl;


import com.devoo.kim.storage.WebPage;
import org.apache.http.HttpConnection;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.DefaultHttpServerConnection;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import sun.net.www.http.HttpClient;

import java.io.*;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by devoo-kim on 16. 10. 12.
 */
public class WebCrawling extends CrawlTask {
    byte[] content;

    public WebCrawling(String taskId) {
        super(taskId);
    }

    @Override
    public WebPage call() throws Exception {
        CloseableHttpClient httpclient = HttpClients.createDefault();

        HttpGet httpGet = new HttpGet("http://www.naver.com");
        try {
            CloseableHttpResponse response = httpclient.execute(httpGet);
            System.out.println(response.getStatusLine());
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            File file = new File("test.txt");
            FileWriter writer = new FileWriter(file);
            String input;
            while ((input = reader.readLine())!=null){
                writer.write(input);
                System.out.println(input);
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public WebPage call2(){
        CloseableHttpClient httpclient = HttpClients.createDefault();

        HttpGet httpGet = new HttpGet("http://www.naver.com");
        try {
            CloseableHttpResponse response1 = httpclient.execute(httpGet);
            System.out.println(response1.getStatusLine());
            BufferedInputStream reader = new BufferedInputStream(response1.getEntity().getContent());
            byte[] contents = new byte[1024]; // 100Kb

            int input= reader.read(contents);
            int offset= input;
            int max = contents.length-input;

            while ((input = reader.read(contents,offset, max-offset)) != -1
                    || max<=0){
                System.out.println(offset);
                offset += input;
                max -= input;
            }


            File file = new File("test2.txt");
//            FileWriter writer = new FileWriter(file);
            FileOutputStream out = new FileOutputStream(file);
            out.write(contents);
            out.flush();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    public static void main(String[] args){
        WebCrawling cralwer = new WebCrawling("Naver Crawler");
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        executorService.submit(cralwer);

    }
}
