package com.fli.salesagentapp.fliagentapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.fli.salesagentapp.fliagentapp.R;
import com.fli.salesagentapp.fliagentapp.data.CenterItem;
import com.fli.salesagentapp.fliagentapp.data.CollectionItem;

import java.util.ArrayList;

/**
 * Created by janithah on 8/8/2018.
 */

public class CollectionAdapter extends ArrayAdapter {

 //   private final ArrayList<CollectionItem> items ;
    private final ArrayList<CenterItem> items ;
    private final LayoutInflater mInflater;
    private final Context context;

    public CollectionAdapter(Context context,ArrayList<CenterItem> items) {
        super(context, R.layout.layout_collection_item,items);
        this.items = items;
        this.mInflater = LayoutInflater.from(context);
        this.context =context;
    }


    @Override
    public View getView(int position, View view,  ViewGroup viewGroup) {
        View v = view;
        TextView center_name,no_of_loans,total_collection,pending_sync;
        if (v == null) {
            v = mInflater.inflate(R.layout.layout_collection_item, viewGroup, false);
            v.setTag(R.id.center_name, v.findViewById(R.id.center_name));
            v.setTag(R.id.no_of_loans, v.findViewById(R.id.no_of_loans));
            v.setTag(R.id.total_collection, v.findViewById(R.id.total_collection));
            v.setTag(R.id.pending_sync, v.findViewById(R.id.pending_sync));
        }
        center_name = (TextView) v.getTag(R.id.center_name);
        no_of_loans = (TextView) v.getTag(R.id.no_of_loans);
        total_collection = (TextView) v.getTag(R.id.total_collection);
        pending_sync = (TextView) v.getTag(R.id.pending_sync);

//        CollectionItem item = items.get(position);
//        center_name.setText(item.center_name);
//        no_of_loans.setText(item.no_of_loans);
//        total_collection.setText(item.total_collection);
//        pending_sync.setText(item.pending_sync);

        CenterItem item = items.get(position);
        center_name.setText(item.name);
        no_of_loans.setText(String.valueOf(item.no_of_loans));
        total_collection.setText(String.valueOf(item.total_collection));
        pending_sync.setText(String.valueOf(item.pending_sync_count));

        return v;
    }
}
