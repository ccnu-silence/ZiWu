package com.github.tinkerti.ziwu.ui.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by tiankui on 7/1/17.
 */

public class CommonUtils {

    private static final AtomicInteger counter = new AtomicInteger();

    public static int getID() {
        return counter.getAndIncrement() + 1;
    }

    public static void setEditTextFocus(List<EditText> editTextList) {
        for (EditText editText : editTextList) {
            editText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //点击editText的时候获取焦点
                    editTextGetFocus((EditText) v);
                }
            });

            //获取和失去焦点的监听
            editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        editTextGetFocus((EditText) v);
                    } else {
                        editTextLoseFocus((EditText) v);
                    }
                }
            });
        }

    }

    public static void setEditTextFocus(EditText editText) {
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击editText的时候获取焦点
                editTextGetFocus((EditText) v);
            }
        });

        //获取和失去焦点的监听
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    editTextGetFocus((EditText) v);
                } else {
                    editTextLoseFocus((EditText) v);
                }
            }
        });
    }

    private static void editTextGetFocus(final EditText editText) {
        editText.requestFocus();
        editText.post(new Runnable() {
            @Override
            public void run() {
                InputMethodManager manager = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                manager.showSoftInput(editText, 0);
            }
        });
        editText.setSelection(editText.getText().length());
    }

    private static void editTextLoseFocus(final EditText editText) {
        editText.clearFocus();
        editText.post(new Runnable() {
            @Override
            public void run() {
                InputMethodManager manager = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                manager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
            }
        });
    }
}
