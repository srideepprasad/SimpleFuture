package com.sp.asyncj;

import com.sp.asyncj.callbacks.TaskListener;
import com.sp.asyncj.executor.SimpleExecutor;
import com.sp.asyncj.model.Result;
import com.sp.asyncj.support.Function;
import com.sp.asyncj.tasks.AsyncTask;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class Dummy {
    public static void main(String args[]) throws InterruptedException {
        final SimpleExecutor simpleExecutor = new SimpleExecutor();

        final AsyncTask<Integer> integerAsyncTask = simpleExecutor.taskFor(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                //Real async code
                return 1;
            }
        });

        integerAsyncTask.listen(new TaskListener<Integer>() {
            @Override
            public void onComplete(Result<Integer> result) {
                System.out.println(result.getValue());

                System.out.println(result.map(new Function<Integer, Integer>() {
                    @Override
                    public Integer execute(Integer input) {
                        return input + 1;
                    }
                }).map(new Function<Integer, Double>() {
                    @Override
                    public Double execute(Integer input) {
                        return input / 9d;
                    }
                }).getValue());

            }

            @Override
            public void onCancel() {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });

        integerAsyncTask.execute();
        simpleExecutor.awaitTermination(0, TimeUnit.SECONDS);
        simpleExecutor.shutdown();
    }


}
