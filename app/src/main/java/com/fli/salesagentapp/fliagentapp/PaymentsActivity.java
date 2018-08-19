package com.fli.salesagentapp.fliagentapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.fli.salesagentapp.fliagentapp.adapters.IssuePaymentAdapter;
import com.fli.salesagentapp.fliagentapp.adapters.PaymentLoadCentersAdapter;
import com.fli.salesagentapp.fliagentapp.adapters.PaymentLoadGroupsAdapter;
import com.fli.salesagentapp.fliagentapp.data.CenterItem;
import com.fli.salesagentapp.fliagentapp.data.GroupItem;
import com.fli.salesagentapp.fliagentapp.data.PayeeItem;
import com.fli.salesagentapp.fliagentapp.utils.Utility;

import java.util.ArrayList;
import java.util.HashMap;

public class PaymentsActivity extends AppCompatActivity { // implements TextWatcher
    final String TAG_PAYMENT = "Payment";
    ArrayList<CenterItem> initialCenters,centers;
    HashMap<String,ArrayList<GroupItem>> centerGroups;
    HashMap<String,ArrayList<PayeeItem>> groupPayees;
    AutoCompleteTextView center_names;
    Spinner center_groups,spinner_center_names;
    Button btn_select,btn_pay;
    TextView pay_total;
    ListView center_payees;
    PaymentLoadCentersAdapter pl_center_adapter;
    PaymentLoadGroupsAdapter pl_group_adapter;
    CenterItem selected_center;
    ArrayList<GroupItem> groups;
    ArrayList<PayeeItem> payees;
    GroupItem selected_group;

    boolean init_centers =false;
    boolean init_groups =false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payments);
        center_names = (AutoCompleteTextView)findViewById(R.id.center_names);
        center_groups = (Spinner)findViewById(R.id.center_groups);
        spinner_center_names = (Spinner)findViewById(R.id.spinner_center_names);
        btn_pay = (Button) findViewById(R.id.btn_pay);
        btn_select = (Button)findViewById(R.id.btn_select);
        pay_total = (TextView)findViewById(R.id.pay_total);
        center_payees = (ListView)findViewById(R.id.center_payees);
        center_payees.setItemsCanFocus(true);
        initItems();
        Log.e("SIZE ",""+centers.size());
       // pl_center_adapter = new PaymentLoadCentersAdapter(getApplicationContext(),centers);
       // center_names.setAdapter(pl_center_adapter);
        spinner_center_names.setAdapter(new PaymentLoadCentersAdapter(getApplicationContext(),centers));
        setGroupsforCenter(null);
        center_names.setThreshold(1);
        center_names.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                search_center_name(s.toString());
            }
        });

        spinner_center_names.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e("FLI","CENTERS");
                if(init_centers) {
                    selected_center = initialCenters.get(position);
                    setGroupsforCenter(selected_center);
                    //init_centers =false;
                }
                else{
                    init_centers = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        center_names.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selected_center = centers.get(position);
               setGroupsforCenter(centers.get(position));

            }
        });

        center_groups.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e("FLI","CENTER GROUPS");
                if(init_groups) {
                    populate_group_payments(position);
                   // init_groups = false;
                }
                else{
                    init_groups = true;
                }
