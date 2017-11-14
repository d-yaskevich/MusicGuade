package com.example.daria.musicguade;

import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * Created by yakov on 13.11.2017.
 */

public class ListLoder extends AsyncTask<File, Integer, ArrayList<Item>> {

    private final String TAG = "ListLoder (: ";

    private final SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm");

    private MyListFragment fragment;

    public ListLoder(MyListFragment fragment) {
        this.fragment = fragment;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected ArrayList<Item> doInBackground(File... params) {
        ArrayList<Item> mItems = new ArrayList<>();
        if (params[0] != null){
            String path = params[0].getAbsolutePath();
            if (params[0].listFiles() != null) {
                MusicFilter filter = new MusicFilter();
                File[] listAudioFiles = params[0].listFiles(filter);
                if (listAudioFiles != null) {
                    Log.i(TAG, "There are " + params[0].listFiles().length
                            + " elements and " + listAudioFiles.length
                            + " audio elements in '" + params[0].getName() + "' folder");
                    Log.i(TAG, "There are " + filter.getSubFolders().size()
                            + " audio folder and " + filter.getSubFiles().size()
                            + " audio files in '" + params[0].getName() + "' folder");

                    Set<Map.Entry<File, Integer>> foldersNameSet = filter.getSubFolders().entrySet();
                    Log.d(TAG,"folder absolute path: "+path);
                    for (Map.Entry<File, Integer> currentFoldersName : foldersNameSet) {
                        addFolderToItemList(mItems, currentFoldersName.getKey(), path,
                                currentFoldersName.getValue());
                    }
                    ArrayList<File> filesName = filter.getSubFiles();
                    Collections.sort(filesName);
                    for (File currentFileName : filesName) {
                        addFileToItemList(mItems, currentFileName, path);
                    }
                    return mItems;
                } else Log.w(TAG, "There are no audio files in '"
                        + params[0].getName() + "' folder.");
            } else Log.w(TAG, "'" + params[0].getName()
                    + "' folder is empty");
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Item> items) {
        super.onPostExecute(items);
        fragment.setNewItems(items);
    }

    private void addFileToItemList(ArrayList<Item> mItems, File file, String path) {
        mItems.add(new Item(
                file.getAbsolutePath().replace(path+File.separator, ""),
                "",
                toBytes(file.getTotalSpace()),
                formatter.format(new Date(file.lastModified())),
                file.getAbsolutePath()
        ));
    }

    private void addFolderToItemList(ArrayList<Item> mItems, File folder, String path, int count) {
        Log.d(TAG,"subfolder absolute path:"+folder.getAbsolutePath());
        mItems.add(new Item(
                folder.getAbsolutePath().replace(path+File.separator, ""),
                toObject(count),
                "",
                formatter.format(new Date(folder.lastModified())),
                folder.getAbsolutePath()
        ));
    }

    private String toObject(int length) {
        String count = String.valueOf(length);
        if (length == 0 || length == 1) {
            count += " object";
        } else count += " objects";
        return count;
    }

    private String toBytes(long totalSpace) {
        String space;
        if (totalSpace % 1000000000 > 1) {
            space = new DecimalFormat("#0.00").format((double) totalSpace / 1000000000);
            space += " GB";
        } else if (totalSpace % 1000000 > 1) {
            space = new DecimalFormat("#0.00").format((double) totalSpace / 1000000);
            space += " MB";
        } else if (totalSpace % 1000 > 1) {
            space = new DecimalFormat("#0.00").format((double) totalSpace / 1000);
            space += " KB";
        } else {
            space = String.valueOf(totalSpace);
            space += " B";
        }
        return space;
    }
}
