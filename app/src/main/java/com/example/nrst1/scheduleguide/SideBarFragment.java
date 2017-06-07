package com.example.nrst1.scheduleguide;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class SideBarFragment extends Fragment {

    EditText searchScheduleEdit;
    ImageView searchScheduleImg;
    TextView addScheduleText;
    TextView searchTodayScheduleText;
    TextView setTagText;


    public SideBarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_side_bar, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        searchScheduleEdit = (EditText) getActivity().findViewById(R.id.side_bar_search_edit);
        searchScheduleImg = (ImageView) getActivity().findViewById(R.id.side_bar_search_img);
        addScheduleText = (TextView) getActivity().findViewById(R.id.side_bar_add_schedule);
        searchTodayScheduleText = (TextView) getActivity().findViewById(R.id.side_bar_search_today_schedule);
        setTagText = (TextView) getActivity().findViewById(R.id.side_bar_set_tag);

        // Search Schedule
        searchScheduleImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String searchQuery = searchScheduleEdit.getText().toString();

                FirebaseHandler firebaseHandler = new FirebaseHandler(getActivity());
                DatabaseReference scheduleTable = firebaseHandler.getScheduleTable();

                scheduleTable.orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Schedule schedule = null;
                        boolean exit = false;

                        for (DataSnapshot yearData : dataSnapshot.getChildren()) {
                            for (DataSnapshot monthData : yearData.getChildren()) {
                                for (DataSnapshot dayData : monthData.getChildren()) {
                                    for (DataSnapshot keyData : dayData.getChildren()) {
                                        Schedule result = keyData.getValue(Schedule.class);
                                        if (result.getTitle().equals(searchQuery)) {
                                            schedule = result;
                                            exit = true;
                                            break;
                                        }
                                    }

                                    if (exit) break;
                                }

                                if (exit) break;
                            }

                            if (exit) break;
                        }

                        if (schedule == null) Toast.makeText(getActivity(), "일정이 없습니다.", Toast.LENGTH_LONG).show();
                        else {
                            Calendar calendar = new Day().getDateFromString(schedule.getStartDate());
                            int year = calendar.get(Calendar.YEAR);
                            int month = calendar.get(Calendar.MONTH) + 1;
                            int day = calendar.get(Calendar.DAY_OF_MONTH);
                            int key = schedule.getKey();

                            Intent intent = new Intent(getActivity(), DetailSchedule.class);
                            intent.putExtra("year", year);
                            intent.putExtra("month", month);
                            intent.putExtra("day", day);
                            intent.putExtra("key", key);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        // Add Schedule
        addScheduleText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH) + 1;
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int dayOfTheWeek = calendar.get(Calendar.DAY_OF_WEEK);

                Intent intent = new Intent(getActivity(), AddScheduleActivity.class);
                intent.putExtra("year", year);
                intent.putExtra("month", month);
                intent.putExtra("day", day);
                intent.putExtra("dayOfTheWeek", dayOfTheWeek);
                startActivity(intent);
            }
        });

        // Today's Schedules
        searchTodayScheduleText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH) + 1;
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                Intent intent = new Intent(getActivity(), ShowScheduleListActivity.class);
                intent.putExtra("year", year);
                intent.putExtra("month", month);
                intent.putExtra("day", day);
                startActivity(intent);
            }
        });

        // Setting Tags
        setTagText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TagListActivity.class);
                startActivity(intent);
            }
        });
    }
}
