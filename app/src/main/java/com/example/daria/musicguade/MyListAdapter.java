package com.example.daria.musicguade;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MyListAdapter extends ArrayAdapter {

    private final String TAG = "MyListAdapter (: ";

    private Context context;
    private ArrayList<Item> mItems;
    private int itemViewResId;

    public MyListAdapter(Context context, int itemViewResId, ArrayList<Item> mItems) {
        super(context, android.R.layout.simple_list_item_1);
        Log.i(TAG, "constructor, create new object");
        this.context = context;
        this.mItems = mItems;
        this.itemViewResId = itemViewResId;
    }

    public Item getItem(int position) {
        return mItems.get(position);
    }


    @Override
    public int getCount() {
        return mItems.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View item = inflater.inflate(itemViewResId, parent, false);

        TextView nameTextView = (TextView) item.findViewById(R.id.name_item);
        nameTextView.setText(mItems.get(position).getName());

        TextView dateTextView = (TextView) item.findViewById(R.id.info_item);
        dateTextView.setText(mItems.get(position).getDate());

        ImageView iconImageView = (ImageView) item.findViewById(R.id.icon_item);
        iconImageView.setImageResource(mItems.get(position).getImage());

        TextView subTextView = (TextView) item.findViewById(R.id.subname_item);
        if (mItems.get(position).isFolder()) {
            subTextView.setText(mItems.get(position).getCount());
        } else {
            subTextView.setText(mItems.get(position).getData());
        }
        return item;
    }
}
