package com.example.daria.musicguade;

import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class MyListFragment extends ListFragment {

    private final String TAG = "MyListFragment (: ";

    private ArrayList<Item> mItems;
    private final SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm");
    private final String[] audioFileExtension = {"wv", "wvc", "ape", "mpc", "mpp", "mp+",
            "mp4", "m4a", "m4b", "aac", "mp4", "m4a", "m4b", "lac", "spx", "wav", "oga", "ogg",
            "opus", "wav", "aiff", "mp3", "mp2", "mp1", "ogg", "xm", "it", "s3m", "mod", "mtm", "umx"};

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i(TAG, "onAttach()");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG, "onActivityCreated()");
        mItems = new ArrayList<>();
        fillItemList(Environment.getRootDirectory());
        MyListAdapter adapter = new MyListAdapter(getActivity(),
                R.layout.item_fragment, mItems);
        setListAdapter(adapter);
    }

    private void fillItemList(File mFile) {
        ArrayList<File> folders = new ArrayList<>();
        ArrayList<File> files = new ArrayList<>();

        if (mFile.listFiles() != null) {
            Log.i(TAG, "There are " + mFile.listFiles().length
                    + " elements in '" + mFile.getName() + "' folder");
            for (File curFile : mFile.listFiles()) {
                if (curFile.isDirectory()) {
                    folders.add(curFile);
                } else {
                    files.add(curFile);
                }

            }
            Log.i(TAG, "There are " + folders.size()
                    + " folder and " + files.size()
                    + " files in '" + mFile.getName() + "' folder");
            Collections.sort(folders);
            Collections.sort(files);

            for (File currentFolder : folders) {
                if(currentFolder.list() != null){
                    addFolderToItemList(currentFolder);
                }else Log.w(TAG, "'" + currentFolder.getName()
                        + "' folder is not displayed. There are no files in this folder.");
            }
            for (File currentFile : files) {
                if (isAudioFile(currentFile)) {
                    addFileToItemList(currentFile);
                } else Log.w(TAG, "'" + currentFile.getName()
                        + "' file is not displayed. This isn't an audio file");
            }
        } else Log.w(TAG, "list files is empty");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView()");
        return inflater.inflate(R.layout.list_fragment, null);
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

        Toast.makeText(getActivity(),
                getListView().getItemAtPosition(position).toString(),
                Toast.LENGTH_LONG).show();
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

    public ArrayList<Item> getItems() {
        return mItems;
    }

    private boolean isAudioFile(File file) {
        String fileExtension = getFileExtension(file);
        for (String extension : audioFileExtension) {
            if (extension.compareToIgnoreCase(fileExtension) == 0) {
                return true;
            }
        }
        return false;
    }

    private static String getFileExtension(File file) {
        String fileName = file.getName();
        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        else return "";
    }

    private void addFolderToItemList(File currentFolder) {
        mItems.add(new Item(
                currentFolder.getName(),
                toObject(currentFolder.list().length),
                "",
                formatter.format(new Date(currentFolder.lastModified())),
                currentFolder.getAbsolutePath()
        ));
    }

    private void addFileToItemList(File currentFile) {
        mItems.add(new Item(
                currentFile.getName(),
                "",
                toBytes(currentFile.getTotalSpace()),
                formatter.format(new Date(currentFile.lastModified())),
                currentFile.getAbsolutePath()
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
