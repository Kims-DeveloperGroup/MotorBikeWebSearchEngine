package com.devoo.kim.storage.data;

import org.apache.http.Header;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * @Responsibility: This class contains of metadata of a crawled web page,
 *                  and the response headers of the last request.
 */
public class WebPage extends CrawlData {
    private final String DATA_TYPE ="WebPage";
    public URL url; // TODO:Make final
    public String urlStr; // TODO:Make final
    private Map<String, WebPage> outlinks= new HashMap<>(4);// TODO: 16. 10. 18 How-To-Handle 
    int status;
    Header[] headers;
    String document;

    /***CONSTRUCTOR****
     * @param url :url of web page
     * @param status : status code (when being crawled)
     * @param document : HTML DOM of the page
     * @param headers : Response 'Header-s'
     * @throws MalformedURLException
     */
    public WebPage(String url, int status, String document, Header[] headers) throws MalformedURLException {
        this(url, status, document);
        this.headers = headers;
    }

    public WebPage(String url, int status, String document) throws MalformedURLException {
        this.urlStr=url;
        this.url = new URL(url);
        this.status =status;
        this.document =document;
    }

    public WebPage(String url) throws MalformedURLException{
        this.urlStr=url;
        this.url = new URL(url);
    }

    public void update(int status, Header[] headers, String document) {
        super.update();
        this.status = status;
        this.headers =headers;
        this.document =document;
//      TODO: 16. 10. 15 Logging
    }

    @Override
    public String getDataType() {
        return this.DATA_TYPE;
    }
}
