package com.example.nrst1.scheduleguide;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddScheduleActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    FrameLayout sideMenuContainer;

    final int START_DATE=1;
    final int START_TIME=2;
    final int END_DATE=3;
    final int END_TIME=4;
    int startYear,startMonth,startDay,startDayOfWeek,endYear,endMonth,endDay,endDayOfWeek;
    String dayoftheweek;
    Spinner tag;
    EditText title;
    Spinner alarm;
    EditText location;
    AutoCompleteTextView attend;
    Spinner color;
    EditText memo;

    TextView startTime;
    TextView startDate;
    TextView endTime;
    TextView endDate;

    double ringring;
    String col;
    ArrayAdapter tagadapter;
    ArrayList<Tag> tagList;
    ArrayList<String> tagNameList;
    int selectTag;

    ArrayList<Contact> contactList;
    ArrayList<String> callList;

    ArrayList<Tag> tags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule);

        initActionBar();
        init();
    }

    public void initActionBar() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        sideMenuContainer = (FrameLayout) findViewById(R.id.side_bar_fragment_container);

        ActionBarHandler actionBarHandler = new ActionBarHandler(this, getSupportActionBar());
        actionBarHandler.setBasicActionBar();
        actionBarHandler.setTitle("일정 추가");
        actionBarHandler.setDrawerMenu(drawerLayout, sideMenuContainer);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(sideMenuContainer)) drawerLayout.closeDrawer(sideMenuContainer);
        else super.onBackPressed();
    }

    public void init(){
        Intent intent=getIntent();
        startYear=intent.getIntExtra("year",-1);
        endYear=startYear;
        startMonth=intent.getIntExtra("month",-1);
        endMonth=startMonth;
        startDay=intent.getIntExtra("day",-1);
        endDay=startDay;
        startDayOfWeek=intent.getIntExtra("dayOfTheWeek",-1);
        endDayOfWeek=startDayOfWeek;
        switch (startDayOfWeek){
            case 0:
                dayoftheweek="일";
                break;
            case 1:
                dayoftheweek="월";
                break;
            case 2:
                dayoftheweek="화";
                break;
            case 3:
                dayoftheweek="수";
                break;
            case 4:
                dayoftheweek="목";
                break;
            case 5:
                dayoftheweek="금";
                break;
            case 6:
                dayoftheweek="토";
                break;
        }
        //intent를 이용해 시작 페이지에서 년 월 일을 가져왔다
        //이제 db에서 이 사람의 이 날짜의 일정과 태그를 가져온다
        tag=(Spinner)findViewById(R.id.add_schedule_tag);
        title=(EditText)findViewById(R.id.add_schedule_title);
        alarm=(Spinner)findViewById(R.id.add_schedule_alarm);
        location=(EditText)findViewById(R.id.add_schedule_location);
        attend=(AutoCompleteTextView) findViewById(R.id.add_schedule_attendance);
        color=(Spinner)findViewById(R.id.add_schedule_color);
        memo=(EditText)findViewById(R.id.add_schedule_memo);

        startDate=(TextView)findViewById(R.id.add_schedule_start_date);
        startTime=(TextView)findViewById(R.id.add_schedule_start_time);
        endDate=(TextView)findViewById(R.id.add_schedule_end_date);
        endTime=(TextView)findViewById(R.id.add_schedule_end_time);


        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(START_DATE);
            }
        });
        startDate.setText(String.valueOf(startYear) + "-" + String.valueOf(startMonth) + "-" + String.valueOf(startDay));
        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(START_TIME);
            }
        });
        startTime.setText("9:0");
        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(END_DATE);
            }
        });
        endDate.setText(String.valueOf(endYear) + "-" + String.valueOf(endMonth) + "-" + String.valueOf(endDay));
        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(END_TIME);
            }
        });
        endTime.setText("9:0");

        alarm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ringring = position * 0.5;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //알람 스피너 설정

        FirebaseHandler firebaseHandler=new FirebaseHandler(this);
        DatabaseReference tagdatabase=firebaseHandler.getTagTable();

        tagNameList=new ArrayList<String>();
        tagList=new ArrayList<Tag>();
        tags = new ArrayList<>();
        tagdatabase.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    Tag t=data.getValue(Tag.class);
                    tagList.add(t);
                    tags.add(t);
                    tagNameList.add(t.getName());
                }
                tagadapter=new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,tagNameList);
                tag.setAdapter(tagadapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        tag.setSelection(0);
        tag.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectTag=tagList.get(position).getKey();

                color.setSelection(0);
                col = tags.get(position).getColor();
                color.setBackgroundColor(Color.parseColor(col));

                double tagAlarm = tags.get(position).getAlarm();
                alarm.setSelection((int) (tagAlarm / 0.5));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectTag=-1;
            }
        });

        //태그 설정

        contactList=new ArrayList<>();
        callList=new ArrayList<>();
        contactList=getContactList();

        for(int i=0;i<contactList.size();i++) {
            callList.add(contactList.get(i).getName());
        }
        //연락처의 이름들 모두 callList로 옮김
