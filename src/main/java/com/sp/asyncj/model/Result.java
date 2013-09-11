package com.sp.asyncj.model;

import com.sp.asyncj.support.Composable;
import com.sp.asyncj.support.Function;
import com.sp.asyncj.support.internal.ComposableFunction;

public class Result<OUTPUT_TYPE> implements Composable<OUTPUT_TYPE> {
    public final OUTPUT_TYPE result;

    public Result(OUTPUT_TYPE result) {
        this.result = result;
    }


    @Override
    public OUTPUT_TYPE getValue() {
        return result;
    }

    public <FUNCTION_OUTPUT_TYPE> Composable<FUNCTION_OUTPUT_TYPE> map(Function<OUTPUT_TYPE, FUNCTION_OUTPUT_TYPE> function){
        return new ComposableFunction<OUTPUT_TYPE, FUNCTION_OUTPUT_TYPE>(function, this);
    }

    @Override
    public boolean equals(Object resultToCompare) {
        if (this == resultToCompare) return true;
        if (!(resultToCompare instanceof Result)) return false;

        Result otherResult = (Result) resultToCompare;

        if (result != null ? !result.equals(otherResult.result) : otherResult.result != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return result != null ? result.hashCode() : 0;
    }
}
