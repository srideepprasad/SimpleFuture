package com.sp.asyncj.support;

import com.sp.asyncj.model.Result;

public interface Callback<RESULT_TYPE>{
    public void onComplete(Result<RESULT_TYPE> result);
    public void onException(Exception exception);
    public void onCancel();

}
