package com.sp.simplefuture.executor;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class DefaultExecutor extends ThreadPoolExecutor{

    private static final int MAX_THREAD_POOL_SIZE = 20;
    public static final int MIN_THREAD_POOL_SIZE = 1;
    private static volatile DefaultExecutor instance;

    private DefaultExecutor() {
        super(MIN_THREAD_POOL_SIZE, MAX_THREAD_POOL_SIZE,0l, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
    }

    public static DefaultExecutor getInstance(){
        if ((instance == null) || (instance.isShutdown())){
            instance = new DefaultExecutor();
        }
        return instance;
    }
}
