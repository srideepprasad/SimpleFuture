package com.sp.simplefuture.tasks;

import com.sp.simplefuture.callbacks.NoOpResultHandler;
import com.sp.simplefuture.callbacks.TaskResultHandler;
import com.sp.simplefuture.executor.DefaultExecutor;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

/*
Defines a AsyncTask (Future)
Allows creating a Future (background) task for the given Callable. Can use DefaultExecutor if none is provided, or can plug into any other executor.
Defines a method resultTo(), which specifies the TaskResultHandler callback on which to report task notifications / completion.
 */
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

    public AsyncTask<OUTPUT_TYPE> submit(){
        final AsyncTask<OUTPUT_TYPE> asyncTask = new AsyncTask<OUTPUT_TYPE>(callable, (taskResultHandler != null) ? taskResultHandler : new NoOpResultHandler<OUTPUT_TYPE>(), executor);
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
