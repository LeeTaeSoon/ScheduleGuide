package com.example.nrst1.scheduleguide;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by nrst1 on 2017-06-03.
 */

public class ScheduleSimpleAdapter extends BaseAdapter {
    Context context;
    ArrayList<Schedule> schedules;
    int day;
    int scheduleIndex;

    ScheduleSimpleAdapter() {}
    ScheduleSimpleAdapter(Context context, ArrayList<Schedule> schedules, int day) {
        this.context = context;
        this.schedules = schedules;
        this.day = day;

        scheduleIndex = 0;
    }

    @Override
    public int getCount() {
        return schedules.size();
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

        if (day == 0) return v;
        if (day == 1) scheduleIndex = 0;

        final Schedule schedule = schedules.get(position);

        if(schedule != null) {
            try {
                Date date = new SimpleDateFormat("yyyy-mm-dd HH:mm").parse(schedule.getStartDate());
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);

                if (day == calendar.get(Calendar.DAY_OF_MONTH)) {
                    TextView name = (TextView) v.findViewById(R.id.schedule_simple_text);

                    if (name != null) {
                        name.setText(schedule.getTitle());
                        StringBuffer stringBuffer = new StringBuffer(schedule.getColor());
                        String color = stringBuffer.insert(1, "88").toString();
                        name.setBackgroundColor(Color.parseColor(color));
                    }

                    scheduleIndex++;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return v;
    }
}
