package com.fli.salesagentapp.fliagentapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.fli.salesagentapp.fliagentapp.adapters.AttendanceAdapter;
import com.fli.salesagentapp.fliagentapp.adapters.IssuePaymentAdapter;
import com.fli.salesagentapp.fliagentapp.adapters.PaymentLoadCentersAdapter;
import com.fli.salesagentapp.fliagentapp.adapters.PaymentLoadGroupsAdapter;
import com.fli.salesagentapp.fliagentapp.data.AttendanceItem;
import com.fli.salesagentapp.fliagentapp.data.CenterItem;
import com.fli.salesagentapp.fliagentapp.data.GroupItem;
import com.fli.salesagentapp.fliagentapp.data.PayeeItem;

import java.util.ArrayList;
import java.util.HashMap;

public class AttendanceActivity extends AppCompatActivity {
    final String TAG_ATTENDANCE = "Attendance";
    ArrayList<CenterItem> initialCenters,centers;
    HashMap<String,ArrayList<GroupItem>> centerGroups;
    HashMap<String,ArrayList<PayeeItem>> groupPayees;
    AutoCompleteTextView center_names;
    Spinner center_groups,spinner_center_names;
    Button btn_select,btn_mark_attendance;
    TextView pay_total;
    ListView center_attendances;
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
        setContentView(R.layout.activity_attendance);

        center_names = (AutoCompleteTextView)findViewById(R.id.center_names);
        center_groups = (Spinner)findViewById(R.id.center_groups);
        spinner_center_names = (Spinner)findViewById(R.id.spinner_center_names);
        btn_mark_attendance = (Button) findViewById(R.id.btn_mark_attendance);
       // btn_select = (Button)findViewById(R.id.btn_select);
       // pay_total = (TextView)findViewById(R.id.pay_total);
        center_attendances = (ListView)findViewById(R.id.center_attendances);
        initItems();
        Log.e("SIZE ",""+centers.size());
        pl_center_adapter = new PaymentLoadCentersAdapter(getApplicationContext(),centers);
        center_names.setAdapter(pl_center_adapter);
        spinner_center_names.setAdapter(pl_center_adapter);
        setGroupsforCenter(null);
        center_names.setThreshold(1);


        spinner_center_names.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(init_centers) {
                    selected_center = initialCenters.get(position);
                    setGroupsforCenter(selected_center);
                }
                else{
                    init_centers =true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        center_groups.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(init_groups) {
                    populate_group_payments(position);
                }
                else{
                    init_groups =true;
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
    }


    private void populate_group_payments(int group_index){
        selected_group =groups.get(group_index);
        AttendanceAdapter attendanceAdapter = new AttendanceAdapter(getApplicationContext(),selected_group.payees);
        center_attendances.setAdapter(attendanceAdapter);
    }

    public void setGroupsforCenter(CenterItem item){
        if(item == null){
            ArrayList<GroupItem>defaultgrouList = new ArrayList<GroupItem>();
            GroupItem defaultgroupItem = new GroupItem();
            defaultgroupItem.name = "Select a Center First";
            defaultgroupItem.isdefault = true;
            defaultgrouList.add(defaultgroupItem);
            groups = new ArrayList<GroupItem>();
            pl_group_adapter = new PaymentLoadGroupsAdapter(getApplicationContext(),defaultgrouList);
            center_groups.setAdapter(pl_group_adapter);
        }
        else{
            groups = centerGroups.get(item.name);
            pl_group_adapter = new PaymentLoadGroupsAdapter(getApplicationContext(),groups);
            center_groups.setAdapter(pl_group_adapter);
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
                ArrayList<AttendanceItem> at = new ArrayList<AttendanceItem>();
                for(int  k=0; k<2;k++){
                    at = new ArrayList<AttendanceItem>();
                    payee = new PayeeItem();
                    payee.id ="X"+(k+1);
                    payee.name = "MR. A"+(k+1);

                    for(int m=0; m<4;m++){
                        at.add(new AttendanceItem());
                    }
                    payee.attends  = at;
                    payees.add(payee);
                }
                group.payees =payees;

            }
            centerGroups.put(center.name,groupList);
            initialCenters.add(center);
        }

        centers = initialCenters;
    }
}
