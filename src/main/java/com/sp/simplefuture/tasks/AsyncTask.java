package com.sp.simplefuture.tasks;

import com.sp.simplefuture.callbacks.TaskResultHandler;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.FutureTask;

/*
Note: No tests for this class - it is exercised via AsyncTaskDefTest.
This class implements the Java Future interface. Represents a running background task. Has an extra method getExecutor(), to allow
end users to get hold of the executor running this task - may be needed if one uses the default executor, and needs to control its thread pool.
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
