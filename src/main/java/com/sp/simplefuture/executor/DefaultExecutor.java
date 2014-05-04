package com.sp.simplefuture.executor;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/*
Default Executor implementation
Maintains a thread pool of max 20 threads.
 */
public class DefaultExecutor extends ThreadPoolExecutor{

    private static final int MAX_THREAD_POOL_SIZE = 20;
    public static final int MIN_THREAD_POOL_SIZE = 1;
    private static volatile DefaultExecutor instance;

    private DefaultExecutor() {
        super(MIN_THREAD_POOL_SIZE, MAX_THREAD_POOL_SIZE,0l, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
    }

    /*
    Returns an instance of ThreadPoolExecutor
    Returns a singleton instance if the executor is running. If shutdown, returns a new singleton instance
     */
    public static DefaultExecutor getInstance(){
        if ((instance == null) || (instance.isShutdown())){
            instance = new DefaultExecutor();
        }
        return instance;
    }
}
