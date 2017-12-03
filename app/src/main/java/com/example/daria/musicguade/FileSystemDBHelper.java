package com.example.daria.musicguade;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static com.example.daria.musicguade.FileSystemContract.FilesTable;
import static com.example.daria.musicguade.FileSystemContract.ListTable;
import static com.example.daria.musicguade.FileSystemDBManager.queryWhereEquality;

public class FileSystemDBHelper extends SQLiteOpenHelper {

    private final String TAG = "FileSystemDBHelper (: ";

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "AudioFileSystem.db";

    private String rootPath;

    public FileSystemDBHelper(Context context, String rootPath) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.rootPath = rootPath;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, Thread.currentThread().getId() + " - onCreate() DB");
        db.execSQL(FilesTable.SQL_CREATE_TABLE_FILES);
        db.execSQL(ListTable.SQL_CREATE_TABLE_LIST);
        onLoad(db);
    }

    public void onRecreate(SQLiteDatabase db) {
        Log.i(TAG, Thread.currentThread().getId() + " - onRecreate() DB");
        db.execSQL(FilesTable.SQL_DELETE_TABLE_FILES);
        db.execSQL(ListTable.SQL_DELETE_TABLE_LIST);
        onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG, Thread.currentThread().getId() + " -  onUpgrade() DB");
        db.execSQL(FilesTable.SQL_DELETE_TABLE_FILES);
        db.execSQL(ListTable.SQL_DELETE_TABLE_LIST);
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        Log.i(TAG, Thread.currentThread().getId() + " - onOpen() DB");
        super.onOpen(db);
    }

    public void onLoad(SQLiteDatabase db) {
        Log.i(TAG, Thread.currentThread().getId() + " - onLoad() DB");
        new FileSystemDBLoder(db).onLoad(rootPath);
    }


    public void onUpload(SQLiteDatabase db) {
        Log.i(TAG, Thread.currentThread().getId() + " - onUpload() DB");
        //check if root path is exist in DB
        Cursor cursor = queryWhereEquality(db, FilesTable.TABLE_NAME, FilesTable.COLUMN_PATH, rootPath);
        if (cursor.moveToFirst()) {
            new FileSystemDBLoder(db).onUpload();
        } else {
            onRecreate(db);
        }
    }
}
