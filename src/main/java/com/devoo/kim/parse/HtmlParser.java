package com.devoo.kim.parse;

import com.devoo.kim.storage.data.CrawlData;
import com.devoo.kim.storage.data.WebPage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @Responsibility:
 * Parses documents of web pages.
 */
public class HtmlParser implements Parser {

    public HtmlParser(CrawlData crawlData) {
    }

    /**
     * Parses a html page.
     * Out-links and texts in a document ara analyzed.
     * Filtered out-links are added.
     * By Analyzing content of the document, the given page is determined to be stored or not.
     * @param page
     */
    public void parse(WebPage page){
        Element body = Jsoup.parse(page.getDocument()).body();
        Elements outlinks = body.getElementsByTag("a");
        for (Element outlink : outlinks){

        }
    }
}
