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
    private String date;
    private String data;
    private String count;
    private String path;
    private int image;
    private boolean folder = false;

    protected Item(Parcel in) {
        String[] s = new String[5];
        in.readStringArray(s);
        name = s[0];
        count = s[1];
        data = s[2];
        date = s[3];
        path = s[4];
        image = in.readInt();
        boolean[] f = new boolean[1];
        in.readBooleanArray(f);
        folder = f[0];
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

    /**
     * Use when file isn't folder
     * @param path parent path
     * @param file current file for item
     */
    public Item(String path, File file) {
        if (file.isDirectory()) {
            this.image = R.drawable.ic_folder_black_24px;
            this.folder = true;
        } else {
            this.image = R.drawable.ic_music_note_black_24px;
        }
        this.count = "0";
        this.name = file.getAbsolutePath().replace(path + File.separator, "");
        this.data = toBytes(file.length());
        this.date = formatter.format(new Date(file.lastModified()));
        this.path = file.getAbsolutePath();
    }

    /**
     * Use if file is folder
     * @param path parent path
     * @param file current folder for item
     * @param count count input files
     */
    public Item(String path, File file, int count) {
        this(path,file);
        if (file.isDirectory()) {
            this.count = toObject(count);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {name,count,data,date,path});
        dest.writeInt(image);
        dest.writeBooleanArray(new boolean[]{folder});
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

    public int getImage() {
        return image;
    }

    public String getPath() {
        return path;
    }

    public boolean isFolder() {
        return folder;
    }

    @Override
    public String toString() {
        return name;
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
}
