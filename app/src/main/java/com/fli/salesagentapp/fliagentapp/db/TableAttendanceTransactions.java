package com.fli.salesagentapp.fliagentapp.db;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by janithah on 8/15/2018.
 */

public class TableAttendanceTransactions {

    public static final String TABLE_NAME = "attendancedeposits";

    public static final String ATTENDANCE_ID = "id";
    public static final String ATTENDANCE_ATTEND = "attendace";
    public static final String ATTENDANCE_CLIENT_ID = "clientid";
    public static final String ATTENDANCE_CENTER_ID = "centerid";
    public static final String ATTENDANCE_GROUP_ID = "groupid";
    public static final String ATTENDANCE_SYNCED= "synced";
    public static final String ATTENDANCE_TIME = "time";

    public static final String TABLE_CREATE = "Create Table IF NOT EXISTS " + TABLE_NAME + " ( "
            + ATTENDANCE_ID + " integer primary key autoincrement,"
            + ATTENDANCE_ATTEND + " char(40) NOT NULL,"
            + ATTENDANCE_CLIENT_ID + " char(40) NOT NULL,"
            + ATTENDANCE_CENTER_ID + " char(40) NOT NULL,"
            + ATTENDANCE_GROUP_ID + " char(40) NULL,"
            + ATTENDANCE_SYNCED + " char(1) NOT NULL,"
            + ATTENDANCE_TIME + " TEXT NOT NULL );";


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
