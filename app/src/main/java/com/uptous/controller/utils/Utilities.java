package com.uptous.controller.utils;
/**
 * This file contain all common functions which are use in application
 */

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static android.content.Context.ACTIVITY_SERVICE;

@SuppressLint("SimpleDateFormat")
public class Utilities {

    private static PrintWriter exceptionLogWriter = null;

    public static void logExceptionToFile(String extraMessage,
                                          Throwable exceptionToLog) {
        try {
            if (null == exceptionLogWriter)
                initializeExceptionLogFile();

            if (null != exceptionLogWriter) {
                StringBuffer logBuffer = new StringBuffer();
                logBuffer.append("------------------ " + new Date()
                        + " ------------------\n");
                logBuffer.append("\n*******************************\n");
                if (null != extraMessage) {
                    logBuffer.append(extraMessage + "\n");
                }
                exceptionLogWriter.println(logBuffer.toString());
                if (null != exceptionToLog)
                    exceptionToLog.printStackTrace(exceptionLogWriter);
                exceptionLogWriter.println();
            }

            if (null == extraMessage)
                extraMessage = "extra message";
            if (null != exceptionToLog) {
                Log.e("VMDUtil", extraMessage, exceptionToLog);
            } else {
                Log.e("VMDUtil", extraMessage);
            }

        } catch (Throwable e) {
            Log.e("Utilities", "exception in writing to exception log file.", e);
        }
    }

    public static void logExceptionToFile(Throwable exceptionToLog) {
        logExceptionToFile(null, exceptionToLog);
    }

    private static void initializeExceptionLogFile() {
        try {
            if (null != exceptionLogWriter)
                return;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd_MM_yy");
            String logFileName = "log.log";

            File directory = new File(Environment.getExternalStorageDirectory()
                    .toString() + "/UpToUsExecption");
            boolean b = directory.mkdirs();

            if (b) {

            } else {
                directory.mkdirs();
            }

            logFileName = "uptous_exception_"
                    + simpleDateFormat.format(new Date()) + ".txt";
            File logFile = new File(directory, logFileName);
            if (!logFile.exists()) {
                logFile.createNewFile();
            }
            exceptionLogWriter = new PrintWriter(new FileOutputStream(logFile,
                    true), true);

        } catch (Throwable e) {
            Log.e("Utilities ", "initializing exception log file", e);
        }
    }


    public static Boolean isLastAppActivity(Context context)
    {
        ActivityManager mngr = (ActivityManager) context.getSystemService( ACTIVITY_SERVICE );
        List<ActivityManager.RunningTaskInfo> taskList = mngr.getRunningTasks(5);
        if(taskList.get(0).numActivities == 1 ) {
            return true;
        }
        return false;
    }


}
