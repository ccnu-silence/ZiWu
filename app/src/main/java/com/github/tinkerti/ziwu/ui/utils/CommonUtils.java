package com.github.tinkerti.ziwu.ui.utils;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by tiankui on 7/1/17.
 */

public class CommonUtils {

    private static final AtomicInteger counter=new AtomicInteger();

    public static int getID(){
        return counter.getAndIncrement()+1;
    }
}
