package com.example.nrst1.scheduleguide;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by nrst1 on 2017-05-30.
 */

public class Schedule {
    int key;
    int tag;
    String title;
    String startDate;
    String endDate;
    double alarm;
    String location;
    String attandances;
    String color;
    String memo;

    Schedule() {}
    Schedule(int tag, String title, String startDate, String endDate, double alarm, String location, String attandances, String color, String memo) {
        this.key = -1;
        this.tag = tag;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.alarm = alarm;
        this.location = location;
        this.attandances = attandances;
        this.color = color;
        this.memo = memo;
    }

    void setKey(int key) { this.key = key; }
    void setTag(int tag) { this.tag = tag; }
    void setTitle(String title) { this.title = title; }
    void setStartDate(String startDate) { this.startDate = startDate; }
    void setEndDate(String endDate) { this.endDate = endDate; }
    void setAlarm(double alarm) { this.alarm = alarm; }
    void setLocation(String location) { this.location = location; }
    void setAttandances(String attandances) { this.attandances = attandances; }
    void setColor(String color) { this.color = color; }
    void setMemo(String memo) { this.memo = memo; }

    int getKey() { return this.key; }
    int getTag() { return this.tag; }
    String getTitle() { return this.title; }
    String getStartDate() { return this.startDate; }
    String getEndDate() { return this.endDate; }
    double getAlarm() { return this.alarm; }
    String getLocation() { return this.location; }
    String getAttandances() { return this.attandances; }
    String getColor() { return this.color; }
    String getMemo() { return this.memo; }

    public interface ScheduleCallback {
        public void getSchedules(ArrayList<Schedule> schedules);
    }

    /*
     *   year/month
     *   addValueEventListner
     */
    public void getMonthSchedules(int year, int month, final ScheduleCallback callback) {
        FirebaseHandler firebaseHandler = new FirebaseHandler();
        firebaseHandler.setFirebase();
        DatabaseReference scheduleTable = firebaseHandler.getScheduleTable();

        final ArrayList<Schedule> schedules = new ArrayList<>();

        scheduleTable.child(String.valueOf(year)).child(String.valueOf(month)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dayData : dataSnapshot.getChildren()) {
                    for(DataSnapshot data : dayData.getChildren()) {
                        Schedule schedule = data.getValue(Schedule.class);
                        schedules.add(schedule);
                    }
                }

                callback.getSchedules(schedules);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /*
     *  year/month/day
     *  addListenerForSingleValueEvent
     */
    public void getDaySchedulesSingle(int year, int month, int day, final ScheduleCallback callback) {
        FirebaseHandler firebaseHandler = new FirebaseHandler();
        firebaseHandler.setFirebase();
        DatabaseReference scheduleTable = firebaseHandler.getScheduleTable();

        final ArrayList<Schedule> schedules = new ArrayList<>();

        scheduleTable.child(String.valueOf(year)).child(String.valueOf(month)).child(String.valueOf(day)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren()) {
                    Schedule schedule = data.getValue(Schedule.class);
                    schedules.add(schedule);
                }

                callback.getSchedules(schedules);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
