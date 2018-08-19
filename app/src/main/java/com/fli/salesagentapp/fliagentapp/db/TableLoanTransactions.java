package com.fli.salesagentapp.fliagentapp.db;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.fli.salesagentapp.fliagentapp.utils.Constants;

/**
 * Created by janithah on 8/15/2018.
 */

public class TableLoanTransactions {

    public static final String TABLE_NAME = "loandeposits";

    public static final String DEPOSIT_ID = "id";
    public static final String DEPOSIT_LOAN_ID = "loanid";
    public static final String DEPOSIT_AMOUNT = "amount";
    public static final String DEPOSIT_CLIENT_ID = "clientid";
    public static final String DEPOSIT_CENTER_ID = "centerid";
    public static final String DEPOSIT_GROUP_ID = "groupid";
    public static final String DEPOSIT_PAY_TYPE = "paytype";
    public static final String DEPOSIT_PAY_DEFAULT = "paydefault";
    public static final String DEPOSIT_CHEQUE_NUMBER = "paychequeno";
    public static final String DEPOSIT_BANK_NUMBER = "paybankno";
    public static final String DEPOSIT_NOTE = "paynote";
    public static final String DEPOSIT_SYNCED= "synced";
    public static final String DEPOSIT_TIME = "time";

    public static final String TABLE_CREATE = "Create Table IF NOT EXISTS " + TABLE_NAME + " ( "
            + DEPOSIT_ID + " integer primary key autoincrement,"
            + DEPOSIT_CLIENT_ID + " char(40)  NULL,"
            + DEPOSIT_LOAN_ID + " char(40)  NULL,"
            + DEPOSIT_CENTER_ID + " char(40)  NULL,"
            + DEPOSIT_GROUP_ID + " char(40)  NULL,"
            + DEPOSIT_PAY_TYPE + " char(40) NULL,"
            + DEPOSIT_CHEQUE_NUMBER + " char(40)  NULL,"
            + DEPOSIT_BANK_NUMBER + " char(40) NULL,"
            + DEPOSIT_PAY_DEFAULT + " char(40) NULL,"
            + DEPOSIT_AMOUNT + " char(40)  NULL,"
            + DEPOSIT_NOTE + " char(40)  NULL,"
            + DEPOSIT_SYNCED + " char(1) default '"+ Constants.SYCED_NOT+"',"
            + DEPOSIT_TIME + " TEXT  NULL );";


    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(TABLE_CREATE);
    }

    public static void droptable(SQLiteDatabase database) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }
    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.e(TableRecievedLoans.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        //db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        //onCreate(db);
    }


}
