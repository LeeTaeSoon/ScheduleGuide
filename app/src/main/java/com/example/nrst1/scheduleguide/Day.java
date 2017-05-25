package com.example.nrst1.scheduleguide;

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
}
