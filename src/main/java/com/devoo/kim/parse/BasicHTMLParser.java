package com.devoo.kim.parse;

import com.devoo.kim.parse.filter.Filter;
import com.devoo.kim.parse.extractor.Extractor;
import com.devoo.kim.storage.data.CrawlData;
import com.devoo.kim.storage.data.WebPage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @Responsibility:
 * Parses documents of web pages.
 */
public class BasicHTMLParser implements Parser {
    Filter filter;
    Extractor extractor;

    public BasicHTMLParser(CrawlData crawlData) {
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
        filter.filter(outlinks);
        extractor.extract(body.outerHtml());
        //Provides out-links to 'CrawlingGenerator', so that additional links will be crawled.
        // TODO:
        // outlinks are managed shortly after the current page is finished, or
        // are waiting to be scheduled later.
        // If they are being waited, think about a way of scheduling in 'CrawlingGenerator'
    }
}
