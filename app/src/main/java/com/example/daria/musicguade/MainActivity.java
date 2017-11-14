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
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnItemSelectedListener {

    private final String TAG = "MainActivity " + this.hashCode() + " (: ";

    private static final int REQUEST_CODE_EXTERNAL_STORAGE = 1;
    public final static String FRAGMENT_INSTANCE_NAME = "fragment";
    public final static String PATH = "path";
    public final static String ITEM_LIST = "item_list";

    public static final String PATH_FOR_UI = "sdcard";
    public static String pathMain = "/mnt/sdcard";
    private String path = pathMain;
    private ArrayList<Item> mItem = null;

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

    private void createUI() {
        if (mItem == null) {
            mItem = new ArrayList<>();
        }
        mFragmentManager = getFragmentManager();
        fragment = mFragmentManager.findFragmentByTag(FRAGMENT_INSTANCE_NAME);
        if (fragment == null) {
            sendDataToNewFragment(mItem);
            ListLoder loder = new ListLoder((MyListFragment)fragment);
            loder.execute(new File(path));
            mFragmentManager.beginTransaction()
                    .add(R.id.fragment_place, fragment, FRAGMENT_INSTANCE_NAME)
                    .commit();
        }
    }

    public void sendDataToNewFragment(ArrayList<Item> item) {
        fragment = new MyListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(PATH,path);
        bundle.putParcelableArrayList(ITEM_LIST, item);
        fragment.setArguments(bundle);
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

    @Override
    public void onItemSelected(String newPath) {
        path = newPath;
        File file = new File(path);
        if(file.isDirectory()){
            mItem = new ArrayList<>();
            sendDataToNewFragment(mItem);
            ListLoder loder = new ListLoder((MyListFragment)fragment);
            loder.execute(file);
            mFragmentManager.beginTransaction()
                    .replace(R.id.fragment_place, fragment, FRAGMENT_INSTANCE_NAME)
                    .addToBackStack(null)
                    .commit();
        }else{
            Toast.makeText(this,"Can not open this file :(", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    @Override
    public void putPath(String path) {
        address.setText(path.replace(pathMain,PATH_FOR_UI));
    }

    public Fragment getFragment() {
        return fragment;
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

    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                REQUEST_CODE_EXTERNAL_STORAGE);
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            showAlertDialog(R.string.permission_required,
                    R.string.permission_message_one,
                    R.string.settings,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int arg1) {
                            // Request permission
                            startLocationPermissionRequest();
                        }
                    });
        } else {
            Log.i(TAG, "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            startLocationPermissionRequest();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_EXTERNAL_STORAGE) {
            if (grantResults.length <= 0) {
                /**
                 * If user interaction was interrupted, the permission request is cancelled and you
                 * receive empty arrays.
                 */
                fragment = null;
                Log.w(TAG, "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted.
                createUI();
            } else {
                /**
                 * Permission denied.
                 *
                 * Notify the user via a SnackBar that they have rejected a core permission for the
                 * app, which makes the Activity useless. In a real app, core permissions would
                 * typically be best requested during a welcome-screen flow.
                 *
                 * Additionally, it is important to remember that a permission might have been
                 * rejected without asking the user for permission (device policy or "Never ask
                 * again" prompts). Therefore, a user interface affordance is typically implemented
                 * when permissions are denied. Otherwise, your app could appear unresponsive to
                 * touches or interactions which have required permissions.
                 */
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
                fragment = null;
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

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
                        //ничего не отображать
                    }
                });
        ad.setCancelable(false).show();
    }
}
