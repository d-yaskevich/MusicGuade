package com.example.daria.musicguade;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView address;
    private final String TAG = "MainActivity (: ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG,"onCreate()");
        setContentView(R.layout.activity_main);
        address = (TextView) findViewById(R.id.address_view);
    }

    @Override
    protected void onStart() {
        Log.i(TAG,"onStart()");
        super.onStart();
    }

    @Override
    protected void onResume() {
        Log.i(TAG,"onResume()");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.i(TAG,"onPause()");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.i(TAG,"onStop()");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG,"onDestroy()");
        super.onDestroy();
    }
}
