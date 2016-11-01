package com.devoo.kim.crawl;


import com.devoo.kim.crawl.exception.NoCrawlingTargetException;
import com.devoo.kim.storage.data.CrawlData;
import com.devoo.kim.storage.data.WebPage;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
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
    private WebPage webPage;

    public WebCrawling(CrawlData crawlData){
        System.out.println("WebCrawling(CrawlData);");//OK
        webPage = (WebPage) crawlData;
    }
    @Override
    public WebPage call() throws NoCrawlingTargetException { // TODO: 16. 10. 22 Add Time-out function to be cancelled.
        String content;
        int status;
        Header[] headers;
        
        WebPage page = webPage;
        try {
            httpGet = new HttpGet(page.urlStr);
            response = httpClient.execute(httpGet);  // TODO: Stuck and Blocked (NOT SOLVED)
            content = new BasicResponseHandler().handleResponse(response);
            status = response.getStatusLine().getStatusCode();
            headers = response.getAllHeaders();
        }catch (IOException e) {
            // TODO: Logging for fail crawling
            // TODO: Set Status and info for failure.
        }finally {
            try { response.close();
            } catch (IOException e) {}
        }

//        page.update(status, headers, content);
        return page; // TODO: Caller handles page to be store. 
    }

    public static void main(String[] args){}
}
