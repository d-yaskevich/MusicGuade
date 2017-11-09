package com.example.daria.musicguade;

import android.util.Log;

public class Item {

    private final String TAG = "Item (: ";

    private String name;
    private String count;
    private String data;
    private String date;
    private String path;
    private boolean isFolder;

    public Item(String name, String count, String data, String date, String path, boolean isFolder) {
        Log.i(TAG,"constructor, create new object");
        this.name = name;
        this.count = count;
        this.data = data;
        this.date = date;
        this.path = path;
        this.isFolder = isFolder;
    }

    public String getName() {
        return name;
    }

    public String getCount() {
        return count;
    }

    public String getData() {
        return data;
    }

    public String getDate() {
        return date;
    }

    public String getPath() {
        return path;
    }

    public boolean isFolder() {
        return isFolder;
    }
}
