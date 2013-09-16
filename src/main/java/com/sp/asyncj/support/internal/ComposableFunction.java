package com.sp.asyncj.support.internal;

import com.sp.asyncj.support.Composable;
import com.sp.asyncj.support.Evaluateable;
import com.sp.asyncj.support.Function;

public class ComposableFunction<INPUT_TYPE,OUTPUT_TYPE> implements Composable<OUTPUT_TYPE> {

    private final Function<INPUT_TYPE, OUTPUT_TYPE> function;
    private final Evaluateable<INPUT_TYPE> input;

    public ComposableFunction(Function<INPUT_TYPE, OUTPUT_TYPE> function, Evaluateable<INPUT_TYPE> input) {
        this.function = function;
        this.input = input;
    }

    @Override
    public OUTPUT_TYPE getValue(){
        return function.execute(input.getValue());
    }

    public <FUNCTION_OUTPUT_TYPE> ComposableFunction<OUTPUT_TYPE, FUNCTION_OUTPUT_TYPE> map(Function<OUTPUT_TYPE, FUNCTION_OUTPUT_TYPE> function){
        return new ComposableFunction<OUTPUT_TYPE, FUNCTION_OUTPUT_TYPE>(function, this);
    }
}
