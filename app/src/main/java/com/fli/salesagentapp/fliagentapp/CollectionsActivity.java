package com.fli.salesagentapp.fliagentapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.fli.salesagentapp.fliagentapp.adapters.CollectionAdapter;
import com.fli.salesagentapp.fliagentapp.data.CollectionItem;

import java.util.ArrayList;

public class CollectionsActivity extends AppCompatActivity {

    ListView list_collections;
    CollectionAdapter c_adapter;
    ArrayList<CollectionItem> collections;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collections);
        list_collections = (ListView)findViewById(R.id.list_collections);
//        collections = initItems();
//        c_adapter =new CollectionAdapter(getApplicationContext(),collections);
//        list_collections.setAdapter(c_adapter);

    }

    private ArrayList<CollectionItem> initItems(){
        ArrayList<CollectionItem> items = new ArrayList<CollectionItem>();
        CollectionItem item;
        for(int i=0; i< 5; i++){
            item = new CollectionItem();
            item.center_name = "Center "+(i+1);
            item.no_of_loans = ""+(i*5);
            item.total_collection= ""+(i*20+1);
            item.pending_sync = ""+0;
            items.add(item);
        }
        return items;
    }
}
