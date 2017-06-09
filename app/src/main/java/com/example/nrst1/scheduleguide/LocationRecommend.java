package com.example.nrst1.scheduleguide;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Scanner;

public class LocationRecommend extends AppCompatActivity {


    String name;
    String address;

    ArrayList<KangNam> arrayList;
    ArrayAdapter<KangNam> adapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_recommend);

        init();

    }

    public void init() {
        listView=(ListView)findViewById(R.id.listView);
        arrayList=new ArrayList<>();
        Intent intent = getIntent();
        String location = intent.getStringExtra("location");

        Scanner scanner=new Scanner(getResources().openRawResource(R.raw.kangnam));
        String query="";
        while(scanner.hasNextLine()){
            query=scanner.nextLine();
            try {
                JSONObject json=new JSONObject(query);
                name=json.getString("UPSO_NM");
                address=json.getString("ADMDNG_NM");

                KangNam kangNam=new KangNam(name,address);
                arrayList.add(kangNam);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        adapter=new KangNamAdapter(this,R.layout.row,arrayList);
        listView.setAdapter(adapter);

    }


}
