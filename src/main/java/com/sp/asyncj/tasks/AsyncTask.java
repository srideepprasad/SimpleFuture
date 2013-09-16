package com.sp.asyncj.tasks;

import com.sp.asyncj.callbacks.TaskExceptionListener;
import com.sp.asyncj.callbacks.TaskListener;
import com.sp.asyncj.exceptions.AsyncExcecutionException;
import com.sp.asyncj.exceptions.IllegalOperationException;
import com.sp.asyncj.executor.Executor;
import com.sp.asyncj.model.Result;
import com.sp.asyncj.support.Composable;
import com.sp.asyncj.support.Function;
import com.sp.asyncj.support.internal.CallableFunction;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class AsyncTask<OUTPUT_TYPE> extends FutureTask<OUTPUT_TYPE> implements Composable<OUTPUT_TYPE> {
    private TaskListener<OUTPUT_TYPE> taskListener;
    private Executor executor;
    private OUTPUT_TYPE result;
    private AsyncTask<?> mapTask;
    private TaskExceptionListener taskExeceptionListener;
    private boolean submitted = false;

    public AsyncTask(Callable<OUTPUT_TYPE> callable, Executor executor) {
        super(callable);
        this.executor = executor;
    }

    private AsyncTask(Callable<OUTPUT_TYPE> callable, Executor executor, TaskExceptionListener taskExeceptionListener) {
        super(callable);
        this.executor = executor;
        this.taskExeceptionListener = taskExeceptionListener;
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


    @Override
    public void run() {
        callChainedTaskIfDefined();
        super.run();
    }

    @Override
    public OUTPUT_TYPE getValue() {
        return result;
    }

    @Override
    public <FUNCTION_OUTPUT_TYPE> AsyncTask<FUNCTION_OUTPUT_TYPE> map(Function<OUTPUT_TYPE, FUNCTION_OUTPUT_TYPE> function) {
        checkIfCanBeMapped();
        mapTask = new AsyncTask<FUNCTION_OUTPUT_TYPE>(new CallableFunction<OUTPUT_TYPE, FUNCTION_OUTPUT_TYPE>(function, this), this.executor, this.taskExeceptionListener);
        return (AsyncTask<FUNCTION_OUTPUT_TYPE>) mapTask;
    }

    private void checkIfCanBeMapped() {
        if (isSubmitted()){
            throw IllegalOperationException.forInvalidMappingOperation();
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

    private void callChainedTaskIfDefined() {
        if (mapTask!=null)mapTask.execute();
    }


}
