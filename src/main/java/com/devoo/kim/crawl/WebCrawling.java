package com.devoo.kim.crawl;


import com.devoo.kim.storage.CrawlData;
import com.devoo.kim.storage.WebPage;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.*;

/**
 * Created by devoo-kim on 16. 10. 12.
 */
public class WebCrawling extends CrawlTask {
    CloseableHttpClient httpClient = HttpClients.createDefault();
    HttpGet httpGet;
    CloseableHttpResponse response;

    char[] content;
    int status;
    Header[] headers;

    public WebCrawling(String taskId) {
        super(taskId);
    }

    public WebPage getFetchItem(){
        WebPage page =null;// Retrieve from source list.
        return page;
    }

    @Override
    public WebPage call() throws Exception {
        WebPage page = getFetchItem();
        httpGet = new HttpGet(page.url.toString());
        response = httpClient.execute(httpGet);
        status =response.getStatusLine().getStatusCode();
        headers =response.getAllHeaders();
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
    }
}