//                if(selected_center == null || isCenterAvailable(center_names.getText().toString())){
//                    Utility.showMessage("Select a Correct Center",PaymentsActivity.this);
//                }
//                else{
//                    populate_group_payments(position);
//                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btn_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public void calculate_total(){
        int tot =0;
        PayeeItem payee;
        for(int i=0; i< selected_group.payees.size();i++){
            payee = selected_group.payees.get(i);
            tot += Integer.parseInt(payee.payment);
        }
        pay_total.setText(""+tot);
    }

    private void populate_group_payments(int group_index){
        selected_group =groups.get(group_index);
//        IssuePaymentAdapter issuePaymentAdapter = new IssuePaymentAdapter(getApplicationContext(),selected_group.payees,pay_total);
//        center_payees.setAdapter(issuePaymentAdapter);


    }

    private boolean isCenterAvailable(String text){
        boolean isCenerFound = false;
        for(int i=0;i<centers.size();i++ ){
            if(text.toLowerCase().equals(centers.get(i).name.toLowerCase())){
                selected_center = centers.get(i);
                isCenerFound =true;
                break;
            }
        }
        return isCenerFound;
    }

    public void search_center_name(String stext){
        Log.e(TAG_PAYMENT+" text",stext);
        String retString;
        centers = new ArrayList<>();
        boolean isGroupListSet = false;
        if(stext.equals("")){
            centers =  initialCenters ;
            setGroupsforCenter(null);
        }
        else{
            for(int i=0;i<initialCenters.size();i++){
                retString = initialCenters.get(i).name;
                Log.e(TAG_PAYMENT+" res",retString);

                if(retString.toLowerCase().equals(stext.toLowerCase())){
                      center_names.setText(retString);
                      selected_center = initialCenters.get(i);
                }

                if(retString.contains(stext) || retString.contains(stext.toLowerCase()) ||retString.contains(stext.toUpperCase())){
                    Log.e(TAG_PAYMENT+" match",retString);
                    centers.add(initialCenters.get(i));
                    if(!isGroupListSet){
                       setGroupsforCenter(initialCenters.get(i));
                        isGroupListSet =true;
                    }
                }
            }
            if(!isGroupListSet){
               setGroupsforCenter(null);
            }
        }



            Log.e("SIZE ",""+centers.size());
            pl_center_adapter = new PaymentLoadCentersAdapter(PaymentsActivity.this,centers);
            center_names.setAdapter(pl_center_adapter);
    }

    public void setGroupsforCenter(CenterItem item){
        if(item == null){
            ArrayList<GroupItem>defaultgrouList = new ArrayList<GroupItem>();
            GroupItem defaultgroupItem = new GroupItem();
            defaultgroupItem.name = "Select a Center First";
            defaultgroupItem.isdefault = true;
            defaultgrouList.add(defaultgroupItem);
            groups = new ArrayList<GroupItem>();
        //    pl_group_adapter = new PaymentLoadGroupsAdapter(getApplicationContext(),defaultgrouList);
            center_groups.setAdapter(new PaymentLoadGroupsAdapter(getApplicationContext(),defaultgrouList));
        }
        else{
            groups = centerGroups.get(item.name);
           // pl_group_adapter = new PaymentLoadGroupsAdapter(getApplicationContext(),groups);
            center_groups.setAdapter(new PaymentLoadGroupsAdapter(getApplicationContext(),groups));
        }
    }

    public void initItems(){
        initialCenters = new ArrayList<CenterItem>();
        centerGroups = new HashMap<String, ArrayList<GroupItem>>();
        CenterItem center;
        GroupItem group;
        ArrayList<GroupItem> groupList;
        ArrayList<PayeeItem> payees;
        PayeeItem payee;
        for(int i=0; i< 5;i++){
            center = new CenterItem();
            groupList = new ArrayList<GroupItem>();
            center.id =""+i;
            if(i%2 == 0){
                center.name = "ABC "+(i+1);
            }
            else{
                center.name = "XYZ "+(i+1);
            }
            for(int j=0; j<3;j++){
                group = new GroupItem();
                group.id = ""+(i+1)+"_"+(j+1);
                group.name = ""+(i+1)+"_"+(j+1);
                groupList.add(group);
                payees = new ArrayList<PayeeItem>();
                for(int  k=0; k<2;k++){
                    payee = new PayeeItem();
                    payee.id ="X"+(k+1);
                    payee.name = "MR. A"+(k+1);
                    payee.payment = ""+1200;
                    payee.payment_due =""+600;
                    payees.add(payee);
                }
                group.payees =payees;

            }
            if(i==0 ){
                selected_center = center;
            }

            centerGroups.put(center.name,groupList);
            initialCenters.add(center);
        }

        centers = initialCenters;
    }

    public void recalculateTotal(String stringtotal){

    }

//    @Override
//    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//    }
//
//    @Override
//    public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//    }
//
//    @Override
//    public void afterTextChanged(Editable s) {
//
//    }
}
