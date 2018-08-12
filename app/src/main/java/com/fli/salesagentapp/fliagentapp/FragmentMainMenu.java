package com.fli.salesagentapp.fliagentapp;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class FragmentMainMenu extends FragmentActivity {

    public static FragmentTabHost mTabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_main_menu);

        int fragment_no= getIntent().getIntExtra("FRAGMENT_NO",0);
        mTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        int i=0;
        mTabHost.addTab(mTabHost.newTabSpec("tab1").setIndicator(""),
                LoansFragment.class, null);
        mTabHost.getTabWidget().getChildAt(i++).setBackgroundResource(R.drawable.loanactiveselector);
        mTabHost.addTab(mTabHost.newTabSpec("tab2").setIndicator(""),
                PaymentsFragment.class, null);
        mTabHost.getTabWidget().getChildAt(i++).setBackgroundResource(R.drawable.paymentactiveselector);
        mTabHost.addTab(mTabHost.newTabSpec("tab3").setIndicator(""),
                CollectionsFragment.class,null);
        mTabHost.getTabWidget().getChildAt(i++).setBackgroundResource(R.drawable.collectionactiveselector);
        mTabHost.addTab(mTabHost.newTabSpec("tab4").setIndicator(""),
                AttendanceFragment.class,null);
        mTabHost.getTabWidget().getChildAt(i++).setBackgroundResource(R.drawable.attendanceactiveselector);

        mTabHost.setCurrentTab(fragment_no);

    }
}
