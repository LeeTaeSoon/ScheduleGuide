package com.example.nrst1.scheduleguide;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class DetailSchedule extends AppCompatActivity {

    int year,month,day,key;

    DrawerLayout drawerLayout;
    FrameLayout sideMenuContainer;

    TextView tag;
    TextView title;
    TextView startDate;
    TextView endDate;
    TextView alarm;
    TextView location;
    ImageView location_search;
    TextView attend;
    ImageView attend_call;
    TextView color;
    TextView memo;

    FirebaseHandler firebasedb;
    DatabaseReference scheduleDatabase;
    DatabaseReference tagDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_schedule);

        initActionBar();
        init();
    }

    public void initActionBar() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        sideMenuContainer = (FrameLayout) findViewById(R.id.side_bar_fragment_container);

        ActionBarHandler actionBarHandler = new ActionBarHandler(this, getSupportActionBar());
        actionBarHandler.setBasicActionBar();
        actionBarHandler.setTitle("일정 추가");
        actionBarHandler.setDrawerMenu(drawerLayout, sideMenuContainer);
    }
    //tag가져오기
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(sideMenuContainer)) drawerLayout.closeDrawer(sideMenuContainer);
        else super.onBackPressed();
    }

    public void init(){
//        Intent intent=getIntent();
//        year=intent.getIntExtra("year",-1);
//        month=intent.getIntExtra("month",-1);
//        day=intent.getIntExtra("day",-1);
//        key=intent.getIntExtra("key",-1);
        year=2017;
        month=6;
        day=1;
        key=0;

        tag=(TextView)findViewById(R.id.add_schedule_tag);
        title=(TextView)findViewById(R.id.add_schedule_title);
        startDate=(TextView)findViewById(R.id.add_schedule_start_date);
        endDate=(TextView)findViewById(R.id.add_schedule_end_date);
        alarm=(TextView)findViewById(R.id.add_schedule_alarm);
        location=(TextView)findViewById(R.id.add_schedule_location);
        location_search=(ImageView) findViewById(R.id.add_schedule_location_search);
        attend=(TextView)findViewById(R.id.add_schedule_attendance);
        attend_call=(ImageView)findViewById(R.id.add_schedule_attendance_call);
        color=(TextView)findViewById(R.id.add_schedule_color);
        memo=(TextView)findViewById(R.id.add_schedule_memo);

        firebasedb=new FirebaseHandler(this);
        scheduleDatabase=firebasedb.getScheduleTable();
        tagDatabase=firebasedb.getTagTable();

        scheduleDatabase.child(String.valueOf(year)).child(String.valueOf(month)).child(String.valueOf(day)).child(String.valueOf(key))
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Schedule schedule=dataSnapshot.getValue(Schedule.class);

                tagDatabase.child(String.valueOf(schedule.getTag())).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Tag tag1=dataSnapshot.getValue(Tag.class);
                        tag.setText(tag1.getName());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                title.setText(schedule.getTitle());
                startDate.setText(schedule.getStartDate());//이거 수정
                endDate.setText(schedule.getEndDate());
                int min=(int)(schedule.getAlarm()*60);
                int hour=min/60;
                min=min%60;
                if(hour!=0) {
                    alarm.setText(String.valueOf(hour + "시간" + min + "분"));//int로 바꿔서 시간분으로
                }
                else{
                    alarm.setText(String.valueOf(min+"분"));
                }
                location.setText(schedule.getLocation());
                attend.setText(schedule.getAttandances());
                color.setBackgroundColor(Color.parseColor(schedule.getColor()));
                memo.setText(schedule.getMemo());
                }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //TODO Form만들기 완료



    }


    public void btnUpdate(View view) {
        Intent intent=new Intent(this,UpdateSchedule.class);
        intent.putExtra("year",year);
        intent.putExtra("month",month);
        intent.putExtra("day",day);
        intent.putExtra("key",key);
        startActivity(intent);
        finish();
    }

    public void btnCancle(View view) {
        finish();
    }
}
