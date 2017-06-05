package com.example.nrst1.scheduleguide;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by nrst1 on 2017-05-25.
 */

public class DayAdapter extends BaseAdapter {
    Context context;
    ArrayList<Day> days;
    ArrayList<Schedule> schedules;

    int dayIndex;

    public DayAdapter(Context context, ArrayList<Day> days, ArrayList<Schedule> schedules) {
        this.context = context;
        this.days = days;
        this.schedules = schedules;

        dayIndex = 0;
    }

    @Override
    public int getCount() {
        return days.size() + days.get(0).getDayOfTheWeek();
    }

    @Override
    public Object getItem(int position) {
        return days.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if(v == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.day, null);
        }

        if(position == 0) dayIndex = 0;

        final Day day = days.get(dayIndex);

        if(day != null) {
            TextView dayView = (TextView) v.findViewById(R.id.day);
            RecyclerView scheduleRecycler = (RecyclerView) v.findViewById(R.id.schedule_simple_recycler);

            if(dayView != null) {
                if (day.getDayOfTheWeek() == position % 7) {
                    dayView.setText(String.valueOf(day.getDay()));
                    dayIndex++;
                } else dayView.setText("");
            }

            if (scheduleRecycler != null) {
                if (dayIndex > 0) {
                    RecyclerView.Adapter scheduleAdapter = new ScheduleSimpleAdapter(context, schedules, days.get(dayIndex - 1));
                    RecyclerView.LayoutManager layoutManager;

                    layoutManager = new LinearLayoutManager(context);
                    scheduleRecycler.setLayoutManager(layoutManager);
                    scheduleRecycler.setAdapter(scheduleAdapter);
                } else {
                    scheduleRecycler.removeAllViews();
                }
            }
        }

        // TODO : 메인 화면의 크기가 작아지는 경우 달력의 크기도 작아짐 (ex. 키보드 )
        int height = parent.getHeight() / 6;
        v.setLayoutParams(new GridView.LayoutParams(GridView.AUTO_FIT, height));

        return v;
    }
}
