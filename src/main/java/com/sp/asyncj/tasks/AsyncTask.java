package com.sp.asyncj.tasks;

import com.sp.asyncj.executor.Executor;
import com.sp.asyncj.model.Result;
import com.sp.asyncj.support.Callback;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class AsyncTask<OUTPUT_TYPE> extends FutureTask<OUTPUT_TYPE> {
    private Callback<OUTPUT_TYPE> callback;
    private Executor executor;
    private OUTPUT_TYPE result;

    public AsyncTask(Callable<OUTPUT_TYPE> callable, Executor executor) {
        super(callable);
        this.executor = executor;
    }


    public AsyncTask<OUTPUT_TYPE> execute(){
        return this.executor.submit(this);
    }

    public void onFinish(Callback<OUTPUT_TYPE> callback){
        this.callback = callback;
    }

    @Override
    protected void done() {
        if (callback == null) return;
        if (isCancelled()){
            callback.onCancel();
        }else
        {
            resolve();
        }
    }


    private void resolve() {
        try {
            result = get();
            callback.onComplete(new Result<OUTPUT_TYPE>(result));
        } catch (Exception e) {
            callback.onException(e);
        }
    }

}
