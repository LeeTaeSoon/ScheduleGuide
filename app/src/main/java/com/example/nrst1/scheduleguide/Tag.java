package com.example.nrst1.scheduleguide;

/**
 * Created by nrst1 on 2017-05-26.
 */

public class Tag {
    int key;
    String name;
    String color;
    double alarm;
    state status;         // 0 : show, 1 : edit, 2 : delete

    enum state { SHOW, EDIT, DELETE }

    Tag() {}
    Tag(String name, String color, double alarm) { this.key = -1; this.name = name; this.color = color; this.alarm = alarm; this.status = state.EDIT; }

    void setKey(int key) { this.key = key; }
    void setName(String name) { this.name = name; }
    void setColor(String color) { this.color = color; }
    void setAlarm(double alarm) { this.alarm = alarm; }
    void setStatus(state status) { this.status = status; }

    int getKey() { return this.key; }
    String getName() { return this.name; }
    String getColor() { return this.color; }
    double getAlarm() { return this.alarm; }
    state getStatus() { return this.status; }
}
