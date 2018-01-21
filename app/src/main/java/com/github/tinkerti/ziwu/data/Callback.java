package com.github.tinkerti.ziwu.data;

/**
 * Created by tiankui on 1/21/18.
 */

public abstract class Callback<T> {

    protected abstract void onSuccess(T t);

    protected abstract void onFail(ErrorCode errorCode);
}
