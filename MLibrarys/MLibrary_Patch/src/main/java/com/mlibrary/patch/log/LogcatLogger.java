package com.mlibrary.patch.log;

import android.util.Log;

class LogcatLogger implements Logger {
    private final String tag;

    LogcatLogger(Class<?> classType) {
        this(classType.getSimpleName());
    }

    LogcatLogger(String _tag) {
        this.tag = _tag;
    }

    @Override
    public void d(String msg) {
        if (!LoggerFactory.isNeedLog || (LogLevel.DEBUG.getLevel() < LoggerFactory.minLevel.getLevel()))
            return;
        Log.d(tag, msg);
    }

    @Override
    public void i(String msg) {
        if (!LoggerFactory.isNeedLog || (LogLevel.INFO.getLevel() < LoggerFactory.minLevel.getLevel()))
            return;
        Log.i(tag, msg);
    }

    @Override
    public void w(String msg) {
        if (!LoggerFactory.isNeedLog || (LogLevel.WARN.getLevel() < LoggerFactory.minLevel.getLevel()))
            return;
        Log.w(tag, msg);
    }

    @Override
    public void e(String msg) {
        if (!LoggerFactory.isNeedLog || (LogLevel.ERROR.getLevel() < LoggerFactory.minLevel.getLevel()))
            return;
        Log.e(tag, msg);
    }

    @Override
    public void e(String msg, Throwable throwable) {
        if (!LoggerFactory.isNeedLog || (LogLevel.ERROR.getLevel() < LoggerFactory.minLevel.getLevel()))
            return;
        Log.e(tag, msg, throwable);
    }

    @Override
    public void log(String msg) {
        if (!LoggerFactory.isNeedLog) return;
        switch (LoggerFactory.minLevel) {
            case DEBUG:
                Log.d(tag, msg);
                break;
            case INFO:
                Log.i(tag, msg);
                break;
            case WARN:
                Log.w(tag, msg);
                break;
            case ERROR:
                Log.e(tag, msg);
                break;
            default:
                break;
        }
    }

    @Override
    public void log(String msg, LogLevel level) {
        if (!LoggerFactory.isNeedLog) return;
        if (level.getLevel() < LoggerFactory.minLevel.getLevel()) return;
        switch (level) {
            case DEBUG:
                Log.d(tag, msg);
                break;
            case INFO:
                Log.i(tag, msg);
                break;
            case WARN:
                Log.w(tag, msg);
                break;
            case ERROR:
                Log.e(tag, msg);
                break;
            default:
                break;
        }
    }

    @Override
    public void log(String msg, LogLevel level, Throwable th) {
        if (!LoggerFactory.isNeedLog) return;
        if (level.getLevel() < LoggerFactory.minLevel.getLevel()) return;
        switch (level) {
            case DEBUG:
                Log.d(tag, msg, th);
                break;
            case INFO:
                Log.i(tag, msg, th);
                break;
            case WARN:
                Log.w(tag, msg, th);
                break;
            case ERROR:
                Log.e(tag, msg, th);
                break;
            default:
                break;
        }
    }
}
