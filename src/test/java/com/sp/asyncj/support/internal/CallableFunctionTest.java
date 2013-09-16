package com.sp.asyncj.support.internal;

import com.sp.asyncj.support.Evaluateable;
import com.sp.asyncj.support.Function;
import org.junit.Test;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CallableFunctionTest {

    @Test
    public void call_shouldInvokeFunction() throws Exception {
        final Function<Integer, Object> function = mock(Function.class);
        final Evaluateable<Integer> input = mock(Evaluateable.class);
        final Object expectedReturnObj = new Object();
        final CallableFunction<Integer, Object> sut = new CallableFunction<Integer, Object>(function, input);

        when(input.getValue()).thenReturn(1);
        when(function.execute(1)).thenReturn(expectedReturnObj);

        final Object actualReturnObj = sut.call();

        verify(function).execute(1);
        assertSame(expectedReturnObj, actualReturnObj);
    }

}
