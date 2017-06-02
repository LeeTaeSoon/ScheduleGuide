package com.example.nrst1.scheduleguide;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddScheduleActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    FrameLayout sideMenuContainer;

    int year,month,day,dayOfTheWeek;
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
//tag가져오기
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(sideMenuContainer)) drawerLayout.closeDrawer(sideMenuContainer);
        else super.onBackPressed();
    }

    public void init(){
        Intent intent=getIntent();
        year=intent.getIntExtra("year",-1);
        month=intent.getIntExtra("month",-1);
        day=intent.getIntExtra("day",-1);
        dayOfTheWeek=intent.getIntExtra("dayOfTheWeek",-1);
        switch (dayOfTheWeek){
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



        alarm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ringring=(position+1)*30;//분
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
        tagdatabase.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    Tag t=data.getValue(Tag.class);
                    tagList.add(t);
                    tagNameList.add(t.getName());
                }
                tagadapter=new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,tagNameList);
                tag.setAdapter(tagadapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        tag.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectTag=tagList.get(position).getKey();

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
                    col=String.valueOf(-1);
                }
                else if(position==1){
                    view.setBackgroundColor(Color.RED);
                    col=String.valueOf(Color.RED);
                }
                else if(position==2){
                    view.setBackgroundColor(Color.GREEN);
                    col=String.valueOf(Color.GREEN);
                }
                else if(position==3){
                    view.setBackgroundColor(Color.BLUE);
                    col=String.valueOf(Color.BLUE);
                }
                else if(position==4){
                    view.setBackgroundColor(Color.CYAN);
                    col=String.valueOf(Color.CYAN);
                }
                else if(position==5){
                    view.setBackgroundColor(Color.YELLOW);
                    col=String.valueOf(Color.YELLOW);
                }
                else if(position==6){
                    view.setBackgroundColor(Color.MAGENTA);
                    col=String.valueOf(Color.MAGENTA);
                }
                else{
                    view.setBackgroundColor(Color.BLACK);
                    col=String.valueOf(Color.BLACK);
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

        String startday=year+"-"+month+"-"+day/*+" "+"01:30"*/;
        String endday=year+"-"+month+"-"+day;//default로 날짜는 오늘날짜


        Schedule schedule=new Schedule(selectTag,Title,startday,endday,ringring,Location,Attend,col,Memo);

        //이거 이제 디비에 넣으면 됨

        FirebaseHandler database=new FirebaseHandler(this);
        DatabaseReference rdatabase=database.getScheduleTable();
        rdatabase.child(startday).setValue(schedule);
        finish();

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

}
