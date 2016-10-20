package com.devoo.kim.injection;

import com.devoo.kim.storage.Storage;

/**
 * Created by devoo-kim on 16. 10. 14.
 *
 * At frist, Injection Task runs in a single thread.
 */
public class Injector extends Thread {

    Storage source;
    public Injector(String inputPath, String outputPath){ //Storage contains loaded data from File system, DB, etc.

    }
    @Override
    public void run() {
        super.run();
    }
}
