package com.fli.salesagentapp.fliagentapp;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import java.util.Date;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoansFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LoansFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoansFragment extends Fragment {

    LinearLayout layout_cheque_details;
    EditText loan_date;

    RadioButton pay_type_cheque,pay_type_cash;
    private DatePicker datePicker;
    private Calendar calendar;
    private int year, month, day;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public LoansFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoansFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoansFragment newInstance(String param1, String param2) {
        LoansFragment fragment = new LoansFragment();
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

        View view = inflater.inflate(R.layout.fragment_loans, container, false);
                layout_cheque_details = (LinearLayout)view.findViewById(R.id.layout_cheque_details);
        loan_date = (EditText)view.findViewById(R.id.edit_loan_date);
        pay_type_cheque = (RadioButton)view.findViewById(R.id.pay_type_cheque);
        pay_type_cash = (RadioButton)view.findViewById(R.id.pay_type_cash);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            calendar = Calendar.getInstance();
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
           // showDate(year, month+1, day);

        }

        loan_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new SelectDateFragment();
                newFragment.show(getActivity().getFragmentManager(), "DatePicker");

                //getActivity().showDialog(999);
            }
        });

        pay_type_cheque.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout_cheque_details.setVisibility(View.VISIBLE);
            }
        });

        pay_type_cash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout_cheque_details.setVisibility(View.GONE);
            }
        });
        loan_date.setText(day+"-"+(month+1)+"-"+year);

        return view;
    }


    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(getContext(),
                    myDateListener, year, month, day);
        }
        return null;
    }

    private void showDate(int year, int month, int day) {
        loan_date.setText(new StringBuilder().append(day).append("-")
                .append(month).append("-").append(year));
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    showDate(arg1, arg2+1, arg3);
                }
            };


    public void onPayTypeClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()) {
            case R.id.pay_type_cheque:
                if (checked)
                    layout_cheque_details.setVisibility(View.VISIBLE);
                break;
            case R.id.pay_type_cash:
                if (checked)
                    layout_cheque_details.setVisibility(View.GONE);
                break;
        }


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

     @SuppressLint("ValidFragment")
     class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int yy, int mm, int dd) {
            year = yy;
            month = mm;
            day =dd;
            populateSetDate(yy, mm+1, dd);
        }
        public void populateSetDate(int year, int month, int day) {
            loan_date.setText(day+"-"+month+"-"+year);
        }

    }
}
