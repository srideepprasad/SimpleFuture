package com.sp.simplefuture.executor;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

public class DefaultExecutorTest {

    @Test
    public void shouldReturnExecutorWithFixedPoolSize(){
        DefaultExecutor sut = DefaultExecutor.getInstance();
        assertEquals(20, sut.getMaximumPoolSize());
        assertEquals(1, sut.getCorePoolSize());
    }

    @Test
    public void shouldReturnSingletonInstanceIfExecutorIsRunning(){
        DefaultExecutor sut = DefaultExecutor.getInstance();
        assertSame(sut, DefaultExecutor.getInstance());
    }

    @Test
    public void shouldReturnNewInstanceIfExecutorIsShutdown(){
        DefaultExecutor sut = DefaultExecutor.getInstance();
        sut.shutdownNow();
        assertNotSame(sut, DefaultExecutor.getInstance());
    }
}
