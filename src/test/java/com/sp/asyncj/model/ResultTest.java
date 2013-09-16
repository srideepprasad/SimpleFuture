package com.sp.asyncj.model;

import com.sp.asyncj.support.Composable;
import com.sp.asyncj.support.Function;
import com.sp.asyncj.test.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;

public class ResultTest {
    private Result<Integer> sut;

    @Before
    public void setup(){
        sut = new Result<Integer>(1);
    }

    @Test
    public void getValue_shouldReturnCurrentValue(){
        assertEquals((Integer)1, sut.getValue());
    }

    @Test
    public void map_shouldReturnComposableFunction_ForAGivenMappingFunction() throws IllegalAccessException {
        Function<Integer, Object> function = mock(Function.class);
        final Composable<Object> returnedFunction = sut.map(function);
        assertSame(function, TestUtils.readField(returnedFunction, "function"));
        assertSame(sut, TestUtils.readField(returnedFunction, "input"));

    }
}
