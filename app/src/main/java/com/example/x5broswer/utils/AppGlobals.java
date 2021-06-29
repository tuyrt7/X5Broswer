package com.example.x5broswer.utils;

import android.app.Application;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class AppGlobals {

    private static Application sApplication;

    public static Application getApplication() {
        if (sApplication == null) {
            try {
                Method method = Class.forName("android.app.ActivityThread").getDeclaredMethod("currentApplication");
                sApplication = (Application) method.invoke(null);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return sApplication;
    }

    /**
     * 是否主进程（多进程Application 重复初始化）
     * @return
     */
    public static boolean isMainProcess() {
        try {
            Class<?> clazz = Class.forName("android.app.ActivityThread");
            Method currentActivityThreadMethod = clazz.getDeclaredMethod("currentActivityThread");
            currentActivityThreadMethod.setAccessible(true);
            Object tCurrentActivityThread = currentActivityThreadMethod.invoke(null);

            Method tGetProcessNameMethod = clazz.getDeclaredMethod("getProcessName");
            tGetProcessNameMethod.setAccessible(true);
            String tProcessName = (String) tGetProcessNameMethod.invoke(tCurrentActivityThread);
            return getApplication().getPackageName().equalsIgnoreCase(tProcessName);
        } catch (Throwable e) {
            return true;
        }
    }
}
