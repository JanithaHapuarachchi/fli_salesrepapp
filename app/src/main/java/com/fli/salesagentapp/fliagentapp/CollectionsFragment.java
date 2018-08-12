package com.fli.salesagentapp.fliagentapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.fli.salesagentapp.fliagentapp.adapters.CollectionAdapter;
import com.fli.salesagentapp.fliagentapp.data.CollectionItem;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CollectionsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CollectionsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CollectionsFragment extends Fragment {

    ListView list_collections;
    CollectionAdapter c_adapter;
    ArrayList<CollectionItem> collections;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public CollectionsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CollectionsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CollectionsFragment newInstance(String param1, String param2) {
        CollectionsFragment fragment = new CollectionsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_collections, container, false);

        list_collections = (ListView)view.findViewById(R.id.list_collections);
        collections = initItems();
        c_adapter =new CollectionAdapter(getContext(),collections);
        list_collections.setAdapter(c_adapter);

        return view;
    }

    private ArrayList<CollectionItem> initItems(){
        ArrayList<CollectionItem> items = new ArrayList<CollectionItem>();
        CollectionItem item;
        for(int i=0; i< 5; i++){
            item = new CollectionItem();
            item.center_name = "Center "+(i+1);
            item.no_of_loans = ""+(i*5);
            item.total_collection= ""+(i*20+1);
            item.pending_sync = ""+0;
            items.add(item);
        }
        return items;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
