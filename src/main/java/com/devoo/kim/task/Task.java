package com.devoo.kim.task;

import java.util.concurrent.Callable;

/**
 * Created by devoo-kim on 16. 10. 12.
 */
abstract public class Task<T1> implements Callable<T1>, Runnable{
    public static final byte NOT_IN_WORK=0;
    public static final byte STANDBY=1;
    public static final byte WORKING=2;
    public static final byte DELAYING=3;
    public static final byte FINISHED=4; //

    private int status=0;
    private String taskId="";
    private int startTime=-1; //In Mills
    private int finishTime=-1;
    private int totalTime=-1;
    private int activatedTime=-1;
    private int devativatedTime=-1;
    private int throughput=-1; //Byte

    public Task(String taskId){
        this.taskId = taskId;
    }

    @Override
    abstract public T1 call() throws Exception;

    @Override
    public void run() {
        try {
            call();
        } catch (Exception e) {}
    }
}
