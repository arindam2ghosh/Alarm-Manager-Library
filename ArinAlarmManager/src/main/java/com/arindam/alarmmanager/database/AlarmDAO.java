package com.arindam.alarmmanager.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.arindam.alarmmanager.model.Alarm;

import java.util.ArrayList;
import java.util.List;

public class AlarmDAO {

    public static final String TABLE = "alarm";

    public static class Columns implements BaseColumns {

        public static final String EXTERNAL_ID = "external_id";
        public static final String IS_EXACT = "is_exact";
        public static final String TYPE = "type";
        public static final String TIME = "time";
        public static final String ENABLED = "enabled";
        public static final String TITLE = "title";
        public static final String MESSAGE = "message";
        public static final String KEEP_AFTER_REBOOT = "keep_after_reboot";
        public static final String ALARM_HANDLE_LISTENER = "alarm_handle_listener";

        public static final int ID_INDEX = 0;
        public static final int EXTERNAL_ID_INDEX = 1;
        public static final int IS_EXACT_INDEX = 2;
        public static final int TYPE_INDEX = 3;
        public static final int TIME_INDEX = 4;
        public static final int ENABLED_INDEX = 5;
        public static final int TITLE_INDEX = 6;
        public static final int MESSAGE_INDEX = 7;
        public static final int KEEP_AFTER_REBOOT_INDEX = 8;
        public static final int ALARM_HANDLE_LISTENER_INDEX = 9;
    }

    private Context mContext;

    public AlarmDAO(Context context) {
        mContext = context;
    }

    public AlarmCursorWrapper select(String[] columns, String selection, String[] selectionArgs,
                                     String groupBy, String having, String orderBy) {

        if (columns == null) {
            columns = new String[]{
                    Columns._ID,
                    Columns.EXTERNAL_ID,
                    Columns.IS_EXACT,
                    Columns.TYPE,
                    Columns.TIME,
                    Columns.ENABLED,
                    Columns.TITLE,
                    Columns.MESSAGE,
                    Columns.KEEP_AFTER_REBOOT,
                    Columns.ALARM_HANDLE_LISTENER
            };
        }

        SQLiteDatabase db = Database.Manager.getInstance(mContext).getReadableDatabase();

        Cursor cursor = db.query(TABLE, columns, selection, selectionArgs,
                groupBy, having, orderBy);

        return new AlarmCursorWrapper(cursor);
    }

    public AlarmCursorWrapper select(String selection, String[] selectionArgs) {

        Cursor cursor = select(null, selection, selectionArgs, null, null, Columns.TIME);

        return new AlarmCursorWrapper(cursor);
    }

    public AlarmCursorWrapper select() {

        Cursor cursor = select(null, null, null, null, null, Columns.TIME);

        return new AlarmCursorWrapper(cursor);
    }

    public Alarm select(String column, String value) {

        AlarmCursorWrapper wrapper = select(column + "=?", new String[]{value});

        if (wrapper.moveToFirst()) {
            return wrapper.getAlarm();
        } else {
            throw new SQLException("Failed to select Alarm with id " + value);
        }
    }

    public Alarm selectById(long id) {
        return select(Columns._ID, String.valueOf(id));
    }

    public Alarm selectByExternalId(long id) {
        return select(Columns.EXTERNAL_ID, String.valueOf(id));
    }

    public long insert(Alarm alarm) {
        SQLiteDatabase db = Database.Manager.getInstance(mContext).getWritableDatabase();

        ContentValues cv = toContentValues(alarm);

        long rowId = db.insert(TABLE, null, cv);
        if (rowId < 0) {
            throw new SQLException("Failed to insert Alarm " + cv);
        }

        return rowId;
    }

    public int update(Alarm alarm) {
        SQLiteDatabase db = Database.Manager.getInstance(mContext).getWritableDatabase();
        int count;
        long rowId = alarm.getId();

        ContentValues cv = toContentValues(alarm);

        String selection = Columns._ID + "=" + rowId;

        count = db.update(TABLE, cv, selection, null);

        return count;
    }

    public int delete(String whereClause, String[] whereArgs) {
        SQLiteDatabase db = Database.Manager.getInstance(mContext).getWritableDatabase();
        int count;

        count = db.delete(TABLE, whereClause, whereArgs);

        return count;
    }

    public int delete(String column, String value) {
        return delete(column + "=?", new String[]{value});
    }

    /**
     *
     * @param id the alarm id to be deleted
     * @return the number of rows affected
     */
    public int delete(long id) {
        return delete(Columns._ID + "=?", new String[]{String.valueOf(id)});
    }

    public int delete(Alarm alarm) {
        return delete(alarm.getId());
    }

    public int deleteAll(){
        SQLiteDatabase db = Database.Manager.getInstance(mContext).getWritableDatabase();
        int count;
        count = db.delete(TABLE, null, null);
        return count;
    }

    public List<Alarm> getAllRecord(){
        SQLiteDatabase db = Database.Manager.getInstance(mContext).getWritableDatabase();
        AlarmCursorWrapper cursor = select();

        List<Alarm> alarmList = new ArrayList<>();

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                Alarm alarm = cursor.getAlarm();
                alarmList.add(alarm);
                cursor.moveToNext();
            }
        }
        return alarmList;
    }

    private ContentValues toContentValues(Alarm alarm) {
        ContentValues cv = new ContentValues();
        cv.put(Columns.EXTERNAL_ID, alarm.getExternalId());
        cv.put(Columns.IS_EXACT, alarm.isExact());
        cv.put(Columns.TYPE, alarm.getType());
        cv.put(Columns.TIME, alarm.getTime());
        cv.put(Columns.ENABLED, alarm.isEnabled() ? 1 : 0);
        cv.put(Columns.TITLE, alarm.getTitle());
        cv.put(Columns.MESSAGE, alarm.getMessage());
        cv.put(Columns.KEEP_AFTER_REBOOT, alarm.isKeepAfterReboot() ? 1 : 0);
        cv.put(Columns.ALARM_HANDLE_LISTENER, alarm.getAlarmHandleListener());
        return cv;
    }
}
