package com.devoo.kim.crawl.event;

import com.devoo.kim.storage.data.CrawlData;

/**
 * Created by devoo-kim on 16. 10. 22.
 */
public interface TaskListener {

    public void completeTask(CrawlData result);
}
