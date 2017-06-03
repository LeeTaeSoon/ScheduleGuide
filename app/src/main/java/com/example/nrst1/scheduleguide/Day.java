package com.example.nrst1.scheduleguide;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by nrst1 on 2017-05-25.
 */

public class Day {
    int year;
    int month;
    int day;
    int dayOfTheWeek;

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

    public int getDayOfTheWeek(int year, int month, int day) {
        FirebaseHandler firebaseHandler = new FirebaseHandler();
        firebaseHandler.setFirebase();
        DatabaseReference calenderTable = firebaseHandler.getCalanderTable();

        final int[] first = {-1};

        calenderTable.child(String.valueOf(year)).child(String.valueOf(month)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    JSONObject day = new JSONObject(dataSnapshot.toString());
                    first[0] = day.getInt(String.valueOf("1"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        while (first[0] == -1);

        int dayOfTheWeek = day - 1;
        dayOfTheWeek = dayOfTheWeek + first[0] % 7;

        return dayOfTheWeek;
    }
}
