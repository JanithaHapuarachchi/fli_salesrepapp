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

import com.fli.salesagentapp.fliagentapp.data.CurrentUser;
import com.fli.salesagentapp.fliagentapp.data.ResObject;
import com.fli.salesagentapp.fliagentapp.utils.Constants;
import com.fli.salesagentapp.fliagentapp.utils.ProgressBarController;
import com.fli.salesagentapp.fliagentapp.utils.RequestHandler;
import com.fli.salesagentapp.fliagentapp.utils.Utility;
import com.fli.salesagentapp.fliagentapp.utils.WSCalls;

import org.json.JSONException;
import org.json.JSONObject;

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
    static CurrentUser currentUser;
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
        new InitiateSSL().execute();
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
//                            currentUser =CurrentUser.getCurrentUser();
//                            if(today_date.after(last_logged_date)){ // login day for the first time
//                                new CallAuthenticate().execute(uname,pswrd);
//                            }
//                            else{  //login today again
//
//                                currentUser.username = uname;
//                                currentUser.password = pswrd;
//                                currentUser.loggeddate = str_today;
//                                 if(Utility.isCurrentUser(getApplicationContext(),currentUser)){
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

    class InitiateSSL extends AsyncTask <Void,Void,Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            prgController.showProgressBar("Initializing SSL...");
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            prgController.hideProgressBar();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                RequestHandler.inititateSSL();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
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
            if(response.validity.equals(Constants.VALIDITY_SUCCESS)){
                checkAutehnticate(response);
            }
            else{
                Utility.showMessage(response.msg,getApplicationContext());
            }
            Log.e("RES", response.toString());
            prgController.hideProgressBar();
        }

        @Override
        protected ResObject doInBackground(String... params) {
            return wscalls.autherise_user(params[0],params[1]);

        }
    }


    public void checkAutehnticate(ResObject resObject){
        try {
            JSONObject jsonResponse = new JSONObject(resObject.msg);
            if(Utility.isJSONKeyAvailable(jsonResponse,"authenticated")){
                if(jsonResponse.getBoolean("authenticated")){
                    currentUser.authkey = jsonResponse.getString("base64EncodedAuthenticationKey");
                    currentUser.userid = jsonResponse.getString("userId");
                    Utility.setCurrentUser(getApplicationContext(),currentUser);
                    startActivity(new Intent(getApplicationContext(),MainMenu.class));
                }
                else{
                    Utility.showMessage("Login Failed",getApplicationContext());
                }
            }
            else if(Utility.isJSONKeyAvailable(jsonResponse,"defaultUserMessage")){
                Utility.showMessage(jsonResponse.getString("defaultUserMessage"),getApplicationContext());
            }

        } catch (JSONException e) {
            Utility.showMessage(e.getMessage(),getApplicationContext());
          //  e.printStackTrace();
        }

    }
}
