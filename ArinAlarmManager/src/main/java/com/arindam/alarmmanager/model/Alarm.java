package com.arindam.alarmmanager.model;

import android.app.AlarmManager;

public class Alarm {

    public static final int NOT_SET = -1;

    public static final int TIMEUNIT_MILLISECOND = 1001;
    public static final int TIMEUNIT_SECOND = 1002;
    public static final int TIMEUNIT_MINUTE = 1003;
    public static final int TIMEUNIT_HOUR = 1004;

    private long mId;
    private long mExternalId;
    private boolean mExact;
    private long mTime;
    private int mPeriodicType;
    private int mPeriodicValue;
    private long mPeriodicStartTime;
    private boolean mEnabled;
    private String mTitle;
    private String mMessage;
    private boolean mKeepAfterReboot;
    private String mAlarmHandleListener;

    public Alarm() {
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public long getExternalId() {
        return mExternalId;
    }

    public void setExternalId(long externalId) {
        mExternalId = externalId;
    }

    public boolean isPeriodic() {
        return mExact;
    }

    public void setPeriodic(boolean exact) {
        mExact = exact;
    }

    public long getTime() {
        return mTime;
    }

    public void setTime(long time) {
        mTime = time;
    }

    public boolean isEnabled() {
        return mEnabled;
    }

    public void setEnabled(boolean enabled) {
        mEnabled = enabled;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public boolean isKeepAfterReboot() {
        return mKeepAfterReboot;
    }

    public void setKeepAfterReboot(boolean keepAfterReboot) {
        mKeepAfterReboot = keepAfterReboot;
    }

    public String getAlarmHandleListener() {
        return mAlarmHandleListener;
    }

    public void setAlarmHandleListener(String alarmHandleListener) {
        mAlarmHandleListener = alarmHandleListener;
    }

    public int getPeriodicType() {
        return mPeriodicType;
    }

    public void setPeriodicType(int mPeriodicType) {
        this.mPeriodicType = mPeriodicType;
    }

    public int getPeriodicValue() {
        return mPeriodicValue;
    }

    public void setPeriodicValue(int mPeriodicValue) {
        this.mPeriodicValue = mPeriodicValue;
    }

    public long getPeriodicStartTime() {
        return mPeriodicStartTime;
    }

    public void setPeriodicStartTime(long periodicStartTime) {
        this.mPeriodicStartTime = periodicStartTime;
    }

    @Override
    public String toString() {
        return "Alarm{" +
                "mId=" + mId +
                ", mExternalId=" + mExternalId +
                ", mExact=" + mExact +
                ", mPeriodicType=" + mPeriodicType +
                ", mPeriodicValue=" + mPeriodicValue +
                ", mPeriodicStartTime=" + mPeriodicStartTime +
                ", mTime=" + mTime +
                ", mEnabled=" + mEnabled +
                ", mTitle='" + mTitle + '\'' +
                ", mMessage='" + mMessage + '\'' +
                ", mKeepAfterReboot=" + mKeepAfterReboot +
                ", mAlarmHandleListener='" + mAlarmHandleListener + '\'' +
                '}';
    }
}
