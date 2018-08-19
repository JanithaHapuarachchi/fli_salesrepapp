package com.fli.salesagentapp.fliagentapp.data;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by janithah on 8/18/2018.
 */

public class ClientPaymentsInfo {
    public  ArrayList<CenterItem> centers;
    public ArrayList<GroupItem> groups;
    public HashMap<String,ArrayList<GroupItem>> centergroups;
    public HashMap<String,ArrayList<ClientItem>> groupclients;


}
