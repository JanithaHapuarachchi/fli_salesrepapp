package com.fli.salesagentapp.fliagentapp.utils;

import android.content.Context;
import android.util.Log;

import com.fli.salesagentapp.fliagentapp.data.ClientItem;
import com.fli.salesagentapp.fliagentapp.data.MarkedAttendace;
import com.fli.salesagentapp.fliagentapp.data.PayeeItem;
import com.fli.salesagentapp.fliagentapp.data.RecievedLoan;
import com.fli.salesagentapp.fliagentapp.data.ResObject;
import com.fli.salesagentapp.fliagentapp.db.DBOperations;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by janithamh on 8/12/18.
 */

public class WSCalls {

    Context context;
    DataManager dtManager;

    public WSCalls(Context context){
        this.context =context;
        dtManager = new DataManager(context);
    }

    public ResObject autherise_user(String username, String password) {

        ResObject res_object = new ResObject();
        String response;
        String request  =  Constants.AUTHENTICATION_URL+"?username="+username+"&password="+password;
        try {
            if(Utility.isConnected(context)) {
                response = RequestHandler.sendPost(new JSONObject(), request, context);
                res_object.validity = Constants.VALIDITY_SUCCESS;
                res_object.msg = response;
            }
            else{
                res_object.validity = Constants.VALIDITY_FAILED;
                res_object.msg = Constants.NETWORK_NOT_FOUND;
            }

        } catch (Exception e) {
            Log.e("FLI E AUTH",e.getMessage());
            res_object.validity = Constants.VALIDITY_FAILED;
            res_object.msg = "Login Failed";
          // res_object.msg = e.getMessage();
        }
        return res_object;
    }

    public RecievedLoan get_loan_associations(RecievedLoan loan) throws Exception{
        int periodCheckNo =1;
        double rental = 0.00;
        String request,response ;
        request = Constants.LOANS_URL + "/" + loan.loan_id+Constants.ALL_ASSOCIATIONS ;
        response = RequestHandler.sendGet(request, context);
        JSONObject resJSON,scheduleJSON;
        JSONArray periodsJSON,transactionJSON;
        resJSON = new JSONObject(response);
        scheduleJSON = resJSON.getJSONObject("repaymentSchedule");
        periodsJSON = scheduleJSON.getJSONArray("periods");
        transactionJSON = resJSON.getJSONArray("transactions");
        rental= periodsJSON.getJSONObject(periodCheckNo).getDouble("principalDue")+periodsJSON.getJSONObject(periodCheckNo).getDouble("interestDue");
        loan.rental = String.valueOf(rental);
        if(transactionJSON ==null || transactionJSON.length()==0){
            loan.def =  String.valueOf(rental);
        }
        else{
            loan.def =  String.valueOf(transactionJSON.getJSONObject(transactionJSON.length()-1).getDouble("amount"));
        }
        return loan;
    }

    public ResObject sync_loans(){
        ResObject res_object = new ResObject();
        ArrayList <JSONObject> loans = new ArrayList<JSONObject>();
        ArrayList <RecievedLoan> res_loans_set;
        ArrayList <RecievedLoan> res_loans = new ArrayList<RecievedLoan>();
        RecievedLoan resLoan;
        long loopcount = 1;
        String request,response ;
        JSONObject resJSON,arrayObject;
        JSONArray resArray;
        long limit =800;
        long offset = 0;
        long totalresults;
        String urlencodestrring;
        try {
            if(Utility.isConnected(context)) {
                urlencodestrring = URLEncoder.encode(Constants.ACTIVE_USER_URL + Utility.getStaffID(context) + Constants.ACTIVE_LOANS_URL, "UTF-8");
                for (long i = 0; i < loopcount; i++) {
                    Log.e("FLI loopcount i", "" + loopcount + " : " + i);
                    request = Constants.LOANS_URL + "?" + "offset=" + offset + "&limit=" + limit + "&" + Constants.SQL_SEARCH + urlencodestrring;
                    try {
                        response = RequestHandler.sendGet(request, context);
                        resJSON = new JSONObject(response);
                        // resLoan = new RecievedLoan();
                        resArray = resJSON.getJSONArray("pageItems");
                        res_loans_set = getLoansFromArray(resArray);
                        res_loans.addAll(res_loans_set);
                        //loans.addAll(Utility.getArrayListformJSONARRAY( resJSON.getJSONArray("pageItems")));
                        if (i == 0) {
                            totalresults = resJSON.getLong("totalFilteredRecords");
                            Log.e("FLI TOT COUNT", "" + totalresults);
                            if (totalresults > limit) {
                                loopcount = totalresults / limit;
                                Log.e("FLI loopcount COUNT", "" + loopcount);
                                Log.e("FLI loopcount COUNT 2", "" + totalresults % limit);
                                if ((totalresults % limit) != 0) {
                                    loopcount++;
                                }
                                //loopcount--; // for default loop
                            }
                        }
                    } catch (Exception e) {
                        Utility.clearCurrentUserLoginDate(context);
                        Log.e("FLI E LOANS", e.getMessage());
                        res_object.validity = Constants.VALIDITY_FAILED;
                        res_object.msg = "Sync Loans Failed";
                        // res_object.msg = e.getMessage();
                    }

                    offset = (i + 1) * limit;
                }
            }
            else{
                res_object = new ResObject();
                res_object.validity = Constants.VALIDITY_FAILED;
                res_object.msg = Constants.NETWORK_NOT_FOUND;
                return res_object;
            }
        }
        catch (Exception e) {
            Utility.clearCurrentUserLoginDate(context);
        Log.e("FLI E2 LOANS", e.getMessage());
        res_object.validity = Constants.VALIDITY_FAILED;
        res_object.msg = e.getMessage();
        // res_object.msg = e.getMessage();
    }

        new DBOperations(context).saveRecievedLoans(res_loans);
//        Log.e("FLI LOANS",loans.toString());
        return null;
    }

