package com.mlibrary.multiapk.base.util;

import android.text.TextUtils;
import android.util.Log;

public class LogUtil {
    private static boolean isDebugging = false;

    public static void setDebugAble(boolean isDebug) {
        isDebugging = isDebug;
    }

    public static void v(String tag, String msg) {
        v(tag, msg, null);
    }

    public static void v(String tag, String msg, Throwable tr) {
        if (isDebugging) {
            Log.v(tag, getLocation() + msg, tr);
        }
    }

    public static void d(String tag, String msg) {
        d(tag, msg, null);
    }

    public static void d(String tag, String msg, Throwable tr) {
        if (isDebugging) {
            Log.d(tag, getLocation() + msg, tr);
        }
    }

    public static void i(String tag, String msg) {
        i(tag, msg, null);
    }

    public static void i(String tag, String msg, Throwable tr) {
        if (isDebugging) {
            Log.i(tag, getLocation() + msg, tr);
        }
    }

    public static void w(String tag, String msg) {
        w(tag, msg, null);
    }

    public static void w(String tag, String msg, Throwable tr) {
        if (isDebugging) {
            Log.w(tag, getLocation() + msg, tr);
        }
    }

    public static void e(String tag, String msg) {
        e(tag, msg, null);
    }

    public static void e(String tag, String msg, Throwable tr) {
        if (isDebugging) {
            Log.e(tag, getLocation() + msg, tr);
        }
    }

    public static String getLocation() {
        final String className = LogUtil.class.getName();
        boolean found = false;
        for (StackTraceElement trace : Thread.currentThread().getStackTrace()) {
            try {
                if (found) {
                    if (!trace.getClassName().startsWith(className)) {
                        Class<?> clazz = Class.forName(trace.getClassName());
                        return "[" + getClassName(clazz) + ":"
                                + trace.getMethodName() + ":"
                                + trace.getLineNumber() + "]: ";
                    }
                } else if (trace.getClassName().startsWith(className)) {
                    found = true;
                }
            } catch (ClassNotFoundException ignored) {
            }
        }
        return "[]: ";
    }

    public static String getClassName(Class<?> clazz) {
        if (clazz != null) {
            if (!TextUtils.isEmpty(clazz.getSimpleName()))
                return clazz.getSimpleName();
            return getClassName(clazz.getEnclosingClass());
        }
        return null;
    }
}
