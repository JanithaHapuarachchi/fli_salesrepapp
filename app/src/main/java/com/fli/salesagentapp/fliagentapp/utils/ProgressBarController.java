package com.fli.salesagentapp.fliagentapp.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fli.salesagentapp.fliagentapp.R;

import org.w3c.dom.Text;

/**
 * Created by janithamh on 8/12/18.
 */

public class ProgressBarController {

    Context context;
    Activity activity;
    View view;
    TextView prg_text;
    RelativeLayout prg_layout;
    PopupWindow popWindow;
    Point screensize;
    public  ProgressBarController(Activity _activity){
        this.activity = _activity;
        prg_text = (TextView) this.activity.findViewById(R.id.prg_text);
        prg_layout = (RelativeLayout)this.activity.findViewById(R.id.prg_layout);


    }

    public void showProgressBar(String msg){
        prg_text.setText(msg);
        prg_layout.setVisibility(View.VISIBLE);
    }

    public void hideProgressBar(){
        prg_layout.setVisibility(View.GONE);
    }

}
