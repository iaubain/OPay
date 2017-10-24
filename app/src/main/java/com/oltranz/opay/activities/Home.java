package com.oltranz.opay.activities;

import android.Manifest;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.DialogOnAnyDeniedMultiplePermissionsListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.oltranz.opay.R;
import com.oltranz.opay.config.Config;
import com.oltranz.opay.fragments.AccountRecover;
import com.oltranz.opay.fragments.Login;
import com.oltranz.opay.fragments.Register;
import com.oltranz.opay.models.signup.SignUpUserResponse;
import com.oltranz.opay.utilities.DataFactory;
import com.oltranz.opay.utilities.updator.alarm.MyAlarmManager;
import com.oltranz.opay.utilities.updator.logic.DeviceProvider;
import com.oltranz.opay.utilities.updator.logic.DirManager;
import com.oltranz.opay.utilities.updator.updtservice.UpdateService;
import com.oltranz.opay.utilities.views.Label;
import com.oltranz.opay.utilities.views.MButton;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Home extends AppCompatActivity implements Register.OnRegisterListener, Login.OnLoginListener, AccountRecover.OnRecoverListener {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.forgot)
    Label forgotPin;
    @BindView(R.id.signup)
    Label signUp;
    private DirManager dirManager;
    private Dialog dialog;

    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String action = intent.getAction();
            if (action.equals(UpdateService.ACTION_TRIGGER)) {
                Log.d("receiver", "Got action: " + action);
                showWarningDialog();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        setContentView(R.layout.home);
        ButterKnife.bind(this);
        dirManager = new DirManager(Home.this);
        setSupportActionBar(toolbar);
        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null) {
                return;
            }
            initPermissions();
            fragmentHandler(login());
        }
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentHandler(register());
            }
        });
        forgotPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentHandler(recover());
            }
        });
    }

    private void initPermissions() {
        Dexter.withActivity(Home.this)
                .withPermissions(
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.CAMERA,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.RECEIVE_BOOT_COMPLETED,
                        Manifest.permission.VIBRATE,
                        Manifest.permission.RECORD_AUDIO

                ).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                if (report.isAnyPermissionPermanentlyDenied()) {
                    String message = "As for terms and conditions the following permissions are required for OPay in order to server its purpose:\n";
                    int deniedPermissions = report.getDeniedPermissionResponses().size();
                    int count = 1;
                    for (PermissionDeniedResponse response : report.getDeniedPermissionResponses()) {
                        if (deniedPermissions == count)
                            message += " and " + response.getPermissionName() + ".";
                        else
                            message += response.getPermissionName() + ",";
                        count++;
                    }

                    popUpPermissions("Permissions", message);
                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                token.continuePermissionRequest();
            }
        }).check();
    }


    private void popUpPermissions(String title, String message) {
        try {
            builder = new AlertDialog.Builder(Home.this, R.style.SimpleBlackDialog);
            builder.setMessage(message != null ? message : "NO_MESSAGE")
                    .setTitle(title);
            builder.setIcon(R.mipmap.ic_launcher);
            // Add the buttons
            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                    initPermissions();
                }
            });
            dialog = builder.create();
            dialog.show();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(Home.this, message != null ? message : "NO_MESSAGE", Toast.LENGTH_SHORT).show();
        }
    }

    public void scheduleUpdateAlarm() {
        Calendar cal = Calendar.getInstance();
        Intent alarmIntent = new Intent(getApplicationContext(), UpdateService.class);
        alarmIntent.setAction(UpdateService.ACTION_CHECK_UPDATES);

        PendingIntent pIntent = PendingIntent.getService(getApplicationContext(),
                MyAlarmManager.REQUEST_CODE,
                alarmIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarm = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);

        cal.add(Calendar.MINUTE, 1);
        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 9 * 60 * 1000, pIntent);
    }

    public void cancelUpdateAlarm() {
        Intent intent = new Intent(getApplicationContext(), UpdateService.class);
        final PendingIntent pIntent = PendingIntent.getBroadcast(this, MyAlarmManager.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        alarm.cancel(pIntent);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("Resume", "Application called onResume");
        LocalBroadcastManager.getInstance(Home.this).registerReceiver(
                mMessageReceiver, new IntentFilter(UpdateService.ACTION_TRIGGER));
        scheduleUpdateAlarm();
    }

    @Override
    protected void onPause() {
        // Unregister since the activity is paused.
        LocalBroadcastManager.getInstance(Home.this).unregisterReceiver(
                mMessageReceiver);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("Destroy", "Application called onDestroy");
        cancelUpdateAlarm();
    }

    private void showWarningDialog() {
        if (dialog != null && dialog.isShowing())
            return;
        try {
            dialog = new Dialog(Home.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setContentView(R.layout.update_alert);

            final Label boxTitle = (Label) dialog.findViewById(R.id.dialogTitle);
            MButton ok = (MButton) dialog.findViewById(R.id.ok);
            LinearLayout boxContent = (LinearLayout) dialog.findViewById(R.id.dialogContent);
            Label warning = (Label) dialog.findViewById(R.id.warning);

            warning.setText("This application has new update that require to be installed. Kindly click Okay and proceed with the installation.");

            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //proceed with other validations
                    try {
                        triggerUpdate();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    dialog.dismiss();
                }
            });

            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                triggerUpdate();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    private void triggerUpdate() throws IOException {
        try {
            File updateFile = dirManager.getUpdateFile(new DeviceProvider(Home.this).genDevice());
            if (updateFile.length() <= 0) {
                Log.d("LocalFile", "Bad update file");
                return;
            }
            double kilobytes = (updateFile.length() / 1024);
            double megabytes = (kilobytes / 1024);
            Log.d("Updater", "Updating file with: " + megabytes + "Mbs from path: " + updateFile.getAbsolutePath());

            try {
                if (!dirManager.isUpdateRequired(new DeviceProvider(Home.this).genDevice())) {
                    Log.d("Updater", "No update required");
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.fromFile(updateFile), "application/vnd.android.package-archive");
            startActivity(intent);
            //finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            super.onBackPressed();
        } else if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            finish();
        }

        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (currentFragment instanceof Login)
            getSupportActionBar().hide();
        else {
            if (!getSupportActionBar().isShowing())
                getSupportActionBar().show();
        }
    }

    private void fragmentHandler(Object object) {
        if (object instanceof Login) {
            getSupportActionBar().hide();
        } else {
            if (!getSupportActionBar().isShowing())
                getSupportActionBar().show();
        }
        Fragment fragment = (Fragment) object;
        String backStateName = fragment.getClass().getSimpleName();

        FragmentManager fragmentManager = getSupportFragmentManager();
        boolean fragmentPopped = fragmentManager.popBackStackImmediate(backStateName, 0);

        if (!fragmentPopped && fragmentManager.findFragmentByTag(backStateName) == null) { //fragment not in back stack, create it.
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.fragment_container, fragment, backStateName);
            ft.addToBackStack(backStateName);
            ft.commit();
        }
    }

    private Object register() {
        return Register.newInstance();
    }

    private Object login() {
        return Login.newInstance();
    }

    private Object recover() {
        return AccountRecover.newInstance("", "");
    }

    @Override
    public void onRegister(boolean isRegistration, SignUpUserResponse registrationResponse, Object extra) {
        if(!isRegistration)
            return;
        fragmentHandler(login());
        Fragment loginFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if(loginFragment instanceof Login){
            ((Login)loginFragment).afterRegistration(registrationResponse);
        }
    }

    @Override
    public void onLogin(boolean isLogin, Object object) {
        if (isLogin) {
            Intent intent = new Intent(Home.this, MainHome.class);
            Bundle bundle = new Bundle();
            bundle.putString(Config.TOKEN, DataFactory.objectToString(object));
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    @Override
    public void onRecover(Uri uri) {

    }
}