    public ArrayList<RecievedLoan> getLoansFromArray(JSONArray recieved){
        JSONObject res,group,summary,loanType;
        ArrayList<RecievedLoan> resloans = new ArrayList<RecievedLoan>();
        RecievedLoan loan;
        for(int i=0; i< recieved.length();i++){
            try {
            res= recieved.getJSONObject(i);
            loan = new RecievedLoan();
            loan.loan_id = res.getString("id");
            loan.loan_name = res.getString("loanProductName");
            loan.loan_accountno =  res.getString("accountNo");
            loan.loan_externalid = res.getString("externalId");
            group = res.getJSONObject("group");
            summary = res.getJSONObject("summary");
            loanType =  res.getJSONObject("loanType");
            loan.type = loanType.getString("value");
                if(loan.type.equals(Constants.LOAN__TYPE_GROUP)) {
                    loan.group_id = String.valueOf(group.getLong("id"));
                    loan.group_name = group.getString("name");
                    loan.center_id = String.valueOf(group.getLong("centerId"));
                    loan.center_name = group.getString("centerName");
                }
                else{
                    loan.group_id = "";
                    loan.group_name = "";
                    loan.center_id = "";
                    loan.center_name ="";
                }

            loan.client_id = String.valueOf(res.getLong("clientId"));
            loan.client_name = res.getString("clientName");
            loan.total_balance = String.valueOf(summary.getDouble("principalDisbursed"));
            loan.outstanding_balance = String.valueOf(summary.getDouble("totalOutstanding"));
            loan.arrears =  String.valueOf(summary.getDouble("totalOverdue"));

            loan = get_loan_associations(loan);
            resloans.add(loan);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return  resloans;
    }

    public void sync_MarkedAttendance(){
        ResObject res_object;
        String response,request;
        JSONObject jRequest,jResponse;
        JSONArray jClients;
        ArrayList<MarkedAttendace> markedAttendaces = dtManager.getMarkedAttendance();
        MarkedAttendace attendace;
        ClientItem clientItem;
        for(int i=0; i<markedAttendaces.size();i++){
            attendace =markedAttendaces.get(i);
             res_object = new ResObject();
             request  =  Constants.GROUP_URL+attendace.group_id+"/"+Constants.ATTENDANCE_URL+"?calenderId="+attendace.center_id;
            try {
                jRequest = new JSONObject();
                jClients = new JSONArray();
                Log.e("FLI SYNC GROUP",attendace.group_id);
                jRequest.put("dateFormat",Constants.DATE_FORMAT);
                jRequest.put("meetingDate",attendace.meeting_date);
                jRequest.put("calendarId",attendace.center_id);
                jRequest.put("locale","en");

                for(int j=0; j< attendace.clients.size();j++){
                    clientItem = attendace.clients.get(j);
                    jClients.put(new JSONObject().put("clientId",clientItem.id).put("attendanceType",clientItem.attendancetype));
                }
                jRequest.put("clientsAttendance",jClients);
                if(Utility.isConnected(context)) {
                    response = RequestHandler.sendServicePost(jRequest, request, context);
                    Log.e("FLI SYNC RES",response);
                    jResponse = new JSONObject(response);
                    if(Utility.isJSONKeyAvailable(jResponse,"resourceId")){
                        dtManager.deleteAttendanceForGroupId(attendace.group_id);
                    }
                   // res_object.validity = Constants.VALIDITY_SUCCESS;
                   // res_object.msg = response;
                }
                else{
                    //res_object.validity = Constants.VALIDITY_FAILED;
                   // res_object.msg = Constants.NETWORK_NOT_FOUND;
                }

            } catch (Exception e) {
                Log.e("FLI E AUTH",e.getMessage());
                //res_object.validity = Constants.VALIDITY_FAILED;
                //res_object.msg = "Login Failed";
                // res_object.msg = e.getMessage();
            }

        }
    }

    public void sync_PayedLoans(){
        String response,request;
        JSONObject jRequest,jResponse;
        ArrayList<PayeeItem> payedLoans = dtManager.getPayedLoans();
        PayeeItem loan;
        for(int i=0; i<payedLoans.size();i++){
            loan =payedLoans.get(i);

            request  =  Constants.LOANS_URL+"/"+loan.loan_id+Constants.LOAN_TRANSACTION_URL;
            try {
                jRequest = new JSONObject();
                Log.e("FLI SYNC GROUP",loan.loan_id);
                jRequest.put("dateFormat",Constants.DATE_FORMAT);
                jRequest.put("transactionDate",loan.transaction_date);
                jRequest.put("transactionAmount",loan.amount);
                jRequest.put("paymentTypeId",loan.payment_type_id);
                jRequest.put("note",loan.note);
                if(loan.payment_type_id.equals(Constants.PAYMENT_TYPE_ID_CHEQUE)){
                    jRequest.put("transactionAmount",loan.bank_no);
                    jRequest.put("paymentTypeId",loan.checque_no);
                }
                jRequest.put("locale","en");
                if(Utility.isConnected(context)) {
                    response = RequestHandler.sendServicePost(jRequest, request, context);
                    Log.e("FLI SYNC RES",response);
                    jResponse = new JSONObject(response);
                    if(Utility.isJSONKeyAvailable(jResponse,"resourceId")){
                        dtManager.updateSyncedPayment(loan.loan_id);
                    }

                }

            } catch (Exception e) {
                Log.e("FLI E AUTH",e.getMessage());
                //res_object.validity = Constants.VALIDITY_FAILED;
                //res_object.msg = "Login Failed";
                // res_object.msg = e.getMessage();
            }

        }
    }

}
