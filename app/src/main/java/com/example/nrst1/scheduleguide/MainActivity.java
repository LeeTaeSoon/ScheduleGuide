package com.example.nrst1.scheduleguide;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    GridView calanderGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBarHandler actionBarHandler = new ActionBarHandler(this, getSupportActionBar());
        actionBarHandler.setBasicActionBar();

        ArrayList<Day> days = new ArrayList<>();
        for(int i = 1 ; i < 32; i++) {
            Day day = new Day(2017, 5, i, i % 7);
            days.add(day);
        }

        calanderGrid = (GridView) findViewById(R.id.grid_calander);
        DayAdapter dayAdapter = new DayAdapter(this, days);
        calanderGrid.setAdapter(dayAdapter);
    }
}
