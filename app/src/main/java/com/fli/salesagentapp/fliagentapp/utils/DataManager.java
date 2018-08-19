package com.fli.salesagentapp.fliagentapp.utils;

import android.content.Context;

import com.fli.salesagentapp.fliagentapp.data.ClientAttendanceInfo;
import com.fli.salesagentapp.fliagentapp.data.ClientPaymentsInfo;
import com.fli.salesagentapp.fliagentapp.data.MarkedAttendace;
import com.fli.salesagentapp.fliagentapp.data.PayeeItem;
import com.fli.salesagentapp.fliagentapp.data.RecievedLoan;
import com.fli.salesagentapp.fliagentapp.db.DBOperations;

import java.util.ArrayList;

/**
 * Created by janithah on 8/18/2018.
 */

public class DataManager {

    Context context;
    public DataManager(Context context){
        this.context =context;
    }


    public RecievedLoan getDetailsforLoanID(String loanid){
        return new DBOperations(context).getDetailsforLoanID(loanid);
    }

    public ClientPaymentsInfo getAvailablePayments(){
        return new DBOperations(context).getAvailablePayments();
    }
    public ClientAttendanceInfo getAvailableAttendance(){
        return new DBOperations(context).getAvailableAttendance();
    }

    public void saveMarkedAttendance(MarkedAttendace markedattendance){
        new DBOperations(context).saveMarkedAttendance(markedattendance);
    }

    public ArrayList<MarkedAttendace> getMarkedAttendance(){
        return new DBOperations(context).getMarkedAttendance();
    }

    public void deleteAttendanceForGroupId(String groupid){
         new DBOperations(context).deleteAttendanceForGroupId(groupid);
    }
    public void savePayment(PayeeItem item){
        new DBOperations(context).savePayment(item);
    }
}
