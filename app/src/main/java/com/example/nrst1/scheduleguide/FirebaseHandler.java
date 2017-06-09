package com.example.nrst1.scheduleguide;

import android.content.Context;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by nrst1 on 2017-05-28.
 */

public class FirebaseHandler {

    Context context;
    static String uuid;

    FirebaseDatabase database;
    DatabaseReference calendarTable;
    DatabaseReference tagTable;
    DatabaseReference scheduleTable;

    FirebaseHandler()  {}
    FirebaseHandler(Context context) {
        this.context = context;
        setFirebase();
    }

    public void setFirebase() {
        DeviceUuidFactory deviceUuidFactory = new DeviceUuidFactory(context);
        uuid = deviceUuidFactory.getDeviceUuid();

        database = FirebaseDatabase.getInstance();
        calendarTable = database.getReference("Calendar");
        tagTable = database.getReference(uuid + "/Tag");
        scheduleTable = database.getReference(uuid + "/Schedule");
    }

    public DatabaseReference getCalendarTable() { return this.calendarTable; }

    public DatabaseReference getTagTable() { return this.tagTable; }
    
    public DatabaseReference getScheduleTable() { return this.scheduleTable; }
}
