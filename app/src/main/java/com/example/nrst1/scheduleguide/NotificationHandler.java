package com.example.nrst1.scheduleguide;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;

import com.google.firebase.database.DatabaseReference;

import java.util.Calendar;

/**
 * Created by nrst1 on 2017-06-09.
 */

public class NotificationHandler {
    Context context;
    NotificationManager notificationManager;
    int maxID = Integer.MIN_VALUE;

    AlarmManager alarmManager;

    NotificationHandler() {}
    NotificationHandler(Context context) {
        this.context = context;
        this.notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        this.alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    NotificationManager getNotificationManager() { return this.notificationManager; }
    AlarmManager getAlarmManager() { return this.alarmManager; }

    public interface NotificationCallBack {
        public void setNotification(int maxID);
    }

    public void setNotification(String title, String content, int key, Calendar calendar, double alarm) {
        FirebaseHandler firebaseHandler = new FirebaseHandler(context);
        DatabaseReference scheduleTable = firebaseHandler.getScheduleTable();

        final Notification.Builder builder = new Notification.Builder(context);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher));

        builder.setTicker(title);
        builder.setContentTitle(title);
        builder.setContentText(content);

        builder.setVibrate(new long[]{0,1000});

        Uri soundUri= RingtoneManager.getActualDefaultRingtoneUri(context, RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(soundUri);

        builder.setAutoCancel(true);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        final Intent intent = new Intent(context, DetailSchedule.class);
        intent.putExtra("year", year);
        intent.putExtra("month", month);
        intent.putExtra("day", day);
        intent.putExtra("key", key);
        String dateKey = String.valueOf(year) + String.valueOf(month) + String.valueOf(day) + String.valueOf(key);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, Integer.parseInt(dateKey), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        Notification notification = builder.build();
        notificationManager.notify(Integer.parseInt(dateKey), notification);
    }

    public void setAlarm(String title, String content, int key, Calendar calendar, double alarm) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        int hour = (int) alarm;
        int min = (int) ((alarm - hour) * 60);
        calendar.add(Calendar.HOUR, -hour);
        calendar.add(Calendar.MINUTE, -min);

        final Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("title", title);
        intent.putExtra("content", content);
        intent.putExtra("key", key);
        intent.putExtra("year", year);
        intent.putExtra("month", month);
        intent.putExtra("day", day);
        intent.putExtra("alarm", alarm);
        String dateKey = String.valueOf(year) + String.valueOf(month) + String.valueOf(day) + String.valueOf(key);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, Integer.parseInt(dateKey), intent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }
}
