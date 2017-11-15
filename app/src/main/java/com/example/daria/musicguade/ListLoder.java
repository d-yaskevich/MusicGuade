package com.example.daria.musicguade;

import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * Created by yakov on 13.11.2017.
 */

public class ListLoder extends AsyncTask<File, Integer, ArrayList<Item>> {

    private final String TAG = "ListLoder (: ";

    private MyListFragment fragment;

    public ListLoder(MyListFragment fragment) {
        this.fragment = fragment;
    }

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
        if (items == null) {
            items = new ArrayList<>();
            items.add(new Item("/mnt", new File(fragment.getPath()), 0));
        }
        fragment.setNewItems(items);
    }

}
