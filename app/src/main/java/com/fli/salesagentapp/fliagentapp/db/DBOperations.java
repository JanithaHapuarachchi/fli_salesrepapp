package com.fli.salesagentapp.fliagentapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.fli.salesagentapp.fliagentapp.data.CenterItem;
import com.fli.salesagentapp.fliagentapp.data.ClientAttendanceInfo;
import com.fli.salesagentapp.fliagentapp.data.ClientItem;
import com.fli.salesagentapp.fliagentapp.data.ClientPaymentsInfo;
import com.fli.salesagentapp.fliagentapp.data.GroupItem;
import com.fli.salesagentapp.fliagentapp.data.GroupPaymentItem;
import com.fli.salesagentapp.fliagentapp.data.MarkedAttendace;
import com.fli.salesagentapp.fliagentapp.data.PayeeItem;
import com.fli.salesagentapp.fliagentapp.data.RecievedLoan;
import com.fli.salesagentapp.fliagentapp.utils.Constants;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by janithah on 8/17/2018.
 */

public class DBOperations  extends SQLiteOpenHelper {
    public final static String DATABASE = "fli_salesrepapp.db";
    public final static int DATABASE_VERSION = 1;
    Context context;
    SQLiteDatabase db;

    public DBOperations(Context context){
        super(context, DATABASE, null, DATABASE_VERSION);
        this.context =context;

    }

    public void truncateDB(){
        db = this.getWritableDatabase();
        TableAttendanceTransactions.droptable(db);
        TableLoanTransactions.droptable(db);
        TableRecievedLoans.droptable(db);
        TableAttendanceTransactions.onCreate(db);
        TableLoanTransactions.onCreate(db);
        TableRecievedLoans.onCreate(db);
      //  db.close();
      //  db = null;
    }

    public ClientAttendanceInfo getAvailableAttendance(){
        String filter_type = Constants.FILTER_FOR_ATTENDANCE;
        ClientAttendanceInfo info = new ClientAttendanceInfo();
        db = this.getReadableDatabase();
        //db.beginTransaction();
        ArrayList<CenterItem> centers = getAvailableCenters(filter_type);
        ArrayList<GroupItem> groups = new ArrayList<GroupItem>();
        ArrayList<GroupItem> group_set;
        ArrayList<ClientItem> client_set;
        HashMap<String,ArrayList<GroupItem>> centergroups = new HashMap<String, ArrayList<GroupItem>>();
        HashMap<String,ArrayList<ClientItem>> groupclients = new HashMap<String, ArrayList<ClientItem>>();
        GroupItem groupItem;
        for(int i=0; i<centers.size();i++){
            group_set = getGroupsForCenter(centers.get(i),filter_type);
            groups.addAll(group_set);
            centergroups.put(centers.get(i).id,group_set);
        }
        for(int j=0; j<groups.size();j++){
            groupItem = groups.get(j);
            client_set = getClientsForGroups(groupItem,filter_type);
            groupItem.clients =client_set;
            groups.set(j,groupItem);
            groupclients.put(groupItem.id,client_set);
        }
       // db.endTransaction();
      //  db.close();
      //  db = null;

        info.centers = centers;
        info.centergroups =centergroups;
        info.groups = groups;
        info.groupclients = groupclients;
        return info;
    }

    public ClientPaymentsInfo getAvailablePayments(){
        String filter_type = Constants.FILTER_FOR_PAYMENT;
        ClientPaymentsInfo info = new ClientPaymentsInfo();
        db = this.getReadableDatabase();
       // db.beginTransaction();
        ArrayList<CenterItem> centers = getAvailableCenters(filter_type);
        ArrayList<GroupItem> groups = new ArrayList<GroupItem>();
        ArrayList<GroupItem> group_set;
        ArrayList<ClientItem> client_set;
        HashMap<String,ArrayList<GroupItem>> centergroups = new HashMap<String, ArrayList<GroupItem>>();
        HashMap<String,ArrayList<ClientItem>> groupclients = new HashMap<String, ArrayList<ClientItem>>();
        GroupItem groupItem;
        for(int i=0; i<centers.size();i++){
            group_set = getGroupsForCenter(centers.get(i),filter_type);
            groups.addAll(group_set);
            centergroups.put(centers.get(i).id,group_set);
        }
        for(int j=0; j<groups.size();j++){
            groupItem = groups.get(j);
            client_set = getClientsForGroups(groupItem,filter_type);
            groupItem.clients =client_set;
            groupItem.total_def = getTotalExpectedPaymentForGroup(groupItem.id);
            groups.set(j,groupItem);
            groupclients.put(groupItem.id,client_set);
        }
        //db.endTransaction();
       // db.close();
        //db = null;

        info.centers = centers;
        info.centergroups =centergroups;
        info.groups = groups;
        info.groupclients = groupclients;
        return info;
    }

