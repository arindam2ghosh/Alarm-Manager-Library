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
import com.arindam.alarmmanager.util.Util;

import java.util.Calendar;
import java.util.List;

public class ArinAlarmController {

    public static final String ALARM_ALERT_ACTION = "com.arindam.alarmmanager.ALARM_ALERT_ACTION";

    public static final String EXTRA_ALARM_ID = "com.arindam.alarmmanager.EXTRA_ALARM_ID";
    public static final String EXTRA_EXTERNAL_ID = "com.arindam.alarmmanager.EXTRA_EXTERNAL_ID";
    public static final String EXTRA_PERIODIC = "com.arindam.alarmmanager.EXTRA_PERIODIC";
    public static final String EXTRA_PERIODIC_TYPE = "com.arindam.alarmmanager.EXTRA_PERIODIC_TYPE";
    public static final String EXTRA_PERIODIC_VAL = "com.arindam.alarmmanager.EXTRA_PERIODIC_VAL";
    public static final String EXTRA_PERIODIC_STARTTIME = "com.arindam.alarmmanager.EXTRA_PERIODIC_STARTTIME";
    public static final String EXTRA_TITLE = "com.arindam.alarmmanager.EXTRA_TITLE";
    public static final String EXTRA_MESSAGE = "com.arindam.alarmmanager.EXTRA_MESSAGE";
    public static final String EXTRA_WHEN = "com.arindam.alarmmanager.EXTRA_WHEN";
    public static final String EXTRA_HANDLER = "com.arindam.alarmmanager.EXTRA_HANDLER";

    private static final String TAG = "AlarmController";

    public ArinAlarmController() {}

    public void setAlarm(Context context, Alarm alarm) {
        Log.d(TAG, "setAlarm: " + alarm);

        long id = new AlarmDAO(context)
                .insert(alarm);

        Log.i("Arindam", "Alarm id: "+id);
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
        intent.putExtra(EXTRA_PERIODIC, alarm.isPeriodic());
        intent.putExtra(EXTRA_PERIODIC_TYPE, alarm.getPeriodicType());
        intent.putExtra(EXTRA_PERIODIC_VAL, alarm.getPeriodicValue());
        intent.putExtra(EXTRA_PERIODIC_STARTTIME, alarm.getPeriodicStartTime());
        intent.putExtra(EXTRA_PERIODIC, alarm.isPeriodic());
        intent.putExtra(EXTRA_TITLE, alarm.getTitle());
        intent.putExtra(EXTRA_MESSAGE, alarm.getMessage());
        intent.putExtra(EXTRA_WHEN, alarm.getTime());
        intent.putExtra(EXTRA_HANDLER, alarm.getAlarmHandleListener());

        int alarmId = (int) alarm.getId();

        PendingIntent sender = PendingIntent.getBroadcast(context, alarmId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager.AlarmClockInfo alarmClockInfo;
        if(!alarm.isPeriodic()) {
            long triggerAt = alarm.getTime();
            alarmClockInfo = new AlarmManager.AlarmClockInfo(triggerAt, sender);
        }else{
            Calendar calendar = validateTime(alarm.getPeriodicStartTime(), alarm.getPeriodicType(), alarm.getPeriodicValue());
            alarmClockInfo = new AlarmManager.AlarmClockInfo(calendar.getTimeInMillis(), sender);
        }

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setAlarmClock(alarmClockInfo, sender);
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

    public Calendar validateTime(long timeInMillis, int periodicType, int periodicVal){

        Calendar now = Calendar.getInstance();

        Log.i("Arindam", "Now: "+ now.getTime().toString());
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeInMillis);
        do {
            if (periodicType == Alarm.TIMEUNIT_MILLISECOND)
                calendar.add(Calendar.MILLISECOND, periodicVal);
            else if (periodicType == Alarm.TIMEUNIT_SECOND)
                calendar.add(Calendar.SECOND, periodicVal);
            else if (periodicType == Alarm.TIMEUNIT_MINUTE)
                calendar.add(Calendar.MINUTE, periodicVal);
            else if (periodicType == Alarm.TIMEUNIT_HOUR)
                calendar.add(Calendar.HOUR, periodicVal);

            Log.i("Arindam", "Adjust: "+ calendar.getTime().toString());
        }while (now.after(calendar));

        return calendar;
    }
}
