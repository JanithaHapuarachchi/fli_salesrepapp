package com.fli.salesagentapp.fliagentapp.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.fli.salesagentapp.fliagentapp.R;
import com.fli.salesagentapp.fliagentapp.data.CollectionItem;
import com.fli.salesagentapp.fliagentapp.utils.Constants;

import java.util.ArrayList;

/**
 * Created by janithah on 8/9/2018.
 */

public class AttendaceTypesAdapter extends ArrayAdapter {

    private final String[] items ;
    private final LayoutInflater mInflater;
    private final Context context;

    public AttendaceTypesAdapter(Context context) {
        super(context, R.layout.text_item2,Constants.ATTENDANCE_TYPES);
        this.items = Constants.ATTENDANCE_TYPES;
        this.mInflater = LayoutInflater.from(context);
        this.context =context;
    }



    public View getView(int position, View view, ViewGroup viewGroup) {
        View v = view;
        TextView txt_attendance;
        if (v == null) {
            v = mInflater.inflate(R.layout.text_item2, viewGroup, false);
            v.setTag(R.id.txt_attendance, v.findViewById(R.id.text1));
        }
        txt_attendance = (TextView) v.getTag(R.id.txt_attendance);

//        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
//            txt_attendance.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.rounded_border_green) );
//        } else {
//            txt_attendance.setBackground(ContextCompat.getDrawable(context, R.drawable.rounded_border_green));
//        }
        Log.e("FLI ARRAY",items[position]);
        txt_attendance.setText(items[position]);
        return v;

        }

    @Override
    public View getDropDownView(int position, View convertView,ViewGroup parent) {
        return getView(position,convertView,parent);
    }



}
