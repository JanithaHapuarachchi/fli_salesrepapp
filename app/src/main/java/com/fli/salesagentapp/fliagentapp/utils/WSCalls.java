package com.fli.salesagentapp.fliagentapp.utils;

import android.content.Context;

import com.fli.salesagentapp.fliagentapp.data.ResObject;

import org.json.JSONObject;

import java.io.IOException;

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

        } catch (IOException e) {
            res_object.validity = Constants.VALIDITY_FAILED;
            res_object.msg = e.getMessage();
        }
        return res_object;
    }

}
