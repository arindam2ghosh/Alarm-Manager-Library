package com.arindam.alarmmanager.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.arindam.alarmmanager.ArinAlarmController;
import com.arindam.alarmmanager.database.AlarmCursorWrapper;
import com.arindam.alarmmanager.database.AlarmDAO;
import com.arindam.alarmmanager.model.Alarm;

/**
 * This BroadcastReceiver automatically (re)starts the alarm when the device is
 * rebooted. This receiver is set to be disabled (android:enabled="false") in the
 * application's manifest file.
 */
public class BootReceiver extends BroadcastReceiver {

    private static final String TAG = "BootReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive, action = " + intent.getAction());

        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {

            AlarmCursorWrapper wrapper = new AlarmDAO(context).select();
            if (wrapper.moveToFirst()) {

                ArinAlarmController arinAlarmController = new ArinAlarmController();
                AlarmDAO alarmDao = new AlarmDAO(context);
                long currentTime = System.currentTimeMillis();
                do {
                    Alarm alarm = wrapper.getAlarm();
                    if (alarm.isKeepAfterReboot() || alarm.getTime() > currentTime) {
                        arinAlarmController.enableAlarm(context, alarm);
                    } else {
                        alarmDao.delete(alarm);
                    }
                } while (wrapper.moveToNext());
            }
        }
    }
}
