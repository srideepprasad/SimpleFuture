package com.sp.simplefuture.tasks;

import com.sp.simplefuture.callbacks.TaskResultHandler;
import com.sp.simplefuture.executor.DefaultExecutor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.concurrent.*;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AsyncTaskDefTest {

    private AsyncTask sut;

    @Mock
    private Callable<Object> callable;

    @Mock
    private TaskResultHandler<Object> taskResultHandler;

    @Test
    public void shouldCreateAndExecuteTaskWithDefaultExecutorAndNoTaskResultHandler() throws Exception {
        Object expectedReturn = new Object();
        AsyncTaskDef<Object> asyncTaskDef = AsyncTaskDef.forTask(callable);
        when(callable.call()).thenReturn(expectedReturn);

        AsyncTask<Object> task = asyncTaskDef.submit();

        /*
        Not needed in real world - this is done to wait until completion, and to ensure that core Java contracts with regards to Futures are not broken
         */
        Object actualResult = awaitCompletionAndGet(task);

        verify(callable).call();
        assertSame(DefaultExecutor.getInstance(), task.getExecutor());
        assertSame(expectedReturn, actualResult);
    }

    @Test
    public void shouldCreateAndExecuteTaskWithSpecificExecutorAndNoTaskResultHandler() throws Exception {
        Object expectedReturn = new Object();
        ExecutorService expectedExecutor = Executors.newSingleThreadExecutor();
        AsyncTaskDef<Object> asyncTaskDef = AsyncTaskDef.forTask(callable, expectedExecutor);
        asyncTaskDef.resultTo(taskResultHandler);

        when(callable.call()).thenReturn(expectedReturn);

        AsyncTask<Object> task = asyncTaskDef.submit();

        /*
        Not needed in real world - this is done to wait until completion, and to ensure that core Java contracts with regards to Futures are not broken
         */
        Object actualResult = awaitCompletionAndGet(task);

        verify(callable).call();
        verify(taskResultHandler).onComplete(same(expectedReturn));
        assertSame(expectedExecutor, task.getExecutor());
        assertSame(expectedReturn, actualResult);
    }

    @Test
    public void shouldCreateAndExecuteTaskWithGivenTaskResultHandler() throws Exception {
        Object expectedReturn = new Object();
        AsyncTaskDef<Object> asyncTaskDef = AsyncTaskDef.forTask(callable);
        asyncTaskDef.resultTo(taskResultHandler);
        when(callable.call()).thenReturn(expectedReturn);

        AsyncTask<Object> task = asyncTaskDef.submit();

        /*
        Not needed in real world - this is done to wait until completion, and to ensure that core Java contracts with regards to Futures are not broken
         */
        Object actualResult = awaitCompletionAndGet(task);

        verify(callable).call();
        verify(taskResultHandler).onComplete(same(expectedReturn));
        assertSame(expectedReturn, actualResult);
    }

    @Test
    public void shouldCreateAndExecuteTaskAndIgnoreExceptionsWhenNoTaskResultHandlerIsDefined() throws Exception {
        AsyncTaskDef<Object> asyncTaskDef = AsyncTaskDef.forTask(callable);
        when(callable.call()).thenThrow(new Exception("DUMMY EXCEPTION"));

        AsyncTask<Object> task = asyncTaskDef.submit();

        /*
        Not needed in real world - this is done to wait until completion, and to ensure that core Java contracts with regards to Futures are not broken
         */
        awaitCompletionAndGet(task);

        verify(callable).call();
    }

    @Test
    public void shouldCreateAndExecuteTaskAndReportExceptionToTaskResultHandler() throws Exception {
        ArgumentCaptor<ExecutionException> executionExceptionArgumentCaptor = ArgumentCaptor.forClass(ExecutionException.class);
        AsyncTaskDef<Object> asyncTaskDef = AsyncTaskDef.forTask(callable);
        asyncTaskDef.resultTo(taskResultHandler);
        Exception expectedException = new Exception("DUMMY EXCEPTION");
        when(callable.call()).thenThrow(expectedException);

        AsyncTask<Object> task = asyncTaskDef.submit();

        /*
        Not needed in real world - this is done to wait until completion, and to ensure that core Java contracts with regards to Futures are not broken
         */
        awaitCompletionAndGet(task);

        verify(callable).call();
        verify(taskResultHandler).onException(executionExceptionArgumentCaptor.capture());

        assertSame(expectedException, executionExceptionArgumentCaptor.getValue().getCause());
    }

    @Test
    public void shouldCreateAndExecuteTaskAndReportCancellationToTaskResultHandler() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        AsyncTaskDef<Object> asyncTaskDef = AsyncTaskDef.forTask(getTestTaskForCancellation(latch));
        asyncTaskDef.resultTo(taskResultHandler);

        AsyncTask<Object> task = asyncTaskDef.submit();
        latch.await();  //Wait for task to start

        task.cancel(true);

        /*
        Not needed in real world - this is done to wait until completion, and to ensure that core Java contracts with regards to Futures are not broken
         */
        awaitCompletionAndGet(task);

        verify(taskResultHandler).onCancel();
        assertTrue(task.isCancelled());
    }

    @Test
    public void shouldCreateAndExecuteTaskAndHandleCancellationIfNoTaskHandlerDefined() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        AsyncTaskDef<Object> asyncTaskDef = AsyncTaskDef.forTask(getTestTaskForCancellation(latch));

        AsyncTask<Object> task = asyncTaskDef.submit();
        latch.await();  //Wait for task to start

        task.cancel(true);

        /*
        Not needed in real world - this is done to wait until completion, and to ensure that core Java contracts with regards to Futures are not broken
         */
        awaitCompletionAndGet(task);

        assertTrue(task.isCancelled());
    }

    private static Object awaitCompletionAndGet(AsyncTask<Object> task){
        try {
            return task.get();
        } catch (Exception e) {
            return null;
        }
    }


    private static Callable<Object> getTestTaskForCancellation(final CountDownLatch latch) {
        return new Callable<Object>() {
            @Override public Object call() throws Exception {
                latch.countDown();

                while (!Thread.interrupted()){

                }
                return null;
            }
        };
    }


}
