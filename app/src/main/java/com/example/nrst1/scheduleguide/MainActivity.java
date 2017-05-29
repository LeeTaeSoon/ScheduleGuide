package com.example.nrst1.scheduleguide;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    GridView calanderGrid;
    FrameLayout sideMenuContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        sideMenuContainer = (FrameLayout) findViewById(R.id.side_bar_fragment_container);

        ActionBarHandler actionBarHandler = new ActionBarHandler(this, getSupportActionBar());
        actionBarHandler.setBasicActionBar();
        actionBarHandler.setDrawerMenu(drawerLayout, sideMenuContainer);

        ArrayList<Day> days = new ArrayList<>();
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
                Intent intent = new Intent(getApplication(), AddScheduleActivity.class);
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