//
        ArrayAdapter adapter=new ArrayAdapter(this,android.R.layout.simple_dropdown_item_1line,callList);
        attend.setAdapter(adapter);
        //attend에 callList를 어댑터로 붙여서 Autocomplete만듬
        //참석자

        color.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    col = "#FFFFFF";
                }
                else if(position==1){
                    color.setBackgroundColor(Color.RED);
                    col = "#FF0000";
                }
                else if(position==2){
                    color.setBackgroundColor(Color.GREEN);
                    col = "#00FF00";
                }
                else if(position==3){
                    color.setBackgroundColor(Color.BLUE);
                    col = "#0000FF";
                }
                else if(position==4){
                    color.setBackgroundColor(Color.CYAN);
                    col = "#00FFFF";
                }
                else if(position==5){
                    color.setBackgroundColor(Color.YELLOW);
                    col = "#FFFF00";
                }
                else if(position==6){
                    color.setBackgroundColor(Color.MAGENTA);
                    col = "#FF00FF";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }

    public void btnCancle(View view) {
        finish();
    }

    public void btnAdd(View view) {
        String Title=title.getText().toString();
        String Location=location.getText().toString();
        String Attend=attend.getText().toString();
        String Memo=memo.getText().toString();

        final String startday = startDate.getText().toString() + " " + startTime.getText().toString();
                /*+" "+"01:30"*/;
        String endday=endDate.getText().toString()+" "+endTime.getText().toString();//default로 날짜는 오늘날짜


        final Schedule schedule = new Schedule(selectTag, Title, startday, endday, ringring, Location, Attend, col, Memo);

        //TODO 여기다가 번호추가
        FirebaseHandler database = new FirebaseHandler(this);
        final DatabaseReference scheduleTable = database.getScheduleTable();

        new Schedule().getDaySchedulesSingle(startYear, startMonth, startDay, new Schedule.ScheduleCallback() {
            @Override
            public void getSchedules(ArrayList<Schedule> schedules) {
                int scheduleNum = schedules.size();
                if (scheduleNum == 0) {
                    schedule.setKey(0);
                    scheduleTable.child(String.valueOf(startYear)).child(String.valueOf(startMonth)).child(String.valueOf(startDay)).child("0").setValue(schedule);
                } else {
                    schedule.setKey(schedules.get(scheduleNum - 1).getKey() + 1);
                    scheduleTable.child(String.valueOf(startYear)).child(String.valueOf(startMonth)).child(String.valueOf(startDay)).child(String.valueOf(schedule.getKey())).setValue(schedule);
                }

                finish();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private ArrayList<Contact> getContactList() {


        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

        String[] projection = new String[] {
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID, // 연락처 ID -> 사진 정보 가져오는데 사용
                ContactsContract.CommonDataKinds.Phone.NUMBER,        // 연락처
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME }; // 연락처 이름.

        String[] selectionArgs = null;

        String sortOrder = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
                + " COLLATE LOCALIZED ASC";

        Cursor contactCursor = managedQuery(uri, projection, null,
                selectionArgs, sortOrder);

        ArrayList<Contact> contactlist = new ArrayList<Contact>();

        if (contactCursor.moveToFirst()) {
            do {
                String phonenumber = contactCursor.getString(1).replaceAll("-",
                        "");
                if (phonenumber.length() == 10) {
                    phonenumber = phonenumber.substring(0, 3) + "-"
                            + phonenumber.substring(3, 6) + "-"
                            + phonenumber.substring(6);
                } else if (phonenumber.length() > 8) {
                    phonenumber = phonenumber.substring(0, 3) + "-"
                            + phonenumber.substring(3, 7) + "-"
                            + phonenumber.substring(7);
                }

                Contact acontact = new Contact();
                acontact.setPhotoid(contactCursor.getLong(0));
                acontact.setPhonenum(phonenumber);
                acontact.setName(contactCursor.getString(2));

                contactlist.add(acontact);
            } while (contactCursor.moveToNext());
        }

        return contactlist;

    }
    @Override
    @Deprecated
    protected Dialog onCreateDialog(int id) {           //날짜시간 다이얼로그만들기
        switch(id){
            case START_DATE :
                DatePickerDialog sdpd = new DatePickerDialog
                        (AddScheduleActivity.this, // 현재화면의 제어권자
                                new DatePickerDialog.OnDateSetListener() {
                                    public void onDateSet(DatePicker view,
                                                          int year, int monthOfYear,int dayOfMonth) {
                                        monthOfYear+=1;
                                        startYear=year;
                                        startMonth=monthOfYear;
                                        startDay=dayOfMonth;
                                        startDate.setText(year+"-"+monthOfYear+"-"+dayOfMonth);
                                    }
                                }
                                , // 사용자가 날짜설정 후 다이얼로그 빠져나올때
                                //    호출할 리스너 등록
                                2017, 05, 01); // 기본값 연월일
                return sdpd;
            case START_TIME :
                TimePickerDialog stpd =
                        new TimePickerDialog(AddScheduleActivity.this,
                                new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker view,
                                                          int hourOfDay, int minute) {
                                        startTime.setText(hourOfDay+":"+minute);
                                    }
                                }, // 값설정시 호출될 리스너 등록
                                9,00, false); // 기본값 시분 등록
                // true : 24 시간(0~23) 표시
                // false : 오전/오후 항목이 생김
                return stpd;
            case END_DATE :
                DatePickerDialog edpd = new DatePickerDialog
                        (AddScheduleActivity.this, // 현재화면의 제어권자
                                new DatePickerDialog.OnDateSetListener() {
                                    public void onDateSet(DatePicker view,
                                                          int year, int monthOfYear,int dayOfMonth) {
                                        monthOfYear+=1;
                                        endYear=year;
                                        endMonth=monthOfYear;
                                        endDay=dayOfMonth;
                                        endDate.setText(year+"-"+monthOfYear+"-"+dayOfMonth);
                                    }
                                }
                                , // 사용자가 날짜설정 후 다이얼로그 빠져나올때
                                //    호출할 리스너 등록
                                2017, 05, 01); // 기본값 연월일
                return edpd;
            case END_TIME :
                TimePickerDialog etpd =
                        new TimePickerDialog(AddScheduleActivity.this,
                                new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker view,
                                                          int hourOfDay, int minute) {
                                        endTime.setText(hourOfDay+":"+minute);
                                    }
                                }, // 값설정시 호출될 리스너 등록
                                9,00, false); // 기본값 시분 등록
                // true : 24 시간(0~23) 표시
                // false : 오전/오후 항목이 생김
                return etpd;
        }


        return super.onCreateDialog(id);
    }

}
