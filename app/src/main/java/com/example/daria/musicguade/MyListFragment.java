package com.example.daria.musicguade;

import android.app.ListFragment;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import static com.example.daria.musicguade.FileSystemDBManager.queryForList;
import static com.example.daria.musicguade.MainActivity.PATH;

public class MyListFragment extends ListFragment {

    private final String TAG = "MyListFragment " + this.hashCode() + " (: ";

    private MyListAdapter adapter;
    private ArrayList<Item> mItems;
    private View view = null;
    private String path;

    OnChangeFragmentStateListener mFragmentStateListener;

    FileSystemDBHelper mDbHelper = null;
    SQLiteDatabase db = null;

    public MyListFragment() {
        this.setRetainInstance(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i(TAG, "onAttach()");
        mFragmentStateListener = (OnChangeFragmentStateListener) context;
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
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    new FileSystemDBAgent().execute();
                }
            }
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG, "onActivityCreated()");
        setNewItems(mItems);
    }

    @Override
    public void onStart() {
        Log.i(TAG, "onStart()");
        super.onStart();
        if (path != null) {
            mFragmentStateListener.uploadPath(path);
        }
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
     *
     * @param items new data of list
     */
    public void setNewItems(ArrayList<Item> items) {
        mItems = items;
        adapter = new MyListAdapter(getActivity(), R.layout.item_fragment, mItems);
        setListAdapter(adapter);
    }

    public class FileSystemDBAgent extends AsyncTask<Void, Void, ArrayList<Item>> {

        private final String TAG = "FileSystemDBAgent (: ";

        private SQLiteDatabase db;
        private FileSystemDBHelper mDBHelper;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDBHelper = new FileSystemDBHelper(getContext());
        }

        @Override
        protected void onPostExecute(ArrayList<Item> items) {
            super.onPostExecute(items);
            if (items != null) {
                setNewItems(items);
            }
        }

        @Override
        protected ArrayList<Item> doInBackground(Void... params) {
            db = mDBHelper.getReadableDatabase();
            return getItemsListFromDB();
        }

        private ArrayList<Item> getItemsListFromDB() {
            ArrayList<Item> items = new ArrayList<>();
            Map<String, Integer> kidsPath = queryForList(db, path);
            if (kidsPath != null) {
                ArrayList<String> files = new ArrayList<>();
                Set<Map.Entry<String, Integer>> kidsPathSet = kidsPath.entrySet();
                for (Map.Entry<String, Integer> mKidsPathSet : kidsPathSet) {
                    if (mKidsPathSet.getValue() != 0) {
                        items.add(new Item(path,
                                new File(mKidsPathSet.getKey()),
                                mKidsPathSet.getValue())
                        );
                    }else{
                        files.add(mKidsPathSet.getKey());
                    }
                }
                for (String file: files) {
                    items.add(new Item(path,new File(file)));
                }
            }
            if (items == null)
                items.add(new Item(path, new File(path), 0));
            return items;
        }
    }
}
