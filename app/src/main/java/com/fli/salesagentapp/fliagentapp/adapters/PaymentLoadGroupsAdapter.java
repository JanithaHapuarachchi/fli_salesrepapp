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
import com.fli.salesagentapp.fliagentapp.data.GroupItem;

import java.util.ArrayList;

/**
 * Created by janithah on 8/8/2018.
 */

public class PaymentLoadGroupsAdapter extends ArrayAdapter {

    private final Context context;
    LayoutInflater inf;
    ArrayList<GroupItem> groupList;
    int i;
    GroupItem item;

    public PaymentLoadGroupsAdapter( Context context, ArrayList<GroupItem> groupList) {
        super(context, R.layout.text_item,groupList);
        inf = LayoutInflater.from(context);
        inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.groupList =groupList;
    }


    //@Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View v = view;
        TextView text;
        if (v == null) {
            v = inf.inflate(R.layout.text_item, viewGroup, false);
            v.setTag(R.id.text1, v.findViewById(R.id.text1));
        }
        item = groupList.get(position);
        Log.e("FLI GROUP",groupList.get(position).toString());
        text = (TextView) v.getTag(R.id.text1);
        text.setText(item.name);
        return v;
    }

    @Override
    public View getDropDownView(int position, View convertView,ViewGroup parent) {
        return getView(position,convertView,parent);
    }
}
