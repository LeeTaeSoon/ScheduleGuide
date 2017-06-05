package com.example.nrst1.scheduleguide;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by nrst1 on 2017-05-25.
 */

public class Day {
    int year;
    int month;
    int day;
    int dayOfTheWeek;

    Day() {}
    Day(int year, int month, int day, int dayOfTheWeek) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.dayOfTheWeek = dayOfTheWeek;
    }

    void setYear(int year) { this.year = year; }
    void setMonth(int month) { this.month = month; }
    void setDay(int day) { this.day = day; }
    void setDayOfTheWeek(int dayOfTheWeek) { this.dayOfTheWeek = dayOfTheWeek; }

    int getYear() { return this.year; }
    int getMonth() { return this.month; }
    int getDay() { return this.day; }
    int getDayOfTheWeek() { return this.dayOfTheWeek; }

    public interface DayCallback {
        public void getMonthInfo(int first, int num);
    }

    public void getMonthInfo(int year, int month, final int day, final DayCallback callback) {
        FirebaseHandler firebaseHandler = new FirebaseHandler();
        firebaseHandler.setFirebase();
        DatabaseReference calenderTable = firebaseHandler.getCalanderTable();

        calenderTable.child(String.valueOf(year)).child(String.valueOf(month)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    JSONObject dayInfo = new JSONObject(dataSnapshot.getValue().toString());
                    int first = dayInfo.getInt("first");
                    int dayNum = dayInfo.getInt("num");

                    int dayOfTheWeek = day - 1;
                    dayOfTheWeek = dayOfTheWeek + first % 7;

                    callback.getMonthInfo(dayOfTheWeek, dayNum);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public Calendar getDateFromString(String date) {
        try {
            Date d = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(d);

            return calendar;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }
}
