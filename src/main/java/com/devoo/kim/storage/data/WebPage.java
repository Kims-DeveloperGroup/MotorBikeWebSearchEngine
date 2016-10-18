package com.devoo.kim.storage.data;

import org.apache.http.Header;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by devoo-kim on 16. 10. 12.
 */
public class WebPage extends CrawlData {
    public URL url;
    public String urlStr;
    private Map<String, WebPage> outlinks= new HashMap<>(4);// TODO: 16. 10. 18 How-To-Handle 
    int staus;
    Header[] haeders;
    String body;

    public WebPage(String url, int status, Header[] headers, String bodyContent){
        this(url, status, bodyContent);
        headers = headers;
    }

    public WebPage(String url, int status, String body){
        this(url);
        this.staus=status;
        this.body =body;
    }

    public WebPage(String url){
        this.urlStr=url;
        try {
            this.url = new URL(url);
        } catch (MalformedURLException e) {} // TODO: Handle Malformed Format of URL
    }

    public void update(int staus, Header[] headers, String body) {
        super.update();
        System.out.println("URL:"+urlStr+" Status:"+ staus);//// TODO: 16. 10. 15 Logging 
        //set Value with prams
    }
}
