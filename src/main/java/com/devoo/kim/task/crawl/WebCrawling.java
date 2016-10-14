package com.devoo.kim.task.crawl;


import com.devoo.kim.schedul.Scheduler;
import com.devoo.kim.storage.data.WebPage;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by devoo-kim on 16. 10. 12.
 */
public class WebCrawling extends Crawling {
    CloseableHttpClient httpClient = HttpClients.createDefault();
    HttpGet httpGet;
    CloseableHttpResponse response;

    WebPage page;
    String url;
    char[] content;
    int status;
    Header[] headers;

    public WebCrawling(String taskId, String url) {
        super(taskId);
        page = new WebPage(url, -1, null);
        System.out.println(taskId);
    }

    public WebPage getFetchItem(){
//        WebPage page =null;// Retrieve from source list.
        return page;
    }

    @Override
    public WebPage call() throws Exception {
        WebPage page = getFetchItem();
        httpGet = new HttpGet(page.url.toString());
        response = httpClient.execute(httpGet);  // TODO: Stuck and Blocked (NOT SOLVED)
        status =response.getStatusLine().getStatusCode();
        headers =response.getAllHeaders();
        System.out.println(url+" status: "+ status);
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            writeContent(reader, content);
        } catch (IOException e) {
        }finally {
            response.close();
        }
        page.update(status, headers, content);
        return page;
    }

    public void writeContent(Reader reader, char[] content) throws IOException {
        content = new char[1024*100];
        int input;
        int offset=0;
        int length = content.length;
        while ((input=reader.read(content,offset, length))!=-1 && length>0){
            offset+= input;
            length-= input;
        }
    }

    public static void main(String[] args){
        Scheduler<WebPage> scheduler = new Scheduler<>(1);
        Future<WebPage>  future =scheduler.submitTask(new WebCrawling("id", "http://www.naver.com/"));
    }
}
