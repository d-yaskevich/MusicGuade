package com.example.daria.musicguade;

/**
 * OnChangeFragmentStateListener interface for communicating the ListFragment with Activity
 */

interface OnChangeFragmentStateListener {

    /**
     * Use to send information about the item that was pressed
     * @param path information to send
     */
    void onItemSelected(String path);

    /**
     * Use to send basic information about a fragment as it changes
     * @param path information to send
     */
    void uploadPath(String path);
}
