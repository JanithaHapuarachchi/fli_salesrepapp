package com.fli.salesagentapp.fliagentapp;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.fli.salesagentapp.fliagentapp.adapters.IssuePaymentAdapter;
import com.fli.salesagentapp.fliagentapp.adapters.PaymentLoadCentersAdapter;
import com.fli.salesagentapp.fliagentapp.adapters.PaymentLoadGroupsAdapter;
import com.fli.salesagentapp.fliagentapp.data.CenterItem;
import com.fli.salesagentapp.fliagentapp.data.ClientItem;
import com.fli.salesagentapp.fliagentapp.data.ClientPaymentsInfo;
import com.fli.salesagentapp.fliagentapp.data.GroupItem;
import com.fli.salesagentapp.fliagentapp.data.GroupPaymentItem;
import com.fli.salesagentapp.fliagentapp.data.PayeeItem;
import com.fli.salesagentapp.fliagentapp.services.SubmitDataService;
import com.fli.salesagentapp.fliagentapp.utils.Constants;
import com.fli.salesagentapp.fliagentapp.utils.DataManager;
import com.fli.salesagentapp.fliagentapp.utils.ProgressBarController;
import com.fli.salesagentapp.fliagentapp.utils.Utility;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PaymentsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PaymentsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PaymentsFragment extends Fragment {

    final String TAG_PAYMENT = "Payment";
    ArrayList<CenterItem> initialCenters,centers;
   // ArrayList<GroupItem> groups;
    HashMap<String,ArrayList<GroupItem>> centerGroups;
    HashMap<String,ArrayList<ClientItem>> groupClients;
    AutoCompleteTextView center_names;
    Spinner center_groups,spinner_center_names;
    Button btn_select,btn_pay;
    TextView pay_total;
    ListView center_payees;
    PaymentLoadCentersAdapter pl_center_adapter;
    PaymentLoadGroupsAdapter pl_group_adapter;
    CenterItem selected_center;
    ArrayList<GroupItem> groups,allgroups;
    ArrayList<PayeeItem> payees;
    GroupItem selected_group;
    ClientPaymentsInfo info;
    DataManager dmManager;
    ProgressBarController prgController;
    IssuePaymentAdapter issuePaymentAdapter;
    ArrayList<ClientItem> payed_clients;
    String str_today;
    boolean init_centers =false;
    boolean init_groups =false;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public PaymentsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PaymentsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PaymentsFragment newInstance(String param1, String param2) {
        PaymentsFragment fragment = new PaymentsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_payments, container, false);

        center_names = (AutoCompleteTextView)view.findViewById(R.id.center_names);
        center_groups = (Spinner)view.findViewById(R.id.center_groups);
        spinner_center_names = (Spinner)view.findViewById(R.id.spinner_center_names);
        btn_pay = (Button) view.findViewById(R.id.btn_pay);
        btn_select = (Button)view.findViewById(R.id.btn_select);
        pay_total = (TextView)view.findViewById(R.id.pay_total);
        center_payees = (ListView)view.findViewById(R.id.center_payees);
        center_payees.setItemsCanFocus(true);

        dmManager = new DataManager(getContext());
        prgController  =new ProgressBarController(getActivity());

        //initItems();
        SimpleDateFormat df = new SimpleDateFormat(Constants.DATE_FORMAT);
        str_today = df.format(Calendar.getInstance().getTime());
        SubmitDataService.stopAsync();
        new LoadClientPaymentInfo().execute();
//        Log.e("SIZE ",""+centers.size());
        // pl_center_adapter = new PaymentLoadCentersAdapter(getApplicationContext(),centers);
        // center_names.setAdapter(pl_center_adapter);
//        spinner_center_names.setAdapter(new PaymentLoadCentersAdapter(getContext(),centers));
//        setGroupsforCenter(null);
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
              //  search_center_name(s.toString());
            }
        });

        btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selected_center == null || selected_group == null){
                    Utility.showMessage("Please Select center and group",getContext());
                }
                else if(selected_group.clients.size()==0){
                    Utility.showMessage("There are no Clients",getContext());
                }
                else{
                    SubmitDataService.stopAsync();
                    new SaveClientPayments().execute();
                }
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


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
      //  initItems();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

    private void populate_group_payments(int group_index){
        selected_group =groups.get(group_index);
      //  IssuePaymentAdapter issuePaymentAdapter = new IssuePaymentAdapter(getContext(),selected_group.payees,pay_total);
      //  center_payees.setAdapter(issuePaymentAdapter);
         issuePaymentAdapter = new IssuePaymentAdapter(getContext(),groupClients.get(selected_group.id),pay_total);
        center_payees.setAdapter(issuePaymentAdapter);

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
        pl_center_adapter = new PaymentLoadCentersAdapter(getContext(),centers);
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
            center_groups.setAdapter(new PaymentLoadGroupsAdapter(getContext(),defaultgrouList));
        }
        else{
            groups = centerGroups.get(item.id);
            // pl_group_adapter = new PaymentLoadGroupsAdapter(getApplicationContext(),groups);
            center_groups.setAdapter(new PaymentLoadGroupsAdapter(getContext(),groups));
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


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    private void setupinfo(){
        if(info.centers.size()==0){
            Utility.showMessage("Details are not available",getContext());
            spinner_center_names.setAdapter(new PaymentLoadCentersAdapter(getContext(),new ArrayList<CenterItem>()));
            setGroupsforCenter(null);
        }
        else{
            centers = info.centers;
            initialCenters = centers;
            centerGroups = info.centergroups;
            allgroups = info.groups;
            groupClients = info.groupclients;
            spinner_center_names.setAdapter(new PaymentLoadCentersAdapter(getContext(),centers));
            setGroupsforCenter(null);
            Log.e("FLI CENTERS",centers.toString());
            Log.e("FLI CENTER GROUPS",centerGroups.toString());
            Log.e("FLI GROUPS",allgroups.toString());
            Log.e("FLI GROUP CLIENTS",groupClients.toString());
        }
    }

    private void removeGroupFromLists(){
        groups =  centerGroups.get(selected_center.id);
        int pos_selectedgroup = groups.indexOf(selected_group);
        groups.remove(pos_selectedgroup);
        centerGroups.put(selected_center.id,groups);
        if(groups.size()==0){
            int pos_selectedcenter = initialCenters.indexOf(selected_center);
            centerGroups.remove(selected_center.id);
            centers.remove(pos_selectedcenter);
            initialCenters.remove(pos_selectedcenter);
        }
        spinner_center_names.setAdapter(new PaymentLoadCentersAdapter(getContext(),centers));
    }

    class SaveClientPayments extends AsyncTask<Void,Void,Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            prgController.showProgressBar("Saving Data...");
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            prgController.hideProgressBar();
            removeGroupFromLists();
        }

        @Override
        protected Void doInBackground(Void... params) {
            payed_clients = issuePaymentAdapter.getPaymentDetails();
            GroupPaymentItem payment = new GroupPaymentItem();
            payment.center_id =selected_center.id;
            payment.group_id =selected_group.id;
            payment.clients = payed_clients;
            payment.pay_date = str_today;
            dmManager.saveGroupPaymennts(payment);
            return null;
        }
    }

    class LoadClientPaymentInfo extends AsyncTask<Void,ClientPaymentsInfo,ClientPaymentsInfo>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            prgController.showProgressBar("Loading Data...");
        }

        @Override
        protected void onPostExecute(ClientPaymentsInfo clientPaymentsInfo) {
            super.onPostExecute(clientPaymentsInfo);
            prgController.hideProgressBar();
            info = clientPaymentsInfo;
            setupinfo();
        }

        @Override
        protected ClientPaymentsInfo doInBackground(Void... params) {
            return dmManager.getAvailablePayments();
        }
    }
}
