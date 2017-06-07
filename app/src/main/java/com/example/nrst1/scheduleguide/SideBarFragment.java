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
                String searchQuery = searchScheduleEdit.getText().toString();
                // TODO : 일정 검색
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
                // TODO : 오늘 일정 목록 화면
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
