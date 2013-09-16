package com.sp.asyncj.tasks;

import com.sp.asyncj.callbacks.TaskExceptionListener;
import com.sp.asyncj.callbacks.TaskListener;
import com.sp.asyncj.exceptions.AsyncExcecutionException;
import com.sp.asyncj.exceptions.IllegalOperationException;
import com.sp.asyncj.executor.Executor;
import com.sp.asyncj.model.Result;
import com.sp.asyncj.support.Function;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AsyncTaskTest {

    private AsyncTask sut;
    @Mock
    private Callable callable;
    @Mock
    private Executor executor;
    @Mock
    private TaskExceptionListener taskExceptionListener;
    @Mock private TaskListener taskListener;

    @Before
    public void setup(){
        sut = new AsyncTask(callable, executor);
    }

    @Test
    public void execute_shouldSubmitCurrentTask(){
        sut.execute();

        verify(executor).submit(sut);
    }

    @Test (expected = IllegalOperationException.class)
    public void execute_shouldFailIfTaskAlreadySubmitted(){
        sut.execute();
        sut.execute();
    }

    @Test
    public void done_shouldIDoNothingIfCallbackNotDefinedAndTaskIsCancelled() throws ExecutionException, InterruptedException {
        AsyncTask sut = spy(this.sut); //Need to use a spy here to partially mock the base implementation which is part of JDK - no other way around here, for a unit test

        sut.cancel(true);
        sut.done();

        verify(sut, never()).get();
    }

    @Test
    public void done_shouldNotFail_IfNoTaskListenerIsDefined() throws ExecutionException, InterruptedException {
        AsyncTask sut = spy(this.sut); //Need to use a spy here to partially mock the base implementation which is part of JDK - no other way around here, for a unit test
        doReturn(null).when(sut).get();

        sut.done();
    }


    @Test
    public void done_shouldInvokeCancelledCallback_IfTaskIsCancelled(){
        sut.onException(taskExceptionListener);
        sut.cancel(true);
        sut.listen(taskListener);
        sut.done();

        verify(taskListener).onCancel();
        verify(taskListener,never()).onComplete(any(Result.class));
        verify(taskExceptionListener,never()).onException(any(Exception.class));
    }

    @Test
    public void done_shouldInvokeSuccessCallback_IfTaskIsComplete() throws ExecutionException, InterruptedException {
        Integer retVal = 1;
        Result<Integer> expectedResult = new Result<Integer>(retVal);
        AsyncTask sut = spy(this.sut); //Need to use a spy here to partially mock the base implementation which is part of JDK - no other way around here, for a unit test

        doReturn(retVal).when(sut).get();

        sut.onException(taskExceptionListener);
        sut.listen(taskListener);
        sut.done();

        verify(taskListener).onComplete(eq(expectedResult));
        verify(taskListener,never()).onCancel();
        verify(taskExceptionListener,never()).onException(any(Exception.class));
    }

    @Test
    public void done_shouldInvokeExceptionHandler_InCaseOfException() throws ExecutionException, InterruptedException {
        RuntimeException exception = new RuntimeException();
        AsyncTask sut = spy(this.sut); //Need to use a spy here to partially mock the base implementation which is part of JDK - no other way around here, for a unit test

        doThrow(exception).when(sut).get();

        sut.listen(taskListener);
        sut.onException(taskExceptionListener);
        sut.done();

        verify(taskListener,never()).onComplete(any(Result.class));
        verify(taskListener,never()).onCancel();
        verify(taskExceptionListener).onException(exception);
    }

    @Test (expected = AsyncExcecutionException.class)
    public void done_shouldThrowException_IfExceptionOccurs_AndNoExceptionHandlerIsDefined() throws ExecutionException, InterruptedException {
        RuntimeException exception = new RuntimeException();
        AsyncTask sut = spy(this.sut); //Need to use a spy here to partially mock the base implementation which is part of JDK - no other way around here, for a unit test

        doThrow(exception).when(sut).get();

        sut.listen(taskListener);
        sut.done();

        verify(taskListener,never()).onComplete(any(Result.class));
        verify(taskListener,never()).onCancel();
    }

    @Test
    public void run_shouldSubmitMappedTask_WhenDefined() throws ExecutionException, InterruptedException, IllegalAccessException {

        Function mock = mock(Function.class);
        ArgumentCaptor<AsyncTask> taskArgumentCaptor = ArgumentCaptor.forClass(AsyncTask.class);
        AsyncTask<Integer> sut = spy(this.sut);

        doReturn(1).when(sut).get();

        sut.map(mock);

        sut.run();

        verify(executor).submit(taskArgumentCaptor.capture());
        assertSame(mock, readField(taskArgumentCaptor.getValue(), "sync.callable.function"));
        System.out.println(taskArgumentCaptor.getValue());
    }

    @Test
    public void map_shouldReturnMappedTask_WithCurrentExceptionHandler() throws IllegalAccessException {
        Function function = mock(Function.class);
        sut.onException(taskExceptionListener);

        final AsyncTask mappedTask = sut.map(function);

        assertSame(function, readField(mappedTask, "sync.callable.function"));
        assertSame(taskExceptionListener, readField(mappedTask, "taskExeceptionListener"));
    }


    @Test (expected = IllegalOperationException.class)
    public void map_shouldFail_IfTaskAlreadySubmitted(){
        sut.execute();

        Function function = mock(Function.class);
        sut.map(function);
    }


    private static Object readField(Object obj, String field) throws IllegalAccessException {
        final String[] parts = field.split("\\.");
        Object target = obj;
        for (String part : parts) {
            target = FieldUtils.readField(target, part, true);
        }
        return target;
    }

}
