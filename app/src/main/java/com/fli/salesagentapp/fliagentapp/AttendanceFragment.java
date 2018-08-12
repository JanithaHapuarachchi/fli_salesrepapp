package com.fli.salesagentapp.fliagentapp;

import android.content.Context;
import android.net.Uri;
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

import com.fli.salesagentapp.fliagentapp.adapters.AttendanceAdapter;
import com.fli.salesagentapp.fliagentapp.adapters.PaymentLoadCentersAdapter;
import com.fli.salesagentapp.fliagentapp.adapters.PaymentLoadGroupsAdapter;
import com.fli.salesagentapp.fliagentapp.data.AttendanceItem;
import com.fli.salesagentapp.fliagentapp.data.CenterItem;
import com.fli.salesagentapp.fliagentapp.data.GroupItem;
import com.fli.salesagentapp.fliagentapp.data.PayeeItem;
import com.fli.salesagentapp.fliagentapp.utils.ProgressBarController;

import java.util.ArrayList;
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

    ProgressBarController prgController;

    boolean init_centers =false;
    boolean init_groups =false;

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
        center_names = (AutoCompleteTextView)view.findViewById(R.id.center_names);
        center_groups = (Spinner)view.findViewById(R.id.center_groups);
        spinner_center_names = (Spinner)view.findViewById(R.id.spinner_center_names);
        btn_mark_attendance = (Button) view.findViewById(R.id.btn_mark_attendance);
        // btn_select = (Button)findViewById(R.id.btn_select);
        // pay_total = (TextView)findViewById(R.id.pay_total);
        center_attendances = (ListView)view.findViewById(R.id.center_attendances);
        initItems();
        Log.e("SIZE ",""+centers.size());
        pl_center_adapter = new PaymentLoadCentersAdapter(getContext(),centers);
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

        return  view;
    }

    private void populate_group_payments(int group_index){
        selected_group =groups.get(group_index);
        AttendanceAdapter attendanceAdapter = new AttendanceAdapter(getContext(),selected_group.payees);
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
            groups = centerGroups.get(item.name);
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
}
