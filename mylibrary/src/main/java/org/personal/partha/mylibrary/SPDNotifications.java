package org.personal.partha.mylibrary;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.io.Serializable;
import java.util.Map;

public class SPDNotifications {

    public static long AUTO_SYNC_DURATION;
    public static String CHANNEL_ID;
    public static String CHANNEL_NAME;
    public static String CHANNEL_DESC;

    static {
        AUTO_SYNC_DURATION = 900000L;
        CHANNEL_ID = "203124";
        CHANNEL_NAME = "Notifications (Shapartha)";
        CHANNEL_DESC = "Manage notifications specific to this app";
    }

    public static void createNotificationChannel(Context mContext) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance);
            channel.setDescription(CHANNEL_DESC);
            long[] v = {500, 500};
            Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            channel.setSound(soundUri, new AudioAttributes.Builder().setLegacyStreamType(AudioManager.STREAM_NOTIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION_EVENT).build());
            channel.setVibrationPattern(v);
            channel.enableVibration(true);
            channel.setLightColor(android.R.color.white);
            channel.enableLights(true);
            NotificationManager notificationManager = mContext.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public static NotificationCompat.Builder getNotificationBuilder(Context mContext, String title, String message) {
        return getNotificationBuilder(mContext, title, message, null);
    }

    public static NotificationCompat.Builder getNotificationBuilder(Context mContext, String title, String message, String bigMessage) {
        return getNotificationBuilder(mContext, title, message, bigMessage, null);
    }

    public static NotificationCompat.Builder getNotificationBuilder(Context mContext, String title, String message, String bigMessage, Class<?> className) {
        return getNotificationBuilder(mContext, title, message, bigMessage, className, null);
    }

    public static NotificationCompat.Builder getNotificationBuilder(Context mContext, String title, String message, String bigMessage, Class<?> className, Map<String, Serializable> intentParams) {
        if (className == null) {
            return null;
        }
        Intent intent = new Intent(mContext, className);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        if (intentParams != null) {
            for (Map.Entry<String, Serializable> mapEntry : intentParams.entrySet()) {
                intent.putExtra(mapEntry.getKey(), mapEntry.getValue());
                SPDUtilities.writeLog(SPDUtilities.LOG_LEVEL.INFO, ">>" + mapEntry.getValue().toString());
            }
        }
        int requestCode = (int) (Math.random() * 1000);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        if (bigMessage != null) {
            builder.setStyle(new NotificationCompat.BigTextStyle().bigText(bigMessage));
        }
        return builder;
    }

    public static void sendNotification(Context mContext, Map<Integer, NotificationCompat.Builder> notificationBundle) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(mContext);
        for (Map.Entry<Integer, NotificationCompat.Builder> entry : notificationBundle.entrySet()) {
            notificationManager.notify(entry.getKey(), entry.getValue().build());
        }
        try {
            /*
            Minimum Delay between two consecutive notifications
             */
            Thread.sleep(500);
        } catch (InterruptedException e) {
            SPDUtilities.writeLog(SPDUtilities.LOG_LEVEL.INFO, ">>" + e.getMessage());
        }
    }

    public static void cancelNotification(Context mContext, int notificationId) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(mContext);
        notificationManager.cancel(notificationId);
    }

    public static void cancelAllNotification(Context mContext) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(mContext);
        notificationManager.cancelAll();
    }

    public static void myJobScheduler(Context mContext, Class<?> className, int jobId, long reTriggerDuration) {
        JobScheduler jobScheduler = (JobScheduler) mContext.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        if (isJobServiceOn(jobId, mContext)) {
            SPDUtilities.writeLog(SPDUtilities.LOG_LEVEL.INFO, "Job Scheduled already - " + jobId);
        } else {
            int hasScheduled = jobScheduler.schedule(new JobInfo.Builder(jobId,
                    new ComponentName(mContext, className))
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                    .setPeriodic(reTriggerDuration)
                    .build());
            SPDUtilities.writeLog(SPDUtilities.LOG_LEVEL.INFO, "Job Scheduled - " + jobId + ":" + hasScheduled);
        }
    }

    public static boolean isJobServiceOn(int jobId, Context mContext) {
        JobScheduler scheduler = (JobScheduler) mContext.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        boolean hasBeenScheduled = false;
        for (JobInfo jobInfo : scheduler.getAllPendingJobs()) {
            if (jobInfo.getId() == jobId) {
                hasBeenScheduled = true;
                break;
            }
        }
        return hasBeenScheduled;
    }

    public static void startAlert(Context mContext, Class<?> className) {
        Intent intent = new Intent(mContext, className);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, 252248126, intent, 0);
        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), AUTO_SYNC_DURATION, pendingIntent);
    }
}
