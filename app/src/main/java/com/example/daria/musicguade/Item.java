package com.example.daria.musicguade;

public class Item {

    private final String TAG = "Item (: ";

    private String name;
    private String count;
    private String data;
    private String date;
    private String path;

    public Item(String name, String count, String data, String date, String path) {
        this.name = name;
        if (count.compareTo("") == 0){
            this.count = null;
        }else this.count = count;
        this.data = data;
        this.date = date;
        this.path = path;
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
        if(getCount() == null){
            return false;
        }else return true;
    }

    @Override
    public String toString() {
        return name;
    }
}
