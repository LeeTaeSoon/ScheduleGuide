package com.example.nrst1.scheduleguide;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by nrst1 on 2017-05-30.
 */

public class Schedule {
    int tag;
    String title;
    SimpleDateFormat startDate;
    SimpleDateFormat endDate;
    double alarm;
    String location;
    ArrayList<Attandance> attandances;
    String color;
    String memo;

    Schedule() {}
    Schedule(int tag, String title, SimpleDateFormat startDate, SimpleDateFormat endDate, double alarm, String location, ArrayList<Attandance> attandances, String color, String memo) {
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

    void setTag(int tag) { this.tag = tag; }
    void setTitle(String title) { this.title = title; }
    void setStartDate(SimpleDateFormat startDate) { this.startDate = startDate; }
    void setEndDate(SimpleDateFormat endDate) { this.endDate = endDate; }
    void setAlarm(double alarm) { this.alarm = alarm; }
    void setLocation(String location) { this.location = location; }
    void setAttandances(ArrayList<Attandance> attandances) { this.attandances = attandances; }
    void setColor(String color) { this.color = color; }
    void setMemo(String memo) { this.memo = memo; }

    int getTag() { return this.tag; }
    String getTitle() { return this.title; }
    SimpleDateFormat getStartDate() { return this.startDate; }
    SimpleDateFormat getEndDate() { return this.endDate; }
    double getAlarm() { return this.alarm; }
    String getLocation() { return this.location; }
    ArrayList<Attandance> getAttandances() { return this.attandances; }
    String getColor() { return this.color; }
    String getMemo() { return this.memo; }
}
