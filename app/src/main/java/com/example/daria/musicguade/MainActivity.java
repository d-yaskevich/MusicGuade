package com.example.daria.musicguade;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity
        implements OnChangeFragmentStateListener {

    private final String TAG = "MainActivity " + this.hashCode() + " (: ";

    private static final int REQUEST_CODE_EXTERNAL_STORAGE = 1;
    public final static String FRAGMENT_INSTANCE_NAME = "fragment";
    public final static String PATH = "path";

    public static String mainPath = Environment.getExternalStorageDirectory().toString();

    public TextView address;
    private Fragment fragment;
    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate()");
        setContentView(R.layout.activity_main);
        address = (TextView) findViewById(R.id.address_view);
    }

    @Override
    protected void onStart() {
        Log.i(TAG, "onStart()");
        super.onStart();
        if (checkPermissions()) {
            createUI();
        } else {
            requestPermissions();
        }
    }

    @Override
    protected void onResume() {
        Log.i(TAG, "onResume()");
        super.onResume();
    }

    /**
     * Create UI as a fragment.
     * If the old fragment does not found:
     * creates a new one with create new data for it.
     */
    private void createUI() {
        mFragmentManager = getFragmentManager();
        fragment = mFragmentManager.findFragmentByTag(FRAGMENT_INSTANCE_NAME);
        if (fragment == null) {
            fragment = createFragmentWithData(mainPath);
            mFragmentManager.beginTransaction()
                    .add(R.id.fragment_place, fragment, FRAGMENT_INSTANCE_NAME)
                    .commit();
        }
    }

    /**
     * Create new fragment and push data to the fragment.
     *
     * @param path
     * @return new object MyListFragment class
     */
    public MyListFragment createFragmentWithData(String path) {
        MyListFragment fragment = new MyListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(PATH, path);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onItemSelected(String newPath) {
        File file = new File(newPath);
        if (file.isDirectory()) {
            fragment = createFragmentWithData(newPath);
            mFragmentManager.beginTransaction()
                    .replace(R.id.fragment_place, fragment, FRAGMENT_INSTANCE_NAME)
                    .addToBackStack(null)
                    .commit();
        } else {
            Toast.makeText(this, "Can not open this file :(", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    @Override
    public void uploadPath(String newPath) {
        address.setText(newPath);
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

    /**
     * Check permission READ_EXTERNAL_STORAGE
     *
     * @return true if permission granted,
     * false otherwise
     */
    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Start the request for permission READ_EXTERNAL_STORAGE
     */
    private void startPermissionRequest() {
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                REQUEST_CODE_EXTERNAL_STORAGE);
    }

    /**
     * Checks whether it is necessary to provide additional rationale to the user.
     */
    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.READ_EXTERNAL_STORAGE);

        if (shouldProvideRationale) {
            // This would happen if the user denied the request previously,
            // but didn't check the "Don't ask again" checkbox.
            showAlertDialog(R.string.permission_required,
                    R.string.permission_message_one,
                    R.string.settings,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int arg1) {
                            // Request permission
                            startPermissionRequest();
                        }
                    });
        } else {
            Log.i(TAG, "Requesting permission");
            // It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            startPermissionRequest();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_EXTERNAL_STORAGE) {
            if (grantResults.length <= 0) {
                //If user interaction was interrupted, the permission request is cancelled and you
                //receive empty arrays.
                if (fragment != null) {
                    mFragmentManager.beginTransaction().remove(fragment);
                }
                Log.w(TAG, "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted.
                createUI();
            } else {
                //Permission denied.
                //Notify the user via a AlertDialog that they have rejected a core permission for the
                //app, which makes the Activity useless.
                Log.w(TAG, "Permission denied.");
                showAlertDialog(R.string.permission_required,
                        R.string.permission_message_two,
                        R.string.settings,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int arg1) {
                                // Build intent that displays the App settings screen.
                                Intent intent = new Intent();
                                intent.setAction(
                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package",
                                        BuildConfig.APPLICATION_ID, null);
                                intent.setData(uri);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        });
                if (fragment != null) {
                    mFragmentManager.beginTransaction().remove(fragment);
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * Build and show AlertDialog with two buttons.
     * Nothing will happen when the negative button is pressed.
     *
     * @param titleTextStringId string Id for title
     * @param mainTextStringId  string Id for content
     * @param actionStringId    strind Id for positive button
     * @param listener          action when positive button is pressed
     */
    private void showAlertDialog(final int titleTextStringId,
                                 final int mainTextStringId,
                                 final int actionStringId,
                                 DialogInterface.OnClickListener listener) {

        AlertDialog.Builder ad = new AlertDialog.Builder(this)
                .setTitle(titleTextStringId)
                .setMessage(mainTextStringId)
                .setPositiveButton(actionStringId, listener)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {
                        //to do nothing
                    }
                });
        ad.setCancelable(false).show();
    }
}
