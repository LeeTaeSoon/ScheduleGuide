package com.example.nrst1.scheduleguide;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class AddScheduleActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    FrameLayout sideMenuContainer;

    int year,month,day;
    Spinner tag;
    EditText title;
    Spinner alarm;
    EditText location;
    EditText attend;
    EditText memo;

    TextView startTime;
    TextView startDate;
    TextView endTime;
    TextView endDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule);

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
        Intent intent=getIntent();
        year=intent.getIntExtra("year",-1);
        month=intent.getIntExtra("month",-1);
        day=intent.getIntExtra("day",-1);
        //intent를 이용해 시작 페이지에서 년 월 일을 가져왔다
        //이제 db에서 이 사람의 이 날짜의 일정과 태그를 가져온다
        tag=(Spinner)findViewById(R.id.add_schedule_tag);
        title=(EditText)findViewById(R.id.add_schedule_title);
        alarm=(Spinner)findViewById(R.id.add_schedule_alarm);
        location=(EditText)findViewById(R.id.add_schedule_location);
        attend=(EditText)findViewById(R.id.add_schedule_attendance);
        memo=(EditText)findViewById(R.id.add_schedule_memo);

        startDate=(TextView)findViewById(R.id.add_schedule_start_date);
        startTime=(TextView)findViewById(R.id.add_schedule_start_time);
        endDate=(TextView)findViewById(R.id.add_schedule_end_date);
        endTime=(TextView)findViewById(R.id.add_schedule_end_time);



    }
}
