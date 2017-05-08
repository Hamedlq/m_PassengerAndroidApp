package com.mibarim.main.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by Hamed on 5/7/2016.
 */
public class BootReceiver extends BroadcastReceiver {
    final static String TAG = "BootReceiver";

    @Override
    public void onReceive(Context context, Intent receiverIntent) {
        try {
            //schedule for service calls
            AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, PeriodicReceiver.class);
            PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
                    AlarmManager.INTERVAL_FIFTEEN_MINUTES, alarmIntent);
            Log.d(TAG, "setSchedules");
        } catch (Exception e) {
            Log.e(TAG, "onReceive Exception", e);
        }
    }
}

