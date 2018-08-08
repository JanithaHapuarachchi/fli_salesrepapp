package com.fli.salesagentapp.fliagentapp.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by janithah on 8/8/2018.
 */

public class Utility {

    public static void showMessage(String txt, Context context) {
        Toast.makeText(context, txt, Toast.LENGTH_LONG).show();
    }

}
