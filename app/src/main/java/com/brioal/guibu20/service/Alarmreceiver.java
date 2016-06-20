package com.brioal.guibu20.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Brioal on 2016/6/20.
 */

public class Alarmreceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("com.brioal.restart.step.service")){
            Intent  i = new Intent();
            i.setClass(context, StepService.class);
            // 启动service
            // 多次调用startService并不会启动多个service而是会多次调用onStart
            context.startService(i);
        }
}
}