    public Double getTotalExpectedPaymentForGroup(String group_id){
        double total = 0.00;
        String countQuery = "SELECT SUM("
                +TableRecievedLoans.LOAN_DEFAULT+") AS sumtotal"
                +" FROM " + TableRecievedLoans.TABLE_NAME+"  WHERE "+TableRecievedLoans.LOAN_GROUP_ID+" =?  AND "+TableRecievedLoans.MARKED_PAYMENT+" =?";
        Cursor cursor = db.rawQuery(countQuery, new String[]{group_id,Constants.SYCED_NOT});
        if(cursor.moveToFirst()){
            total = cursor.getDouble(0);
        }
        return total;
    }

    public ArrayList<ClientItem> getClientsForGroups(GroupItem group,String filter_type){
        ArrayList<ClientItem> clients = new ArrayList<ClientItem>();
        ClientItem client;
        String countQuery;
        if(filter_type.equals(Constants.FILTER_FOR_PAYMENT)) {
            countQuery = "SELECT "
                    + TableRecievedLoans.LOAN_CLIENT_ID + ","
                    + TableRecievedLoans.LOAN_CLIENT_NAME + ","
                    + TableRecievedLoans.LOAN_EXTERNALID + ","
                    + TableRecievedLoans.LOAN_ID + ","
                    + TableRecievedLoans.LOAN_NAME + ","
                    + TableRecievedLoans.LOAN_DEFAULT + ","
                    + TableRecievedLoans.LOAN_RENTAL + ","
                    + TableRecievedLoans.LOAN_ARREARS + ","
                    + TableRecievedLoans.LOAN_OUT_BALANCE
                    + " FROM " + TableRecievedLoans.TABLE_NAME + " WHERE " + TableRecievedLoans.LOAN_GROUP_ID + " =? AND " + TableRecievedLoans.LOAN_TYPE + " =? AND " + TableRecievedLoans.MARKED_PAYMENT + " =?";
        }
        else{
            countQuery = "SELECT "
                    + TableRecievedLoans.LOAN_CLIENT_ID + ","
                    + TableRecievedLoans.LOAN_CLIENT_NAME + ","
                    + TableRecievedLoans.LOAN_EXTERNALID + ","
                    + TableRecievedLoans.LOAN_ID + ","
                    + TableRecievedLoans.LOAN_NAME + ","
                    + TableRecievedLoans.LOAN_DEFAULT + ","
                    + TableRecievedLoans.LOAN_RENTAL + ","
                    + TableRecievedLoans.LOAN_ARREARS + ","
                    + TableRecievedLoans.LOAN_OUT_BALANCE
                    + " FROM " + TableRecievedLoans.TABLE_NAME + " WHERE " + TableRecievedLoans.LOAN_GROUP_ID + " =? AND " + TableRecievedLoans.LOAN_TYPE + " =? AND " + TableRecievedLoans.MARKED_ATTENDANCE + " =?";

        }
            Cursor cursor = db.rawQuery(countQuery,new String[]{group.id, Constants.LOAN__TYPE_GROUP,Constants.SYCED_NOT});
        //cursor.moveToFirst();
        if(cursor.moveToFirst()){
            do {
            client = new ClientItem();
            client.id = cursor.getString(0);
            client.name = cursor.getString(1);
            client.externalid = cursor.getString(2);
            client.loanid = cursor.getString(3);
            client.loanname = cursor.getString(4);
            client.def = cursor.getString(5);
            client.rental = cursor.getString(6);
            client.arrears = cursor.getString(7);
            client.outstanding = cursor.getString(8);

            clients.add(client);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return clients;
    }

    public ArrayList<GroupItem> getGroupsForCenter(CenterItem center,String filter_type){
        ArrayList<GroupItem> groups = new ArrayList<GroupItem>();
        GroupItem group;
        String countQuery;
        if(filter_type.equals(Constants.FILTER_FOR_PAYMENT)) {
            countQuery = "SELECT DISTINCT "
                    + TableRecievedLoans.LOAN_GROUP_ID + ","
                    + TableRecievedLoans.LOAN_GROUP_NAME
                    + " FROM " + TableRecievedLoans.TABLE_NAME + " WHERE " + TableRecievedLoans.LOAN_CENTER_ID + " =? AND " + TableRecievedLoans.LOAN_TYPE + " =? AND " + TableRecievedLoans.MARKED_PAYMENT + " =? ORDER BY "+ TableRecievedLoans.LOAN_GROUP_NAME +" ASC";
        }
        else{
            countQuery = "SELECT DISTINCT "
                    + TableRecievedLoans.LOAN_GROUP_ID + ","
                    + TableRecievedLoans.LOAN_GROUP_NAME
                    + " FROM " + TableRecievedLoans.TABLE_NAME + " WHERE " + TableRecievedLoans.LOAN_CENTER_ID + " =? AND " + TableRecievedLoans.LOAN_TYPE + " =? AND " + TableRecievedLoans.MARKED_ATTENDANCE + " =? ORDER BY "+ TableRecievedLoans.LOAN_GROUP_NAME +" ASC";
        }
            Cursor cursor = db.rawQuery(countQuery,new String[]{center.id, Constants.LOAN__TYPE_GROUP,Constants.SYCED_NOT});
        //cursor.moveToFirst();
        if(cursor.moveToFirst()){
            do {
            group = new GroupItem();
            group.id = cursor.getString(0);
            group.name = cursor.getString(1);
            groups.add(group);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return groups;
    }

    public ArrayList<ClientItem> getAttendaceMarkedClientsForGroup(String groupid){
        ArrayList<ClientItem> clients = new ArrayList<ClientItem>();
        String countQuery;
        ClientItem client;

        countQuery = "SELECT "
                    + TableAttendanceTransactions.ATTENDANCE_CLIENT_ID + ","
                    + TableAttendanceTransactions.ATTENDANCE_ATTEND
                    + " FROM " + TableAttendanceTransactions.TABLE_NAME + " WHERE " + TableAttendanceTransactions.ATTENDANCE_GROUP_ID + " =?";

        Cursor cursor = db.rawQuery(countQuery,new String[]{groupid});
        //Cursor cursor =  db.query(TableRecievedLoans.TABLE_NAME,new String[]{TableRecievedLoans.LOAN_CENTER_ID,TableRecievedLoans.LOAN_CENTER_NAME}, null,null,null,null,null,null);
        Log.e("FLI CUR COUNT",""+cursor.getCount());
        //  cursor.moveToFirst();
        if(cursor.moveToFirst()){
            Log.e("FLI CUR",""+cursor.getString(0));
            do {
                client = new ClientItem();
                client.id = cursor.getString(0);
                client.attendancetype = Integer.parseInt(cursor.getString(1));
                clients.add(client);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return clients;
    }

    public ArrayList<CenterItem> getAvailableCenters(String filter_type){
        ArrayList<CenterItem> centers = new ArrayList<CenterItem>();
        String countQuery;
        CenterItem center;
        if(filter_type.equals(Constants.FILTER_FOR_PAYMENT)) {
             countQuery = "SELECT DISTINCT "
                    + TableRecievedLoans.LOAN_CENTER_ID + ","
                    + TableRecievedLoans.LOAN_CENTER_NAME
                    + " FROM " + TableRecievedLoans.TABLE_NAME + " WHERE " + TableRecievedLoans.MARKED_PAYMENT + " =? ";
        }
        else{
             countQuery = "SELECT DISTINCT "
                    + TableRecievedLoans.LOAN_CENTER_ID + ","
                    + TableRecievedLoans.LOAN_CENTER_NAME
                    + " FROM " + TableRecievedLoans.TABLE_NAME + " WHERE " + TableRecievedLoans.MARKED_ATTENDANCE + " =?";
        }
        Cursor cursor = db.rawQuery(countQuery,new String[]{Constants.SYCED_NOT});
        //Cursor cursor =  db.query(TableRecievedLoans.TABLE_NAME,new String[]{TableRecievedLoans.LOAN_CENTER_ID,TableRecievedLoans.LOAN_CENTER_NAME}, null,null,null,null,null,null);
       Log.e("FLI CUR COUNT",""+cursor.getCount());
      //  cursor.moveToFirst();
        if(cursor.moveToFirst()){
            Log.e("FLI CUR",""+cursor.getString(0));
            do {
                center = new CenterItem();
                center.id = cursor.getString(0);
                center.name = cursor.getString(1);
                centers.add(center);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return centers;
    }




    public ArrayList<MarkedAttendace> getAttendaceMarkedGroups(){
        ArrayList<MarkedAttendace> attendaces = new ArrayList<MarkedAttendace>();
        String countQuery;
        MarkedAttendace attendace;
        countQuery = "SELECT DISTINCT "
                    + TableAttendanceTransactions.ATTENDANCE_GROUP_ID + ","
                    + TableAttendanceTransactions.ATTENDANCE_CENTER_ID+ ","
                    + TableAttendanceTransactions.ATTENDANCE_TIME
                    + " FROM " + TableAttendanceTransactions.TABLE_NAME+ " WHERE "+TableAttendanceTransactions.ATTENDANCE_SYNCED+" = ?";

        Cursor cursor = db.rawQuery(countQuery,new String[]{Constants.SYCED_NOT});
        //Cursor cursor =  db.query(TableRecievedLoans.TABLE_NAME,new String[]{TableRecievedLoans.LOAN_CENTER_ID,TableRecievedLoans.LOAN_CENTER_NAME}, null,null,null,null,null,null);
        Log.e("FLI CUR COUNT",""+cursor.getCount());
        //  cursor.moveToFirst();
        if(cursor.moveToFirst()){
            Log.e("FLI CUR",""+cursor.getString(0));
            do {
                attendace = new MarkedAttendace();
                attendace.group_id = cursor.getString(0);
                attendace.center_id = cursor.getString(1);
                attendace.meeting_date  =cursor.getString(2);
                attendaces.add(attendace);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return attendaces;
    }

    public RecievedLoan getSavedAttendace(){
        RecievedLoan loan =null;
        db = this.getReadableDatabase();
//        String countQuery = "SELECT "
//                +TableAttendanceTransactions.ATTENDANCE_CLIENT_ID+","
//                +TableAttendanceTransactions.ATTENDANCE_ATTEND
//                +" FROM " + TableAttendanceTransactions.TABLE_NAME;
        String countQuery = "SELECT "
                +TableRecievedLoans.LOAN_ID+","
                +TableRecievedLoans.LOAN_NAME
                +" FROM " + TableRecievedLoans.TABLE_NAME+" WHERE "+TableRecievedLoans.MARKED_ATTENDANCE+"=?";
        Cursor cursor = db.rawQuery(countQuery,new String[]{Constants.SYCED_YES});
        if(cursor.moveToFirst()){
            do {
                Log.e("FLI ATT", cursor.getString(0));

            }while (cursor.moveToNext());
        }
        cursor.close();
        //db.endTransaction();
        // db.close();
      //  db = null;
        if(loan != null)
            Log.e("FLI LOAN",loan.toString());
        return loan;
    }

    public String getCenterNameforId(String center_id){
        String center_name = "";
        String countQuery = "SELECT "
                +TableRecievedLoans.LOAN_CENTER_NAME+""
                +" FROM " + TableRecievedLoans.TABLE_NAME+"  WHERE "+TableRecievedLoans.LOAN_CENTER_ID+" =?";
        Cursor cursor = db.rawQuery(countQuery, new String[]{center_id});
        if(cursor.moveToFirst()){
            center_name = cursor.getString(0);
        }
        return center_name;
    }

    public int getPendingSyncCount(String center_id){
        int count = 0;
        String countQuery = "SELECT COUNT("
                +TableLoanTransactions.DEPOSIT_LOAN_ID+") AS pendingsynccount"
                +" FROM " + TableLoanTransactions.TABLE_NAME+"  WHERE "+TableLoanTransactions.DEPOSIT_CENTER_ID+" =?  AND "+TableLoanTransactions.DEPOSIT_SYNCED+" =?";
        Cursor cursor = db.rawQuery(countQuery, new String[]{center_id,Constants.SYCED_NOT});
        if(cursor.moveToFirst()){
            count = cursor.getInt(0);
        }
        return count;
    }

    public int getTotalPendingSyncCount(){
        int count = 0;
        db = this.getReadableDatabase();
        String countQuery = "SELECT COUNT("
                +TableLoanTransactions.DEPOSIT_LOAN_ID+") AS pendingsynccount"
                +" FROM " + TableLoanTransactions.TABLE_NAME+"  WHERE "+TableLoanTransactions.DEPOSIT_SYNCED+" = ? ";
                Cursor cursor = db.rawQuery(countQuery, new String[]{Constants.SYCED_NOT});
        if(cursor.moveToFirst()){
            count = cursor.getInt(0);
        }
       // db.close();
        return count;
    }

    public ArrayList<CenterItem> getCollectionSheet(){
        ArrayList<CenterItem>  centers = new ArrayList<CenterItem>();
        CenterItem center;

        db = this.getReadableDatabase();
        String countQuery = "SELECT COUNT ("
                +TableLoanTransactions.DEPOSIT_LOAN_ID+") AS loancount, SUM ("
                +TableLoanTransactions.DEPOSIT_AMOUNT+") AS loanstotal, "
                +TableLoanTransactions.DEPOSIT_CENTER_ID+""
                +" FROM " + TableLoanTransactions.TABLE_NAME+"  GROUP BY "+TableLoanTransactions.DEPOSIT_CENTER_ID; // WHERE "+TableLoanTransactions.DEPOSIT_CENTER_ID+" =?
        Cursor cursor = db.rawQuery(countQuery, null ); //new String[]{""}
        if(cursor.moveToFirst()){
            do {
                center = new CenterItem();
                center.no_of_loans = cursor.getInt(0);
                center.total_collection = cursor.getDouble(1);
                center.id = cursor.getString(2);
                center.name = getCenterNameforId(center.id);
                center.pending_sync_count = getPendingSyncCount(center.id);
                centers.add(center);
            } while (cursor.moveToNext());
        }
        cursor.close();
       // db.close();
      //  db = null;
        return centers;
    }

    public RecievedLoan getDetailsforLoanNumber(String loannumber){
        RecievedLoan loan =null;
        db = this.getReadableDatabase();
        String countQuery = "SELECT "
                +TableRecievedLoans.LOAN_ID+","
                +TableRecievedLoans.LOAN_NAME+","
                +TableRecievedLoans.LOAN_CLIENT_ID+","
                +TableRecievedLoans.LOAN_CLIENT_NAME+","
                +TableRecievedLoans.LOAN_GROUP_ID+","
                +TableRecievedLoans.LOAN_GROUP_NAME+","
                +TableRecievedLoans.LOAN_CENTER_ID+","
                +TableRecievedLoans.LOAN_CENTER_NAME+","
                +TableRecievedLoans.LOAN_OUT_BALANCE+","
                +TableRecievedLoans.LOAN_TOTAL_BALANCE+","
                +TableRecievedLoans.LOAN_RENTAL+","
                +TableRecievedLoans.LOAN_DEFAULT+","
                +TableRecievedLoans.LOAN_ARREARS+","
                +TableRecievedLoans.LOAN_EXTERNALID+","
                +TableRecievedLoans.LOAN_ACCOUNTNO+","
                +TableRecievedLoans.LOAN_TYPE
                +" FROM " + TableRecievedLoans.TABLE_NAME+" WHERE "+TableRecievedLoans.LOAN_ACCOUNTNO+" =?";
        Cursor cursor = db.rawQuery(countQuery, new String[]{loannumber});
        if(cursor.moveToFirst()){
            Log.e("FLI CURSOR LOAN",cursor.toString());
            loan = new RecievedLoan();
            loan.loan_id = cursor.getString(0);
            loan.loan_name = cursor.getString(1);
            loan.client_id = cursor.getString(2);
            loan.client_name = cursor.getString(3);
            loan.group_id = cursor.getString(4);
            loan.group_name = cursor.getString(5);
            loan.center_id = cursor.getString(6);
            loan.center_name = cursor.getString(7);
            loan.outstanding_balance = cursor.getString(8);
            loan.total_balance = cursor.getString(9);
            loan.rental = cursor.getString(10);
            loan.def = cursor.getString(11);
            loan.arrears = cursor.getString(12);
            loan.loan_externalid = cursor.getString(13);
            loan.loan_accountno = cursor.getString(14);
            loan.type = cursor.getString(15);
            Log.e("FLI CURSOR EXT ID",loan.loan_externalid);
        }
        cursor.close();
       // db.close();
       // db = null;
        if(loan != null)
        Log.e("FLI LOAN",loan.toString());
        return loan;
    }

    public ArrayList<RecievedLoan> getDetailsforLoanExID(String loanid){
        ArrayList<RecievedLoan> loanList = new ArrayList<RecievedLoan>();
        RecievedLoan loan =null;
        db = this.getReadableDatabase();
        String countQuery = "SELECT "
                +TableRecievedLoans.LOAN_ID+","
                +TableRecievedLoans.LOAN_NAME+","
                +TableRecievedLoans.LOAN_CLIENT_ID+","
                +TableRecievedLoans.LOAN_CLIENT_NAME+","
                +TableRecievedLoans.LOAN_GROUP_ID+","
                +TableRecievedLoans.LOAN_GROUP_NAME+","
                +TableRecievedLoans.LOAN_CENTER_ID+","
                +TableRecievedLoans.LOAN_CENTER_NAME+","
                +TableRecievedLoans.LOAN_OUT_BALANCE+","
                +TableRecievedLoans.LOAN_TOTAL_BALANCE+","
                +TableRecievedLoans.LOAN_RENTAL+","
                +TableRecievedLoans.LOAN_DEFAULT+","
                +TableRecievedLoans.LOAN_ARREARS+","
                +TableRecievedLoans.LOAN_EXTERNALID+","
                +TableRecievedLoans.LOAN_ACCOUNTNO+","
                +TableRecievedLoans.LOAN_TYPE
                +" FROM " + TableRecievedLoans.TABLE_NAME+" WHERE "+TableRecievedLoans.LOAN_EXTERNALID+" =? OR " +TableRecievedLoans.LOAN_ACCOUNTNO+ "=?";//LIKE
        Cursor cursor = db.rawQuery(countQuery, new String[]{loanid, loanid});//Cursor cursor = db.rawQuery(countQuery, new String[]{loanid+"%"});
        if(cursor.moveToFirst()){
            while (!cursor.isAfterLast()) {
                Log.e("FLI CURSOR LOAN",cursor.toString());
                loan = new RecievedLoan();
                loan.loan_id = cursor.getString(0);
                loan.loan_name = cursor.getString(1);
                loan.client_id = cursor.getString(2);
                loan.client_name = cursor.getString(3);
                loan.group_id = cursor.getString(4);
                loan.group_name = cursor.getString(5);
                loan.center_id = cursor.getString(6);
                loan.center_name = cursor.getString(7);
                loan.outstanding_balance = cursor.getString(8);
                loan.total_balance = cursor.getString(9);
                loan.rental = cursor.getString(10);
                loan.def = cursor.getString(11);
                loan.arrears = cursor.getString(12);
                loan.loan_externalid = cursor.getString(13);
                loan.loan_accountno = cursor.getString(14);
                loan.type = cursor.getString(15);
                loanList.add(loan);
                Log.e("FLI CURSOR EXT ID",loan.loan_externalid);

                cursor.moveToNext();
            }
        }
        cursor.close();
        // db.close();
        // db = null;
        if(!loanList.isEmpty())
            Log.e("FLI LOAN",loanList.toString());
        return loanList;
    }

    public void updateLoanForPaymentLoan(String loanid){
        ContentValues values = new ContentValues();
        db = this.getWritableDatabase();
        db.beginTransaction();
        values.put(TableRecievedLoans.MARKED_PAYMENT, Constants.SYCED_YES);
        db.update(TableRecievedLoans.TABLE_NAME, values, TableRecievedLoans.LOAN_ID + " = ?",
                new String[]{loanid});
        db.setTransactionSuccessful();
        db.endTransaction();
        //  db.close();
//        db =null;
    }

    public void updateLoanForAttedance(String groupdid){
        ContentValues values = new ContentValues();
        db.beginTransaction();
        values.put(TableRecievedLoans.MARKED_ATTENDANCE, Constants.SYCED_YES);
        db.update(TableRecievedLoans.TABLE_NAME, values, TableRecievedLoans.LOAN_GROUP_ID + " = ?",
                new String[]{groupdid});
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public void updateLoanForGroupPayment(String groupdid){
        ContentValues values = new ContentValues();
        db.beginTransaction();
        values.put(TableRecievedLoans.MARKED_PAYMENT, Constants.SYCED_YES);
        db.update(TableRecievedLoans.TABLE_NAME, values, TableRecievedLoans.LOAN_GROUP_ID + " = ?",
                new String[]{groupdid});
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public void saveGroupPaymennts(GroupPaymentItem payment) {
        db = this.getWritableDatabase();
        db.beginTransaction();
        ContentValues values;
        ClientItem item;
        ArrayList<ClientItem> clients = payment.clients;
        for (int i = 0; i < clients.size(); i++) {
            values = new ContentValues();
            item = clients.get(i);
            values.put(TableLoanTransactions.DEPOSIT_CLIENT_ID, item.id);
            values.put(TableLoanTransactions.DEPOSIT_CENTER_ID, payment.center_id);
            values.put(TableLoanTransactions.DEPOSIT_GROUP_ID, payment.group_id);
            values.put(TableLoanTransactions.DEPOSIT_LOAN_ID, item.loanid);
            values.put(TableLoanTransactions.DEPOSIT_AMOUNT, Double.parseDouble(item.def));
            values.put(TableLoanTransactions.DEPOSIT_CHEQUE_NUMBER, "");
            values.put(TableLoanTransactions.DEPOSIT_BANK_NUMBER, "");
            values.put(TableLoanTransactions.DEPOSIT_PAY_DEFAULT,0.00);
            values.put(TableLoanTransactions.DEPOSIT_TIME, payment.pay_date);
            values.put(TableLoanTransactions.DEPOSIT_NOTE, "");
            values.put(TableLoanTransactions.DEPOSIT_PAY_TYPE, Constants.PAYMENT_TYPE_ID_CASH);
            db.insert(TableLoanTransactions.TABLE_NAME, null, values);
        }

        db.setTransactionSuccessful();
        db.endTransaction();
//        if (clients.size() > 0)
//            updateLoanForGroupPayment(payment.group_id);


        //db.close();
        //db = null;
    }

    public void saveMarkedAttendance(MarkedAttendace markedattendance){
        db = this.getWritableDatabase();
        db.beginTransaction();
        ContentValues values;
        ClientItem clientItem;
        ArrayList<ClientItem> items = markedattendance.clients;
        Log.e("FLI CLIENTS",items.toString());
        Log.e("FLI Date",markedattendance.meeting_date);
        Log.e("FLI GROUPID",markedattendance.group_id);
        Log.e("FLI CENTERID",markedattendance.center_id);
        for(int i=0; i< items.size();i++) {
            values = new ContentValues();
            clientItem = items.get(i);

            values.put(TableAttendanceTransactions.ATTENDANCE_ATTEND,String.valueOf(clientItem.attendancetype));
            values.put(TableAttendanceTransactions.ATTENDANCE_CLIENT_ID,clientItem.id);
            values.put(TableAttendanceTransactions.ATTENDANCE_CENTER_ID,markedattendance.center_id);
            values.put(TableAttendanceTransactions.ATTENDANCE_GROUP_ID,markedattendance.group_id);
            values.put(TableAttendanceTransactions.ATTENDANCE_SYNCED,Constants.SYCED_NOT);
            values.put(TableAttendanceTransactions.ATTENDANCE_TIME,markedattendance.meeting_date);
            db.insert(TableAttendanceTransactions.TABLE_NAME,null,values);

        }

        db.setTransactionSuccessful();
        db.endTransaction();
        updateLoanForAttedance(markedattendance.group_id);
       // db.close();
       // db = null;
    }

    public void saveRecievedLoans(ArrayList<RecievedLoan> recievedloans){
        db = this.getWritableDatabase();
        db.beginTransaction();
        ContentValues values;
        RecievedLoan loan;
        for(int i=0; i< recievedloans.size();i++){
            values = new ContentValues();
            loan=  recievedloans.get(i);
            Log.e("Recieved Loan: ",loan.loan_id);
            values.put(TableRecievedLoans.LOAN_ID,loan.loan_id);
            values.put(TableRecievedLoans.LOAN_NAME,loan.loan_name);
            values.put(TableRecievedLoans.LOAN_ACCOUNTNO,loan.loan_accountno);
            values.put(TableRecievedLoans.LOAN_EXTERNALID,loan.loan_externalid);
            //values.put(TableRecievedLoans.LOAN_AMOUNT,loan.amount);
            values.put(TableRecievedLoans.LOAN_CENTER_ID,loan.center_id);
            values.put(TableRecievedLoans.LOAN_CENTER_NAME,loan.center_name);
            values.put(TableRecievedLoans.LOAN_GROUP_ID,loan.group_id);
            values.put(TableRecievedLoans.LOAN_GROUP_NAME,loan.group_name);
            values.put(TableRecievedLoans.LOAN_CLIENT_ID,loan.client_id);
            values.put(TableRecievedLoans.LOAN_CLIENT_NAME,loan.client_name);
            values.put(TableRecievedLoans.LOAN_TOTAL_BALANCE,loan.total_balance);
            values.put(TableRecievedLoans.LOAN_OUT_BALANCE,loan.outstanding_balance);
            values.put(TableRecievedLoans.LOAN_ARREARS,loan.arrears);
            values.put(TableRecievedLoans.LOAN_DEFAULT,loan.def);
            values.put(TableRecievedLoans.LOAN_RENTAL,loan.rental);
            values.put(TableRecievedLoans.LOAN_TYPE,loan.type);
            db.insert(TableRecievedLoans.TABLE_NAME,null,values);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
      //  db.close();
     //   db = null;
    }

    public ArrayList<PayeeItem> getPayedLoans(){
        ArrayList<PayeeItem> payedloans = new ArrayList<PayeeItem>();
        PayeeItem loan;

        db = this.getReadableDatabase();
        String countQuery;
        countQuery = "SELECT "
                + TableLoanTransactions.DEPOSIT_CLIENT_ID + ","
                + TableLoanTransactions.DEPOSIT_LOAN_ID+ ","
                + TableLoanTransactions.DEPOSIT_CENTER_ID+","
                + TableLoanTransactions.DEPOSIT_PAY_TYPE + ","
                + TableLoanTransactions.DEPOSIT_CHEQUE_NUMBER+ ","
                + TableLoanTransactions.DEPOSIT_BANK_NUMBER+","
              //  + TableLoanTransactions.DEPOSIT_PAY_DEFAULT+","
                + TableLoanTransactions.DEPOSIT_AMOUNT + ","
                + TableLoanTransactions.DEPOSIT_NOTE+ ","
                + TableLoanTransactions.DEPOSIT_TIME
                + " FROM " + TableLoanTransactions.TABLE_NAME+" WHERE "+TableLoanTransactions.DEPOSIT_SYNCED+" = ?";
        Cursor cursor = db.rawQuery(countQuery,new String[]{Constants.SYCED_NOT});
        //Cursor cursor =  db.query(TableRecievedLoans.TABLE_NAME,new String[]{TableRecievedLoans.LOAN_CENTER_ID,TableRecievedLoans.LOAN_CENTER_NAME}, null,null,null,null,null,null);
        Log.e("FLI CUR COUNT",""+cursor.getCount());
        //  cursor.moveToFirst();
        if(cursor.moveToFirst()){
            Log.e("FLI CUR",""+cursor.getString(0));
            do {
                loan = new PayeeItem();
                loan.client_id = cursor.getString(0);
                loan.loan_id = cursor.getString(1);
                loan.center_id  =cursor.getString(2);
                loan.payment_type_id = cursor.getString(3);
                loan.checque_no = cursor.getString(4);
                loan.bank_no  =cursor.getString(5);
                loan.amount = String.valueOf(cursor.getDouble(6));
                loan.note  =cursor.getString(7);
                loan.transaction_date  =cursor.getString(8);
                payedloans.add(loan);
            }while (cursor.moveToNext());
        }
        cursor.close();
        // db.endTransaction();
       // db.close();
       // db = null;

        return payedloans;
    }

    public ArrayList<MarkedAttendace>getMarkedAttendance(){
        ArrayList<MarkedAttendace> markedAttendaces = new ArrayList<MarkedAttendace>();
        MarkedAttendace mattendance;
       // if( db ==null ||!db.isOpen()) {
            db = this.getReadableDatabase();
            //db.beginTransaction();
            markedAttendaces = getAttendaceMarkedGroups();
            for (int i = 0; i < markedAttendaces.size(); i++) {
                mattendance = markedAttendaces.get(i);
                Log.e("FLI AT",mattendance.toString());
                mattendance.clients = getAttendaceMarkedClientsForGroup(mattendance.group_id);
            }
           // db.endTransaction();
          //  db.close();
          //  db = null;
       // }
        return  markedAttendaces;
    }

    public void deleteAttendanceForGroupId(String groupid){
        db = this.getWritableDatabase();
        db.beginTransaction();

        db.delete(TableAttendanceTransactions.TABLE_NAME, TableAttendanceTransactions.ATTENDANCE_GROUP_ID + " = ?",
                new String[] { groupid });
        db.endTransaction();
       // db.close();
      //  db = null;
    }

    public void updateSyncedPayment(String loanid){
        ContentValues values = new ContentValues();
        db.beginTransaction();
        values.put(TableLoanTransactions.DEPOSIT_SYNCED, Constants.SYCED_YES);
        db.update(TableLoanTransactions.TABLE_NAME, values, TableLoanTransactions.DEPOSIT_LOAN_ID + " = ?",
                new String[]{loanid});
        db.setTransactionSuccessful();
        db.endTransaction();
      //  db = null;
    }

    public void savePayment(PayeeItem item){
        db = this.getWritableDatabase();
        db.beginTransaction();
        ContentValues  values = new ContentValues();
        values.put(TableLoanTransactions.DEPOSIT_CLIENT_ID,item.client_id);
        values.put(TableLoanTransactions.DEPOSIT_CENTER_ID,item.center_id);
        values.put(TableLoanTransactions.DEPOSIT_GROUP_ID,item.group_id);
        values.put(TableLoanTransactions.DEPOSIT_LOAN_ID,item.loan_id);
        values.put(TableLoanTransactions.DEPOSIT_AMOUNT,Double.parseDouble(item.amount));
        values.put(TableLoanTransactions.DEPOSIT_CHEQUE_NUMBER,item.checque_no);
        values.put(TableLoanTransactions.DEPOSIT_BANK_NUMBER,item.bank_no);
        values.put(TableLoanTransactions.DEPOSIT_PAY_DEFAULT,0.00);
        values.put(TableLoanTransactions.DEPOSIT_TIME,item.transaction_date);
        values.put(TableLoanTransactions.DEPOSIT_NOTE,item.note);
        values.put(TableLoanTransactions.DEPOSIT_PAY_TYPE,item.payment_type_id);
        db.insert(TableLoanTransactions.TABLE_NAME,null,values);
        db.setTransactionSuccessful();
        db.endTransaction();
        updateLoanForPaymentLoan(item.loan_id);
       // db.close();
        //db = null;
    }




    @Override
    public void onCreate(SQLiteDatabase db) {
        TableAttendanceTransactions.onCreate(db);
        TableLoanTransactions.onCreate(db);
        TableRecievedLoans.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        TableAttendanceTransactions.droptable(db);
        TableLoanTransactions.droptable(db);
        TableRecievedLoans.droptable(db);
        onCreate(db);
    }




}
