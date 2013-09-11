package com.sp.asyncj.executor;

import com.sp.asyncj.tasks.AsyncTask;

public interface Executor {

    public <T> AsyncTask<T> submit(AsyncTask<T> tCallable);

}
