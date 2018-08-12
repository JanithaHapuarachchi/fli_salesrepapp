package com.fli.salesagentapp.fliagentapp.data;

/**
 * Created by janithamh on 8/12/18.
 */

public class CurrentUser {

    public static String username;
    public static String password;
    public static String userid;
    public static String authkey;
    public static String loggeddate;
    public static CurrentUser currentUser;

    public static CurrentUser getCurrentUser() {
        if(currentUser ==null){
            currentUser = new CurrentUser();
        }
        return currentUser;
    }

}

