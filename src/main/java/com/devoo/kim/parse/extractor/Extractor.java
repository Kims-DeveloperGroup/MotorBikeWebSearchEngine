package com.devoo.kim.parse.extractor;

/**
 * Extracts data from text of document.
 */
public interface Extractor {

    String[] extract(String document);
}
