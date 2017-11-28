package com.example.daria.musicguade;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class MusicFilter implements FileFilter {

    private final String TAG = "MusicFilter (: ";

    private static final String[] audioFileExtension = {"wv", "wvc", "ape", "mpc", "mpp", "mp+",
            "mp4", "m4a", "m4b", "aac", "mp4", "m4a", "m4b", "lac", "spx", "wav", "oga", "ogg",
            "opus", "wav", "aiff", "mp3", "mp2", "mp1", "ogg", "xm", "it", "s3m", "mod", "mtm", "umx"};

    private Map<File, Integer> mSubFolders;
    private ArrayList<File> mSubFiles;

    public MusicFilter() {
        mSubFolders = new TreeMap<>();
        mSubFiles = new ArrayList<>();
    }

    @Override
    public boolean accept(File pathname) {
        if (pathname.isDirectory()) {
            String[] list = pathname.list(new MusicFileFilter());
            if (list != null && list.length != 0) {
                mSubFolders.put(pathname, list.length);
                return true;
            } else {
                MusicFilter filter = new MusicFilter();
                File[] listAudioFiles = pathname.listFiles(filter);
                if (listAudioFiles != null && listAudioFiles.length != 0) {
                    if (listAudioFiles.length == 1
                            && listAudioFiles[0].isDirectory()) {
                        mSubFolders.putAll(filter.getSubFolders());
                    } else {
                        mSubFolders.put(pathname, listAudioFiles.length);
                    }
                    return true;
                }
            }
        } else {
            if (isAudioFile(pathname.getName())) {
                mSubFiles.add(pathname);
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
    public static boolean isAudioFile(String name) {
        for (String currentExtension : audioFileExtension) {
            if (name.toLowerCase().endsWith(currentExtension)) {
                return true;
            }
        }
        return false;
    }

    public Map<File, Integer> getSubFolders() {
        return mSubFolders;
    }

    public ArrayList<File> getSubFiles() {
        return mSubFiles;
    }

    private class MusicFileFilter implements FilenameFilter {


        @Override
        public boolean accept(File dir, String name) {
            if (isAudioFile(name)) {
                return true;
            }
            return false;
        }
    }
}
