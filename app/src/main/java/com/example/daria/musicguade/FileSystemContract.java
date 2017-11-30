package com.example.daria.musicguade;

import android.provider.BaseColumns;

public final class FileSystemContract {

    public static final String INTEGER_TYPE = " INTEGER";
    public static final String TEXT_TYPE = " TEXT";
    public static final String NOT_NULL = " NOT NULL";
    public static final String COMMA_SEP = ", ";

    public FileSystemContract() {
    }

    public static abstract class FilesTable implements BaseColumns {

        public static final String TABLE_NAME = "files";
        public static final String COLUMN_PATH = "pathname";
        public static final String COLUMN_IS_DIRECTORY = "directory";

        protected static final String SQL_CREATE_TABLE_FILES =
                "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                        FilesTable._ID + INTEGER_TYPE + " PRIMARY KEY AUTOINCREMENT" + COMMA_SEP +
                        FilesTable.COLUMN_PATH + TEXT_TYPE + NOT_NULL + COMMA_SEP +
                        FilesTable.COLUMN_IS_DIRECTORY + INTEGER_TYPE + NOT_NULL + ");";

        protected static final String SQL_DELETE_TABLE_FILES =
                "DROP TABLE IF EXISTS " + FilesTable.TABLE_NAME + ";";
    }

    public static abstract class ListTable implements BaseColumns {

        public static final String TABLE_NAME = "list";
        public static final String COLUMN_FILE_ID = "file_id";
        public static final String COLUMN_KID_FILE_ID = "kidfile_id";

        protected static final String SQL_CREATE_TABLE_LIST =
                "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                        ListTable.COLUMN_FILE_ID + INTEGER_TYPE + NOT_NULL + COMMA_SEP +
                        ListTable.COLUMN_KID_FILE_ID + INTEGER_TYPE + NOT_NULL + COMMA_SEP +
                        "PRIMARY KEY (" + ListTable.COLUMN_FILE_ID + COMMA_SEP + ListTable.COLUMN_KID_FILE_ID + ")" + COMMA_SEP +
                        "FOREIGN KEY (" + COLUMN_FILE_ID + ") REFERENCES " + FilesTable.TABLE_NAME + "(" + FilesTable._ID + ")" + COMMA_SEP +
                        "FOREIGN KEY (" + COLUMN_KID_FILE_ID + ") REFERENCES " + FilesTable.TABLE_NAME + "(" + FilesTable._ID + "));";

        protected static final String SQL_DELETE_TABLE_LIST =
                "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
    }
}
