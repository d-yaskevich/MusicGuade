package com.example.daria.musicguade;

import android.os.Parcel;
import android.os.Parcelable;

public class Item implements Parcelable {

    private final String TAG = "Item (: ";

    private String name;
    private String count;
    private String data;
    private String date;
    private String path;

    public Item(String name, String count, String data, String date, String path) {
        this.name = name;
        if (count.compareTo("") == 0) {
            this.count = null;
        } else this.count = count;
        this.data = data;
        this.date = date;
        this.path = path;
    }

    protected Item(Parcel in) {
        name = in.readString();
        count = in.readString();
        data = in.readString();
        date = in.readString();
        path = in.readString();
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

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
        if (getCount() == null) {
            return false;
        } else return true;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(count);
        dest.writeString(data);
        dest.writeString(date);
        dest.writeString(path);
    }
}
