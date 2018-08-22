package com.fli.salesagentapp.fliagentapp.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.util.Log;

import com.fli.salesagentapp.fliagentapp.MainMenu;
import com.fli.salesagentapp.fliagentapp.utils.Utility;
import com.fli.salesagentapp.fliagentapp.utils.WSCalls;

public class SubmitDataService extends Service {

    public static boolean isServiceRunning = false;
    WSCalls wsCalls;
    public static Sync sync;
    public static  SubmitDataService cuurentService;
    public SubmitDataService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        isServiceRunning =true;
        cuurentService = this;
        Log.e("FLI Service","Created");
        wsCalls = new WSCalls(getApplicationContext());
        Intent ser = new Intent(SubmitDataService.this, SubmitDataService.class);
        PendingIntent pendingIntent = PendingIntent.getService(SubmitDataService.this, 0, ser, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarm = (AlarmManager) SubmitDataService.this.getSystemService(Context.ALARM_SERVICE);
        alarm.setInexactRepeating(AlarmManager.RTC, System.currentTimeMillis(),
                1*60* 1000, pendingIntent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (Utility.isConnected(SubmitDataService.this)) {
            isServiceRunning =true;
            Log.e("FLI Service","Started");
            sync = new Sync();
            sync.execute();


        }
        return START_NOT_STICKY;
    }



    public static void stopAsync(){
        if(sync != null)
        sync.cancel(true);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isServiceRunning =false;
    }

    class Sync extends AsyncTask<Void,Void,Void> {
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (Utility.isConnected(SubmitDataService.this)) {
                try {
                    wsCalls.sync_MarkedAttendance();
                    wsCalls.sync_PayedLoans();
                } catch (Exception e) {
                    e.getStackTrace();
                    Log.e("FLI Major", e.toString());
                }
            }
            return null;
        }


    }
}
