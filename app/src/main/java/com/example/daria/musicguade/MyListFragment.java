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
import android.widget.Toast;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Set;

public class MyListFragment extends ListFragment {

    private final String TAG = "MyListFragment (: ";

    private final SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm");
    private final String DEFAULT_PATH = "/mnt/";

    private MyListAdapter adapter;
    private ArrayList<Item> mItems;
    private String path;

    public MyListFragment() {
        this.path = DEFAULT_PATH;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i(TAG, "onAttach()");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG, "onActivityCreated()");
        setRetainInstance(true);
        mItems = new ArrayList<>();
        new FillList().execute(new File(path));
        adapter = new MyListAdapter(getActivity(), R.layout.item_fragment, mItems);
        setListAdapter(adapter);
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
                "PUSH " + getListView().getItemAtPosition(position).toString(),
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

    public void setPath(String path) {
        this.path = path;
    }

    private class FillList extends AsyncTask<File, Integer, MusicFilter> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected MusicFilter doInBackground(File... params) {
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
                    return filter;
                } else Log.w(TAG, "There are no audio files in '"
                        + params[0].getName() + "' folder.");
            } else Log.w(TAG, "'" + params[0].getName()
                    + "' folder is empty");
            return null;
        }

        @Override
        protected void onPostExecute(MusicFilter musicFilter) {
            super.onPostExecute(musicFilter);
            if (musicFilter != null) {
                Set<Map.Entry<File, Integer>> foldersNameSet = musicFilter.getSubFolders().entrySet();
                for (Map.Entry<File, Integer> currentFoldersName : foldersNameSet) {
                    addFolderToItemList(currentFoldersName.getKey(),
                            currentFoldersName.getValue());
                }
                ArrayList<File> filesName = musicFilter.getSubFiles();
                Collections.sort(filesName);
                for (File currentFileName : filesName) {
                    addFileToItemList(currentFileName);
                }
                setListAdapter(adapter);
            } else {
                Toast.makeText(getActivity(),
                        "Folder is empty",
                        Toast.LENGTH_LONG).show();
            }

        }
    }
}
