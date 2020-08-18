package com.arindam.alarmmanager.listener;

import android.content.Context;
import android.content.Intent;

public abstract class AlarmHandleListener {

    public AlarmHandleListener() {
        // must have public default constructor
    }

    public abstract void onHandle(Context context, Intent intent);
}
