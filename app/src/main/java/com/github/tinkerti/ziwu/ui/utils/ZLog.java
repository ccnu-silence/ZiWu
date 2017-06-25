package com.github.tinkerti.ziwu.ui.utils;

import android.util.Log;

/**
 * Created by tiankui on 6/25/17.
 */

public class ZLog {
    public static final String TAG="ZiWuLog" ;
    public static void d(String tag,String content){
        Log.d(TAG,"["+tag+"] "+content);
    }

    public static void e(String tag,String content){
        Log.e(TAG,"["+tag+"] "+content);
    }
}
