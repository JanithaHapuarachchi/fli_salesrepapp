package com.fli.salesagentapp.fliagentapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fli.salesagentapp.fliagentapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by janithamh on 8/6/18.
 */

public class MainMenuAdapter extends BaseAdapter {

    private final ArrayList<JSONObject> items ;
    private final LayoutInflater mInflater;
    private final Context context;

    public MainMenuAdapter(Context context, ArrayList<JSONObject> items) {
        this.items = items;
        this.mInflater = LayoutInflater.from(context);
        this.context =context;
    }


    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = view;
        ImageView img;
        TextView txt;

        if (v == null) {
            v = mInflater.inflate(R.layout.menu_item, viewGroup, false);
            v.setTag(R.id.img, v.findViewById(R.id.img));
            v.setTag(R.id.txt, v.findViewById(R.id.txt));
        }

        img = (ImageView) v.getTag(R.id.img);
        txt = (TextView) v.getTag(R.id.txt);

        JSONObject item = (JSONObject) getItem(i);
        int id = 0;
        try {
            id = context.getResources().getIdentifier(item.getString("img"), "drawable", context.getPackageName());
            img.setImageResource(id);
            txt.setText(item.getString("txt"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //picture.setImageResource(item.getString("img"));


        return v;
    }
}
