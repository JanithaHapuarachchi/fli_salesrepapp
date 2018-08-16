package com.fli.salesagentapp.fliagentapp.db;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by janithah on 8/15/2018.
 */

public class TableLoanTransactions {

    public static final String TABLE_NAME = "loandeposits";

    public static final String TABLE_ID = "id";
    public static final String LOAN_ID= "loanid";
    public static final String LOAN_AMOUNT = "loanamount";
    public static final String LOAN_CENTER_ID = "centerid";
    public static final String LOAN_GROUP_ID = "groupid";
    public static final String LOAN_CLIENT_ID = "clientid";
    public static final String LOAN_CLIENT_NAME = "clientname";
    public static final String LOAN_CENTER_NAME= "centername";
    public static final String LOAN_NAME = "loanname";
    public static final String LOAN_TOTAL_BALANCE = "totalbalance";
    public static final String LOAN_OUT_BALANCE = "outbalance";
    public static final String LOAN_ARREARS = "arrears";
    public static final String LOAN_DEFAULT = "default";
    public static final String LOAN_RENTAL = "rental";
    public static final String LOAN_TYPE = "type";


    public static final String TABLE_CREATE = "Create Table IF NOT EXISTS " + TABLE_NAME + " ( "
            + TABLE_ID + " integer primary key autoincrement,"
            + LOAN_ID + " char(40) NOT NULL,"
            + LOAN_AMOUNT + " char(40)  NULL,"
            + LOAN_CENTER_ID + " char(40)  NULL,"
            + LOAN_GROUP_ID + " char(40) NULL,"
            + LOAN_CLIENT_ID + " char(40) NULL,"
            + LOAN_CLIENT_NAME + " char(40) NULL,"
            + LOAN_CENTER_NAME + " char(40) NULL,"
            + LOAN_NAME + " char(40)  NULL,"
            + LOAN_TOTAL_BALANCE + " char(40) NULL,"
            + LOAN_OUT_BALANCE + " char(40) NULL,"
            + LOAN_ARREARS + " char(40) NULL,"
            + LOAN_DEFAULT + " char(40) NULL,"
            + LOAN_RENTAL + " char(40) NULL,"
            + LOAN_TYPE + " char(40) NULL );";


    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(TABLE_CREATE);
    }


    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.e(TableLoanTransactions.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        //db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        //onCreate(db);
    }
}
