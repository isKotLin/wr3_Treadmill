package com.vigorchip.treadmill.utils;

import android.util.Log;

/**
 * 需要注意的是 STACK_TRACE , 可以理解是调用函数的堆栈数,调整这个值可以获得第STACK_TRACE次调用的函数,如果现实的函数名不对，试着调下这个值就好了！
 */
public class LogUtils {
    private static final String TAG = "LogUtils";
    private static final int STACK_TRACE = 3;

    public static void log(Object log) {
        StringBuffer sb = new StringBuffer(log+"");
//        sb.append(getSystemMsg());
        Log.println(Log.ASSERT, TAG, sb.toString());
    }

    public static void logStackTrace() {
        LogUtils.log(Log.getStackTraceString(new Throwable()));
    }

    private static String getSystemMsg() {
        StringBuffer sb = new StringBuffer();
        sb.append("  [ ");
        sb.append("file:").append(getFileName()).append(", ");
        sb.append("line:").append(getLineNumber()).append(", ");
        sb.append("method:").append(getMethodName()).append(", ");
        sb.append("class:").append(getClassName());
        sb.append(" ]");
        return sb.toString();
    }

    private static String getLineNumber() {
        return String.valueOf(new Throwable().getStackTrace()[STACK_TRACE].getLineNumber());
    }

    private static String getMethodName() {
        return new Throwable().getStackTrace()[STACK_TRACE].getMethodName();
    }

    private static String getClassName() {
        return new Throwable().getStackTrace()[STACK_TRACE].getClassName();
    }

    private static String getFileName() {
        return new Throwable().getStackTrace()[STACK_TRACE].getFileName();
    }
}