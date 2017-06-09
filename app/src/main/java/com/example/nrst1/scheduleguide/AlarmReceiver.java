package com.example.nrst1.scheduleguide;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

/**
 * Created by nrst1 on 2017-06-09.
 */

public class AlarmReceiver extends BroadcastReceiver {
    AlarmReceiver() {}

    @Override
    public void onReceive(Context context, Intent intent) {
        String title = intent.getStringExtra("title");
        String content = intent.getStringExtra("content");
        int key = intent.getIntExtra("key", -1);
        int year = intent.getIntExtra("year", -1);
        int month = intent.getIntExtra("month", -1);
        int day = intent.getIntExtra("day", -1);
        double alarm = intent.getDoubleExtra("alarm", -1);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, day);

        NotificationHandler notificationHandler = new NotificationHandler(context);
        notificationHandler.setNotification(title, content, key, calendar, alarm);
    }
}
