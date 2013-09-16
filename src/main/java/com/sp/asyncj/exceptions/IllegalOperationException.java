package com.sp.asyncj.exceptions;

public class IllegalOperationException extends RuntimeException {
    private IllegalOperationException(String msg) {
        super(msg);
    }

    public static IllegalOperationException forDuplicateSubmission(){
        return new IllegalOperationException("Cannot resubmit task - task has already been submitted");
    }

    public static IllegalOperationException forInvalidMappingOperation(){
        return new IllegalOperationException("Cannot perform task mapping - task has already been submitted");
    }

}
