package com.fli.salesagentapp.fliagentapp.data;

import java.util.ArrayList;

/**
 * Created by janithah on 8/8/2018.
 */

public class GroupItem {
    public boolean isdefault =false;
    public String id;
    public String name;
    public ArrayList<PayeeItem> payees;
    public ArrayList<ClientItem> clients;
    public Double total_def;

    @Override
    public String toString() {
        return "id: "+id+", name :"+name;
    }
}
