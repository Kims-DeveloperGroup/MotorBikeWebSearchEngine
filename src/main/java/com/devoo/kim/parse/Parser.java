package com.devoo.kim.parse;

import com.devoo.kim.storage.data.CrawlData;
import com.devoo.kim.task.Task;

/**
 * Created by devoo-kim on 16. 10. 12.
 */
public abstract class Parser extends Task {

    public Parser(CrawlData crawlData) {
//        super(crawlData);
    }

    @Override
    public Object call() throws Exception {
        parse();
        return null;
    }

    abstract public void parse();
}
