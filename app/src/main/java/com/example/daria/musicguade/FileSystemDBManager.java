package com.example.daria.musicguade;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import static com.example.daria.musicguade.FileSystemContract.FilesTable;
import static com.example.daria.musicguade.FileSystemContract.ListTable;

public abstract class FileSystemDBManager {

    private static ContentValues toValues(String path, Integer dir) {
        ContentValues values = new ContentValues();
        values.put(FilesTable.COLUMN_PATH, path);
        values.put(FilesTable.COLUMN_IS_DIRECTORY, dir);
        return values;
    }

    private static ContentValues toValues(long file, long kid) {
        ContentValues values = new ContentValues();
        values.put(ListTable.COLUMN_FILE_ID, file);
        values.put(ListTable.COLUMN_KID_FILE_ID, kid);
        return values;
    }

    public static long insert(SQLiteDatabase db, String path, Integer dir) {
        return db.insert(FilesTable.TABLE_NAME, null, toValues(path, dir));
    }

    public static long insert(SQLiteDatabase db, long file, long kid) {
        return db.insert(ListTable.TABLE_NAME, null, toValues(file, kid));
    }

    public static int queryCount(SQLiteDatabase db, String tableName) {
        String cArg;
        switch (tableName) {
            case FilesTable.TABLE_NAME:
                cArg = "COUNT(" + FilesTable._ID + ")";
                break;
            case ListTable.TABLE_NAME:
                cArg = "COUNT(" + ListTable.COLUMN_FILE_ID + ")";
                break;
            default:
                cArg = "";
        }
        return query(db, tableName, cArg);

    }

    public static int query(SQLiteDatabase db, String tableName, String cArg) {
        if (cArg.equals("")) return -1;
        Cursor cursor = db.query(tableName,
                new String[]{cArg},
                null, null, null, null, null);
        cursor.moveToFirst();
        return cursor.getInt(cursor.getColumnIndex(cArg));
    }

    public static boolean emptyDBTables(SQLiteDatabase db) {
        int files = queryCount(db, FilesTable.TABLE_NAME);
        int list = queryCount(db, ListTable.TABLE_NAME);
        if (files == 0 || files == -1 || list == 0 || list == -1) {
            return true;
        }
        return false;
    }
}
