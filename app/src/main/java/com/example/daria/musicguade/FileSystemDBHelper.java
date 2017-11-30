package com.example.daria.musicguade;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static com.example.daria.musicguade.FileSystemContract.FilesTable;
import static com.example.daria.musicguade.FileSystemContract.ListTable;
import static com.example.daria.musicguade.MainActivity.mainPath;

public class FileSystemDBHelper extends SQLiteOpenHelper {

    private final String TAG = "FileSystemDBHelper (: ";

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "FileSystem.db";

    public FileSystemDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG,Thread.currentThread().getId()+" thread DB onCreate() ");
        db.execSQL(FilesTable.SQL_CREATE_TABLE_FILES);
        db.execSQL(ListTable.SQL_CREATE_TABLE_LIST);
        onLoad(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG,Thread.currentThread().getId()+" thread DB onUpgrade() ");
        db.execSQL(FilesTable.SQL_DELETE_TABLE_FILES);
        db.execSQL(ListTable.SQL_DELETE_TABLE_LIST);
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        Log.d(TAG,Thread.currentThread().getId()+" thread DB onOpen() ");
        super.onOpen(db);
    }

    public void onLoad(SQLiteDatabase db){
        new FileSystemDBLoder(db).onLoad(mainPath);
    }

    public void onRecreate(SQLiteDatabase db){
        Log.d(TAG,Thread.currentThread().getId()+" thread DB onRecreate() ");
        db.execSQL(FilesTable.SQL_DELETE_TABLE_FILES);
        db.execSQL(ListTable.SQL_DELETE_TABLE_LIST);
        onCreate(db);
    }

    public void onUpload(SQLiteDatabase db){
        Log.d(TAG,Thread.currentThread().getId()+" thread DB onUpload() ");
        new FileSystemDBLoder(db).onUpload(mainPath);
    }
}
