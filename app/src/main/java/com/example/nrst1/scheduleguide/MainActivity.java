package com.example.nrst1.scheduleguide;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.Spinner;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    GridView calanderGrid;
    FrameLayout sideMenuContainer;

    Spinner spinner_year;
    Spinner spinner_month;
    int year;
    int month;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        initActionBar();
    }

    public void initActionBar(){
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        sideMenuContainer = (FrameLayout) findViewById(R.id.side_bar_fragment_container);

        ActionBarHandler actionBarHandler = new ActionBarHandler(this, getSupportActionBar());
        actionBarHandler.setBasicActionBar();
        actionBarHandler.setDrawerMenu(drawerLayout, sideMenuContainer);
    }

    public void init(){
        spinner_year=(Spinner)findViewById(R.id.year);
        spinner_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                year=position+2017;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                year=2017;
            }
        });
        spinner_month=(Spinner)findViewById(R.id.month);
        spinner_month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                month=position+1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                month=6;
            }
        });
        ArrayList<Day> days = new ArrayList<>();            //여기서 day계산이 필요하다
        for(int i = 1 ; i < 32; i++) {
            Day day = new Day(2017, 5, i, i % 7);
            days.add(day);
        }

        calanderGrid = (GridView) findViewById(R.id.grid_calander);
        DayAdapter dayAdapter = new DayAdapter(this, days);
        calanderGrid.setAdapter(dayAdapter);

        // TODO : 선택한 날짜에 일정이 있으면 일정 목록 페이지로 이동하도록 분기 처리
        calanderGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //여기서 그리드뷰의 position은 1번째가 0부터 시작한다
                Intent intent = new Intent(getApplication(), AddScheduleActivity.class);
                intent.putExtra("year",year);
                intent.putExtra("month",month);
                intent.putExtra("day",position);        //포지션은 계산하겠지
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(sideMenuContainer)) drawerLayout.closeDrawer(sideMenuContainer);
        else super.onBackPressed();
    }
}
