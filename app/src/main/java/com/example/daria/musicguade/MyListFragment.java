package com.example.daria.musicguade;

import android.app.ListFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MyListFragment extends ListFragment{

    ArrayList<Item> array;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        array = new ArrayList<>();
        array.add(new Item("Name", "4 objects", "653 bytes", "08.11.2017 16:19 PM", "", true));
        array.add(new Item("Name", "", "53 bytes", "08.11.2017 16:21 PM", "", false));
        MyListAdapter adapter = new MyListAdapter(getActivity(),
                R.layout.item_fragment, array);
        setListAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list_fragment, null);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Toast.makeText(getActivity(),
                getListView().getItemAtPosition(position).toString(),
                Toast.LENGTH_LONG).show();
    }

}
