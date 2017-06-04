package com.example.nrst1.scheduleguide;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ShowScheduleListActivity extends AppCompatActivity {

    int year;
    int month;
    int day;

    DrawerLayout drawerLayout;
    FrameLayout sideMenuContainer;
    ImageView addScheduleImg;

    FirebaseHandler firebaseHandler;
    DatabaseReference scheduleTable;

    ArrayList<Schedule> schedules;

    RecyclerView scheduleRecycle;
    RecyclerView.Adapter scheduleAdapter;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_schedule_list);

        initActionBar();

        Intent intent = getIntent();
        year = intent.getIntExtra("year", -1);
        month = intent.getIntExtra("month", -1);
        day = intent.getIntExtra("day", -1);

        schedules = new ArrayList<>();

        firebaseHandler = new FirebaseHandler(getApplicationContext());
        scheduleTable = firebaseHandler.getScheduleTable();

        addScheduleImg = (ImageView) findViewById(R.id.add_schedule_img);
        addScheduleImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO : 현재 날짜 정보 보내기
                Intent intent = new Intent(getApplication(), AddScheduleActivity.class);
                startActivity(intent);
            }
        });

        // TODO : 일정 클릭 시 일정 세부 정보 화면으로 전환
        scheduleRecycle = (RecyclerView) findViewById(R.id.schedule_list_recycler);
        layoutManager = new LinearLayoutManager(this);
        scheduleRecycle.setLayoutManager(layoutManager);

        scheduleAdapter = new ScheduleAdapter(this, schedules);
        scheduleRecycle.setAdapter(scheduleAdapter);
        setScheduleTable();

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();

                // TODO : 삭제 테스트 필요 ( 데이터 만들기 힘들어서 테스트 안해봄 )
                scheduleTable.child(String.valueOf(year)).child(String.valueOf(month)).child(String.valueOf(day)).child(String.valueOf(schedules.get(position).getKey())).removeValue();
                schedules.remove(position);
                scheduleAdapter.notifyItemRemoved(position);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(scheduleRecycle);

    }

    public void initActionBar() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        sideMenuContainer = (FrameLayout) findViewById(R.id.side_bar_fragment_container);

        ActionBarHandler actionBarHandler = new ActionBarHandler(this, getSupportActionBar());
        actionBarHandler.setBasicActionBar();

        // TODO : 타이틀을 해당 날짜를 전달받아 보여줄 것
        actionBarHandler.setTitle("일정 목록");
        actionBarHandler.setDrawerMenu(drawerLayout, sideMenuContainer);
    }

    public void setScheduleTable() {
        scheduleTable.child(String.valueOf(year)).child(String.valueOf(month)).child(String.valueOf(day)).orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                schedules.clear();

                for(DataSnapshot data : dataSnapshot.getChildren()) {
                    Schedule schedule = data.getValue(Schedule.class);
                    schedules.add(schedule);
                }

                scheduleAdapter.notifyDataSetChanged();
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
