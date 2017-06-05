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

public class UpdateSchedule extends AppCompatActivity {

    DrawerLayout drawerLayout;
    FrameLayout sideMenuContainer;

    final int START_DATE=1;
    final int START_TIME=2;
    final int END_DATE=3;
    final int END_TIME=4;
    int startYear,startMonth,startDay,startDayOfWeek,endYear,endMonth,endDay,key;
    Spinner tagSpinner;
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
    ArrayList<com.example.nrst1.scheduleguide.Tag> tagList;
    ArrayList<String> tagNameList;
    int selectTag;

    ArrayList<Contact> contactList;
    ArrayList<String> callList;

    FirebaseHandler firebasedb;
    DatabaseReference scheduleDatabase;
    DatabaseReference tagDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_schedule);
        makeForm();
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
    //tag가져오기
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(sideMenuContainer)) drawerLayout.closeDrawer(sideMenuContainer);
        else super.onBackPressed();
    }

    public void makeForm(){
        Intent intent=getIntent();
        startYear=intent.getIntExtra("year",-1);
        endYear=startYear;
        startMonth=intent.getIntExtra("month",-1);
        endMonth=startMonth;
        startDay=intent.getIntExtra("day",-1);
        endDay=startDay;
        key=intent.getIntExtra("key",key);
        //intent를 이용해 시작 페이지에서 년 월 일을 가져왔다
        //이제 db에서 이 사람의 이 날짜의 일정과 태그를 가져온다
        tagSpinner=(Spinner)findViewById(R.id.add_schedule_tag);
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

        firebasedb=new FirebaseHandler(this);
        scheduleDatabase=firebasedb.getScheduleTable();
        tagDatabase=firebasedb.getTagTable();

        scheduleDatabase.child(String.valueOf(startYear)).child(String.valueOf(startMonth)).child(String.valueOf(startDay)).child(String.valueOf(key))
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final Schedule schedule=dataSnapshot.getValue(Schedule.class);

                        tagDatabase.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                int keyNum=0;
                                for(DataSnapshot data:dataSnapshot.getChildren()){
                                    com.example.nrst1.scheduleguide.Tag tag1=data.getValue(com.example.nrst1.scheduleguide.Tag.class);
                                    if(tag1.getKey()==schedule.getTag()){
                                        selectTag=keyNum;
                                        tagSpinner.setSelection(selectTag);
                                        break;
                                    }
                                    keyNum++;
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        title.setText(schedule.getTitle());
                        String startDateTime=schedule.getStartDate();
                        String startDatetime[]=startDateTime.split(" ");
                        startTime.setText(startDatetime[1]);
                        startDate.setText(startDatetime[0]);
                        String endDateTime=schedule.getEndDate();
                        String endDatetime[]=endDateTime.split(" ");
                        endDate.setText(endDatetime[0]);
                        endTime.setText(endDatetime[1]);
                        ringring=(int)(schedule.getAlarm()*2);
                        alarm.setSelection((int) ringring);

                        location.setText(schedule.getLocation());
                        attend.setText(schedule.getAttandances());
                        col=schedule.getColor();
                        color.setBackgroundColor(Color.parseColor(schedule.getColor()));
                        color.setSelection(1);
                        memo.setText(schedule.getMemo());
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        //TODO Form만들기
    }
    public void init(){


        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(START_DATE);
            }
        });
        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(START_TIME);
            }
        });
        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(END_DATE);
            }
        });
        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(END_TIME);
            }
        });

        alarm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ringring=(position+1)*0.5;//분
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                alarm.setSelection((int) ringring);
            }
        });
        //알람 스피너 설정

        FirebaseHandler firebaseHandler=new FirebaseHandler(this);
        DatabaseReference tagdatabase=firebaseHandler.getTagTable();

        tagNameList=new ArrayList<String>();
        tagList=new ArrayList<>();
        tagdatabase.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren()){

                    com.example.nrst1.scheduleguide.Tag t=data.getValue(com.example.nrst1.scheduleguide.Tag.class);

                    tagList.add(t);
                    tagNameList.add(t.getName());

                }
                tagadapter=new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,tagNameList);
                tagSpinner.setAdapter(tagadapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        tagSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectTag=tagList.get(position).getKey();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                tagSpinner.setSelection(selectTag);
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
                    col=String.valueOf(-1);
                }
                else if(position==1){
                    view.setBackgroundColor(Color.RED);
                    col = "#FF0000";
                }
                else if(position==2){
                    view.setBackgroundColor(Color.GREEN);
                    col = "#00FF00";
                }
                else if(position==3){
                    view.setBackgroundColor(Color.BLUE);
                    col = "#0000FF";
                }
                else if(position==4){
                    view.setBackgroundColor(Color.CYAN);
                    col = "#00FFFF";
                }
                else if(position==5){
                    view.setBackgroundColor(Color.YELLOW);
                    col = "#FFFF00";
                }
                else if(position==6){
                    view.setBackgroundColor(Color.MAGENTA);
                    col = "#FF00FF";
                }
                else{
                    view.setBackgroundColor(Color.BLACK);
                    col = "#000000";
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

        //이거 이제 디비에 넣으면 됨

        FirebaseHandler database=new FirebaseHandler(this);
        DatabaseReference rdatabase=database.getScheduleTable();
        //TODO 여기다가 번호추가

        final DatabaseReference scheduleTable = database.getScheduleTable();

        new Schedule().getDaySchedulesSingle(startYear, startMonth, startDay, new Schedule.ScheduleCallback() {
            @Override
            public void getSchedules(ArrayList<Schedule> schedules) {

                schedule.setKey(key);
                scheduleTable.child(String.valueOf(startYear)).child(String.valueOf(startMonth)).child(String.valueOf(startDay)).child(String.valueOf(key)).setValue(schedule);


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
                        (UpdateSchedule.this, // 현재화면의 제어권자
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
                        new TimePickerDialog(UpdateSchedule.this,
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
                        (UpdateSchedule.this, // 현재화면의 제어권자
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
                        new TimePickerDialog(UpdateSchedule.this,
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
