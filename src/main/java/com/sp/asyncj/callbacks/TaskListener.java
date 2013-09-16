package com.sp.asyncj.callbacks;

import com.sp.asyncj.model.Result;

public interface TaskListener<RESULT_TYPE> {
    public void onComplete(Result<RESULT_TYPE> result);
    public void onCancel();

}
