package com.sp.asyncj.support.internal;

import com.sp.asyncj.support.Evaluateable;
import com.sp.asyncj.support.Function;

import java.util.concurrent.Callable;

public class CallableFunction<INPUT_TYPE,OUTPUT_TYPE> implements Callable<OUTPUT_TYPE> {

    private final Function<INPUT_TYPE, OUTPUT_TYPE> function;
    private final Evaluateable<INPUT_TYPE> input;

    public CallableFunction(Function<INPUT_TYPE,OUTPUT_TYPE> function, Evaluateable<INPUT_TYPE> input){
        this.function = function;
        this.input = input;
    }

    @Override
    public OUTPUT_TYPE call() throws Exception {
        return function.execute(input.getValue());
    }
}
