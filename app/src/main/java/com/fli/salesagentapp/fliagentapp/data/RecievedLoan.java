package com.fli.salesagentapp.fliagentapp.data;

/**
 * Created by janithah on 8/17/2018.
 */

public class RecievedLoan {

    public String loan_id;
    public String loan_accountno;
    public String loan_externalid;
    public String loan_name;
    public String amount;
    public String center_name;
    public String center_id;
    public String group_id;
    public String group_name;
    public String client_id;
    public String client_name;
    public String total_balance;
    public String outstanding_balance;
    public String arrears;
    public String def;
    public String rental;
    public String type;


    @Override
    public String toString(){
        return "loan id: "+loan_id+" groupid :"+group_id+" groupname: "+group_name;
    }
}
