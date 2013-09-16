package com.sp.asyncj.exceptions;

public class AsyncExcecutionException extends RuntimeException{
    public AsyncExcecutionException(final Exception cause) {
        super("Unhandled Exception during async processing", cause);
    }
}
