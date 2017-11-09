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
import android.widget.Toast;

import java.util.ArrayList;

public class MyListFragment extends ListFragment{

    private final String TAG = "MyListFragment (: ";

    private ArrayList<Item> mItems;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i(TAG,"onAttach()");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG,"onActivityCreated()");
        mItems = new ArrayList<>();
        mItems.add(new Item("Name", "4 objects", "653 bytes", "08.11.2017 16:19 PM", "", true));
        mItems.add(new Item("Name", "", "53 bytes", "08.11.2017 16:21 PM", "", false));
        MyListAdapter adapter = new MyListAdapter(getActivity(),
                R.layout.item_fragment, mItems);
        setListAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG,"onCreateView()");
        return inflater.inflate(R.layout.list_fragment, null);
    }

    @Override
    public void onStart() {
        Log.i(TAG,"onStart()");
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.i(TAG,"onResume()");
        super.onResume();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Toast.makeText(getActivity(),
                getListView().getItemAtPosition(position).toString(),
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPause() {
        Log.i(TAG,"onPause()");
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.i(TAG,"onStop()");
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        Log.i(TAG,"onDestroyView()");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        Log.i(TAG,"onDestroy()");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        Log.i(TAG,"onDetach()");
        super.onDetach();
    }

    public ArrayList<Item> getItems() {
        return mItems;
    }
}
