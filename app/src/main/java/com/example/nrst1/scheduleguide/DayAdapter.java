package com.example.nrst1.scheduleguide;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListView;
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
        return days.size();
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
            ListView scheduleList = (ListView) v.findViewById(R.id.list_schedule);

            if(dayView != null) {
                if (day.getDayOfTheWeek() == position % 7) {
                    dayView.setText(String.valueOf(day.getDay()));
                    dayIndex++;
                }
            }

            if(scheduleList != null) {
                ScheduleSimpleAdapter adapter = new ScheduleSimpleAdapter(context, schedules, dayIndex);
                scheduleList.setAdapter(adapter);
            }
        }

        // TODO : 메인 화면의 크기가 작아지는 경우 달력의 크기도 작아짐 (ex. 키보드 )
        int height = parent.getHeight() / 6;
        v.setLayoutParams(new GridView.LayoutParams(GridView.AUTO_FIT, height));

        return v;
    }
}
