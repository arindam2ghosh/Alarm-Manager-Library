package com.arindam.alarmmanager.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import com.arindam.alarmmanager.ArinAlarmController;
import com.arindam.alarmmanager.database.AlarmDAO;
import com.arindam.alarmmanager.listener.AlarmHandleListener;
import com.arindam.alarmmanager.util.Util;

public class AlarmService extends JobIntentService {

    private static final String TAG = "Arindam";
    public static final int JOB_ID = 1;

    public static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, AlarmService.class, JOB_ID, work);
    }


    @Override
    protected void onHandleWork(@NonNull Intent intent) {
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

        // delete alarm from the database
        //alarmDAO.delete(alarmId);

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
