package com.example.daria.musicguade;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 1;
    private TextView address;
    private final String TAG = "MainActivity "+this.hashCode()+" (: ";
    final static String PATH = "path";
    private final String testPath = "/mnt/sdcard/";

    private Fragment fragment;
    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    private static String FRAGMENT_INSTANCE_NAME = "fragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate()");
        setContentView(R.layout.activity_main);
        address = (TextView) findViewById(R.id.address_view);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            address.setText(testPath);

            mFragmentManager = getFragmentManager();
            fragment = mFragmentManager.findFragmentByTag(FRAGMENT_INSTANCE_NAME);
            if (fragment == null) {
                fragment = new MyListFragment();
                Bundle bundle = new Bundle();
                bundle.putString(PATH, testPath);
                fragment.setArguments(bundle);
                mFragmentTransaction = mFragmentManager.beginTransaction();
                mFragmentTransaction.add(R.id.fragment_place, fragment, FRAGMENT_INSTANCE_NAME)
                        .addToBackStack(testPath)
                        .commit();
            }

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_CODE);
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE && grantResults.length == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                address.setText(testPath);
            } else {
                Log.w(TAG, "Permission denied!");
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onStart() {
        Log.i(TAG, "onStart()");
        super.onStart();
    }

    @Override
    protected void onResume() {
        Log.i(TAG, "onResume()");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.i(TAG, "onPause()");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.i(TAG, "onStop()");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, "onDestroy()");
        super.onDestroy();
    }
}
