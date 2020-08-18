package com.arindam.alarmmanager.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "com.simplaapliko.wakeup.db";
    private static final int DATABASE_VERSION = 1;

    private Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + AlarmDAO.TABLE + " (" +
                        AlarmDAO.Columns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        AlarmDAO.Columns.EXTERNAL_ID + " INTEGER NOT NULL, " +
                        AlarmDAO.Columns.IS_EXACT + " INTEGER NOT NULL, " +
                        AlarmDAO.Columns.TYPE + " INTEGER NOT NULL, " +
                        AlarmDAO.Columns.TIME + " INTEGER, " +
                        AlarmDAO.Columns.ENABLED + " INTEGER NOT NULL, " +
                        AlarmDAO.Columns.TITLE + " TEXT, " +
                        AlarmDAO.Columns.MESSAGE + " TEXT, " +
                        AlarmDAO.Columns.KEEP_AFTER_REBOOT + " INTEGER NOT NULL, " +
                        AlarmDAO.Columns.ALARM_HANDLE_LISTENER + " TEXT);"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // currently nothing to update
    }

    public static class Manager {

        private static Manager sManager;

        private Database mDatabase;

        public static synchronized Manager getInstance(Context context){
            if (sManager == null) {
                sManager = new Manager(context);
            }
            return sManager;
        }

        private Manager(Context context) {
            mDatabase = new Database(context);
        }

        public SQLiteDatabase getWritableDatabase() {
            return mDatabase.getWritableDatabase();
        }

        public SQLiteDatabase getReadableDatabase() {
            return mDatabase.getReadableDatabase();
        }
    }
}