package com.wxyz.root.lockresources;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private CheckBoxPreference mDisableCameraCheckbox;
    DevicePolicyManager mDPM;
    ComponentName mDeviceAdminSample;


    private boolean isActiveAdmin() {
        return mDPM.isAdminActive(mDeviceAdminSample);
    }


    /*
    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (super.onPreferenceChange(preference, newValue)) {
            return true;
        }
        boolean value = (Boolean) newValue;
        if (preference == mEnableCheckbox) {
            if (value != mAdminActive) {
                if (value) {
                    // return false - don't update checkbox until we're really active
                    return false;
                } else {
                    mDPM.removeActiveAdmin(mDeviceAdminSample);

                }
            }
        } else if (preference == mDisableCameraCheckbox) {
            mDPM.setCameraDisabled(mDeviceAdminSample, value);

        }
        return true;
    }
*/

  @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
  //      setSupportActionBar(toolbar);



       mDPM =
              (DevicePolicyManager)getSystemService(Context.DEVICE_POLICY_SERVICE);

      mDeviceAdminSample = new ComponentName(getApplicationContext(),
              DeviceAdminSample.class);

     // Intent intent = new Intent(DevicePolicyManager.ACTION_SET_NEW_PASSWORD);
      //startActivity(intent);

      // Launch the activity to have the user enable our admin.
      Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
      intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mDeviceAdminSample);
      intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
              "admin extra app");
      startActivityForResult(intent, 0);


      FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                     // Launch the activity to have the user enable our admin.
                    Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                    intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mDeviceAdminSample);
                    intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                            "admin extra app");
                    startActivityForResult(intent, 0);

                    mDPM.setCameraDisabled(mDeviceAdminSample, true);


                Snackbar.make(view, "CameraNo", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

       Snackbar.make(view, "CameraSi", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


    }

    @Override
    protected void onPause() {
        super.onPause();
        //mCamera.lock();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
