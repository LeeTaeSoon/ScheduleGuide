package com.example.nrst1.scheduleguide;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    GridView calanderGrid;
    FrameLayout sideMenuContainer;

    ImageView preMonth;
    ImageView nextMonth;

    Spinner spinnerYear;
    Spinner spinnerMonth;
    int year;
    int month;

    ArrayList<Schedule> schedules;
    ArrayList<Day> days;
    DayAdapter dayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        initActionBar();
    }

    public void initActionBar() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        sideMenuContainer = (FrameLayout) findViewById(R.id.side_bar_fragment_container);

        ActionBarHandler actionBarHandler = new ActionBarHandler(this, getSupportActionBar());
        actionBarHandler.setBasicActionBar();
        actionBarHandler.setDrawerMenu(drawerLayout, sideMenuContainer);
    }

    public void init() {
        preMonth = (ImageView) findViewById(R.id.pre_month);
        nextMonth = (ImageView) findViewById(R.id.next_month);

        preMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int nowMonth = spinnerMonth.getSelectedItemPosition();
                if (nowMonth == 0) {
                    int nowYear = spinnerYear.getSelectedItemPosition();
                    if (nowYear == 0);
                    else {
                        spinnerYear.setSelection(nowYear - 1);
                        spinnerMonth.setSelection(11);
                    }
                }
                else
                    spinnerMonth.setSelection(nowMonth - 1);
            }
        });

        nextMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int nowMonth = spinnerMonth.getSelectedItemPosition();
                if (nowMonth == 11) {
                    int nowYear = spinnerYear.getSelectedItemPosition();
                    if (nowYear == 1);
                    else {
                        spinnerYear.setSelection(nowYear + 1);
                        spinnerMonth.setSelection(0);
                    }
                }
                else
                    spinnerMonth.setSelection(nowMonth + 1);
            }
        });

        year = 2017;
        month = 6;

        days = new ArrayList<>();
        for (int i = 1; i < 31; i++) {
            Day day = new Day(year, month, i, (i + 3) % 7);
            days.add(day);
        }

        schedules = new ArrayList<>();
        calanderGrid = (GridView) findViewById(R.id.grid_calander);
        dayAdapter = new DayAdapter(this, days, schedules);
        calanderGrid.setAdapter(dayAdapter);

        spinnerYear = (Spinner) findViewById(R.id.year);
        spinnerYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                year = position + 2017;

                new Day().getMonthInfo(year, month, 1, new Day.DayCallback() {
                    @Override
                    public void getMonthInfo(int dayOfTheWeek, int dayNum) {
                        days.clear();
                        for (int i = 1; i < dayNum + 1; i++) {
                            Day day = new Day(year, month, i, (i + dayOfTheWeek - 1) % 7);
                            days.add(day);
                        }

                        getSchedules(dayAdapter);
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                year = 2017;
            }
        });
        spinnerYear.setSelection(0);

        spinnerMonth = (Spinner) findViewById(R.id.month);
        spinnerMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                month = position + 1;

                new Day().getMonthInfo(year, month, 1, new Day.DayCallback() {
                    @Override
                    public void getMonthInfo(int dayOfTheWeek, int dayNum) {
                        days.clear();
                        for (int i = 1; i < dayNum + 1; i++) {
                            Day day = new Day(year, month, i, (i + dayOfTheWeek - 1) % 7);
                            days.add(day);
                        }

                        getSchedules(dayAdapter);
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                month = 6;
            }
        });
        spinnerMonth.setSelection(5);

        getSchedules(dayAdapter);

        calanderGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView dayText = (TextView) view.findViewById(R.id.day);
                if (dayText.getText().toString() == "") return;

                int nowDay = Integer.valueOf(dayText.getText().toString());

                Intent intent = null;
                if (schedules.size() == 0) intent = new Intent(getApplication(), AddScheduleActivity.class);
                else {
                    for (int i = 0; i < schedules.size(); i++) {
                        Calendar calendar = new Day().getDateFromString(schedules.get(i).getStartDate());
                        int day = calendar.get(Calendar.DAY_OF_MONTH);
                        if (nowDay == day) {
                            intent = new Intent(getApplication(), ShowScheduleListActivity.class);
                            break;
                        }
                    }

                    if (intent == null)
                        intent = new Intent(getApplication(), AddScheduleActivity.class);
                }

                intent.putExtra("year", year);
                intent.putExtra("month", month);
                intent.putExtra("day", nowDay);
                intent.putExtra("dayOfTheWeek", position % 7);
                startActivity(intent);
            }
        });
    }

    public void getSchedules(final DayAdapter dayAdapter) {
        FirebaseHandler firebaseHandler = new FirebaseHandler(this);
        DatabaseReference scheduleTable = firebaseHandler.getScheduleTable();

        scheduleTable.child(String.valueOf(year)).child(String.valueOf(month)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                schedules.clear();

                for (DataSnapshot dayData : dataSnapshot.getChildren()) {
                    for (DataSnapshot data : dayData.getChildren()) {
                        Schedule schedule = data.getValue(Schedule.class);
                        schedules.add(schedule);
                    }
                }

                dayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(sideMenuContainer)) drawerLayout.closeDrawer(sideMenuContainer);
        else super.onBackPressed();
    }
}
