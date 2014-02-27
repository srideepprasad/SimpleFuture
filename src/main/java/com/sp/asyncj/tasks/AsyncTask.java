package com.sp.asyncj.tasks;

import com.sp.asyncj.callbacks.TaskExceptionListener;
import com.sp.asyncj.callbacks.TaskListener;
import com.sp.asyncj.exceptions.AsyncExcecutionException;
import com.sp.asyncj.exceptions.IllegalOperationException;
import com.sp.asyncj.executor.Executor;
import com.sp.asyncj.model.Result;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class AsyncTask<OUTPUT_TYPE> extends FutureTask<OUTPUT_TYPE> {
    private volatile TaskListener<OUTPUT_TYPE> taskListener;
    private final Executor executor;
    private volatile OUTPUT_TYPE result;
    private volatile TaskExceptionListener taskExeceptionListener;
    private volatile boolean submitted = false;

    public AsyncTask(Callable<OUTPUT_TYPE> callable, Executor executor) {
        super(callable);
        this.executor = executor;
    }

    public AsyncTask<OUTPUT_TYPE> execute(){
        checkIfAlreadySubmitted();
        this.submitted = true;
        return this.executor.submit(this);
    }

    public void listen(TaskListener<OUTPUT_TYPE> taskListener){
        this.taskListener = taskListener;
    }

    public void onException(TaskExceptionListener taskExceptionListener){
        this.taskExeceptionListener = taskExceptionListener;
    }

    public boolean isSubmitted(){
        return submitted;
    }

    @Override
    protected void done() {
        if (isCancelled()){
            if (taskListener!=null)taskListener.onCancel();
        }else
        {
            resolve();
        }
    }


    private void checkIfAlreadySubmitted() {
        if (isSubmitted()){
            throw IllegalOperationException.forDuplicateSubmission();
        }
    }

    private void resolve() {
        try {
            result = get();
            if (taskListener!=null)taskListener.onComplete(new Result<OUTPUT_TYPE>(result));
        } catch (Exception e) {
            if (taskExeceptionListener!=null){
                taskExeceptionListener.onException(e);
            }else
            {
                throw new AsyncExcecutionException(e);
            }
        }
    }



}
