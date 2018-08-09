package com.fli.salesagentapp.fliagentapp.adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
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

public class IssuePaymentAdapter extends BaseAdapter {

    private  ArrayList<PayeeItem> items ;
    private final LayoutInflater mInflater;
    private final Context context;
    TextView pay_total;
    int total = 0 ;
    int beforeval;

    static class ViewHolder {
        TextView txt_id,txt_name,txt_payment_due;
        EditText txt_payment;
        int no;
    }

    public IssuePaymentAdapter(Context context,ArrayList<PayeeItem> items,TextView pay_total) {
       // super(context, R.layout.layout_payment_item,items);
        this.items = items;
        this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//LayoutInflater.from(context);
        this.context =context;
        this.pay_total = pay_total;
        total = 0;
    }


    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return  items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
         return getCount()>0? getCount():1;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if(position ==0) {
            total = 0;
        }
        View v = view;
        final TextView txt_id,txt_name,txt_payment_due;
        EditText txt_payment;
        if (v == null) {
            final ViewHolder holder = new ViewHolder();
            v = mInflater.inflate(R.layout.layout_payment_item, null, true);
            holder.txt_id =  (TextView)v.findViewById(R.id.txt_id);// (R.id.txt_id, v.findViewById(R.id.txt_id));
            holder.txt_name=  (TextView)v.findViewById(R.id.txt_name);//(R.id.txt_name, v.findViewById(R.id.txt_name));
            holder.txt_payment =  (EditText) v.findViewById(R.id.txt_payment); //(R.id.txt_payment, v.findViewById(R.id.txt_payment));
            holder.txt_payment_due =  (TextView)v.findViewById(R.id.txt_payment_due); //(R.id.txt_payment_due, v.findViewById(R.id.txt_payment_due));
            v.setTag(holder);
        }

        final ViewHolder holder = (ViewHolder) v.getTag();
//        txt_id = (TextView) v.getTag(R.id.txt_id);
//        txt_name = (TextView) v.getTag(R.id.txt_name);
//        txt_payment = (EditText) v.getTag(R.id.txt_payment);
//        txt_payment_due = (TextView) v.getTag(R.id.txt_payment_due);

        final PayeeItem item = items.get(position);
        holder.txt_id.setText(String.valueOf(item.id));
        holder.txt_name.setText(item.name);
        holder.txt_payment.setText(item.payment);
        holder.txt_payment.setSelection(holder.txt_payment.getText().length());
       // holder.txt_payment.setTag(position);
        holder.txt_payment_due.setText(item.payment_due);
        holder.no = position;
        total += Integer.parseInt(item.payment);
        pay_total.setText(""+total);
        Log.e("FLI RE 1 TOTAL ",""+total+"->"+position+"->"+item.payment);

        TextEditor oldWatcher = (TextEditor) holder.txt_payment.getTag();
        if(oldWatcher != null)
            holder.txt_payment.removeTextChangedListener(oldWatcher);

        TextEditor newWatcher = new TextEditor(v,position);
            holder.txt_payment.setTag(newWatcher);
        holder.txt_payment.addTextChangedListener(newWatcher);
//        holder.txt_payment.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                String inserted,having;
//                if (!hasFocus) {
//                    final int position = v.getId();
//                    final EditText Caption = (EditText) v;
//                    String val = Caption.getText().toString();
//                    Log.e("FLI CAPTION",val);
//                    if(val.equals("")){
//                        val="0";
//                    }
//                    having = (String) Caption.getTag();
//                    Log.e("FLI HAVING",having);
//                    if(having != null){
//                        total = total + Integer.parseInt(val) - Integer.parseInt(having);
//                        Caption.setTag(val);
//                        items.get(position).payment = val;
//                        pay_total.setText(""+total);
//                    }
//
//                }
//                }
//        });

//        holder.txt_payment.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                if(s.toString().equals("")){
//                    beforeval = 0;
//                }
//                else {
//                    beforeval = Integer.parseInt(s.toString());
//                }
//                //total -= Integer.parseInt(s.toString());
//                Log.e("FLI BEFORE TOTAL",""+total);
//                Log.e("FLI BEFORE",""+beforeval);
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                Log.e("FLI ONCHANGE",s.toString());
//                String stringafter ;
//                if(!s.toString().equals("")) {
//                    stringafter = s.toString();
//                    if(Integer.parseInt(stringafter) != beforeval) {
//                        total += Integer.parseInt(stringafter);
//                        total -= beforeval;
//                    }
//                }
//                else{
//                    stringafter = "0";
//                    if(Integer.parseInt(stringafter) != beforeval) {
//                        //total += 0;
//                        total -= beforeval;
//                    }
//                }
////                PayeeItem it = items.get(holder.no);
////                it.payment = stringafter;
////                items.add(holder.no,it);
//                Log.e("FLI RE TOTAL ",""+total);
//                pay_total.setText(""+total);
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                Log.e("FLI AFTER",s.toString());
//                Log.e("FLI AFTER BEFORE",""+total);
//
//            }
//        });

       // total += Integer.valueOf(item.payment);
        Log.e("Total",""+total);



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

    class TextEditor implements TextWatcher{

        private View view;
        EditText amount;
        int position;

        public TextEditor(View view,int position){
            this.view = view;
            this.position =position;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            Log.e("FLI B",s.toString());
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            amount = (EditText) view.findViewById(R.id.txt_payment);
            Log.e("FLI AMOUNT",amount.getText().toString());
            items.get(position).payment = amount.getText().toString();
          //  pay_total.setText(total);
           // return;
        }
    }
}
