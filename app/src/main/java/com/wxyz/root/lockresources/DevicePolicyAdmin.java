package com.wxyz.root.lockresources;

/**
 * Created by root on 6/02/17.
 */

        import android.media.AudioManager;
        import android.os.Build;
        import android.os.Bundle;
        import android.annotation.TargetApi;
        import android.app.Activity;
        import android.app.admin.DeviceAdminReceiver;
        import android.app.admin.DevicePolicyManager;
        import android.content.ComponentName;
        import android.content.Context;
        import android.content.Intent;
        import android.util.Log;
        import android.widget.CheckBox;
        import android.widget.CompoundButton;
        import android.widget.Toast;

public class DevicePolicyAdmin extends Activity {
    private final static String LOG_TAG = "DevicePolicyAdmin";
    DevicePolicyManager truitonDevicePolicyManager;
    ComponentName truitonDevicePolicyAdmin;
    private CheckBox truitonAdminEnabledCheckbox;
    protected static final int REQUEST_ENABLE = 1;
    protected static final int SET_PASSWORD = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_policy_admin);
        truitonDevicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        truitonDevicePolicyAdmin = new ComponentName(this,
                MyDevicePolicyReceiver.class);

        truitonAdminEnabledCheckbox = (CheckBox) findViewById(R.id.checkBox1);


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isMyDevicePolicyReceiverActive()) {
            truitonAdminEnabledCheckbox.setChecked(true);
        } else {
            truitonAdminEnabledCheckbox.setChecked(false);
        }
        truitonAdminEnabledCheckbox
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {
                        if (isChecked) {
                            Intent intent = new Intent(
                                    DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                            intent.putExtra(
                                    DevicePolicyManager.EXTRA_DEVICE_ADMIN,
                                    truitonDevicePolicyAdmin);
                            intent.putExtra(
                                    DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                                    getString(R.string.admin_explanation));
                            startActivityForResult(intent, REQUEST_ENABLE);
                        } else {
                            truitonDevicePolicyManager
                                    .removeActiveAdmin(truitonDevicePolicyAdmin);


                        }
                    }
                });
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_ENABLE:
                    Log.v(LOG_TAG, "Enabling Policies Now");
                    //truitonDevicePolicyManager.setMaximumTimeToLock(
                      //      truitonDevicePolicyAdmin, 30000L);
                    //truitonDevicePolicyManager.setMaximumFailedPasswordsForWipe(
                      //      truitonDevicePolicyAdmin, 5);
                    //truitonDevicePolicyManager.setPasswordQuality(
                      //      truitonDevicePolicyAdmin,
                        //    DevicePolicyManager.PASSWORD_QUALITY_COMPLEX);
                    truitonDevicePolicyManager.setCameraDisabled(
                            truitonDevicePolicyAdmin, true);
               //     boolean isSufficient = truitonDevicePolicyManager
                 //           .isActivePasswordSufficient();
                   // if (isSufficient) {
                     //   truitonDevicePolicyManager.lockNow();
                    //} else {
                      //  Intent setPasswordIntent = new Intent(
                        //        DevicePolicyManager.ACTION_SET_NEW_PASSWORD);
                        //startActivityForResult(setPasswordIntent, SET_PASSWORD);
                        //truitonDevicePolicyManager.setPasswordExpirationTimeout(
                          //      truitonDevicePolicyAdmin, 10000L);
                    //}
                    break;
            }
        }
    }



    private boolean isMyDevicePolicyReceiverActive() {
        return truitonDevicePolicyManager
                .isAdminActive(truitonDevicePolicyAdmin);
    }

    public static class MyDevicePolicyReceiver extends DeviceAdminReceiver {

        @Override
        public void onDisabled(Context context, Intent intent) {
            Toast.makeText(context, "Truiton's Device Admin Disabled",
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onEnabled(Context context, Intent intent) {
            Toast.makeText(context, "Truiton's Device Admin is now enabled",
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        public CharSequence onDisableRequested(Context context, Intent intent) {
            CharSequence disableRequestedSeq = "Requesting to disable Device Admin";
            return disableRequestedSeq;
        }

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        @Override
        public void onPasswordChanged(Context context, Intent intent) {
            Toast.makeText(context, "Device password is now changed",
                    Toast.LENGTH_SHORT).show();
            DevicePolicyManager localDPM = (DevicePolicyManager) context
                    .getSystemService(Context.DEVICE_POLICY_SERVICE);
            ComponentName localComponent = new ComponentName(context,
                    MyDevicePolicyReceiver.class);
            localDPM.setPasswordExpirationTimeout(localComponent, 0L);
        }

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        @Override
        public void onPasswordExpiring(Context context, Intent intent) {
            // This would require API 11 an above
            Toast.makeText(
                    context,
                    "Truiton's Device password is going to expire, please change to a new password",
                    Toast.LENGTH_LONG).show();

            DevicePolicyManager localDPM = (DevicePolicyManager) context
                    .getSystemService(Context.DEVICE_POLICY_SERVICE);
            ComponentName localComponent = new ComponentName(context,
                    MyDevicePolicyReceiver.class);
            long expr = localDPM.getPasswordExpiration(localComponent);
            long delta = expr - System.currentTimeMillis();
            boolean expired = delta < 0L;
            if (expired) {
                localDPM.setPasswordExpirationTimeout(localComponent, 10000L);
                Intent passwordChangeIntent = new Intent(
                        DevicePolicyManager.ACTION_SET_NEW_PASSWORD);
                passwordChangeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(passwordChangeIntent);
            }
        }

        @Override
        public void onPasswordFailed(Context context, Intent intent) {
            Toast.makeText(context, "Password failed", Toast.LENGTH_SHORT)
                    .show();
        }

        @Override
        public void onPasswordSucceeded(Context context, Intent intent) {
            Toast.makeText(context, "Access Granted", Toast.LENGTH_SHORT)
                    .show();
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(LOG_TAG,
                    "MyDevicePolicyReciever Received: " + intent.getAction());
            super.onReceive(context, intent);
        }
    }
}