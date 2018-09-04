package com.fli.salesagentapp.fliagentapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import android.widget.Toast;

import com.fli.salesagentapp.fliagentapp.adapters.AttendanceAdapter;
import com.fli.salesagentapp.fliagentapp.adapters.PaymentLoadCentersAdapter;
import com.fli.salesagentapp.fliagentapp.adapters.PaymentLoadGroupsAdapter;
import com.fli.salesagentapp.fliagentapp.data.AttendanceItem;
import com.fli.salesagentapp.fliagentapp.data.CenterItem;
import com.fli.salesagentapp.fliagentapp.data.ClientAttendanceInfo;
import com.fli.salesagentapp.fliagentapp.data.ClientItem;
import com.fli.salesagentapp.fliagentapp.data.GroupItem;
import com.fli.salesagentapp.fliagentapp.data.MarkedAttendace;
import com.fli.salesagentapp.fliagentapp.data.PayeeItem;
import com.fli.salesagentapp.fliagentapp.utils.Constants;
import com.fli.salesagentapp.fliagentapp.utils.DataManager;
import com.fli.salesagentapp.fliagentapp.utils.ProgressBarController;
import com.fli.salesagentapp.fliagentapp.utils.Utility;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AttendanceFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AttendanceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AttendanceFragment extends Fragment {

    final String TAG_ATTENDANCE = "Attendance";
    ArrayList<CenterItem> initialCenters,centers;
    HashMap<String,ArrayList<GroupItem>> centerGroups;
    HashMap<String,ArrayList<ClientItem>> groupClients;
    AutoCompleteTextView center_names;
    Spinner center_groups,spinner_center_names;
    Button btn_mark_attendance;
    TextView pay_total;
    ListView center_attendances;
    PaymentLoadCentersAdapter pl_center_adapter;
    PaymentLoadGroupsAdapter pl_group_adapter;
    CenterItem selected_center;
    ArrayList<GroupItem> groups,allgroups;
   // ArrayList<PayeeItem> payees;
    GroupItem selected_group;
    ArrayList<ClientItem> marked_clients;

    ProgressBarController prgController;
    ClientAttendanceInfo info;
    DataManager dmManager;
    AttendanceAdapter attendanceAdapter;
    String str_today;
    boolean init_centers =true;
    boolean init_groups =true;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public AttendanceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AttendanceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AttendanceFragment newInstance(String param1, String param2) {
        AttendanceFragment fragment = new AttendanceFragment();
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
        View view = inflater.inflate(R.layout.fragment_attendance, container, false);

        prgController = new ProgressBarController(getActivity());
        dmManager = new DataManager(getContext());
        center_names = (AutoCompleteTextView)view.findViewById(R.id.center_names);
        center_groups = (Spinner)view.findViewById(R.id.center_groups);
        spinner_center_names = (Spinner)view.findViewById(R.id.spinner_center_names);
        btn_mark_attendance = (Button) view.findViewById(R.id.btn_mark_attendance);
        // btn_select = (Button)findViewById(R.id.btn_select);
        // pay_total = (TextView)findViewById(R.id.pay_total);
        Date c = java.util.Calendar.getInstance().getTime();
        center_attendances = (ListView)view.findViewById(R.id.center_attendances);
        SimpleDateFormat df = new SimpleDateFormat(Constants.DATE_FORMAT);
        str_today = df.format(c);

      //  initItems();
//        Log.e("SIZE ",""+centers.size());
//        pl_center_adapter = new PaymentLoadCentersAdapter(getContext(),centers);
//        //center_names.setAdapter(pl_center_adapter);
//        spinner_center_names.setAdapter(pl_center_adapter);
//        setGroupsforCenter(null);
        center_names.setThreshold(1);
        Utility.stopService();
        new LoadClientAttendanceInfo().execute();

        btn_mark_attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialog();
            }
        });

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

        return  view;
    }

    private void confirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(true);
        builder.setMessage("Are you sure?");
        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(selected_center == null || selected_group == null){
                            Utility.showMessage("Please Select center and group",getContext());
                        }
                        else if(selected_group.clients.size()==0){
                            Utility.showMessage("There are no Clients",getContext());
                        }
                        else{
                            Utility.stopService();
                            new SaveClientAttendants().execute();
                        }
                    }
                });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void populate_group_payments(int group_index){
        selected_group =groups.get(group_index);
     //   AttendanceAdapter attendanceAdapter = new AttendanceAdapter(getContext(),selected_group.payees);
     //   center_attendances.setAdapter(attendanceAdapter);
         attendanceAdapter = new AttendanceAdapter(getContext(),groupClients.get(selected_group.id));
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
            pl_group_adapter = new PaymentLoadGroupsAdapter(getContext(),defaultgrouList);
            center_groups.setAdapter(pl_group_adapter);
        }
        else{
            groups = centerGroups.get(item.id);
            pl_group_adapter = new PaymentLoadGroupsAdapter(getContext(),groups);
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
            spinner_center_names.setAdapter(new PaymentLoadCentersAdapter(getContext(),centers));
        }
       else{
            pl_group_adapter = new PaymentLoadGroupsAdapter(getContext(),groups);
            center_groups.setAdapter(pl_group_adapter);
        }
        Toast.makeText(getContext(),"Attendance Recorded!",Toast.LENGTH_LONG).show();
    }

    class SaveClientAttendants extends AsyncTask<Void,Void,Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            prgController.showProgressBar("Saving...");
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            removeGroupFromLists();
            prgController.hideProgressBar();
        }

        @Override
        protected Void doInBackground(Void... params) {
            marked_clients = attendanceAdapter.markeditems();
            MarkedAttendace attendace = new MarkedAttendace();
            attendace.center_id =selected_center.id;
            attendace.group_id =selected_group.id;
            attendace.clients = marked_clients;
            attendace.meeting_date = str_today;
            dmManager.saveMarkedAttendance(attendace);
            return null;
        }
    }

    class LoadClientAttendanceInfo extends AsyncTask<Void,ClientAttendanceInfo,ClientAttendanceInfo> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            prgController.showProgressBar("Loading Data...");
        }

        @Override
        protected void onPostExecute(ClientAttendanceInfo clientPaymentsInfo) {
            super.onPostExecute(clientPaymentsInfo);
            prgController.hideProgressBar();
            info = clientPaymentsInfo;
            setupinfo();
        }

        @Override
        protected ClientAttendanceInfo doInBackground(Void... params) {
            return dmManager.getAvailableAttendance();
        }
    }
}
