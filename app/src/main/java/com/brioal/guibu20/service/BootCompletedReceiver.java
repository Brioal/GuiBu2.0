package com.brioal.guibu20.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

/**
 * Created by Brioal on 2016/6/20.
 */

public class BootCompletedReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            Intent newIntent = new Intent(context, StepService.class);
            context.startService(newIntent);

            Intent alarmIntent = new Intent(context,Alarmreceiver.class);
            intent.setAction("com.brioal.restart.step.service");
            PendingIntent sender = PendingIntent.getBroadcast(context,0,
                    intent, 0);
            long firstime= SystemClock.elapsedRealtime();
            AlarmManager am = (AlarmManager) context
                    .getSystemService(Context.ALARM_SERVICE);
            // 10秒一个周期，不停的发送广播
            am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,firstime,
                    10 * 1000,sender);
        }
    }
}
