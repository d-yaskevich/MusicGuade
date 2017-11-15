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

import java.io.File;
import java.util.ArrayList;

import static com.example.daria.musicguade.MainActivity.ITEM_LIST;
import static com.example.daria.musicguade.MainActivity.PATH;

public class MyListFragment extends ListFragment {

    private final String TAG = "MyListFragment " + this.hashCode() + " (: ";

    private MyListAdapter adapter;
    private ArrayList<Item> mItems;
    private View view = null;
    private String path;

    OnChangeFragmentStateListener mFragmentStateListener;

    public MyListFragment() {
        this.setRetainInstance(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i(TAG, "onAttach()");
        try {
            mFragmentStateListener = (OnChangeFragmentStateListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView()");
        if (view == null) {
            view = inflater.inflate(R.layout.list_fragment, null);
            Bundle bundle = getArguments();
            if (bundle != null) {
                path = bundle.getString(PATH);
                mItems = new ArrayList<>();
                mItems = bundle.getParcelableArrayList(ITEM_LIST);
            }
        }
        if (path != null) {
            mFragmentStateListener.uploadPath(path + File.separator);
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
        mFragmentStateListener.onItemSelected(item.getPath());
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

    /**
     * Upload fragment information
     * @param items new data of list
     */
    public void setNewItems(ArrayList<Item> items) {
        try {
            mItems = items;
            adapter = new MyListAdapter(getActivity(), R.layout.item_fragment, mItems);
            setListAdapter(adapter);
        } catch (NullPointerException e) {
            Log.e(TAG, "New Item List is empty");
        }
    }

    public String getPath() {
        return path;
    }
}
