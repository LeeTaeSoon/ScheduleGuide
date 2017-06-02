package com.example.nrst1.scheduleguide;

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
}
