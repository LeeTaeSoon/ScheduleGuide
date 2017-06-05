package com.example.nrst1.scheduleguide;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by nrst1 on 2017-06-06.
 */

public class ScheduleSimpleAdapter extends RecyclerView.Adapter {
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
        for (int i = 0; i < schedules.size(); i++) {
            Schedule schedule = schedules.get(i);

            Calendar calendar = new Day().getDateFromString(schedule.getStartDate());
            int scheduleMonth = calendar.get(Calendar.MONTH) + 1;
            int scheduleDay = calendar.get(Calendar.DAY_OF_MONTH);

            if (day.getMonth() == scheduleMonth && day.getDay() == scheduleDay) {
                scheduleIndex = i;
                break;
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View v;

        v = li.inflate(R.layout.schedule_simple_list_row, parent, false);
        return new TextViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (day.getDay() == 0) return;

        final Schedule schedule = schedules.get(scheduleIndex);

        if(schedule != null) {
            TextViewHolder viewHolder = (TextViewHolder) holder;

            Calendar calendar = new Day().getDateFromString(schedule.getStartDate());
            int scheduleMonth = calendar.get(Calendar.MONTH) + 1;
            int scheduleDay = calendar.get(Calendar.DAY_OF_MONTH);

            if (day.getMonth() == scheduleMonth && day.getDay() == scheduleDay) {
                if (viewHolder.scheduleText != null) {
                    viewHolder.scheduleText.setText(schedule.getTitle());
                    StringBuffer stringBuffer = new StringBuffer(schedule.getColor());
                    String color = stringBuffer.insert(1, "88").toString();
                    viewHolder.scheduleText.setBackgroundColor(Color.parseColor(color));
                }
            }

            scheduleIndex++;
            scheduleIndex %= schedules.size();
        }
    }

    @Override
    public int getItemCount() {
        if (schedules.size() == 0) return 0;

        int num = 0;

        for (int i = 0; i < schedules.size(); i++) {
            Schedule schedule = schedules.get(i);

            Calendar calendar = new Day().getDateFromString(schedule.getStartDate());
            int scheduleMonth = calendar.get(Calendar.MONTH) + 1;
            int scheduleDay = calendar.get(Calendar.DAY_OF_MONTH);

            if (day.getMonth() == scheduleMonth && day.getDay() == scheduleDay)
                num++;
        }

        return num;
    }

    public class TextViewHolder extends RecyclerView.ViewHolder {

        TextView scheduleText;

        public TextViewHolder(View itemView) {
            super(itemView);

            scheduleText = (TextView) itemView.findViewById(R.id.schedule_simple_text);
        }
    }
}
