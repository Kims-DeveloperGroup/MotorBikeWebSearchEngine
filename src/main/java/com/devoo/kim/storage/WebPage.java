package com.devoo.kim.storage;

import org.apache.http.Header;
import org.apache.http.HttpEntity;

import java.net.URL;

/**
 * Created by devoo-kim on 16. 10. 12.
 */
public class WebPage extends CrawlData {
    public URL url;
    int staus;
    Header[] haeders;
    char[] body;

    public WebPage(URL url, int status, Header[] headers, char[] body){
    }

    public WebPage(String url, int status, char[] body){
    }

    public void update(int staus, Header[] headers, char[] body) {
        super.update();
        //set Value with prams
    }
}
