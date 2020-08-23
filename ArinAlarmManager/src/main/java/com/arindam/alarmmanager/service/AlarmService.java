package com.arindam.alarmmanager.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.arindam.alarmmanager.ArinAlarmController;
import com.arindam.alarmmanager.database.AlarmDAO;
import com.arindam.alarmmanager.listener.AlarmHandleListener;
import com.arindam.alarmmanager.model.Alarm;
import com.arindam.alarmmanager.util.Util;

import java.util.Date;

public class AlarmService extends IntentService {

    private static final String TAG = "Arindam";

    public AlarmService() {
        super("AlarmService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "onHandleIntent");

        if (intent == null) {
            // nothing to do
            Log.d(TAG, "intent is null");
            return;
        }

        AlarmDAO alarmDAO = new AlarmDAO(this);

        // get alarm from extras
        long notFound = -1;
        long alarmId = intent.getLongExtra(ArinAlarmController.EXTRA_ALARM_ID, notFound);
        boolean isPeriodic = intent.getBooleanExtra(ArinAlarmController.EXTRA_PERIODIC, false);
        if(isPeriodic){
            Alarm alarm = alarmDAO.selectById(alarmId);
            alarm.setPeriodicStartTime(new Date().getTime());
            new ArinAlarmController().setAlarm(this, alarm);
        }

        // delete alarm from the database
        alarmDAO.delete(alarmId);

        // get listener using class name and reflection
        String handler = intent.getStringExtra(ArinAlarmController.EXTRA_HANDLER);
        AlarmHandleListener listener = Util.getInstance(handler);

        // call onHandle
        if (listener != null) {
            listener.onHandle(this, intent);
        } else {
            Log.i(TAG, "not able to get new instance for class " + handler);
        }

        // Finish the execution from a previous startWakefulService(Context, Intent).
        // Any wake lock that was being held will now be released.
        //AlarmReceiver.completeWakefulIntent(intent);
    }
}
