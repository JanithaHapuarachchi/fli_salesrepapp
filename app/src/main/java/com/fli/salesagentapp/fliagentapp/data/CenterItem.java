package com.fli.salesagentapp.fliagentapp.data;

/**
 * Created by janithah on 8/8/2018.
 */

public class CenterItem {

    public String id;
    public String name;
    public int no_of_loans;
    public double total_collection;
    public int pending_sync_count;

    @Override
    public String toString() {
        return "id: "+id+", name :"+name;
    }
}
