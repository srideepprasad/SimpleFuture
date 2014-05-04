package com.sp.simplefuture.callbacks;

/*
NoOperation Task Result Handler - used when no TaskResultHandler is provided by the user
Also can be overridden to handle only those notifications which are necessary.
 */

public class NoOpResultHandler<OUTPUT_TYPE> implements TaskResultHandler<OUTPUT_TYPE>{

    @Override public void onComplete(OUTPUT_TYPE result) {

    }

    @Override public void onCancel() {

    }

    @Override public void onException(Exception ex) {

    }

}
