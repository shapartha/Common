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

    /**
     * Creates a Notification Channel for the application with default values from <code>AUTO_SYNC_DURATION,
     * CHANNEL_ID, CHANNEL_NAME, CHANNEL_DESC</code> unless explicitly specified before calling this method.
     *
     * @param mContext Application Context object to access the application
     */
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

    /**
     * Creates a builder object from {@link NotificationCompat} class, customize it with set argument values and return the Builder object
     * for use in application code. Use this if you don't need expandable message in notification &amp; don't need to
     * pass any values to the {@link PendingIntent} object
     *
     * @param mContext Application Context object to access the application
     * @param title Title string of the notification
     * @param message Message string of the notification
     * @param className Class object to redirect to on tapping the notification
     * @return Builder object of the customized {@link NotificationCompat} object
     */
    public static NotificationCompat.Builder getNotificationBuilder(Context mContext, String title, String message, Class<?> className) {
        return getNotificationBuilder(mContext, title, message, null, className);
    }

    /**
     * Creates a builder object from {@link NotificationCompat} class, customize it with set argument values and return the Builder object
     * for use in application code. Use this if you don't need to pass any values to the {@link PendingIntent} object
     *
     * @param mContext Application Context object to access the application
     * @param title Title string of the notification
     * @param message Message string of the notification
     * @param bigMessage Message string of the notification to be displayed on tapping the expand icon on the notification
     * @param className Class object to redirect to on tapping the notification
     * @return Builder object of the customized {@link NotificationCompat} object
     */
    public static NotificationCompat.Builder getNotificationBuilder(Context mContext, String title, String message, String bigMessage, Class<?> className) {
        return getNotificationBuilder(mContext, title, message, bigMessage, className, null);
    }

    /**
     * Creates a builder object from {@link NotificationCompat} class, customize it with set argument values and return the Builder object
     * for use in application code.
     *
     * @param mContext Application Context object to access the application
     * @param title Title string of the notification
     * @param message Message string of the notification
     * @param bigMessage Message string of the notification to be displayed on tapping the expand icon on the notification
     * @param className Class object to redirect to on tapping the notification
     * @param intentParams Values to pass to the <code>className</code> object on tapping the notification
     * @return Builder object of the customized {@link NotificationCompat} object
     */
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

    /**
     * Sends one or many notification(s) to the android system
     *
     * @param mContext Application context object to access the application
     * @param notificationBundle Map of all notifications bundled in a {@link NotificationCompat.Builder} object each
     */
    public static void sendNotification(Context mContext, Map<Integer, NotificationCompat.Builder> notificationBundle) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(mContext);
        for (Map.Entry<Integer, NotificationCompat.Builder> entry : notificationBundle.entrySet()) {
            notificationManager.notify(entry.getKey(), entry.getValue().build());
        }
        try {
            // Minimum Delay between two consecutive notifications
            Thread.sleep(500);
        } catch (InterruptedException e) {
            SPDUtilities.writeLog(SPDUtilities.LOG_LEVEL.INFO, ">>" + e.getMessage());
        }
    }

    /**
     * Cancels a specific shown notification of the application from the notification drawer (specified by <code>notificationId</code> parameter
     * )
     * @param mContext Application Context object to access the application
     * @param notificationId ID of the notification to hide / remove
     */
    public static void cancelNotification(Context mContext, int notificationId) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(mContext);
        notificationManager.cancel(notificationId);
    }

    /**
     * Cancels all shown notifications of the application from the notification drawer
     * )
     * @param mContext Application Context object to access the application
     */
    public static void cancelAllNotification(Context mContext) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(mContext);
        notificationManager.cancelAll();
    }

    /**
     * Schedules a Job of the application denoted by <code>jobId</code> in the android system settings
     *
     * @param mContext Application Context object to access the application
     * @param className Job Service object that holds the business logic of the job to execute
     * @param jobId Job ID of the job to be scheduled
     * @param reTriggerDuration Milliseconds to wait before re-executing the job
     */
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

    /**
     * Checks whether the specified <code>jobId</code> {@link android.app.job.JobService} is scheduled or not
     * @param jobId Job ID of the scheduled job to check for
     * @param mContext Application Context object to access the application
     * @return <b>true</b> or <b>false</b> based on whether the Job is scheduled or not
     */
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
