package com.sp.simplefuture.tasks;

import com.sp.simplefuture.callbacks.TaskResultHandler;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.FutureTask;

/*
Note: No tests for this class - it is excercised via AsyncTaskDefTest.
 */
public class AsyncTask<OUTPUT_TYPE> extends FutureTask<OUTPUT_TYPE> {
    private final TaskResultHandler<OUTPUT_TYPE> taskResultHandler;
    private final Executor executor;
    private volatile OUTPUT_TYPE result;

    AsyncTask(Callable<OUTPUT_TYPE> callable, TaskResultHandler<OUTPUT_TYPE> taskResultHandler, Executor executor) {
        super(callable);
        this.taskResultHandler = taskResultHandler;
        this.executor = executor;
    }

    @Override
    protected void done() {
        if (isCancelled()) {
            taskResultHandler.onCancel();
        } else {
            resolve();
        }
    }

    private void resolve() {
        try {
            result = get();
            taskResultHandler.onComplete(result);
        } catch (Exception e) {
            taskResultHandler.onException(e);
        }
    }

    public Executor getExecutor(){
        return executor;
    }


}
