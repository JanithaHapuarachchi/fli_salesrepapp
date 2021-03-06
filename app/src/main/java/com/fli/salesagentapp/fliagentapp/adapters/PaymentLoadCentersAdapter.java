package com.fli.salesagentapp.fliagentapp.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.fli.salesagentapp.fliagentapp.R;
import com.fli.salesagentapp.fliagentapp.data.CenterItem;

import java.util.ArrayList;

/**
 * Created by janithah on 8/8/2018.
 */

public class PaymentLoadCentersAdapter extends ArrayAdapter {

    private final Context context;
    LayoutInflater inf;
    ArrayList<CenterItem> centerList;
    int i;
    CenterItem item;

    public PaymentLoadCentersAdapter( Context context, ArrayList<CenterItem> centerList) {
        super(context, R.layout.text_item,centerList);
        Log.e("COUNT ","Constructor");
        inf = LayoutInflater.from(context);
        this.context = context;
        this.centerList = centerList;
    }


    public View getView(int position, View view, ViewGroup viewGroup) {
        Log.e("COUNT ",""+position);
        View v = view;
        TextView text;
        if (v == null) {
            v = inf.inflate(R.layout.text_item, viewGroup, false);
            v.setTag(R.id.text1, v.findViewById(R.id.text1));
        }
        item = centerList.get(position);
        Log.e("FLI CENTER",centerList.get(position).toString());
        text = (TextView) v.getTag(R.id.text1);
        text.setText(item.name);
        return v;
    }

    @Override
    public View getDropDownView(int position, View convertView,ViewGroup parent) {
        return getView(position,convertView,parent);
    }
}
