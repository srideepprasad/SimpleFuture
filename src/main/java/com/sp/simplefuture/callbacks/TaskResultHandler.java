package com.sp.simplefuture.callbacks;

public interface TaskResultHandler<RESULT_TYPE> {
    public void onComplete(RESULT_TYPE result);
    public void onCancel();
    public void onException(Exception ex);
}
