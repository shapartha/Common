package org.personal.partha.mylibrary;

import android.util.Log;

public class Utilities {
    public static final boolean JAVA_LOG_ENABLED = true;
    public static String LOGGER_NAME;
    public enum LOG_LEVEL {
        WARN, DEBUG, INFO, ERROR
    }

    public static void writeLog(LOG_LEVEL logLevel, String logMsg) {
        if (JAVA_LOG_ENABLED) {
            if (logLevel.equals(LOG_LEVEL.WARN)) {
                Log.w(LOGGER_NAME, logMsg);
            } else if (logLevel.equals(LOG_LEVEL.DEBUG)) {
                Log.d(LOGGER_NAME, logMsg);
            } else if (logLevel.equals(LOG_LEVEL.INFO)) {
                Log.i(LOGGER_NAME, logMsg);
            } else if (logLevel.equals(LOG_LEVEL.ERROR)) {
                Log.e(LOGGER_NAME, logMsg);
            }
        }
    }
}
