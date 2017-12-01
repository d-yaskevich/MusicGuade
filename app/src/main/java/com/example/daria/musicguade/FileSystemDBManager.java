package com.example.daria.musicguade;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.Map;
import java.util.TreeMap;

import static com.example.daria.musicguade.FileSystemContract.FilesTable;
import static com.example.daria.musicguade.FileSystemContract.ListTable;

public abstract class FileSystemDBManager {

    private static final String TAG = "FileSystemDBManager (: ";

    private static ContentValues toValues(String path, Integer dir) {
        ContentValues values = new ContentValues();
        values.put(FilesTable.COLUMN_PATH, path);
        values.put(FilesTable.COLUMN_IS_DIRECTORY, dir);
        return values;
    }

    private static ContentValues toValues(Long file, Long kid) {
        ContentValues values = new ContentValues();
        values.put(ListTable.COLUMN_FILE_ID, file);
        values.put(ListTable.COLUMN_KID_FILE_ID, kid);
        return values;
    }

    public static Long insert(SQLiteDatabase db, String path, Integer dir) {
        return db.insert(FilesTable.TABLE_NAME, null, toValues(path, dir));
    }

    public static Long insert(SQLiteDatabase db, Long file, Long kid) {
        return db.insert(ListTable.TABLE_NAME, null, toValues(file, kid));
    }

    /**
     * Query for a DB to find the count of rows in the column.
     * Use WHERE if you want " = ?"
     *
     * @param db           DB for query
     * @param tableName    Table name for query
     * @param column       Column for count
     * @param selection    Column for WHERE
     * @param selectionArg Argument for WHERE
     * @return int: the count of rows in the column, -1 if is empty result
     */
    public static int queryCount(SQLiteDatabase db, String tableName, String column, String selection, Object selectionArg) {
        String[] columns = new String[]{"COUNT(" + column + ")"};
        Cursor cursor;
        if (selection == null || selectionArg == null) {
            cursor = db.query(tableName, columns, null, null, null, null, null, null);
        } else {
            selection += " = ?";
            String[] selectionArgs = new String[]{String.valueOf(selectionArg)};
            cursor = db.query(tableName, columns, selection, selectionArgs, null, null, null);
        }
        if (cursor.moveToFirst())
            return cursor.getInt(cursor.getColumnIndex(columns[0]));
        return -1;
    }

    /**
     * Checks an empty tables: FilesTable and ListTable
     *
     * @param db DB for check
     * @return true if at least one table is empty, otherwise false
     */
    public static boolean emptyDBTables(SQLiteDatabase db) {
        int files = queryCount(db, FilesTable.TABLE_NAME, FilesTable._ID, null, null);
        int list = queryCount(db, ListTable.TABLE_NAME, ListTable.COLUMN_FILE_ID, null, null);
        if (files == 0 || list == 0) {
            if (files == -1 || list == -1)
                Log.w(TAG, "Maybe query is not correct or tables is not create");
            return true;
        }
        return false;
    }

    /**
     * Query for a DB with WHERE " = ?"
     *
     * @param db           DB for query
     * @param tableName    Table name for query
     * @param selection    Column for WHERE
     * @param selectionArg Argument for WHERE
     * @return Cursor object with result of query. Result include all columns of table.
     */
    public static Cursor queryWhereEquality(SQLiteDatabase db, String tableName, String selection, Object selectionArg) {
        selection += " = ?";
        String[] selectionArgs = new String[]{String.valueOf(selectionArg)};
        return db.query(tableName, null, selection, selectionArgs, null, null, null, null);
    }

    /**
     * Getting an ID at the path from FilesTable
     *
     * @param db   DB for getting
     * @param path File path for getting ID
     * @return long ID at the path, null if this path didn't find in the table
     */
    public static Long getIDFromDBFilesTable(SQLiteDatabase db, String path) {
        Cursor cursor = queryWhereEquality(db, FilesTable.TABLE_NAME, FilesTable.COLUMN_PATH, path);
        if (cursor.moveToFirst()) {
            return cursor.getLong(cursor.getColumnIndex(FilesTable._ID));
        }
        return null;
    }

