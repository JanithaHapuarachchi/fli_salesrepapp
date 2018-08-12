package com.fli.salesagentapp.fliagentapp.utils;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

/**
 * Created by janithamh on 8/12/18.
 */

public class RequestHandler {


    public static String sendGet(String methodname, String params, Context context) throws IOException{
        String responseString ="";
        String completeurl = Constants.MAIN_URL+methodname;
        Log.e("URL GET",completeurl);
        String encoding = null;

        URL obj = new URL(completeurl);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Fineract-Platform-TenantId","default");
        encoding = android.util.Base64.encodeToString((Utility.getCurrentUserName(context)+":"+Utility.getCurrentPassword(context)).getBytes(), android.util.Base64.NO_WRAP);
        Log.e("ENCODE", encoding);
        con.setRequestProperty  ("Authorization", "Basic " + encoding);
        int responseCode = con.getResponseCode();
        Log.e("Code",Integer.toString(responseCode));
        /*if (responseCode == HttpURLConnection.HTTP_OK) {*/

        BufferedReader in = new BufferedReader(new InputStreamReader(
                con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }

        responseString = response.toString();
        Log.e("String",responseString);
        in.close();

       /* }
        else {

        }
         */   return responseString;
    }

    public static String sendPost(JSONObject postobject, String methodname,Context context) throws IOException{
        String responseString ="";
        String completeurl = Constants.MAIN_URL+methodname;
        Log.e("URL POST",completeurl);
        URL obj = new URL(completeurl);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setDoInput (true);
        con.setDoOutput (true);
        con.setUseCaches (false);
        con.setRequestProperty("Content-Type","application/json");
        con.setRequestProperty("Fineract-Platform-TenantId","default");
        if(!methodname.equals(Constants.AUTHENTICATION_URL)){
        String encoding = android.util.Base64.encodeToString((Utility.getCurrentUserName(context)+":"+Utility.getCurrentPassword(context)).getBytes(), android.util.Base64.NO_WRAP);
        Log.e("ENCODE", encoding);
        con.setRequestProperty  ("Authorization", "Basic " + encoding);
        }
        con.connect();

        DataOutputStream printout = new DataOutputStream(con.getOutputStream ());
        printout.writeBytes(postobject.toString());
        printout.flush ();
        printout.close ();

        BufferedReader in = new BufferedReader(new InputStreamReader(
                con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }

        responseString = response.toString();
        Log.e("String",responseString);
        in.close();



        return responseString;
    }


}
