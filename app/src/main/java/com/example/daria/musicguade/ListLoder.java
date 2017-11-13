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

import static com.example.daria.musicguade.MainActivity.FRAGMENT_INSTANCE_NAME;

/**
 * Created by yakov on 13.11.2017.
 */

public class ListLoder extends AsyncTask<File, Integer, ArrayList<Item>> {

    private final String TAG = "ListLoder (: ";

    private final SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm");
    private final String DEFAULT_PATH = "/mnt/";

    private MainActivity main;
    private ArrayList<Item> mItems;
    private String path;

    public ListLoder(MainActivity mainActivity) {
        this.main = mainActivity;
        mItems = new ArrayList<>();
    }

    @Override
    protected ArrayList<Item> doInBackground(File... params) {
        if (params[0] == null) {
            path = DEFAULT_PATH;
        } else {
            path = params[0].getAbsolutePath();
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
                    for (Map.Entry<File, Integer> currentFoldersName : foldersNameSet) {
                        addFolderToItemList(currentFoldersName.getKey(),
                                currentFoldersName.getValue());
                    }
                    ArrayList<File> filesName = filter.getSubFiles();
                    Collections.sort(filesName);
                    for (File currentFileName : filesName) {
                        addFileToItemList(currentFileName);
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
        mItems = items;
        main.sendDataToFragment(mItems);
        main.getFragmentManager().beginTransaction()
                .replace(R.id.fragment_place, main.getFragment(), FRAGMENT_INSTANCE_NAME)
                .commit();
    }

    private void addFileToItemList(File file) {
        mItems.add(new Item(
                file.getAbsolutePath().replace(path, ""),
                "",
                toBytes(file.getTotalSpace()),
                formatter.format(new Date(file.lastModified())),
                file.getAbsolutePath()
        ));
    }

    private void addFolderToItemList(File folder, int count) {
        mItems.add(new Item(
                folder.getAbsolutePath().replace(path, ""),
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
