package com.sp.simplefuture.tasks;

import com.sp.simplefuture.callbacks.NoOpResultHandler;
import com.sp.simplefuture.callbacks.TaskResultHandler;
import com.sp.simplefuture.executor.DefaultExecutor;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

public class AsyncTaskDef<OUTPUT_TYPE>{
    private final Callable<OUTPUT_TYPE> callable;
    private final Executor executor;

    private TaskResultHandler<OUTPUT_TYPE> taskResultHandler;

    private AsyncTaskDef(Callable<OUTPUT_TYPE> callable, Executor executor) {
        this.callable = callable;
        this.executor = executor;
    }

    public static <T>  AsyncTaskDef<T> forTask(Callable<T> callable){
        return new AsyncTaskDef<T>(callable, DefaultExecutor.getInstance());
    }

    public static <T>  AsyncTaskDef<T> forTask(Callable<T> callable, ExecutorService executor){
        return new AsyncTaskDef<T>(callable, executor);
    }

    public AsyncTask<OUTPUT_TYPE> execute(){
        final AsyncTask<OUTPUT_TYPE> asyncTask = new AsyncTask<OUTPUT_TYPE>(callable, (taskResultHandler != null) ? taskResultHandler : new NoOpResultHandler<OUTPUT_TYPE>());
        executor.execute(asyncTask);
        return asyncTask;
    }

    /*
    Synchronized not really needed here - just put to ensure that if multiple threads try to set this simultaneously (wonder who'll do that?), our code wont break
     */
    public synchronized void resultTo(TaskResultHandler<OUTPUT_TYPE> taskResultHandler){
        this.taskResultHandler = taskResultHandler;
    }

}
