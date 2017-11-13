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

import static com.example.daria.musicguade.MainActivity.ITEM_LIST;
import static com.example.daria.musicguade.MainActivity.PATH;

public class MyListFragment extends ListFragment {

    private final String TAG = "MyListFragment "+this.hashCode()+" (: ";

    private final SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm");
    private final String DEFAULT_PATH = "/mnt/";

    private MyListAdapter adapter;
    private ArrayList<Item> mItems;
    private String path;
    private View view = null;

    OnItemSelectedListener mItemSelectedListener;

    public MyListFragment() {
        this.path = DEFAULT_PATH;
        this.setRetainInstance(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i(TAG, "onAttach()");
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mItemSelectedListener = (OnItemSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView()");
        if(view == null) {
            Log.d(TAG,"view != null");
            view = inflater.inflate(R.layout.list_fragment, null);
            Bundle bundle = getArguments();
            if (bundle != null) {
                Log.d(TAG,"bundle != null");
                path = bundle.getString(PATH);
                if(mItems == null){
                    Log.d(TAG,"mItems == null");
                    mItems = new ArrayList<>();
                    if(bundle.getParcelableArrayList(ITEM_LIST) != null
                            && bundle.getParcelableArrayList(ITEM_LIST).size() != 0){
                        mItems = bundle.getParcelableArrayList(ITEM_LIST);
                        Log.d(TAG,"bundle.getParcelableArrayList(ITEM_LIST) != null");
                    }else{
                        new FillList().execute(new File(path));
                    }
                }
            }
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
        // Send the event to the host activity
        mItemSelectedListener.onItemSelected(item.getPath());
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
        mItemSelectedListener.saveData(mItems,path);
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
