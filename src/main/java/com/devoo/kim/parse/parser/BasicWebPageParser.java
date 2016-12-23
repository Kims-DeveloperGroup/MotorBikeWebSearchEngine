package com.devoo.kim.parse.parser;

import com.devoo.kim.parse.filter.Filter;
import com.devoo.kim.parse.extractor.Extractor;
import com.devoo.kim.parse.score.Score;
import com.devoo.kim.storage.data.WebPage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @Responsibility:
 * Parses documents of web pages with defined linkFilter, extractor and score.
 */
public class BasicWebPageParser implements Parser {
    Filter linkFilter;
    Extractor extractor;
    Score score;

    public BasicWebPageParser(Filter linkFilter, Extractor extractor, Score score) {
        this.linkFilter = linkFilter;
        this.extractor =extractor;
        this.score = score;
    }


    /**
     * Parses a web page in three steps by using predefined tools .
     * 1) Filters 'out-links' by a predefined filter and updates the links in the page.
     * 2) Extracts keywords from the document.
     * 3) Scores the page by analyzing the page.
     * @param page to be parsed and updated.
     */
    public void parse(WebPage page){
        Element doc= Jsoup.parse(page.getDocument());
        String title = doc.getElementsByTag("title").text();
        Elements outlinks = doc.getElementsByTag("a");

        // TODO: 16. 12. 23 Add out-links.
        String[] keywords;
        linkFilter.filter(outlinks);
        keywords = extractor.extract(doc.outerHtml());
//        score.score()
    }
}
