package com.example.nrst1.scheduleguide;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by AhnJeongHyeon on 2017. 6. 9..
 */

public class KangNamAdapter extends ArrayAdapter {

    Context context;
    ArrayList<KangNam> list;
    public KangNamAdapter(Context context, int resource, ArrayList objects) {
        super(context, resource, objects);
        this.context=context;
        this.list=objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v=convertView;
        if(v==null){
            LayoutInflater vi=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v=vi.inflate(R.layout.row,null);
        }
        KangNam kangNam=list.get(position);
        if(kangNam!=null){
            TextView tv=(TextView)v.findViewById(R.id.kangnam_name);
            TextView tv2=(TextView)v.findViewById(R.id.kangnam_address);

            tv.setText(kangNam.getName()+" ");
            tv2.setText(kangNam.getAddress());
        }
        return v;
    }
}
