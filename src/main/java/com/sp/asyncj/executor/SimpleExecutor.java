package com.sp.asyncj.executor;

import com.sp.asyncj.tasks.AsyncTask;

import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class SimpleExecutor extends ThreadPoolExecutor implements Executor{

    private static final int THREAD_POOL_SIZE = 10;

    public SimpleExecutor(final int threadPoolSize) {
        super(0, threadPoolSize,0l, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
    }

    public SimpleExecutor() {
        super(0, THREAD_POOL_SIZE,0l, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
    }

    public <OUTPUT_TYPE> AsyncTask<OUTPUT_TYPE> taskFor(Callable<OUTPUT_TYPE> callable){
        return new AsyncTask<OUTPUT_TYPE>(callable, this);
    };

    public <T> AsyncTask<T> submit(AsyncTask<T> tCallable) {
        execute(tCallable);
        return tCallable;
    }


}
