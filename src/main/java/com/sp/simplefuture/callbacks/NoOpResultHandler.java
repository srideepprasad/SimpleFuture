package com.sp.simplefuture.callbacks;

public class NoOpResultHandler<OUTPUT_TYPE> implements TaskResultHandler<OUTPUT_TYPE>{

    @Override public void onComplete(OUTPUT_TYPE result) {

    }

    @Override public void onCancel() {

    }

    @Override public void onException(Exception ex) {

    }

}
