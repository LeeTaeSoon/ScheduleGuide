package com.example.nrst1.scheduleguide;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class DetailSchedule extends AppCompatActivity {

    private static final int LOCATION_REQUEST_CODE = 1;
    int year, month, day, key;

    DrawerLayout drawerLayout;
    FrameLayout sideMenuContainer;

    TextView tag;
    TextView title;
    TextView startDate;
    TextView startTime;
    TextView endDate;
    TextView endTime;
    TextView alarm;
    TextView location;
    ImageView location_search;
    TextView attend;
    ImageView attend_call;
    TextView color;
    TextView memo;

    String[] DOTW = {"일", "월", "화", "수", "목", "금", "토"};

    FirebaseHandler firebasedb;
    DatabaseReference scheduleDatabase;
    DatabaseReference tagDatabase;

    ArrayList<Contact> contactList;

    LocationManager locationManager;
    double latitude;
    double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_schedule);

        initActionBar();
        makeForm();
        init();
    }

    public void initActionBar() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        sideMenuContainer = (FrameLayout) findViewById(R.id.side_bar_fragment_container);

        ActionBarHandler actionBarHandler = new ActionBarHandler(this, getSupportActionBar());
        actionBarHandler.setBasicActionBar();
        actionBarHandler.setTitle("일정 내용");
        actionBarHandler.setDrawerMenu(drawerLayout, sideMenuContainer);
    }

    //tag가져오기
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(sideMenuContainer)) drawerLayout.closeDrawer(sideMenuContainer);
        else super.onBackPressed();
    }

    public void makeForm() {

        Intent intent = getIntent();
        year = intent.getIntExtra("year", -1);
        month = intent.getIntExtra("month", -1);
        day = intent.getIntExtra("day", -1);
        key = intent.getIntExtra("key", -1);

        tag = (TextView) findViewById(R.id.add_schedule_tag);
        title = (TextView) findViewById(R.id.add_schedule_title);
        startDate = (TextView) findViewById(R.id.add_schedule_start_date);
        startTime = (TextView) findViewById(R.id.add_schedule_start_time);
        endDate = (TextView) findViewById(R.id.add_schedule_end_date);
        endTime = (TextView) findViewById(R.id.add_schedule_end_time);
        alarm = (TextView) findViewById(R.id.add_schedule_alarm);
        location = (TextView) findViewById(R.id.add_schedule_location);
        location_search = (ImageView) findViewById(R.id.add_schedule_location_search);
        attend = (TextView) findViewById(R.id.add_schedule_attendance);
        attend_call = (ImageView) findViewById(R.id.add_schedule_attendance_call);
        color = (TextView) findViewById(R.id.add_schedule_color);
        memo = (TextView) findViewById(R.id.add_schedule_memo);

        firebasedb = new FirebaseHandler(this);
        scheduleDatabase = firebasedb.getScheduleTable();
        tagDatabase = firebasedb.getTagTable();

        scheduleDatabase.child(String.valueOf(year)).child(String.valueOf(month)).child(String.valueOf(day)).child(String.valueOf(key))
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Schedule schedule = dataSnapshot.getValue(Schedule.class);
                        if (schedule == null) finish();
                        else {
                            tagDatabase.child(String.valueOf(schedule.getTag())).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Tag tag1 = dataSnapshot.getValue(Tag.class);
                                    tag.setText(tag1.getName());
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                            title.setText(schedule.getTitle());

                            String ampm;
                            Calendar sCalendar = new Day().getDateFromString(schedule.getStartDate());
                            if (sCalendar.get(Calendar.AM_PM) == Calendar.AM) ampm = "오전";
                            else ampm = "오후";
                            startDate.setText(String.valueOf(sCalendar.get(Calendar.YEAR)) + "년 " + String.valueOf(sCalendar.get(Calendar.MONTH) + 1) + "월 " +
                                    String.valueOf(sCalendar.get(Calendar.DAY_OF_MONTH)) + "일 (" + DOTW[sCalendar.get(Calendar.DAY_OF_WEEK) - 1] + ")");
                            startTime.setText(ampm + " " + String.valueOf(sCalendar.get(Calendar.HOUR)) + ":" + String.valueOf(sCalendar.get(Calendar.MINUTE)));

                            Calendar eCalendar = new Day().getDateFromString(schedule.getEndDate());
                            if (eCalendar.get(Calendar.AM_PM) == Calendar.AM) ampm = "오전";
                            else ampm = "오후";
                            endDate.setText(String.valueOf(eCalendar.get(Calendar.YEAR)) + "년 " + String.valueOf(eCalendar.get(Calendar.MONTH) + 1) + "월 " +
                                    String.valueOf(eCalendar.get(Calendar.DAY_OF_MONTH)) + "일 (" + DOTW[eCalendar.get(Calendar.DAY_OF_WEEK) - 1] + ")");
                            endTime.setText(ampm + " " + String.valueOf(eCalendar.get(Calendar.HOUR)) + ":" + String.valueOf(eCalendar.get(Calendar.MINUTE)));

                            int min = (int) (schedule.getAlarm() * 60);
                            int hour = min / 60;
                            min = min % 60;
                            if (hour == 0 && min == 0) alarm.setText("알림 없음");
                            else if (hour == 0) alarm.setText(String.valueOf(min + " 분 전"));
                            else if (min == 0) alarm.setText(String.valueOf(hour) + " 시간 전");
                            else alarm.setText(String.valueOf(hour) + " 시간 반 전");

                            location.setText(schedule.getLocation());
                            attend.setText(schedule.getAttandances());
                            color.setBackgroundColor(Color.parseColor(schedule.getColor()));
                            memo.setText(schedule.getMemo());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        //TODO Form만들기 완료

    }

    public void init() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        contactList = new ArrayList<>();
        contactList = getContactList();

        location.setOnClickListener(new View.OnClickListener() {//여기에 이제 장소 추천
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),LocationRecommend.class);
                intent.putExtra("location",location.getText().toString());
                startActivity(intent);
            }
        });

        location_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String loc = location.getText().toString();
                //TODO 여기서 길찾기 엑티비티로 인텐트보내면됨

                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();

                        String uri = "http://maps.google.com/maps?saddr=" + String.valueOf(latitude) + "," + String.valueOf(longitude) + "&daddr=" + loc;
                        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addCategory(Intent.CATEGORY_LAUNCHER);
                        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");

                        if (checkPermission())
                            locationManager.removeUpdates(this);

                        startActivity(intent);
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }

                    @Override
                    public void onProviderEnabled(String provider) {

                    }

                    @Override
                    public void onProviderDisabled(String provider) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                        Toast.makeText(getBaseContext(), "Gps is turned off!! ", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        attend_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String a = attend.getText().toString();
                for (int i = 0; i < contactList.size(); i++) {
                    if (contactList.get(i).getName().equals(a)) {
                        String tel = "tel:" + contactList.get(i).getPhonenum();
                        startActivity(new Intent("android.intent.action.CALL", Uri.parse(tel)));
                    }
                }
            }
        });

    }


    public void btnUpdate(View view) {
        Intent intent = new Intent(this, UpdateSchedule.class);
        intent.putExtra("year", year);
        intent.putExtra("month", month);
        intent.putExtra("day", day);
        intent.putExtra("key", key);
        startActivity(intent);
        //finish();
    }

    public void btnCancle(View view) {
        finish();
    }

    private ArrayList<Contact> getContactList() {


        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

        String[] projection = new String[]{
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID, // 연락처 ID -> 사진 정보 가져오는데 사용
                ContactsContract.CommonDataKinds.Phone.NUMBER,        // 연락처
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME}; // 연락처 이름.

        String[] selectionArgs = null;

        String sortOrder = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
                + " COLLATE LOCALIZED ASC";

        Cursor contactCursor = managedQuery(uri, projection, null,
                selectionArgs, sortOrder);

        ArrayList<Contact> contactlist = new ArrayList<Contact>();

        if (contactCursor.moveToFirst()) {
            do {
                String phonenumber = contactCursor.getString(1).replaceAll("-",
                        "");
                if (phonenumber.length() == 10) {
                    phonenumber = phonenumber.substring(0, 3) + "-"
                            + phonenumber.substring(3, 6) + "-"
                            + phonenumber.substring(6);
                } else if (phonenumber.length() > 8) {
                    phonenumber = phonenumber.substring(0, 3) + "-"
                            + phonenumber.substring(3, 7) + "-"
                            + phonenumber.substring(7);
                }

                Contact acontact = new Contact();
                acontact.setPhotoid(contactCursor.getLong(0));
                acontact.setPhonenum(phonenumber);
                acontact.setName(contactCursor.getString(2));

                contactlist.add(acontact);
            } while (contactCursor.moveToNext());
        }

        return contactlist;

    }

    public void attend_call(View view) {

        attend.getText().toString();
        for (int i = 0; i < contactList.size(); i++) {
            if (contactList.get(i).getName().equals(attend)) {
                String tel = "tel:" + contactList.get(i).getPhonenum();
                startActivity(new Intent("android.intent.action.CALL", Uri.parse(tel)));
            }
        }
    }

    public boolean checkPermission() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            return false;
        }
        return true;
    }
}
