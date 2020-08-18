package com.arindam.alarmmanager.sample;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.arindam.alarmmanager.ArinAlarmController;
import com.arindam.alarmmanager.listener.AlarmHandleListener;
import com.arindam.alarmmanager.model.Alarm;
import com.arindam.alarmmanager.notification.AlarmNotification;


public class SendNotification extends AlarmHandleListener {

    private static final String TAG = "Arindam";

    @Override
    public void onHandle(Context context, Intent intent) {
        Log.d(TAG, "onHandle");
        if (intent == null) {
            Log.d(TAG, "intent is null");
            return;
        }

        long alarmId = intent.getLongExtra(ArinAlarmController.EXTRA_ALARM_ID, -1);
        long externalId = intent.getLongExtra(ArinAlarmController.EXTRA_EXTERNAL_ID, -1);
        String title = intent.getStringExtra(ArinAlarmController.EXTRA_TITLE);
        String text = intent.getStringExtra(ArinAlarmController.EXTRA_MESSAGE);
        long when = intent.getLongExtra(ArinAlarmController.EXTRA_WHEN, Alarm.NOT_SET);

        AlarmNotification alarmNotification = new AlarmNotification(context);
        alarmNotification.createNotification((int)alarmId, title, text, new Intent(context, MainActivity.class));
    }



}
