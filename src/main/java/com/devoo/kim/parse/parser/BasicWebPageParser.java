package com.devoo.kim.parse.parser;

import com.devoo.kim.parse.filter.Filter;
import com.devoo.kim.parse.extractor.Extractor;
import com.devoo.kim.parse.score.Score;
import com.devoo.kim.storage.data.WebPage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Map;

/**
 * @Responsibility:
 * Parses documents of web pages with defined linkFilter, outlinkExtractor and score.
 */
public class BasicWebPageParser implements Parser<WebPage> {
    Filter linkFilter;
    Extractor outlinkExtractor;
    Extractor keywordExtractor;
    // Todo : Filters[] filters
    // TODO: Extractor[] extractors;
    // Todo : How to distinguish type of filters and extractors
    Score score;

    public BasicWebPageParser(Filter linkFilter, Extractor outlinkExtractor, Score score) {
        this.linkFilter = linkFilter;
        this.outlinkExtractor = outlinkExtractor;
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
        page.setTitle(title);
        Elements outlinks = doc.getElementsByTag("a");

        // TODO: 16. 12. 23 Add out-links.
        String[] keywords;
        if (linkFilter!=null)
            linkFilter.filter(outlinks);
        page.setOutlinks(null);
        if (outlinkExtractor !=null)
            keywords = outlinkExtractor.extract(doc.outerHtml());
        if (score!=null) {
            // TODO: Define the purpose of 'Score'.(ex: Priorioty or Accuracy ?)
            score.score(doc.outerHtml());
        }
    }

}
