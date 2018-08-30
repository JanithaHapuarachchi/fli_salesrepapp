package com.fli.salesagentapp.fliagentapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.fli.salesagentapp.fliagentapp.adapters.MainMenuAdapter;
import com.fli.salesagentapp.fliagentapp.data.ResObject;
import com.fli.salesagentapp.fliagentapp.db.DBOperations;
import com.fli.salesagentapp.fliagentapp.services.SubmitDataService;
import com.fli.salesagentapp.fliagentapp.utils.Constants;
import com.fli.salesagentapp.fliagentapp.utils.DataManager;
import com.fli.salesagentapp.fliagentapp.utils.ProgressBarController;
import com.fli.salesagentapp.fliagentapp.utils.Utility;
import com.fli.salesagentapp.fliagentapp.utils.WSCalls;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainMenu extends AppCompatActivity {
    WSCalls wscalls;
    GridView menu_grid;
    ArrayList<JSONObject> menu_items;
    ProgressBarController prgController;
    DataManager dtManager;
    int pendingCount = 0 ;
    ResObject res_objectt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        getSupportActionBar().hide();
        menu_grid = (GridView)findViewById(R.id.menu_grid);
        menu_items = new ArrayList<JSONObject>();
        wscalls = new WSCalls(getApplicationContext());
        dtManager = new DataManager(getApplicationContext());
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
        else{
            new GetPendingCount().execute();
            startService();
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

    private void startService() {
        if(!SubmitDataService.isServiceRunning){
            if (Utility.isConnected(MainMenu.this)) {
                Intent ser = new Intent(MainMenu.this, SubmitDataService.class);
                startService(ser);
            }
        }


    }

   private void stropService(){
       if(SubmitDataService.isServiceRunning) {
           SubmitDataService.cuurentService.stopSelf();
       }
   }

    private void sync_again(){

        new SyncLoans().execute();
    }

    private void generateNotificationForPendingCount(int count){
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(),"");
        mBuilder.setSmallIcon(R.mipmap.ic_launcher)
        .setContentTitle("Pending Loan Sync Count").setContentText(""+count)
        .setPriority(NotificationCompat.PRIORITY_HIGH);
        notificationManager.notify(1,mBuilder.build());

    }

    class GetPendingCount extends AsyncTask<Void,Void,Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            prgController.showProgressBar("Finding Pending...");
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            prgController.hideProgressBar();
            if(pendingCount>0){
                generateNotificationForPendingCount(pendingCount);
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            pendingCount = dtManager.getTotalPendingSyncCount();
            return null;
        }
    }

    public void errorInLoadingLoans(){
        Utility.showMessage("Unable To Load Loans. Try Again",getApplicationContext());
        finish();
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
            if(res_objectt.validity.equals(Constants.VALIDITY_SUCCESS)) {
                startService();
            }
            else{
               errorInLoadingLoans();
            }

        }

        @Override
        protected Void doInBackground(Void... params) {
            dtManager.truncateDB();
            stropService(); //stop existing service to avoid collisions
            wscalls.sync_PayedLoans(); // sync already available payed loans
            res_objectt = wscalls.sync_loans();
            return null;
        }
    }

}
