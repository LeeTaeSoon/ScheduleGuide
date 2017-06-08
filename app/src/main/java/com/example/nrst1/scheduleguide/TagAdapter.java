package com.example.nrst1.scheduleguide;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

/**
 * Created by nrst1 on 2017-05-27.
 */

public class TagAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    ArrayList<Tag> tags;

    public TagAdapter(Context context, ArrayList<Tag> objects) {
        this.context = context;
        this.tags = objects;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View v;

        if (viewType == 0) {
            v = li.inflate(R.layout.tag_list_row, parent, false);
            return new ShowViewHolder(v);
        } else {
            v = li.inflate(R.layout.tag_edit_row, parent, false);
            return new EditViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final Tag tag = tags.get(position);

        FirebaseHandler firebaseHandler = new FirebaseHandler(context);
        final DatabaseReference tagTable = firebaseHandler.getTagTable();

        if (tag != null) {
            if (tag.getStatus() == Tag.state.SHOW) {

                final ShowViewHolder viewHolder = (ShowViewHolder) holder;

                if (viewHolder.tagColorText != null) viewHolder.tagColorText.setBackgroundColor(Color.parseColor(tag.getColor()));
                if (viewHolder.tagNameText != null) viewHolder.tagNameText.setText(tag.getName());
                if (viewHolder.tagAlarmText != null) {
                    double alarm = tag.getAlarm();
                    int hour = (int) alarm;
                    int minute = (int) ((alarm - hour) * 60);

                    if (hour == 0 && minute == 0) viewHolder.tagAlarmText.setText("알림 없음");
                    else if (hour == 0) viewHolder.tagAlarmText.setText(String.valueOf(minute) + " 분 전");
                    else if (minute == 0) viewHolder.tagAlarmText.setText(String.valueOf(hour) + " 시간 전");
                    else viewHolder.tagAlarmText.setText(String.valueOf(hour) + " 시간 반 전");
                }

                viewHolder.tagModifyImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tag.setStatus(Tag.state.EDIT);
                        notifyDataSetChanged();
                    }
                });

            } else if (tag.getStatus() == Tag.state.EDIT) {

                final EditViewHolder viewHolder = (EditViewHolder) holder;

                final String[] color = {tag.getColor()};
                if (viewHolder.tagColorSpin != null) {
                    viewHolder.tagColorSpin.setBackgroundColor(Color.parseColor(color[0]));
                    if (color[0].equals("#FFFFFF")) viewHolder.tagColorSpin.setSelection(0);
                    else if (color[0].equals("#FF0000")) viewHolder.tagColorSpin.setSelection(1);
                    else if (color[0].equals("#00FF00")) viewHolder.tagColorSpin.setSelection(2);
                    else if (color[0].equals("#0000FF")) viewHolder.tagColorSpin.setSelection(3);
                    else if (color[0].equals("#00FFFF")) viewHolder.tagColorSpin.setSelection(4);
                    else if (color[0].equals("#FFFF00")) viewHolder.tagColorSpin.setSelection(5);
                    else if (color[0].equals("#FF00FF")) viewHolder.tagColorSpin.setSelection(6);

                    viewHolder.tagColorSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if (position == 0) {
                                viewHolder.tagColorSpin.setBackgroundColor(Color.WHITE);
                                color[0] = "#FFFFFF";
                            } else if (position == 1) {
                                viewHolder.tagColorSpin.setBackgroundColor(Color.RED);
                                color[0] = "#FF0000";
                            } else if (position == 2) {
                                viewHolder.tagColorSpin.setBackgroundColor(Color.GREEN);
                                color[0] = "#00FF00";
                            } else if (position == 3) {
                                viewHolder.tagColorSpin.setBackgroundColor(Color.BLUE);
                                color[0] = "#0000FF";
                            } else if (position == 4) {
                                viewHolder.tagColorSpin.setBackgroundColor(Color.CYAN);
                                color[0] = "#00FFFF";
                            } else if (position == 5) {
                                viewHolder.tagColorSpin.setBackgroundColor(Color.YELLOW);
                                color[0] = "#FFFF00";
                            } else if (position == 6) {
                                viewHolder.tagColorSpin.setBackgroundColor(Color.MAGENTA);
                                color[0] = "#FF00FF";
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
                if (viewHolder.tagNameEdit != null) viewHolder.tagNameEdit.setText(tag.getName());
                if (viewHolder.tagAlarmSpin != null) viewHolder.tagAlarmSpin.setSelection((int) (tag.getAlarm() * 2));

                viewHolder.tagOkImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name = viewHolder.tagNameEdit.getText().toString();
                        double alarm = viewHolder.tagAlarmSpin.getSelectedItemPosition() * 0.5;

                        tag.setColor(color[0]);
                        tag.setName(name);
                        tag.setAlarm(alarm);
                        tag.setStatus(Tag.state.SHOW);
                        notifyDataSetChanged();

                        if (tag.getKey() == -1) {
                            int tagNum = tags.size();
                            if (tagNum == 1) {
                                tag.setKey(0);
                                tagTable.child("0").setValue(tag);
                            } else {
                                tag.setKey(tags.get(tagNum - 2).getKey() + 1);
                                tagTable.child(String.valueOf(tag.getKey())).setValue(tag);
                            }
                        } else {
                            tagTable.child(String.valueOf(tag.getKey())).setValue(tag);
                        }
                    }
                });

            }
        }
    }

    @Override
    public int getItemCount() {
        return tags.size();
    }

    @Override
    public int getItemViewType(int position) {
        Tag tag = tags.get(position);

        if (tag == null || tag.getStatus() == Tag.state.SHOW) return 0;
        else return 1;
    }

    public class ShowViewHolder extends RecyclerView.ViewHolder {

        TextView tagColorText;
        TextView tagNameText;
        TextView tagAlarmText;
        ImageView tagModifyImg;

        public ShowViewHolder(View itemView) {
            super(itemView);

            tagColorText = (TextView) itemView.findViewById(R.id.tag_color_text);
            tagNameText = (TextView) itemView.findViewById(R.id.tag_name_text);
            tagAlarmText = (TextView) itemView.findViewById(R.id.tag_alarm_text);
            tagModifyImg = (ImageView) itemView.findViewById(R.id.tag_modification_img);
        }
    }

    public class EditViewHolder extends RecyclerView.ViewHolder {

        Spinner tagColorSpin;
        EditText tagNameEdit;
        Spinner tagAlarmSpin;
        ImageView tagOkImg;

        public EditViewHolder(View itemView) {
            super(itemView);
            tagColorSpin = (Spinner) itemView.findViewById(R.id.tag_color_edit);
            tagNameEdit = (EditText) itemView.findViewById(R.id.tag_name_edit);
            tagAlarmSpin = (Spinner) itemView.findViewById(R.id.tag_alarm_edit);
            tagOkImg = (ImageView) itemView.findViewById(R.id.tag_ok_img);
        }
    }
}
