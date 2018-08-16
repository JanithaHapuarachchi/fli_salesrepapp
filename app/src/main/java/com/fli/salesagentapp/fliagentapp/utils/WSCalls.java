package com.fli.salesagentapp.fliagentapp.utils;

import android.content.Context;
import android.util.Log;

import com.fli.salesagentapp.fliagentapp.data.ResObject;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by janithamh on 8/12/18.
 */

public class WSCalls {

    Context context;
    public WSCalls(Context context){
        this.context =context;
    }

    public ResObject autherise_user(String username, String password) {
        ResObject res_object = new ResObject();
        String response;
        String request  =  Constants.AUTHENTICATION_URL+"?username="+username+"&password="+password;
        try {
            response =  RequestHandler.sendPost(new JSONObject(),request,context);
            res_object.validity = Constants.VALIDITY_SUCCESS;
            res_object.msg = response;

        } catch (Exception e) {
            Log.e("FLI E AUTH",e.getMessage());
            res_object.validity = Constants.VALIDITY_FAILED;
            res_object.msg = "Login Failed";
          // res_object.msg = e.getMessage();
        }
        return res_object;
    }



    public ResObject sync_loans(){
        ResObject res_object = new ResObject();
        ArrayList <JSONObject> loans = new ArrayList<JSONObject>();
        long loopcount = 1;
        String request,response ;
        JSONObject resJSON;
        long limit =800;
        long offset = 0;
        long totalresults;
        for(long i=0;i<loopcount;i++){
            Log.e("FLI loopcount i",""+loopcount+" : "+i);
             request =  Constants.LOANS_URL+"?"+"offset="+offset+"&limit="+limit+Constants.ACTIVE_LOANS_URL;
            try {
                response =  RequestHandler.sendGet(request,context);
                resJSON = new JSONObject(response);
                loans.addAll(Utility.getArrayListformJSONARRAY( resJSON.getJSONArray("pageItems")));
                if(i==0){
                    totalresults = resJSON.getLong("totalFilteredRecords");
                    Log.e("FLI TOT COUNT",""+totalresults);
                    if(totalresults >limit){
                        loopcount = totalresults/limit;
                        Log.e("FLI loopcount COUNT",""+loopcount);
                        Log.e("FLI loopcount COUNT 2",""+totalresults%limit);
                        if((totalresults%limit) != 0){
                            loopcount++;
                        }
                        //loopcount--; // for default loop
                    }
                }


            } catch (Exception e) {
                Log.e("FLI E LOANS",e.getMessage());
                res_object.validity = Constants.VALIDITY_FAILED;
                res_object.msg = "Sync Loans Failed";
                // res_object.msg = e.getMessage();
            }

            offset = (i+1)*limit;
        }
//        Log.e("FLI LOANS",loans.toString());
        return null;
    }

}
