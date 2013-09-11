package com.sp.asyncj.support;

public interface Composable<TYPE> {
    public TYPE getValue();

    public <FUNCTION_OUTPUT_TYPE> Composable<FUNCTION_OUTPUT_TYPE> map(Function<TYPE, FUNCTION_OUTPUT_TYPE> function);

}
