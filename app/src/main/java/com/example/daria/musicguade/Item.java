package com.example.daria.musicguade;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Item implements Parcelable {

    private final String TAG = "Item (: ";

    public static final SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm");

    private String name;
    private String count;
    private String data;
    private String date;
    private String path;
    private int image;

    protected Item(Parcel in) {
        name = in.readString();
        count = in.readString();
        data = in.readString();
        date = in.readString();
        path = in.readString();
        image = in.readInt();
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

    public Item(String path, File file, int count) {
        if (file.isDirectory()) {
            this.count = toObject(count);
            this.image = R.drawable.ic_folder_black_24px;
        } else {
            this.count = null;
            this.image = R.drawable.ic_music_note_black_24px;
        }
        this.name = file.getAbsolutePath().replace(path + File.separator, "");
        this.data = "";
        this.date = formatter.format(new Date(file.lastModified()));
        this.path = file.getAbsolutePath();
    }

    public Item(String path, File file) {
        if (file.isDirectory()) {
            this.image = R.drawable.ic_folder_black_24px;
            this.count = "0";
        } else {
            this.image = R.drawable.ic_music_note_black_24px;
            this.count = null;
        }
        this.name = file.getAbsolutePath().replace(path + File.separator, "");
        this.data = toBytes(file.length());
        this.date = formatter.format(new Date(file.lastModified()));
        this.path = file.getAbsolutePath();
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
        dest.writeInt(image);
    }

    public static String toObject(int length) {
        String count = String.valueOf(length);
        if (length == 0 || length == 1) {
            count += " object";
        } else count += " objects";
        return count;
    }

    public static String toBytes(long totalSpace) {
        String space;
        if (totalSpace / (1024 * 1024) > 1) {
            space = new DecimalFormat("#0.00").format((double) totalSpace / (1024 * 1024));
            space += " MB";
        } else if (totalSpace / 1024 > 1) {
            space = new DecimalFormat("#0.00").format((double) totalSpace / 1024);
            space += " KB";
        } else {
            space = String.valueOf(totalSpace);
            space += " B";
        }
        return space;
    }

    public int getImage() {
        return image;
    }
}
