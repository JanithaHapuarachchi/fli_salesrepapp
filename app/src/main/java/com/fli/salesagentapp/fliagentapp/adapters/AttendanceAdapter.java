package com.fli.salesagentapp.fliagentapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.fli.salesagentapp.fliagentapp.R;
import com.fli.salesagentapp.fliagentapp.data.AttendanceItem;
import com.fli.salesagentapp.fliagentapp.data.ClientItem;
import com.fli.salesagentapp.fliagentapp.data.PayeeItem;

import java.util.ArrayList;

/**
 * Created by janithah on 8/8/2018.
 */

public class AttendanceAdapter extends ArrayAdapter {

   // private final ArrayList<PayeeItem> items ;
    public ArrayList<ClientItem> items ;
    private final LayoutInflater mInflater;
    private final Context context;
    TextView pay_total;
    int total = 0 ;
    int beforeval;

    static class ViewHolder {
        TextView txt_today_attendace,txt_name,txt_payment_due;
        LinearLayout layout_attendance;
        Spinner spinner_today_attendace;
        DataHolder data;
        AttendaceTypesAdapter ata;

    }
    static class AttendanceViewHolder {
        TextView attendance;
    }


    public AttendanceAdapter(Context context,ArrayList<ClientItem> items) {
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
            holder.spinner_today_attendace =  (Spinner) v.findViewById(R.id.spinner_today_attendace);
            holder.spinner_today_attendace.setTag(position);
            holder.data =  new DataHolder(context);
            holder.ata = new AttendaceTypesAdapter(context);
            //(R.id.txt_payment_due, v.findViewById(R.id.txt_payment_due));
//            holder.txt_today_attendace.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                }
//            });
            v.setTag(holder);
        }

        final ViewHolder holder = (ViewHolder) v.getTag();
      //  PayeeItem item = items.get(position);
        final ClientItem item = items.get(position);
        if(((LinearLayout)  holder.layout_attendance).getChildCount() > 0)
            ((LinearLayout) holder.layout_attendance).removeAllViews();
        TextView tv;
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT

        );
        param.setMargins(2,2,2,2);
        Log.e("SIZE",""+item.attends.size());
        for(int i=0; i< item.attends.size();i++){

            View vi = mInflater.inflate(R.layout.layout_attendance_item, null); //log.xml is your file.
             tv = (TextView) vi.findViewById(R.id.txt_attendance);
           // tv=new TextView(context);

            tv.setLayoutParams(param);
            tv.setTag(i+100);
           //tv.setlay
            tv.setText(item.attends.get(i).attend);
            tv.setTextColor(ContextCompat.getColor(context,R.color.app_text_color));
            holder.layout_attendance.addView(tv);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                tv.setTextAppearance(context,R.layout.layout_attendance_item);
//            }
           // txt_attendance = (TextView) layout_attendance_item.findViewById(R.id.txt_attendance);
           // txt_attendance.setText(item.attends.get(i).attend);
//            if(layout_attendance_item.getParent()!=null)
//                ((ViewGroup)layout_attendance_item.getParent()).removeView(layout_attendance_item);
         //   holder.layout_attendance.addView(layout_attendance_item);
        }
       holder.spinner_today_attendace.setAdapter(holder.ata);
        //holder.spinner_today_attendace.setAdapter(holder.data.getAdapter());
       // holder.spinner_today_attendace.setAdapter(new AttendaceTypesAdapter(context));
        holder.spinner_today_attendace.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int x= (int) parent.getTag();
                Log.e("FLI TAG",  items.get(x).toString());
                items.get(x).attendancetype =position+1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        holder.txt_name.setText(item.name);

        return v;
    }

    public ArrayList<ClientItem> markeditems(){
        return items;
    }


    class DataHolder {

        private int selected;
       private ArrayAdapter<CharSequence> adapter;
       // private  ArrayAdapter adapter;

        public DataHolder(Context parent) {
          //  adapter = new AttendaceTypesAdapter(parent);
            adapter = ArrayAdapter.createFromResource(parent, R.array.attendance_types, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        }

        public ArrayAdapter<CharSequence> getAdapter() {
            return adapter;
        }

        public String getText() {
            return (String) adapter.getItem(selected);
        }

        public int getSelected() {
            return selected;
        }

        public void setSelected(int selected) {
            this.selected = selected;
        }

    }
}