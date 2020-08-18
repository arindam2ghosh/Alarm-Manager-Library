package com.arindam.alarmmanager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.arindam.alarmmanager.database.AlarmDAO;
import com.arindam.alarmmanager.model.Alarm;
import com.arindam.alarmmanager.receiver.AlarmReceiver;

import java.util.List;

public class ArinAlarmController {

    public static final String ALARM_ALERT_ACTION = "com.simplaapliko.wakeup.ALARM_ALERT_ACTION";

    public static final String EXTRA_ALARM_ID = "com.simplaapliko.wakeup.EXTRA_ALARM_ID";
    public static final String EXTRA_EXTERNAL_ID = "com.simplaapliko.wakeup.EXTRA_EXTERNAL_ID";
    public static final String EXTRA_TITLE = "com.simplaapliko.wakeup.EXTRA_TITLE";
    public static final String EXTRA_MESSAGE = "com.simplaapliko.wakeup.EXTRA_MESSAGE";
    public static final String EXTRA_WHEN = "com.simplaapliko.wakeup.EXTRA_WHEN";
    public static final String EXTRA_HANDLER = "com.simplaapliko.wakeup.EXTRA_HANDLER";

    private static final String TAG = "AlarmController";

    public ArinAlarmController() {}

    public void setAlarm(Context context, Alarm alarm) {
        Log.d(TAG, "setAlarm: " + alarm);

        long id = new AlarmDAO(context)
                .insert(alarm);

        alarm.setId(id);

        enableAlarm(context, alarm);
    }

    public void deleteAlarm(Context context, Alarm alarm) {
        Log.d(TAG, "deleteAlarm: " + alarm);

        int count = new AlarmDAO(context).delete(alarm);

        if (count > 0) {
            disableAlarm(context, alarm);
        }
    }

    public void deleteAllAlarm(Context context) {
        Log.d(TAG, "deleteAllAlarm: ");
        List<Alarm> alarmList = getAllAlarm(context);
        for(int i=0; i<alarmList.size(); i++){
            int count = new AlarmDAO(context).delete(alarmList.get(i));
            if (count > 0) {
                disableAlarm(context, alarmList.get(i));
            }
        }
    }

    /**
     * @param context The context to use.
     * @param externalId External id to be disabled
     */
    public void deleteAlarm(Context context, long externalId) {
        Log.d(TAG, "cancelAlarm by externalId: " + externalId);

        Alarm alarm = new AlarmDAO(context).selectByExternalId(externalId);
        deleteAlarm(context, alarm);
    }

    public List<Alarm> getAllAlarm(Context context){
        return new AlarmDAO(context).getAllRecord();
    }

    public void enableAlarm(Context context, Alarm alarm) {
        Log.d(TAG, "enableAlarm: " + alarm);

        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.setAction(ALARM_ALERT_ACTION);
        intent.putExtra(EXTRA_ALARM_ID, alarm.getId());
        intent.putExtra(EXTRA_EXTERNAL_ID, alarm.getExternalId());
        intent.putExtra(EXTRA_TITLE, alarm.getTitle());
        intent.putExtra(EXTRA_MESSAGE, alarm.getMessage());
        intent.putExtra(EXTRA_WHEN, alarm.getTime());
        intent.putExtra(EXTRA_HANDLER, alarm.getAlarmHandleListener());

        int alarmId = (int) alarm.getId();
        long triggerAt = alarm.getTime();

        PendingIntent sender = PendingIntent.getBroadcast(
                context, alarmId, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarm.isExact()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAt, sender);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerAt, sender);
            } else {
                alarmManager.set(alarm.getType(), triggerAt, sender);
            }
        } else {
            alarmManager.setRepeating(alarm.getType(), triggerAt, AlarmManager.INTERVAL_DAY, sender);
        }
    }

    public void disableAlarm(Context context, Alarm alarm) {
        Log.d(TAG, "disableAlarm: " + alarm);

        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.setAction(ALARM_ALERT_ACTION);

        int alarmId = (int) alarm.getId();

        PendingIntent sender = PendingIntent.getBroadcast(
                context, alarmId, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        sender.cancel();

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }
}
