package com.example.daria.musicguade;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static com.example.daria.musicguade.FileSystemContract.FilesTable.SQL_CREATE_TABLE_FILES;
import static com.example.daria.musicguade.FileSystemContract.FilesTable.SQL_DELETE_TABLE_FILES;
import static com.example.daria.musicguade.FileSystemContract.ListTable.SQL_CREATE_TABLE_LIST;
import static com.example.daria.musicguade.FileSystemContract.ListTable.SQL_DELETE_TABLE_LIST;

public class FileSystemDBHelper extends SQLiteOpenHelper {

    private final String TAG = "FileSystemDBHelper (: ";

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "FileSystem.db";

    public FileSystemDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG,"Create files:"+SQL_CREATE_TABLE_FILES);
        Log.d(TAG,"Create list:"+SQL_CREATE_TABLE_LIST);
        db.execSQL(SQL_CREATE_TABLE_FILES);
        db.execSQL(SQL_CREATE_TABLE_LIST);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG,"Delete files:"+SQL_DELETE_TABLE_FILES);
        Log.d(TAG,"Delete list:"+SQL_DELETE_TABLE_LIST);
        db.execSQL(SQL_DELETE_TABLE_FILES);
        db.execSQL(SQL_DELETE_TABLE_LIST);
        onCreate(db);
    }
}
