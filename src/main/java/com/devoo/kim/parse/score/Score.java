package com.devoo.kim.parse.score;

/**
 * Scores a given document.
 * todo: For what?
 */
public interface Score {
    /**
     * Scores a document
     * @param document
     * @return a score of document.
     */
    int score(String document);
}
