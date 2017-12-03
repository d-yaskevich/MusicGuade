package com.example.daria.musicguade;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;

import static com.example.daria.musicguade.FileSystemContract.FilesTable;
import static com.example.daria.musicguade.FileSystemContract.ListTable;
import static com.example.daria.musicguade.FileSystemDBManager.insert;

public class FileSystemDBLoder {

    private final String TAG = "FileSystemDBLoder (: ";

    private SQLiteDatabase db;

    public FileSystemDBLoder(SQLiteDatabase db) {
        this.db = db;
    }

    /**
     * Upload data in AudioFileSystem DB.
     * This method check all files system for looking audio files and add them in DB
     *
     * @param path Root file path for adding
     */
    protected void onLoad(String path) {
        Log.d(TAG, Thread.currentThread().getId() + " thread ... Start load to DB for path:" + path);
        if (path != null) {
            File file = new File(path);
            AudioFilter filter = new AudioFilter();
            File[] list = file.listFiles(filter);
            if (list != null && list.length != 0) {
                long id = insert(db, path, 1);
                for (Long i : filter.IDs) {
                    insert(db, id, i);
                }
            } else Log.d(TAG, "Root folder is empty");
        }
        Log.d(TAG, Thread.currentThread().getId() + " thread ... Finish load to DB for path:" + path);
    }

    /**
     * Upload all data in AudioFileSystem DB.
     * This method just delete DB info if file from DB is not exists in files system
     */
    public void onUpload() {
        Log.d(TAG, Thread.currentThread().getId() + " thread ... Start upload to DB");
        Cursor cursor = db.query(FilesTable.TABLE_NAME, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String PATH = cursor.getString(cursor.getColumnIndex(FilesTable.COLUMN_PATH));
                File file = new File(PATH);
                if (!file.exists()) {
                    Log.d(TAG, "Delete - " + PATH);
                    Long ID = cursor.getLong(cursor.getColumnIndex(FilesTable._ID));
                    db.delete(FilesTable.TABLE_NAME, FilesTable.COLUMN_PATH + " = ?", new String[]{PATH});
                    db.delete(ListTable.TABLE_NAME, ListTable.COLUMN_KID_FILE_ID + " = ?", new String[]{String.valueOf(ID)});
                    if (file.isDirectory()) {
                        db.delete(ListTable.TABLE_NAME, ListTable.COLUMN_FILE_ID + " = ?", new String[]{String.valueOf(ID)});
                    }
                }
            } while (cursor.moveToNext());
        }
        Log.d(TAG, Thread.currentThread().getId() + " thread ... Finish upload to DB");
    }

    public class AudioFilter implements FileFilter {

        private final String TAG = "AudioFilter (: ";

        private final String[] audioFileExtension = {"wv", "wvc", "ape", "mpc", "mpp", "mp+",
                "mp4", "m4a", "m4b", "aac", "mp4", "m4a", "m4b", "lac", "spx", "wav", "oga", "ogg",
                "opus", "wav", "aiff", "mp3", "mp2", "mp1", "ogg", "xm", "it", "s3m", "mod", "mtm", "umx"};

        private ArrayList<Long> IDs;

        public AudioFilter() {
            this.IDs = new ArrayList<>();
        }

        @Override
        public boolean accept(File file) {
            if (file.isDirectory()) {
                AudioFilter filter = new AudioFilter();
                File[] list = file.listFiles(filter);
                if (list != null && list.length != 0) {
                    long id = insert(db, file.getAbsolutePath(), 1);
                    if (list.length == 1 && list[0].isDirectory()) {
                        IDs.addAll(filter.IDs);
                    } else {
                        for (Long i : filter.IDs) {
                            insert(db, id, i);
                        }
                        IDs.add(id);
                    }
                    return true;
                }
            } else {
                if (isAudioFile(file.getName())) {
                    long id = insert(db, file.getAbsolutePath(), 0);
                    IDs.add(id);
                    return true;
                }
            }
            return false;
        }

        /**
         * Check if the file is an audio file by file extension.
         *
         * @param name is checked file name
         * @return true if the file is an audio file, otherwise false.
         */
        private boolean isAudioFile(String name) {
            for (String currentExtension : audioFileExtension) {
                if (name.toLowerCase().endsWith(currentExtension)) {
                    return true;
                }
            }
            return false;
        }
    }
}
