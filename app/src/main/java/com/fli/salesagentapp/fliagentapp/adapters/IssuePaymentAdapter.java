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
import android.widget.TextView;

import com.fli.salesagentapp.fliagentapp.PaymentsActivity;
import com.fli.salesagentapp.fliagentapp.R;
import com.fli.salesagentapp.fliagentapp.data.CollectionItem;
import com.fli.salesagentapp.fliagentapp.data.PayeeItem;

import java.util.ArrayList;

/**
 * Created by janithah on 8/8/2018.
 */

public class IssuePaymentAdapter extends ArrayAdapter {

    private final ArrayList<PayeeItem> items ;
    private final LayoutInflater mInflater;
    private final Context context;
    TextView pay_total;
    int total = 0 ;
    int beforeval;

    static class ViewHolder {
        TextView txt_id,txt_name,txt_payment_due;
        EditText txt_payment;
    }

    public IssuePaymentAdapter(Context context,ArrayList<PayeeItem> items,TextView pay_total) {
        super(context, R.layout.layout_payment_item,items);
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
        TextView txt_id,txt_name,txt_payment_due;
        EditText txt_payment;
        if (v == null) {
            final ViewHolder holder = new ViewHolder();
            v = mInflater.inflate(R.layout.layout_payment_item, viewGroup, false);
            holder.txt_id =  (TextView)v.findViewById(R.id.txt_id);// (R.id.txt_id, v.findViewById(R.id.txt_id));
            holder.txt_name=  (TextView)v.findViewById(R.id.txt_name);//(R.id.txt_name, v.findViewById(R.id.txt_name));
            holder.txt_payment =  (EditText) v.findViewById(R.id.txt_payment); //(R.id.txt_payment, v.findViewById(R.id.txt_payment));
            holder.txt_payment_due =  (TextView)v.findViewById(R.id.txt_payment_due); //(R.id.txt_payment_due, v.findViewById(R.id.txt_payment_due));

            holder.txt_payment.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    if(!s.toString().equals(""))
                        beforeval = Integer.parseInt(s.toString());
                    //total -= Integer.parseInt(s.toString());
                    Log.e("FLI BEFORE",s.toString());
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    Log.e("FLI ONCHANGE",s.toString());

                    }

                @Override
                public void afterTextChanged(Editable s) {
                    Log.e("FLI AFTER",s.toString());
                    if(!s.toString().equals("")) {
                        if(Integer.parseInt(s.toString()) != beforeval) {
                            total += Integer.parseInt(s.toString());
                            total -= beforeval;
                        }
                    }
                    pay_total.setText(""+total);
                }
            });
            v.setTag(holder);
        }

        final ViewHolder holder = (ViewHolder) v.getTag();
//        txt_id = (TextView) v.getTag(R.id.txt_id);
//        txt_name = (TextView) v.getTag(R.id.txt_name);
//        txt_payment = (EditText) v.getTag(R.id.txt_payment);
//        txt_payment_due = (TextView) v.getTag(R.id.txt_payment_due);

        PayeeItem item = items.get(position);
        holder.txt_id.setText(String.valueOf(item.id));
        holder.txt_name.setText(item.name);
        holder.txt_payment.setText(item.payment);
        holder.txt_payment_due.setText(item.payment_due);
        total += Integer.valueOf(item.payment);
        Log.e("Total",""+total);

//        txt_payment.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                Log.e("FLI BEFORE",s.toString());
////                total -= Integer.parseInt(s.toString());
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                Log.e("FLI ONCHANGE",s.toString());
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                Log.e("FLI AFTER",s.toString());
////                if(!s.toString().equals(""))
////                total += Integer.parseInt(s.toString());
////                pay_total.setText(""+total);
//                //((PaymentsActivity)context).recalculateTotal(s.toString());
//               // calculate_total();
//            }
//        });

        return v;
    }


    public void calculate_total(){
        int tot =0;
        PayeeItem payee;
        for(int i=0; i< items.size();i++){
            payee = items.get(i);
            tot += Integer.parseInt(payee.payment);
        }

        pay_total.setText(""+tot);
    }
}
