package com.arindam.alarmmanager.sample;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import com.arindam.alarmmanager.ArinAlarmController;
import com.arindam.alarmmanager.database.AlarmCursorWrapper;
import com.arindam.alarmmanager.model.Alarm;

import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setReminder();
    }

    private void setReminder(){

        List<Alarm> alarmList = new ArinAlarmController().getAllAlarm(MainActivity.this);
        Log.i("Arindam", ""+alarmList.size());

        new ArinAlarmController().deleteAllAlarm(MainActivity.this);


        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.SECOND, 10);

        Alarm alarm = new Alarm();
        alarm.setExternalId(10);
        alarm.setExact(false);
        alarm.setType(Alarm.RTC_WAKEUP);
        alarm.setTime(calendar.getTimeInMillis());
        alarm.setEnabled(true);
        alarm.setTitle("Title");
        alarm.setMessage(getString(R.string.app_name));
        alarm.setAlarmHandleListener(SendNotification.class.getCanonicalName());

        new ArinAlarmController().setAlarm(MainActivity.this, alarm);

    }
}