package com.fli.salesagentapp.fliagentapp;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.fli.salesagentapp.fliagentapp.R;
import com.fli.salesagentapp.fliagentapp.utils.Constants;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by anjanan on 8/23/2018.
 */
@SuppressLint("ValidFragment")
public class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    private Calendar calendar;
    private int year, month, day;
    String str_payday;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
    //    calendar = Calendar.getInstance();
    //    year = calendar.get(Calendar.YEAR);
    //    month = calendar.get(Calendar.MONTH);
     //   day = calendar.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }


    public SelectDateFragment(int year, int month, int day){
        this.year = year;
        this.month = month;
        this.day =day;
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
        LoansFragment.str_payday = df.format(c);
        EditText loan_date = (EditText) getActivity().findViewById(R.id.edit_loan_date);
        loan_date.setText(LoansFragment.str_payday);

    }

}
