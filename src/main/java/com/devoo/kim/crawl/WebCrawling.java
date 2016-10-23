package com.devoo.kim.crawl;


import com.devoo.kim.storage.data.WebPage;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by devoo-kim on 16. 10. 12.
 */
// TODO: 16. 10. 15 Integrate HttpClinet(for Request and Response over Network)
// TODO:             with jSoup for parsing html dom
public class WebCrawling extends Crawling<WebPage> {
    private AtomicInteger status =new AtomicInteger(Crawler.NOT_INITIALLZED);
    /**Sharing HttpClient among WebCrawling instances to access**/
    static CloseableHttpClient httpClient = HttpClients.createDefault();
    private HttpGet httpGet;
    private CloseableHttpResponse response;

//    public WebCrawling(WebPage crawlData) {
//        super(crawlData);// TODO: 16. 10. 21 Required to fix
//    }

    public WebCrawling() throws Exception {
        super(null);
        throw new Exception();
    }

    @Override
    public WebPage call() throws Exception { // TODO: 16. 10. 22 Add Time-out function to be cancelled. 
        String content;
        int status;
        Header[] headers;
        
        WebPage page = this.crawlData;
        httpGet = new HttpGet(page.urlStr);
        response = httpClient.execute(httpGet);  // TODO: Stuck and Blocked (NOT SOLVED)
        content = new BasicResponseHandler().handleResponse(response);
        status =response.getStatusLine().getStatusCode();
        headers =response.getAllHeaders();
        System.out.println(page.urlStr+" status: "+ status);//// TODO: Log URL and Status of CrawlData
        System.out.println("Content: "+ content);
        response.close();
        page.update(status, headers, content);
        return page; // TODO: Caller handles page to be store. 
    }

    public static void main(String[] args){}
}
