package com.sp.asyncj.support;

public interface Function<INPUT_TYPE, OUTPUT_TYPE> {

    public OUTPUT_TYPE execute(INPUT_TYPE input);

}
