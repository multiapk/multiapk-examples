package com.mlibrary.patch.log;

public interface Logger {

    void d(String msg);

    void i(String msg);

    void w(String msg);

    void e(String msg);

    void e(String msg, Throwable throwable);

    void log(String msg);

    void log(String msg, LogLevel level);

    void log(String msg, LogLevel level, Throwable th);

    enum LogLevel {
        DEBUG(1), INFO(2), WARN(3), ERROR(4);
        private int _level;

        LogLevel(int level) {
            _level = level;
        }

        public static LogLevel getValue(int level) {
            for (LogLevel l : LogLevel.values()) {
                if (l.getLevel() == level) {
                    return l;
                }
            }
            return LogLevel.DEBUG;
        }

        public int getLevel() {
            return this._level;
        }
    }
}
