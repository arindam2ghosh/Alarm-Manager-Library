package com.arindam.alarmmanager.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.arindam.alarmmanager.service.AlarmService;

public class AlarmReceiver extends BroadcastReceiver {

    private static final String TAG = "Arindam";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive");

        Intent serviceIntent = new Intent(context, AlarmService.class);
        serviceIntent.putExtras(intent);
//        context.startService(service);
        AlarmService.enqueueWork(context, serviceIntent);
    }
}
