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
    DatabaseReference calanderTable;
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
        calanderTable = database.getReference("Calander");
        tagTable = database.getReference(uuid + "/Tag");
        scheduleTable = database.getReference(uuid + "/Schedule");
    }

    public DatabaseReference getCalanderTable() { return this.calanderTable; }

    public DatabaseReference getTagTable() { return this.tagTable; }
    
    public DatabaseReference getScheduleTable() { return this.scheduleTable; }
}
