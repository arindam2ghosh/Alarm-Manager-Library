package com.arindam.alarmmanager.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.arindam.alarmmanager.ArinAlarmController;
import com.arindam.alarmmanager.database.AlarmDAO;
import com.arindam.alarmmanager.listener.AlarmHandleListener;
import com.arindam.alarmmanager.model.Alarm;
import com.arindam.alarmmanager.service.AlarmService;
import com.arindam.alarmmanager.util.Util;

import java.util.Date;

public class AlarmReceiver extends BroadcastReceiver {

    private static final String TAG = "Arindam";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive");

//        Intent service = new Intent(context, AlarmService.class);
//        service.putExtras(intent);
//        context.startService(service);

        if (intent == null) {
            // nothing to do
            Log.d(TAG, "intent is null");
            return;
        }

        AlarmDAO alarmDAO = new AlarmDAO(context);

        // get alarm from extras
        long notFound = -1;
        long alarmId = intent.getLongExtra(ArinAlarmController.EXTRA_ALARM_ID, notFound);
        boolean isPeriodic = intent.getBooleanExtra(ArinAlarmController.EXTRA_PERIODIC, false);
        if(isPeriodic){
            Alarm alarm = alarmDAO.selectById(alarmId);
            alarm.setPeriodicStartTime(new Date().getTime());
            new ArinAlarmController().setAlarm(context, alarm);
        }

        // delete alarm from the database
        alarmDAO.delete(alarmId);

        // get listener using class name and reflection
        String handler = intent.getStringExtra(ArinAlarmController.EXTRA_HANDLER);
        AlarmHandleListener listener = Util.getInstance(handler);

        // call onHandle
        if (listener != null) {
            listener.onHandle(context, intent);
        } else {
            Log.i(TAG, "not able to get new instance for class " + handler);
        }
    }
}
