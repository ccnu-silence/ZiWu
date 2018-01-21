package com.github.tinkerti.ziwu.data;

/**
 * Created by tiankui on 1/21/18.
 */

public abstract class SimpleResultCallback<T> extends Callback<T> {
    @Override
    protected final void onSuccess(final T t) {
        TaskManager.getInstance().getUiHandler().post(new Runnable() {
            @Override
            public void run() {
                onSuccessOnUIThread(t);
            }
        });
    }

    @Override
    protected final void onFail(final ErrorCode errorCode) {
        TaskManager.getInstance().getUiHandler().post(new Runnable() {
            @Override
            public void run() {
                onFailOnUIThread(errorCode);
            }
        });
    }

    protected abstract void onSuccessOnUIThread(T t);

    protected void onFailOnUIThread(ErrorCode errorCode) {
    }
}
