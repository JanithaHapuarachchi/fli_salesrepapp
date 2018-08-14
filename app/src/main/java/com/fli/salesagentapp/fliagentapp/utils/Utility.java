package com.fli.salesagentapp.fliagentapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.fli.salesagentapp.fliagentapp.data.CurrentUser;

import org.json.JSONObject;

/**
 * Created by janithah on 8/8/2018.
 */

public class Utility {

    static SharedPreferences preferences;
    private static SharedPreferences getSharedPrefs(Context context){
        if(preferences ==null) {
            preferences = PreferenceManager.getDefaultSharedPreferences(context);
            preferences  = context.getSharedPreferences(Constants.FLI_SHARED_PREFS, Context.MODE_PRIVATE);
        }
            return preferences;
    }


    public static void showMessage(String txt, Context context) {
        Toast.makeText(context, txt, Toast.LENGTH_LONG).show();
    }

    public static String getLastLoggedDate(Context context){
        SharedPreferences preferences = getSharedPrefs(context);
        return preferences.getString(Constants.LAST_LOGGED_DATE, "");
    }

    public static boolean setCurrentUser(Context context, CurrentUser currentuser){
        Log.e("FLI CURRENT USER",currentuser.toString());
        SharedPreferences preferences =  getSharedPrefs(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Constants.USER_NAME, currentuser.username);
        editor.putString(Constants.USER_PASSWORD, currentuser.password);
        editor.putString(Constants.LAST_LOGGED_DATE,currentuser.loggeddate);
        editor.putString(Constants.AUTHENTICATION_KEY,currentuser.authkey);
        editor.putString(Constants.USER_ID,currentuser.userid);
        editor.commit();

        return true;
    }

    public static boolean isCurrentUser(Context context,CurrentUser currentUser){
        SharedPreferences preferences =  getSharedPrefs(context);
        String current_user_name =  preferences.getString(Constants.USER_NAME, "");
        String current_user_password = preferences.getString(Constants.USER_PASSWORD, "");
        if(current_user_name.equals(currentUser.username) && current_user_password.equals(currentUser.password)){
            currentUser.authkey = getAuthKey(context);
            currentUser.userid = getUserID(context);
            return true;
        }
        else{
            return false;
        }

    }

    public static String getUserID(Context context){
        SharedPreferences preferences =  getSharedPrefs(context);
        return preferences.getString(Constants.USER_ID, "");
    }

    public static String getAuthKey(Context context){
        SharedPreferences preferences =  getSharedPrefs(context);
        return preferences.getString(Constants.AUTHENTICATION_KEY, "");
    }

    public static String getCurrentUserName(Context context){
        SharedPreferences preferences =  getSharedPrefs(context);
        return preferences.getString(Constants.USER_NAME, "");
    }
    public static String getCurrentPassword(Context context){
        SharedPreferences preferences =  getSharedPrefs(context);
        return preferences.getString(Constants.USER_PASSWORD, "");
    }

    public static boolean isJSONKeyAvailable(JSONObject jObject,String key){
        if(jObject.has(key)){
            return true;
        }
        return false;
    }

}
