package com.fli.salesagentapp.fliagentapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

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

    public static boolean setCurrentUser(Context context,String username,String password,String loggeddate){
        SharedPreferences preferences =  getSharedPrefs(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Constants.USER_NAME, username);
        editor.putString(Constants.USER_PASSWORD, password);
        editor.putString(Constants.LAST_LOGGED_DATE,loggeddate);
        editor.commit();

        return true;
    }

    public static boolean isCurrentUser(Context context,String username,String password){
        SharedPreferences preferences =  getSharedPrefs(context);
        String current_user_name =  preferences.getString(Constants.USER_NAME, "");
        String current_user_password = preferences.getString(Constants.USER_PASSWORD, "");
        if(current_user_name.equals(username) && current_user_password.equals(password)){
            return true;
        }
        else{
            return false;
        }

    }

    public static String getCurrentUserName(Context context){
        SharedPreferences preferences =  getSharedPrefs(context);
        return preferences.getString(Constants.USER_NAME, "");
    }
    public static String getCurrentPassword(Context context){
        SharedPreferences preferences =  getSharedPrefs(context);
        return preferences.getString(Constants.USER_PASSWORD, "");
    }
}
