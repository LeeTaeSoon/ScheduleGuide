package com.example.nrst1.scheduleguide;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by nrst1 on 2017-05-30.
 */

public class ScheduleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    ArrayList<Schedule> schedules;

    public ScheduleAdapter(Context context, ArrayList<Schedule> objects) {
        this.context = context;
        this.schedules = objects;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View v;

        v = li.inflate(R.layout.schedule_list_row, parent, false);
        return new ShowViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final Schedule schedule = schedules.get(position);

        FirebaseHandler firebaseHandler = new FirebaseHandler(context);
        DatabaseReference scheduleTable = firebaseHandler.getScheduleTable();
        DatabaseReference tagTable = firebaseHandler.getTagTable();

        tagTable.child(String.valueOf(schedule.getTag())).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // TODO : 태그 색깔 적용
                // TODO : 지정된 태그가 삭제 될 경우 어떻게 처리할 지 정의
                Tag tag = dataSnapshot.getValue(Tag.class);
                String tagName = "";
                int tagColor = Color.BLACK;
                if (tag != null) {
                    tagName = tag.getName();
                    StringBuffer stringBuffer = new StringBuffer(tag.getColor());
                    String color = stringBuffer.insert(3, "88").toString();

                    tagColor = Color.parseColor(color);
                }

                if (schedule != null) {
                    ShowViewHolder viewHolder = (ShowViewHolder) holder;

                    if (viewHolder.scheduleTagText != null) {
                        viewHolder.scheduleTagText.setText(tagName);
                        viewHolder.scheduleTagText.setBackgroundColor(tagColor);
                    }

                    if (viewHolder.scheduleTimeText != null) {
                        String time = null;
                        try {
                            Date date = new SimpleDateFormat("yyyy-mm-dd HH:mm").parse(schedule.getStartDate());
                            time = new SimpleDateFormat("a h:mm").format(date);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        viewHolder.scheduleTimeText.setText(time);
                    }

                    if (viewHolder.scheduleTitleText != null) viewHolder.scheduleTitleText.setText(schedule.getTitle());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return schedules.size();
    }

    public class ShowViewHolder extends RecyclerView.ViewHolder {

        TextView scheduleTagText;
        TextView scheduleTimeText;
        TextView scheduleTitleText;

        public ShowViewHolder(View itemView) {
            super(itemView);

            scheduleTagText = (TextView) itemView.findViewById(R.id.schedule_tag_text);
            scheduleTimeText = (TextView) itemView.findViewById(R.id.schedule_time_text);
            scheduleTitleText = (TextView) itemView.findViewById(R.id.schedule_title_text);
        }
    }
}
