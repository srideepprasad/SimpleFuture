SimpleFuture [![Build Status](https://travis-ci.org/srideepprasad/SimpleFuture.png?branch=master)](https://travis-ci.org/srideepprasad/SimpleFuture)
======
####The Future's simple - once you "un-block" it !

Simple library to demonstrate 'non-blocking' future's in Java. No need of Future.get() anymore - instead rely on dynamic notification for completion/cancellation/failure

Demonstrates the techniques by which fancy Future abstractions are provided by various JVM languages / frameworks - at the core, all these rely on the standard Java ExecutorService/ThreadPoolExecutor/Future classes in some way or the other

NOTE: Java 8 includes the CompletableFuture class which is a huge step forward, and may be worth a look.

#####Usage
***
Usage is straightfoward, and self explanatory (if you have used Executors/Futures before especially)

* __Step 1 - Create a Task Definition__

Create an AsyncTaskDef for a standard Java callable task

```
        AsyncTaskDef<Double> taskDef = AsyncTaskDef.forTask(callableTask);
```
By default, an instance of DefaultExecutor is used, which provides a max thread pool of 20 threads. If required, you could use any other standard java executor as well

```
        AsyncTaskDef<Double> taskDef = AsyncTaskDef.forTask(callableTask,someExecutor);
```

* __Step 2 (Optional) - Setup async notification callbacks__

Setup a TaskResultHandler which would receive callback notifications (on task completion/cancellation/exception). These will be executed on the same thread which executed the callable task

```
        taskDef.resultTo(new TaskResultHandler<Double>() {
            @Override public void onComplete(Double result) {
                //Handle result
            }

            @Override public void onCancel() {
                //Handle cancellation
            }

            @Override public void onException(Exception ex) {
                //Handle exception
            }
        });
```
If no TaskResultHandler is defined, a NoOpTaskResultHandler instance is used internally (does nothing).
Alternately, you could also extend the NoOpTaskResultHandler and only override the methods one is interested in

* __Step 3 - Execution__

You are ready to go ! Just call execute()
```
  AsyncTask task = taskDef.execute();
```
AsyncTask is an implementation of standard Java Future interface. So all standard Future / Executor related constructs work as per official JDK specs.
And if you want to relive the old "blocking" Future, you can still call AsyncTask.get() !

* __Step 4 - (Optional) Control the default executor__

In case you need an instance of the default Executor, you may get an instance of the same by calling :
```
  DefaultExecutor.getInstance()
```
This method would return the sample Executor instance everytime, unless its shutdown, following which a new instance will be returned.



