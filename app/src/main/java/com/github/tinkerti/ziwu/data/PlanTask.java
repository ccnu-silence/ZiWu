package com.github.tinkerti.ziwu.data;

/**
 * Created by tiankui on 4/23/17.
 */

public class PlanTask implements ITask {

    private static class SingleTonHolder{
        private static PlanTask sIns=new PlanTask();
    }
    @Override
    public void onInit() {

    }

    @Override
    public void onLoginSuccess() {

    }

    public static PlanTask getInstance(){
        return SingleTonHolder.sIns;
    }


}
