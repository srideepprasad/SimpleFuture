package com.sp.asyncj.tasks;

import com.sp.asyncj.executor.Executor;
import com.sp.asyncj.model.Result;
import com.sp.asyncj.support.Callback;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.concurrent.*;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AsyncTaskTest {

    private AsyncTask sut;
    @Mock
    private Callable callable;
    @Mock
    private Executor executor;

    private static class TestExecutor extends ThreadPoolExecutor implements Executor{
        private TestExecutor() {
            super(0, 1,0l, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());

        }

        @Override
        public <T> AsyncTask<T> submit(AsyncTask<T> tCallable) {
            return tCallable;
        }
    }

    @Before
    public void setup(){
        sut = new AsyncTask(callable, executor);
    }

    @Test
    public void execute_shouldSubmitCurrentTask(){
        sut.execute();

        verify(executor).submit(sut);
    }

    @Test
    public void done_shouldIDoNothingIfCallbackNotDefinedAndTaskIsCancelled() throws ExecutionException, InterruptedException {
        AsyncTask sut = spy(this.sut); //Need to use a spy here to partially mock the base implementation which is part of JDK - no other way around here, for a unit test

        sut.cancel(true);
        sut.done();

        verify(sut, never()).get();
    }

    @Test
    public void done_shouldIDoNothingIfCallbackNotDefinedAndTaskIsNotCancelled() throws ExecutionException, InterruptedException {
        AsyncTask sut = spy(this.sut); //Need to use a spy here to partially mock the base implementation which is part of JDK - no other way around here, for a unit test

        sut.done();
        verify(sut, never()).get();
    }

    @Test
    public void done_shouldInvokeCancelledCallback_IfTaskIsCancelled(){
        Callback callback = mock(Callback.class);
        sut.cancel(true);
        sut.onFinish(callback);
        sut.done();

        verify(callback).onCancel();
        verify(callback,never()).onComplete(any(Result.class));
        verify(callback,never()).onException(any(Exception.class));
    }

    @Test
    public void done_shouldInvokeSuccessCallback_IfTaskIsComplete() throws ExecutionException, InterruptedException {
        Callback callback = mock(Callback.class);
        Integer retVal = 1;
        Result<Integer> expectedResult = new Result<Integer>(retVal);
        AsyncTask sut = spy(this.sut); //Need to use a spy here to partially mock the base implementation which is part of JDK - no other way around here, for a unit test

        doReturn(retVal).when(sut).get();

        sut.onFinish(callback);
        sut.done();

        verify(callback).onComplete(eq(expectedResult));
        verify(callback,never()).onCancel();
        verify(callback,never()).onException(any(Exception.class));
    }

    @Test
    public void done_shouldInvokeExceptionCallback_InCaseOfException() throws ExecutionException, InterruptedException {
        Callback callback = mock(Callback.class);
        RuntimeException exception = new RuntimeException();
        AsyncTask sut = spy(this.sut); //Need to use a spy here to partially mock the base implementation which is part of JDK - no other way around here, for a unit test

        doThrow(exception).when(sut).get();

        sut.onFinish(callback);
        sut.done();

        verify(callback,never()).onComplete(any(Result.class));
        verify(callback,never()).onCancel();
        verify(callback).onException(exception);
    }
}
