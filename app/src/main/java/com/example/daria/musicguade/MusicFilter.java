package com.example.daria.musicguade;

import android.util.Log;

import java.io.File;
import java.io.FileFilter;

import static android.util.Log.d;
import static com.example.daria.musicguade.MainActivity.testPath;

/**
 * Created by yakov on 10.11.2017.
 */

public class MusicFilter implements FileFilter {

    private final String TAG = "MusicFilter (: ";

    private static final String[] audioFileExtension = {"wv", "wvc", "ape", "mpc", "mpp", "mp+",
            "mp4", "m4a", "m4b", "aac", "mp4", "m4a", "m4b", "lac", "spx", "wav", "oga", "ogg",
            "opus", "wav", "aiff", "mp3", "mp2", "mp1", "ogg", "xm", "it", "s3m", "mod", "mtm", "umx"};


    public MusicFilter(String filePath) {
        d(TAG, "constructor for "+filePath);
    }

    @Override
    public boolean accept(File pathname) {
        if (pathname.isDirectory()) {
            MusicFilter filter = new MusicFilter(pathname.getAbsolutePath().replace(testPath,""));
            File[] listAudioFiles = pathname.listFiles(filter);
            if (listAudioFiles != null && listAudioFiles.length != 0) {
                if (listAudioFiles.length == 1
                        && listAudioFiles[0].isDirectory()) {
                    d(TAG, "'" + listAudioFiles[0].getAbsolutePath().replace(testPath, "") + "' once folder!");
                }else Log.d(TAG, "'" + pathname.getAbsolutePath().replace(testPath, "") + "' folder correct!");
                return true;
            } /*else Log.w(TAG, "'" + pathname.getAbsolutePath().replace(testPath, "")
                    + "' folder is not displayed. There are no audio files in this folder.");*/
        } else {
            if (isAudioFile(pathname)) {
                return true;
            } /*else Log.w(TAG, "'" + pathname.getName()
                    + "' file is not displayed. This isn't an audio file");*/
        }
        return false;
    }

    /**
     * Check if the file is an audio file by file extension.
     *
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
     *
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
