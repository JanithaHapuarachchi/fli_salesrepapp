package com.fli.salesagentapp.fliagentapp.data;

import java.util.ArrayList;

/**
 * Created by janithah on 8/18/2018.
 */

public class ClientItem {

    public String id;
    public String name;
    public String externalid;
    public String loanid;
    public String loanname;
    public String def;
    public String rental;
    public String arrears;
    public ArrayList<AttendanceItem> attends =new ArrayList<AttendanceItem>();
    public int attendancetype = 1;


    @Override
    public String toString() {
        return "id: "+id+", name :"+name+", def :"+def+", attendance :"+attendancetype;
    }
}
