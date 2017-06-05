package com.example.nrst1.scheduleguide;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by nrst1 on 2017-06-03.
 */

public class ScheduleSimpleAdapter extends BaseAdapter {
    Context context;
    ArrayList<Schedule> schedules;
    Day day;
    int scheduleIndex;

    ScheduleSimpleAdapter() {}
    ScheduleSimpleAdapter(Context context, ArrayList<Schedule> schedules, Day day) {
        this.context = context;
        this.schedules = schedules;
        this.day = day;

        scheduleIndex = 0;
    }

    @Override
    public int getCount() {
        if (schedules.size() == 0) return 0;

        int num = 0;

        for (int i =0 ; i < schedules.size(); i++) {
            Schedule schedule = schedules.get(i);

            Calendar calendar = new Day().getDateFromString(schedule.getStartDate());
            int scheduleMonth = calendar.get(Calendar.MONTH) + 1;
            int scheduleDay = calendar.get(Calendar.DAY_OF_MONTH);

            if (day.getMonth() == scheduleMonth && day.getDay() == scheduleDay)
                num++;
        }

         return num;
    }

    @Override
    public Object getItem(int position) {
        return schedules.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if(v == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.schedule_simple_list_row, null);
        }

        if (day.getDay() == 0) return v;
        if (day.getDay() == 1) scheduleIndex = 0;

        final Schedule schedule = schedules.get(scheduleIndex);

        if(schedule != null) {
            Calendar calendar = new Day().getDateFromString(schedule.getStartDate());
            int scheduleMonth = calendar.get(Calendar.MONTH) + 1;
            int scheduleDay = calendar.get(Calendar.DAY_OF_MONTH);

            if (day.getMonth() == scheduleMonth && day.getDay() == scheduleDay) {
                TextView name = (TextView) v.findViewById(R.id.schedule_simple_text);

                if (name != null) {
                    name.setText(schedule.getTitle());
                    StringBuffer stringBuffer = new StringBuffer(schedule.getColor());
                    String color = stringBuffer.insert(1, "88").toString();
                    name.setBackgroundColor(Color.parseColor(color));
                }

                scheduleIndex++;
                scheduleIndex %= schedules.size();
            }
        }

        return v;
    }
}
