package com.devoo.kim.task.crawl;


import com.devoo.kim.storage.data.WebPage;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * Created by devoo-kim on 16. 10. 12.
 */
// TODO: 16. 10. 15 Integrate HttpClinet(for Request and Response over Network)
// TODO:             with jSoup for parsing html dom
public class WebCrawling extends Crawling {



    CloseableHttpClient httpClient = HttpClients.createDefault();
    HttpGet httpGet;
    CloseableHttpResponse response;

    WebPage page;
    String url;
    String content;
    int status;
    Header[] headers;

    public WebCrawling(String taskId, String url) {
        super(taskId);
        page = new WebPage(url);
        System.out.println(taskId);
    }

    public WebPage getFetchItem(){
//        WebPage page =null;// Retrieve from source list.
        return page;
    }

    @Override
    public WebPage call() throws Exception {
        WebPage page = getFetchItem();
        httpGet = new HttpGet(page.urlStr);
        response = httpClient.execute(httpGet);  // TODO: Stuck and Blocked (NOT SOLVED)
        content = new BasicResponseHandler().handleResponse(response);
        status =response.getStatusLine().getStatusCode();
        headers =response.getAllHeaders();
        System.out.println(url+" status: "+ status);
        System.out.println("Content: "+ content);
        response.close();
        page.update(status, headers, content);
        return page; // TODO: Caller handles page to be store. 
    }

    public static void main(String[] args){
        String url = "http://www.naver.com/";
        WebCrawling crawling = new WebCrawling("", url);
        try {
            crawling.call();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
