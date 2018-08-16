package com.fli.salesagentapp.fliagentapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.fli.salesagentapp.fliagentapp.adapters.MainMenuAdapter;
import com.fli.salesagentapp.fliagentapp.utils.Constants;
import com.fli.salesagentapp.fliagentapp.utils.ProgressBarController;
import com.fli.salesagentapp.fliagentapp.utils.WSCalls;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainMenu extends AppCompatActivity {
    WSCalls wscalls;
    GridView menu_grid;
    ArrayList<JSONObject> menu_items;
    ProgressBarController prgController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        getSupportActionBar().hide();
        menu_grid = (GridView)findViewById(R.id.menu_grid);
        menu_items = new ArrayList<JSONObject>();
        wscalls = new WSCalls(getApplicationContext());
        prgController = new ProgressBarController(this);
        try {
            menu_items.add(new JSONObject().put("img","loans").put("txt","Loans"));
            menu_items.add(new JSONObject().put("img","payments").put("txt","Payments"));
            menu_items.add(new JSONObject().put("img","report").put("txt","Collections"));
            menu_items.add(new JSONObject().put("img","attendance").put("txt","Attendance"));
            menu_grid.setAdapter(new MainMenuAdapter(getApplicationContext(),menu_items));
        } catch (JSONException e) {
            e.printStackTrace();
        }

      //  sync_again();
        if(getIntent().getExtras().getBoolean(Constants.SHOULD_SYNC_AGAIN)){
            sync_again();
        }

        menu_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position ==0 ){
                    startActivity(new Intent(getApplicationContext(),FragmentMainMenu.class).putExtra("FRAGMENT_NO", position));
                }
                else if(position ==1){
                    startActivity(new Intent(getApplicationContext(),FragmentMainMenu.class).putExtra("FRAGMENT_NO", position));
                }
                else if(position ==2){
                    startActivity(new Intent(getApplicationContext(),FragmentMainMenu.class).putExtra("FRAGMENT_NO", position));
                }
                else if(position ==3){
                    startActivity(new Intent(getApplicationContext(),FragmentMainMenu.class).putExtra("FRAGMENT_NO", position));
                }
                finish();
            }
        });
    }

    private void sync_again(){
        new SyncLoans().execute();
    }

    class SyncLoans extends AsyncTask<Void,Void,Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            prgController.showProgressBar("Loading Loans...");
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            prgController.hideProgressBar();


        }

        @Override
        protected Void doInBackground(Void... params) {

            wscalls.sync_loans();
            return null;
        }
    }

}
