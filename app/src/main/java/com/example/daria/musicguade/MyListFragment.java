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
import android.widget.Toast;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import static com.example.daria.musicguade.MainActivity.testPath;

public class MyListFragment extends ListFragment {

    private final String TAG = "MyListFragment (: ";

    private ArrayList<Item> mItems;
    private final SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm");

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
        fillItemList(new File(testPath));
        MyListAdapter adapter = new MyListAdapter(getActivity(),
                R.layout.item_fragment, mItems);
        setListAdapter(adapter);
    }

    private void fillItemList(File mFile) {
        ArrayList<File> folders = new ArrayList<>();
        ArrayList<File> files = new ArrayList<>();

        if (mFile.listFiles() != null) {
            MusicFilter filter = new MusicFilter();
            File[] listAudioFiles = mFile.listFiles(filter);
            if (listAudioFiles != null) {
                Log.i(TAG, "There are " + mFile.listFiles().length
                        + " elements and " + listAudioFiles.length
                        + " audio elements in '" + mFile.getName() + "' folder");
                Log.d(TAG, "There are " + filter.getOnceFolders().toString().replace(testPath, "") + " once folders");
                for (File currentFile : listAudioFiles) {
                    if (currentFile.isDirectory()) {
                        folders.add(currentFile);
                    } else {
                        files.add(currentFile);
                    }
                }
                Log.i(TAG, "There are " + folders.size()
                        + " audio folder and " + files.size()
                        + " audio files in '" + mFile.getName() + "' folder");

                replaceOnceFolders(folders, filter.getOnceFolders());
                //Collections.sort(folders);
                Collections.sort(files);

                for (File currentFolder : folders) {
                    addFolderToItemList(currentFolder,
                            filter.getCountSubFiles().get(folders.indexOf(currentFolder)));
                }
                for (File currentFile : files) {
                    addFileToItemList(currentFile);
                }
            } else Log.w(TAG, "There are no audio files in '"
                    + mFile.getName() + "' folder.");
        } else Log.w(TAG, "'" + mFile.getName()
                + "' folder is empty");
    }

    private void replaceOnceFolders(ArrayList<File> folders, ArrayList<File> onceFolders) {
        for(File currentFolder : folders){
            for(File currentSubFolder : onceFolders){
                if(currentSubFolder.getAbsolutePath().contains(currentFolder.getName())){
                    folders.set(folders.indexOf(currentFolder),currentSubFolder);
                    onceFolders.remove(currentSubFolder);
                    break;
                }
            }
        }
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

    private void addFileToItemList(File file) {
        mItems.add(new Item(
                file.getAbsolutePath().replace(testPath, ""),
                "",
                toBytes(file.getTotalSpace()),
                formatter.format(new Date(file.lastModified())),
                file.getAbsolutePath()
        ));
    }

    private void addFolderToItemList(File folder, int count) {
        mItems.add(new Item(
                folder.getAbsolutePath().replace(testPath, ""),
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
