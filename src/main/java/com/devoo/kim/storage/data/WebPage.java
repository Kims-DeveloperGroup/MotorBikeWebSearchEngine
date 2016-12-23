package com.devoo.kim.storage.data;

import org.apache.http.Header;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @Responsibility: This class contains of metadata of a crawled web page,
 *                  and the response headers of the last request.
 */
public class WebPage extends CrawlData {
    private final String DATA_TYPE ="webpage";
    public URL url; // TODO:Make final
    public String urlStr; // TODO:Make final
    int status;
    Header[] headers;
    String document;

    // TODO: variables below are processed while parsing.
    String title; //Unique Name
    Set<String> tags;//References of the link from other pages. (Similar to Foreign Key)
    String domain; //From which search comes.
    int depthFromDomain;
    Map<String, WebPage> outlinks= new HashMap<>(4);// TODO: 16. 10. 18 How-To-Handle
    Set<String> keywords;

    /***CONSTRUCTOR****
     * @param url :url of web page
     * @param status : status code (when being crawled)
     * @param document : HTML DOM of the page
     * @param headers : Response 'Header-s'
     * @throws MalformedURLException
     */

    // TODO: 16. 10. 31 URL Normalizer 
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

    /**Setter**/
    public void setStatus(int status) {
        this.status = status;
    }

    public void setHeaders(Header[] headers) {
        this.headers = headers;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public void setDepthFromDomain(int depthFromDomain) {
        this.depthFromDomain = depthFromDomain;
    }

    public void setOutlinks(Map<String, WebPage> outlinks) {
        this.outlinks = outlinks;
    }

    public void setKeywords(Set<String> keywords) {
        this.keywords = keywords;
    }
    /**Getter**/
    public String getDocument() {
        return document;
    }

    @Override
    public String getDataType() {
        return this.DATA_TYPE;
    }
}
