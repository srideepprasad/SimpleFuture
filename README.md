SimpleFuture [![Build Status](https://travis-ci.org/srideepprasad/SimpleFuture.png?branch=master)](https://travis-ci.org/srideepprasad/SimpleFuture)
======
####The Future's simple - once you "un-block" it !

Simple library to demonstrate 'non-blocking' futures (background async tasks) in Java. No need of "blocking" Future.get() anymore - instead rely on dynamic notification for completion/cancellation/failure.
Demonstrates possible techniques by which fancy Future abstractions could be provided - as is done now in Scala for example. At the heart of it, we just need a way to tap into the Future/Executor framework to get dynamic notifications on task execution events.
The core JDK provides all that's necessary for non-blocking Futures - unfortunately. there's no readymade implementation (Java 1.5-1.7), giving rise to the misconception that these are not possible in core Java.

Having said that, Java 8 includes the CompletableFuture class which is a huge step forward, and may be worth a look.

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

You are ready to go ! Just call submit()
```
  Future<Double> task = taskDef.submit();
```
You now have a Future instance, which will notify the TaskResultHandler via callbacks on completion/cancellation/exception of the background task..
No need of Future.get() - though you can still call it if needed! This Future implementation (AsyncTask class) follows standard JDK specs, and therefore could easily plugged in anywhere.

* __Step 4 - (Optional) Obtaining the Executor instance__

If you don't provide an Executor to AsyncTaskDef.forTask(), then the default Executor returned via DefaultExecutor.getInstance() is used. This executor implements a fixed thread pool of 20 threads. If you need an instance of the executor running a Future, you may do as follows:
```
  AsyncTask<Double> task = taskDef.submit();
```
Instead of:
```
  Future<Double> task = taskDef.submit();
```
AsyncTask is the class implementing the Future interface - and has an additional method getExecutor(), which returns the executor executing the task. This method may be needed if you don't provide an Executor while creating the AsyncTaskDef, and still want to control the underlying thread pool.




