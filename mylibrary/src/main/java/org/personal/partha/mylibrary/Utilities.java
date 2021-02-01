package org.personal.partha.mylibrary;

import android.util.Log;

public class Utilities {
    public static final boolean JAVA_LOG_ENABLED = true;
    public static String LOGGER_NAME;
    public enum LOG_LEVEL {
        WARN, DEBUG, INFO, ERROR
    }

    public static void writeLog(String logLevel, String logMsg) {
        if (JAVA_LOG_ENABLED) {
            if (logLevel.equals(LOG_LEVEL.WARN.toString())) {
                Log.w(LOGGER_NAME, logMsg);
            } else if (logLevel.equals(LOG_LEVEL.DEBUG.toString())) {
                Log.d(LOGGER_NAME, logMsg);
            } else if (logLevel.equals(LOG_LEVEL.INFO.toString())) {
                Log.i(LOGGER_NAME, logMsg);
            } else if (logLevel.equals(LOG_LEVEL.ERROR.toString())) {
                Log.e(LOGGER_NAME, logMsg);
            }
        }
    }
}
