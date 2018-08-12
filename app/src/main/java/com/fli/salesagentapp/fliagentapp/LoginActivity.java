package com.fli.salesagentapp.fliagentapp;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.fli.salesagentapp.fliagentapp.data.ResObject;
import com.fli.salesagentapp.fliagentapp.utils.Constants;
import com.fli.salesagentapp.fliagentapp.utils.ProgressBarController;
import com.fli.salesagentapp.fliagentapp.utils.Utility;
import com.fli.salesagentapp.fliagentapp.utils.WSCalls;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class LoginActivity extends AppCompatActivity {
    ProgressBarController prgController;
    EditText txt_username,txt_password;
    Button btn_login;
    String str_today = "";
    Context context;
    WSCalls wscalls;
    String str_last_logged_date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        txt_username = (EditText)findViewById(R.id.txt_username);
        txt_password = (EditText)findViewById(R.id.txt_password);
        btn_login = (Button)findViewById(R.id.btn_login);
        context =getApplicationContext();

        wscalls = new WSCalls(getApplicationContext());
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat(Constants.DATE_FORMAT);
        str_today = df.format(c);
        prgController = new ProgressBarController(this);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uname = txt_username.getText().toString();
                String pswrd  =txt_password.getText().toString();

                startActivity(new Intent(getApplicationContext(),MainMenu.class));
//                if(uname.equals("") || pswrd.equals("")){
//                    Utility.showMessage("Username or Password Cannot be Empty",getApplicationContext());
//                }
//                else{
//                     str_last_logged_date = Utility.getLastLoggedDate(getApplicationContext());
//                    if(str_last_logged_date.equals("")){ // first time for app
//                        new CallAuthenticate().execute(uname,pswrd);
//                    }
//                    else{
//                        try {
//                            Date last_logged_date=new SimpleDateFormat(Constants.DATE_FORMAT).parse(str_last_logged_date);
//                            Date today_date = new SimpleDateFormat(Constants.DATE_FORMAT).parse(str_today);
//                            if(today_date.after(last_logged_date)){ // login day for the first time
//                                new CallAuthenticate().execute(uname,pswrd);
//                            }
//                            else{  //login today again
//                                 if(Utility.isCurrentUser(getApplicationContext(),uname,pswrd)){
//                                     startActivity(new Intent(getApplicationContext(),MainMenu.class));
//                                 }
//                                 else {
//                                     Utility.showMessage("Username or Password is wrong",getApplicationContext());
//                                 }
//                            }
//                        } catch (ParseException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                }



            }
        });


    }


    class CallAuthenticate extends AsyncTask <String,Void,ResObject>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            prgController.showProgressBar("Requesting...");
        }

        @Override
        protected void onPostExecute(ResObject response) {
            super.onPostExecute(response);
            Log.e("RES", response.toString());
            prgController.hideProgressBar();
        }

        @Override
        protected ResObject doInBackground(String... params) {
            return wscalls.autherise_user(params[0],params[1]);

        }
    }
}
