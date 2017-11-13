package com.example.daria.musicguade;

import java.util.ArrayList;

public interface OnItemSelectedListener {
    void onItemSelected(String path);
    void saveData(ArrayList<Item> mItem, String path);
}
