package com.devoo.kim.parse.parser;

/**
 * Parses a given 'CrawlData'
 */
public abstract interface Parser<DataTypeToParse>{

    public void parse(DataTypeToParse data);
}
