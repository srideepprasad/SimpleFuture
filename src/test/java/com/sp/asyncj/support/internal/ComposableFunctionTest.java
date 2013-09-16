package com.sp.asyncj.support.internal;

import com.sp.asyncj.support.Evaluateable;
import com.sp.asyncj.support.Function;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ComposableFunctionTest {

    private ComposableFunction<Integer,Object> sut;

    @Mock
    private Function<Integer, Object> function;
    @Mock private Evaluateable<Integer> input;

    @Before
    public void setup(){
        sut = new ComposableFunction<Integer, Object>(function, input);
    }

    @Test
    public void getValue_shouldInvokeFunction_AndReturnValue(){
        final Object expected = new Object();

        when(input.getValue()).thenReturn(1);
        when(function.execute(1)).thenReturn(expected);

        final Object actual = sut.getValue();

        verify(function).execute(1);
        assertSame(expected,actual);
    }

    @Test
    public void map_shouldReturnMappingFunction_ToGivenFunctionReference(){
        final Function<Object, Boolean> mapFunction = mock(Function.class);
        final Object inputObj = new Object();

        final ComposableFunction<Object, Boolean> resultFunction = sut.map(mapFunction);

        when(input.getValue()).thenReturn(1);
        when(function.execute(1)).thenReturn(inputObj);
        when(mapFunction.execute(inputObj)).thenReturn(true);

        final Boolean returnValue = resultFunction.getValue();

        verify(mapFunction).execute(inputObj);
        assertTrue(returnValue);
    }
}
