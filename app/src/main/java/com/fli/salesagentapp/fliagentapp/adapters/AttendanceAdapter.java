package com.fli.salesagentapp.fliagentapp.adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fli.salesagentapp.fliagentapp.R;
import com.fli.salesagentapp.fliagentapp.data.AttendanceItem;
import com.fli.salesagentapp.fliagentapp.data.PayeeItem;

import java.util.ArrayList;

/**
 * Created by janithah on 8/8/2018.
 */

public class AttendanceAdapter extends ArrayAdapter {

    private final ArrayList<PayeeItem> items ;
    private final LayoutInflater mInflater;
    private final Context context;
    TextView pay_total;
    int total = 0 ;
    int beforeval;

    static class ViewHolder {
        TextView txt_today_attendace,txt_name,txt_payment_due;
        LinearLayout layout_attendance;
    }
    static class AttendanceViewHolder {
        TextView attendance;
    }


    public AttendanceAdapter(Context context,ArrayList<PayeeItem> items) {
        super(context, R.layout.layout_attendance,items);
        this.items = items;
        this.mInflater = LayoutInflater.from(context);
        this.context =context;
        this.pay_total = pay_total;
        total = 0;
    }


    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if(position ==0)
            total =0;
        View v = view;
        View layout_attendance_item =null;
         TextView txt_attendance,txt_name,txt_payment_due;
        EditText txt_payment;
        if (v == null) {
            final ViewHolder holder = new ViewHolder();
            v = mInflater.inflate(R.layout.layout_attendance, viewGroup, false);
            holder.txt_today_attendace =  (TextView)v.findViewById(R.id.txt_today_attendace);// (R.id.txt_id, v.findViewById(R.id.txt_id));
            holder.txt_name=  (TextView)v.findViewById(R.id.txt_name);//(R.id.txt_name, v.findViewById(R.id.txt_name));
            holder.layout_attendance =  (LinearLayout) v.findViewById(R.id.layout_attendance); //(R.id.txt_payment, v.findViewById(R.id.txt_payment));
           // holder.txt_payment_due =  (TextView)v.findViewById(R.id.txt_payment_due); //(R.id.txt_payment_due, v.findViewById(R.id.txt_payment_due));
            holder.txt_today_attendace.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            v.setTag(holder);
        }

        final ViewHolder holder = (ViewHolder) v.getTag();
        PayeeItem item = items.get(position);

     //   if(layout_attendance_item == null){
            layout_attendance_item = mInflater.inflate(R.layout.layout_attendance_item, viewGroup, false);
         //   layout_attendance_item.setTag(R.id.txt_attendance, layout_attendance_item.findViewById(R.id.txt_attendance));
      //  }
        if(((LinearLayout)  holder.layout_attendance).getChildCount() > 0)
            ((LinearLayout) holder.layout_attendance).removeAllViews();
        for(int i=0; i< item.attends.size();i++){
            txt_attendance = (TextView) layout_attendance_item.findViewById(R.id.txt_attendance);
            txt_attendance.setText(item.attends.get(i).attend);
            holder.layout_attendance.addView(layout_attendance_item);
        }

        holder.txt_name.setText(item.name);

        return v;
    }

}