    /**
     * Getting an path at the ID from FilesTable
     *
     * @param db DB for getting
     * @param ID File ID for getting path
     * @return String path at the ID, null if this ID didn't find in the table
     */
    public static String getPathFromDBFilesTable(SQLiteDatabase db, Long ID) {
        Cursor cursor = getCursorFromDBFilesTable(db, ID);
        if (cursor.moveToFirst()) {
            return cursor.getString(cursor.getColumnIndex(FilesTable.COLUMN_PATH));
        }
        return null;
    }

    /**
     * Getting an path at the Cursor object with data about all columns from FilesTable
     *
     * @param cursor Cursor object with data about all columns from FilesTable
     * @return String path at the ID, null if this ID didn't find in the table
     */
    public static String getPathFromDBFilesTable(Cursor cursor) {
        if (cursor.moveToFirst())
            return cursor.getString(cursor.getColumnIndex(FilesTable.COLUMN_PATH));
        return null;
    }

    /**
     * Getting an dir at the ID from FilesTable
     *
     * @param db DB for getting
     * @param ID File ID for getting dir
     * @return int dir at the ID, -1 if this ID didn't find in the table
     */
    public static int getDirFromDBFilesTable(SQLiteDatabase db, Long ID) {
        Cursor cursor = getCursorFromDBFilesTable(db, ID);
        if (cursor.moveToFirst()) {
            return cursor.getInt(cursor.getColumnIndex(FilesTable.COLUMN_IS_DIRECTORY));
        }
        return -1;
    }

    /**
     * Getting an path at the Cursor object with data about all columns from FilesTable
     *
     * @param cursor Cursor object with data about all columns from FilesTable
     * @return int dir at the ID, -1 if this ID didn't find in the table
     */
    public static int getDirFromDBFilesTable(Cursor cursor) {
        if (cursor.moveToFirst())
            return cursor.getInt(cursor.getColumnIndex(FilesTable.COLUMN_IS_DIRECTORY));
        return -1;
    }

    /**
     * Query for a DB with WHERE of equality with ID
     *
     * @param db DB for query
     * @param ID ID for query
     * @return Cursor object with result about all columns FilesTable
     */
    public static Cursor getCursorFromDBFilesTable(SQLiteDatabase db, Long ID) {
        return queryWhereEquality(db, FilesTable.TABLE_NAME, FilesTable._ID, ID);
    }

    public static Map<String, Integer> queryForList(SQLiteDatabase db, String path) {
        long ID = getIDFromDBFilesTable(db, path);
        if (ID == -1) {
            Log.w(TAG, "Folder(" + path + ") don't find in " + FilesTable.TABLE_NAME + " table!");
            return null;
        }
        long kidID;
        Map<String, Integer> kidsList = new TreeMap<>();
        Cursor cursor = queryWhereEquality(db, ListTable.TABLE_NAME, ListTable.COLUMN_FILE_ID, ID);
        if (cursor.moveToFirst()) {
            do {
                kidID = cursor.getLong(cursor.getColumnIndex(ListTable.COLUMN_KID_FILE_ID));
                String kidPATH;
                int kidDIR;
                Cursor kidCursor = getCursorFromDBFilesTable(db, kidID);
                if (kidCursor.moveToFirst()) {
                    kidPATH = getPathFromDBFilesTable(kidCursor);
                    kidDIR = getDirFromDBFilesTable(kidCursor);
                    int count = 0;
                    if (kidDIR == 1) {
                        count = queryCount(db, ListTable.TABLE_NAME, ListTable.COLUMN_KID_FILE_ID, ListTable.COLUMN_FILE_ID, kidID);
                    }
                    kidsList.put(kidPATH, count);
                } else {
                    Log.w(TAG, "kidID=" + kidID + " in folder(" + path + ") don't include in " + FilesTable.TABLE_NAME + " table!");
                    return null;
                }
            } while (cursor.moveToNext());
        } else {
            Log.w(TAG, "Folder(" + path + ") don't contain kids!");
            return null;
        }
        return kidsList;
    }
}
