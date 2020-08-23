package com.arindam.alarmmanager.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.arindam.alarmmanager.model.Alarm;

public class AlarmCursorWrapper extends CursorWrapper {

    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public AlarmCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Alarm getAlarm() {
        if (isBeforeFirst() || isAfterLast()) {
            return null;
        }

        long id = getLong(AlarmDAO.Columns.ID_INDEX);
        long externalId = getLong(AlarmDAO.Columns.EXTERNAL_ID_INDEX);
        boolean exact = getInt(AlarmDAO.Columns.IS_PERIODIC_INDEX) == 1;
        int periodic_type = getInt(AlarmDAO.Columns.PERIODIC_TYPE_INDEX);
        int periodic_value = getInt(AlarmDAO.Columns.PERIODIC_VALUE_INDEX);
        long periodic_start_time = getLong(AlarmDAO.Columns.PERIODIC_START_TIME_INDEX);
        long time = getLong(AlarmDAO.Columns.TIME_INDEX);
        boolean enabled = getInt(AlarmDAO.Columns.ENABLED_INDEX) == 1;
        String title = getString(AlarmDAO.Columns.TITLE_INDEX);
        String message = getString(AlarmDAO.Columns.MESSAGE_INDEX);
        boolean keepAfterReboot = getInt(AlarmDAO.Columns.KEEP_AFTER_REBOOT_INDEX) == 1;
        String alarmHandleListener = getString(AlarmDAO.Columns.ALARM_HANDLE_LISTENER_INDEX);

        Alarm alarm = new Alarm();
        alarm.setId(id);
        alarm.setExternalId(externalId);
        alarm.setPeriodic(exact);
        alarm.setPeriodicType(periodic_type);
        alarm.setPeriodicValue(periodic_value);
        alarm.setPeriodicStartTime(periodic_start_time);
        alarm.setTime(time);
        alarm.setEnabled(enabled);
        alarm.setTitle(title);
        alarm.setMessage(message);
        alarm.setKeepAfterReboot(keepAfterReboot);
        alarm.setAlarmHandleListener(alarmHandleListener);

        return alarm;
    }
}
