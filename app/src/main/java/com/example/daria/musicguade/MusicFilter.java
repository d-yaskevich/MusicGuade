package com.example.daria.musicguade;

import android.util.Log;

import java.io.File;
import java.io.FileFilter;

import static android.content.ContentValues.TAG;

/**
 * Created by yakov on 10.11.2017.
 */

public class MusicFilter implements FileFilter {

    private static final String[] audioFileExtension = {"wv", "wvc", "ape", "mpc", "mpp", "mp+",
            "mp4", "m4a", "m4b", "aac", "mp4", "m4a", "m4b", "lac", "spx", "wav", "oga", "ogg",
            "opus", "wav", "aiff", "mp3", "mp2", "mp1", "ogg", "xm", "it", "s3m", "mod", "mtm", "umx"};

    @Override
    public boolean accept(File folder) {
        if (isAudioFolder(folder)) {
            return true;
        } else Log.w(TAG, "'" + folder.getName()
                + "' folder is not displayed. There are no audio files in this folder.");
        return false;
    }

    /**
     * Checks if there are audio files in the folder.
     * Does not scan subfolders.
     * @param folder is checked folder.
     * @return true if there are audio files in this folder, otherwise false.
     */
    public static boolean isAudioFolder(File folder) {
        for (File currentFile : folder.listFiles()) {
            if (!currentFile.isDirectory()) {
                if (isAudioFile(currentFile)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Check if the file is an audio file by file extension.
     * @param file is checked file
     * @return true if the file is an audio file, otherwise false.
     */
    public static boolean isAudioFile(File file) {
        String fileExtension = getFileExtension(file);
        for (String currentExtension : audioFileExtension) {
            if (currentExtension.compareToIgnoreCase(fileExtension) == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get the file extension.
     * @param file
     * @return file extension.
     */
    public static String getFileExtension(File file) {
        String fileName = file.getName();
        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        else return "";
    }
}
