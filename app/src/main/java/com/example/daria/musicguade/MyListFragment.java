package com.example.daria.musicguade;

import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import static com.example.daria.musicguade.MainActivity.ITEM_LIST;

public class MyListFragment extends ListFragment {

    private final String TAG = "MyListFragment "+this.hashCode()+" (: ";

    Bundle bundle;
    private MyListAdapter adapter;
    private ArrayList<Item> mItems;
    private View view = null;

    OnItemSelectedListener mItemSelectedListener;

    public MyListFragment() {
        this.setRetainInstance(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i(TAG, "onAttach()");
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mItemSelectedListener = (OnItemSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView()");
        if(view == null) {
            Log.d(TAG,"view != null");
            view = inflater.inflate(R.layout.list_fragment, null);
            bundle = getArguments();
            if (bundle != null) {
                Log.d(TAG,"bundle != null");
                //if(mItems == null){
                    Log.d(TAG,"mItems == null");
                    getDataFromActivity();
                //}
            }
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG, "onActivityCreated()");
        adapter = new MyListAdapter(getActivity(), R.layout.item_fragment, mItems);
        setListAdapter(adapter);
    }

    @Override
    public void onStart() {
        Log.i(TAG, "onStart()");
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.i(TAG, "onResume()");
        super.onResume();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Item item = (Item) getListView().getItemAtPosition(position);
        // Send the event to the host activity
        mItemSelectedListener.onItemSelected(item.getPath());
    }

    @Override
    public void onPause() {
        Log.i(TAG, "onPause()");
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.i(TAG, "onStop()");
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        Log.i(TAG, "onDestroyView()");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy()");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        Log.i(TAG, "onDetach()");
        super.onDetach();
    }

    public MyListAdapter getAdapter() {
        return adapter;
    }

    public void getDataFromActivity() {
        mItems = new ArrayList<>();
        mItems = bundle.getParcelableArrayList(ITEM_LIST);
    }

    public void setItems(ArrayList<Item> items) {
        mItems = items;
    }
}
