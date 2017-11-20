package com.example.daria.musicguade;

import android.app.ListFragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

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
        mFragmentStateListener = (OnChangeFragmentStateListener) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView()");
        if (view == null) {
            view = inflater.inflate(R.layout.list_fragment, null);
            Bundle bundle = getArguments();
            if (bundle != null) {
                mItems = new ArrayList<>();
                path = bundle.getString(PATH);
                new ListLoder().execute(new File(path));
            }
        }
        if (path != null) {
            mFragmentStateListener.uploadPath(path);
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
        if (items == null) {
            items = new ArrayList<>();
            items.add(new Item(path, new File(path), 0));
        }
        adapter = new MyListAdapter(getActivity(), R.layout.item_fragment, items);
        setListAdapter(adapter);
    }

    public class ListLoder extends AsyncTask<File, Integer, ArrayList<Item>> {

        private final String TAG = "ListLoder (: ";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.i(TAG, "Start loading . . . ");
        }

        @Override
        protected ArrayList<Item> doInBackground(File... params) {
            ArrayList<Item> mItems = new ArrayList<>();
            if (params[0] != null) {
                String path = params[0].getAbsolutePath();
                if (params[0].listFiles() != null) {
                    MusicFilter filter = new MusicFilter();
                    File[] listAudioFiles = params[0].listFiles(filter);
                    if (listAudioFiles != null) {
                        Set<Map.Entry<File, Integer>> foldersNameSet = filter.getSubFolders().entrySet();
                        for (Map.Entry<File, Integer> currentFoldersName : foldersNameSet) {
                            mItems.add(new Item(path,
                                    currentFoldersName.getKey(),
                                    currentFoldersName.getValue()
                            ));
                        }
                        ArrayList<File> filesName = filter.getSubFiles();
                        Collections.sort(filesName);
                        for (File currentFileName : filesName) {
                            mItems.add(new Item(path, currentFileName));
                        }
                        return mItems;
                    } else Log.w(TAG, "There are no audio files in '"
                            + params[0].getName() + "' folder");
                } else Log.w(TAG, "'" + params[0].getName()
                        + "' folder is empty");
            } else Log.w(TAG, "Inner file is null");
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Item> items) {
            super.onPostExecute(items);
            Log.i(TAG, " . . . Finish loading!");
            mItems = items;
            setNewItems(mItems);
        }
    }
}
