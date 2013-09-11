package com.sp.asyncj.support.internal;

import com.sp.asyncj.support.Composable;
import com.sp.asyncj.support.Function;

public class ComposableFunction<INPUT_TYPE,OUTPUT_TYPE> implements Composable<OUTPUT_TYPE> {
    private Function<INPUT_TYPE, OUTPUT_TYPE> function;
    private Composable<INPUT_TYPE> parent;
    private OUTPUT_TYPE result;

    public ComposableFunction(Function<INPUT_TYPE, OUTPUT_TYPE> function, Composable<INPUT_TYPE> parent) {
        this.function = function;
        this.parent = parent;
    }

    @Override
    public OUTPUT_TYPE getValue() {
        return function.execute(parent.getValue());
    }

    public <FUNCTION_OUTPUT_TYPE> ComposableFunction<OUTPUT_TYPE, FUNCTION_OUTPUT_TYPE> map(Function<OUTPUT_TYPE, FUNCTION_OUTPUT_TYPE> function){
        return new ComposableFunction<OUTPUT_TYPE, FUNCTION_OUTPUT_TYPE>(function, this);
    }
}
