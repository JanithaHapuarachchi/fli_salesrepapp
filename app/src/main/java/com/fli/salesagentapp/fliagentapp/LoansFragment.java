package com.fli.salesagentapp.fliagentapp;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.fli.salesagentapp.fliagentapp.data.PayeeItem;
import com.fli.salesagentapp.fliagentapp.data.RecievedLoan;
import com.fli.salesagentapp.fliagentapp.utils.Constants;
import com.fli.salesagentapp.fliagentapp.utils.DataManager;
import com.fli.salesagentapp.fliagentapp.utils.ProgressBarController;
import com.fli.salesagentapp.fliagentapp.utils.Utility;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


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
    EditText loan_date,edit_loan_default,edit_loan_payment,edit_loan_cheque,edit_loan_bank;
    TextView txt_loan_id,txt_loan_client,txt_loan_name,txt_loan_totbal,txt_loan_totout,txt_loan_arrears,txt_loan_default,txt_loan_rental;
    ProgressBarController prgController;
    RadioButton pay_type_cheque,pay_type_cash;
    private DatePicker datePicker;
    private Calendar calendar;
    private int year, month, day;
    Button btn_load_details,btn_pay;
    EditText txt_loan_no;
    RecievedLoan selected_loan;
    DataManager dmManager;
    boolean cheque_checked = false;
    static String str_payday;
    public static DialogFragment newFragment;
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
        btn_load_details = (Button)view.findViewById(R.id.btn_load_details);
        txt_loan_no = (EditText)view.findViewById(R.id.txt_loan_no);
        edit_loan_default = (EditText)view.findViewById(R.id.edit_loan_default);
        edit_loan_payment = (EditText)view.findViewById(R.id.edit_loan_payment);
        edit_loan_cheque = (EditText)view.findViewById(R.id.edit_loan_cheque);
        edit_loan_bank = (EditText)view.findViewById(R.id.edit_loan_bank);
        txt_loan_id = (TextView)view.findViewById(R.id.txt_loan_id);
        txt_loan_client = (TextView)view.findViewById(R.id.txt_loan_client);
        txt_loan_name = (TextView)view.findViewById(R.id.txt_loan_name);
        txt_loan_totbal = (TextView)view.findViewById(R.id.txt_loan_totbal);
        txt_loan_totout  = (TextView)view.findViewById(R.id.txt_loan_totout);
        txt_loan_arrears = (TextView)view.findViewById(R.id.txt_loan_arrears);
        txt_loan_default = (TextView)view.findViewById(R.id.txt_loan_default);
        txt_loan_rental = (TextView)view.findViewById(R.id.txt_loan_rental);
        btn_pay = (Button)view.findViewById(R.id.btn_pay);
        dmManager = new DataManager(getContext());
        prgController = new ProgressBarController(getActivity());
        btn_load_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String loanno = txt_loan_no.getText().toString();
                if(!loanno.equals("")){
                    Utility.stopService();
                  new LoadLoanData().execute(loanno);
                }
                else{
                    Utility.showMessage("Please Enter Valid Loan Noumber",getContext());
                }
            }
        });

        btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PayeeItem payitem = new PayeeItem();
                String loanno = txt_loan_no.getText().toString();
                String payment = edit_loan_payment.getText().toString();
                String checqueno = edit_loan_cheque.getText().toString();
                String bankno = edit_loan_bank.getText().toString();
                String paydate = loan_date.getText().toString();
                if(selected_loan == null|| payment.equals("")){
                    Utility.showMessage("Please Enter Loan Noumber and Payment",getContext());
                }
                else{
                  //  str_payday = new SimpleDateFormat(Constants.DATE_FORMAT).format(paydate);
                    payitem.loan_id = selected_loan.loan_id;
                    payitem.client_id = selected_loan.client_id;
                    payitem.amount =payment;
                    payitem.center_id = selected_loan.center_id;
                    payitem.group_id = selected_loan.group_id;
                    payitem.transaction_date = str_payday;
                    payitem.note = "";
                    if(cheque_checked){
                        payitem.payment_type_id = Constants.PAYMENT_TYPE_ID_CHEQUE;
                        payitem.bank_no = bankno;
                        payitem.checque_no = checqueno;
                    }
                    else{
                        payitem.bank_no = "";
                        payitem.checque_no = "";
                        payitem.payment_type_id = Constants.PAYMENT_TYPE_ID_CASH;
                    }
                    new SaveLoanPayment().execute(payitem);
                }
            }
        });



            calendar = Calendar.getInstance();
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat(Constants.DATE_FORMAT);
        str_payday = df.format(c);
        loan_date.setText(str_payday);
           // showDate(year, month+1, day);


        loan_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(newFragment ==null) {
                    newFragment = new SelectDateFragment(year,month,day);
                }

                newFragment.show(getFragmentManager(), "DatePicker");

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
      //  loan_date.setText(day+"-"+(month+1)+"-"+year);

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
        cheque_checked = ((RadioButton) view).isChecked();
        switch(view.getId()) {
            case R.id.pay_type_cheque:
                if (cheque_checked)
                    layout_cheque_details.setVisibility(View.VISIBLE);
                break;
            case R.id.pay_type_cash:
                if (cheque_checked)
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

    /* @SuppressLint("ValidFragment")
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
            Log.e("FLI DATE",""+year+" "+month+" "+day);
            Calendar calendar = Calendar.getInstance();
            calendar.clear();
            calendar.set(Calendar.MONTH, month-1);
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.DAY_OF_MONTH,day);
            Date c = calendar.getTime();
            SimpleDateFormat df = new SimpleDateFormat(Constants.DATE_FORMAT);
            str_payday = df.format(c);
            loan_date.setText(str_payday);

        }

    }*/

    private void populateLoanDetails(RecievedLoan loan){
        if(loan ==null){
            selected_loan = null;
            txt_loan_id.setText("");
            txt_loan_client.setText("");
            txt_loan_name.setText("");
            txt_loan_totbal.setText("");
            txt_loan_totout.setText("");
            txt_loan_arrears.setText("");
            txt_loan_default.setText("");
            txt_loan_rental.setText("");
            Utility.showMessage("Loan Details are Not available",getContext());
        }
        else{
            selected_loan = loan;
            txt_loan_id.setText(loan.loan_accountno);
            txt_loan_client.setText(loan.client_name);
            txt_loan_name.setText(loan.loan_name);
            txt_loan_totbal.setText(loan.total_balance);
            txt_loan_totout.setText(loan.outstanding_balance);
            txt_loan_arrears.setText(loan.arrears);
            txt_loan_default.setText(loan.def);
            txt_loan_rental.setText(loan.rental);
        }
    }

    private void clearAllData(){
        selected_loan = null;
        txt_loan_no.setText("");
        txt_loan_id.setText("");
        txt_loan_client.setText("");
        txt_loan_name.setText("");
        txt_loan_totbal.setText("");
        txt_loan_totout.setText("");
        txt_loan_arrears.setText("");
        txt_loan_default.setText("");
        txt_loan_rental.setText("");
        edit_loan_bank.setText("");
        edit_loan_default.setText("");
        edit_loan_cheque.setText("");
        edit_loan_payment.setText("");
    }

    class SaveLoanPayment extends AsyncTask<PayeeItem,Void,Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            prgController.showProgressBar("Saving Loan Data...");
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            prgController.hideProgressBar();
            Utility.showMessage("Successfully Saved Payment",getContext());
            clearAllData();
        }

        @Override
        protected Void doInBackground(PayeeItem... params) {
            PayeeItem item = params[0];
            dmManager.savePayment(item);
            return null;
        }
    }

    class LoadLoanData extends AsyncTask<String,Void,RecievedLoan>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            prgController.showProgressBar("Searching Loan Data...");
        }

        @Override
        protected void onPostExecute(RecievedLoan loan) {
            super.onPostExecute(loan);
            prgController.hideProgressBar();
            populateLoanDetails(loan);


        }

        @Override
        protected RecievedLoan doInBackground(String... params) {
            return dmManager.getDetailsforLoanNumber(params[0]);
        }
    }

}
