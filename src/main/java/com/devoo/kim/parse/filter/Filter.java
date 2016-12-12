package com.devoo.kim.parse.filter;

import java.util.Collection;

/**
 * @Responsibility: Filters links
 */
public interface Filter {

    /**
     * Evaluates if a given url of link is filtered out.
     * @param linkUrl
     * @return true if a link is filtered out, of false if it is not.
     */
    boolean filter(String linkUrl);

    /**
     * Evaluates a collection of links if they are filtered out or not.
     * If there exists any link to be filtered out, that is removed from the 'Collection'
     * @param links collection of links to be filtered.
     */
    void filter(Collection links);
}
