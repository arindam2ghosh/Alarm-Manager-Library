package com.arindam.alarmmanager.util;

import com.arindam.alarmmanager.listener.AlarmHandleListener;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class Util {

    private static final String TAG = "Arindam";

    public static AlarmHandleListener getInstance(String className) {
        
        if (className == null) {
            return null;
        }

        Object instance = null;

        //noinspection TryWithIdenticalCatches
        try {
            Class cl = Class.forName(className);
            @SuppressWarnings("unchecked")
            Constructor constructor = cl.getConstructor();
            instance = constructor.newInstance();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (InstantiationException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (NoSuchMethodException ex) {
            ex.printStackTrace();
        } catch (InvocationTargetException ex) {
            ex.printStackTrace();
        }

        return (AlarmHandleListener) instance;
    }
}
