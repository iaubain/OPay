package com.oltranz.opay.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.oltranz.opay.R;
import com.oltranz.opay.apiclient.MerchantServices;
import com.oltranz.opay.config.Config;
import com.oltranz.opay.fragments.HomeFragment;
import com.oltranz.opay.fragments.Login;
import com.oltranz.opay.fragments.dashboard.Customers;
import com.oltranz.opay.fragments.dashboard.Graphs;
import com.oltranz.opay.fragments.dashboard.Liquidate;
import com.oltranz.opay.models.login.LoginResponse;
import com.oltranz.opay.models.merchant.MerchantDetails;
import com.oltranz.opay.models.payment.PaymentRequest;
import com.oltranz.opay.models.status.PaymentStatus;
import com.oltranz.opay.models.user.UserDetails;
import com.oltranz.opay.models.user.UserProfile;
import com.oltranz.opay.models.wallet.Wallet;
import com.oltranz.opay.utilities.DataFactory;
import com.oltranz.opay.utilities.loader.MerchantLoader;
import com.oltranz.opay.utilities.loader.ProfileLoader;
import com.oltranz.opay.utilities.loader.UserDetailsLoader;
import com.oltranz.opay.utilities.services.TransactionCleaner;
import com.oltranz.opay.utilities.updator.updtservice.UpdateService;
import com.oltranz.opay.utilities.views.Label;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainHome extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        HomeFragment.OnHomeFragment,
        Liquidate.OnLiquidate,
        Graphs.OnGraphs,
        Customers.OnCustomer,
        UserDetailsLoader.OnUserDetails,
        ProfileLoader.OnProfileLoader,
        MerchantLoader.OnMerchantLoader {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;

    private ImageView profilePic;
    private Label userName;

    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private ProgressDialog progressDialog;
    private LoginResponse loginResponse;
    private Bundle bundle;
    private Intent alarmIntent;
    private PendingIntent pendingIntent;
    private AlarmManager alarm;

    private UserDetails userDetails;
    private UserProfile userProfile;
    private MerchantDetails merchantDetails;
    private Wallet wallet;

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String action = intent.getAction();
            if (action.equals(TransactionCleaner.PAYMENT_BROADCAST_FILTER)) {
                Log.d("receiver", "Got action: " + action);
                try {
                    PaymentRequest paymentRequest = (PaymentRequest) DataFactory.stringToObject(PaymentRequest.class, intent.getExtras().getString(TransactionCleaner.PAYMENT_DATA));
                    PaymentStatus paymentStatus = (PaymentStatus) DataFactory.stringToObject(PaymentStatus.class, intent.getExtras().getString(TransactionCleaner.PAYMENT_STATUS));

                    if (paymentStatus.getBody().getStatus().equals("100")) {
                        popUpPaymentStatus("Payment", "Successful payment \nAmount: " + paymentRequest.getAmount() + "\n" +
                                "From: " + paymentRequest.getPayingAccountId() + "\n" +
                                "ID: " + paymentRequest.getRequestRef());
                    } else if (paymentStatus.getBody().getStatus().equals("101") || paymentStatus.getBody().getStatus().equals("102")) {
                        Toast.makeText(MainHome.this, "Pending payment of:"+paymentRequest.getAmount()+", From:"+paymentRequest.getPayingAccountId(),Toast.LENGTH_SHORT).show();
                    }else{
                        popUpPaymentStatus("Payment", "Payment failure \nAmount: " + paymentRequest.getAmount() + "\n" +
                                "From: " + paymentRequest.getPayingAccountId() + "\n" +
                                "ID: " + paymentRequest.getRequestRef() + "\n" +
                                "Reason: " + paymentStatus.getBody().getStatusDescr());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        setContentView(R.layout.activity_main_home);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        bundle = getIntent().getExtras();
        try {
            this.loginResponse = (LoginResponse) DataFactory.stringToObject(LoginResponse.class, bundle.getString(Config.TOKEN));
        } catch (Exception e) {
            e.printStackTrace();
            finish();
        }
        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null) {
                return;
            }

            mProgress("Loading user details");
            UserDetailsLoader userLoader = new UserDetailsLoader(loginResponse.getAccess_token(), Config.APP_ID, MainHome.this);
            userLoader.startLoading();

            scheduleAlarm();
        }
    }

    private void popUpPaymentStatus(String title, String message) {
        try {
            builder = new AlertDialog.Builder(MainHome.this, R.style.SimpleBlackDialog);
            builder.setMessage(message != null ? message : "NO_MESSAGE")
                    .setTitle(title);
            builder.setIcon(R.mipmap.ic_launcher);
            // Add the buttons
            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });
            dialog = builder.create();
            dialog.show();

            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            if(fragment instanceof HomeFragment){
                HomeFragment homeFragment = (HomeFragment) fragment;
                homeFragment.refreshDashBoard();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(MainHome.this, message != null ? message : "NO_MESSAGE", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(MainHome.this).registerReceiver(
                mMessageReceiver, new IntentFilter(TransactionCleaner.PAYMENT_BROADCAST_FILTER));
        scheduleAlarm();
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(MainHome.this).unregisterReceiver(
                mMessageReceiver);
        cancelAlarm();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (getSupportFragmentManager().getBackStackEntryCount() > 1)
                super.onBackPressed();
            else {
                popBox("Are you sure you want to logout?");
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Query the list of element within a proper fragment.
                return false;
            }
        });
        // Define the listener
        MenuItemCompat.OnActionExpandListener expandListener = new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                // Do something when action item collapses
                return true;
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                // Do something when expanded
                return true;
            }
        };
        // Assign the listener to that action item
        MenuItemCompat.setOnActionExpandListener(searchItem, expandListener);

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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_home) {

        } else if (id == R.id.nav_payment) {

        } else if (id == R.id.nav_history) {

        } else if (id == R.id.nav_user) {

        } else if (id == R.id.nav_logout) {
            popBox("Are you sure you want to logout?");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void scheduleAlarm() {
        alarmIntent = new Intent(MainHome.this, TransactionCleaner.class);
        alarmIntent.setAction(TransactionCleaner.POST_TRANSACTIONS);
        pendingIntent = PendingIntent.getService(MainHome.this,
                999,
                alarmIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        alarm = (AlarmManager) MainHome.this.getSystemService(Context.ALARM_SERVICE);
        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 3 * 1000, pendingIntent);
    }

    public void cancelAlarm() {
        if (alarm != null && pendingIntent != null) {
            alarm.cancel(pendingIntent);
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

    private Fragment getHomeFragment() {
        return HomeFragment.newInstance(loginResponse.getAccess_token(),
                DataFactory.objectToString(merchantDetails),
                DataFactory.objectToString(userDetails));
    }

    private void popBox(String message) {
        try {
            builder = new AlertDialog.Builder(MainHome.this, R.style.SimpleBlackDialog);
            builder.setMessage(message != null ? message : "NO_MESSAGE")
                    .setTitle("Logout");
            // Add the buttons
            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                    finish();
                }
            });
            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dialog = builder.create();
            dialog.show();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(MainHome.this, message != null ? message : "NO_MESSAGE", Toast.LENGTH_SHORT).show();
        }
    }

    private void customBox(String title, String message) {
        try {
            builder = new AlertDialog.Builder(MainHome.this, R.style.SimpleBlackDialog);
            builder.setMessage(message != null ? message : "NO_MESSAGE")
                    .setTitle(title);
            // Add the buttons
            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });
            dialog = builder.create();
            dialog.show();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(MainHome.this, message != null ? message : "NO_MESSAGE", Toast.LENGTH_SHORT).show();
        }
    }

    private void refreshToGetUser(String title, String message) {
        try {
            builder = new AlertDialog.Builder(MainHome.this, R.style.SimpleBlackDialog);
            builder.setMessage(message != null ? message : "NO_MESSAGE")
                    .setTitle(title);
            builder.setCancelable(false);
            // Add the buttons
            builder.setPositiveButton("Refresh", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    mProgress("Loading user details");
                    UserDetailsLoader userLoader = new UserDetailsLoader(loginResponse.getAccess_token(), Config.APP_ID, MainHome.this);
                    userLoader.startLoading();
                    dialog.dismiss();
                }
            });
            dialog = builder.create();
            dialog.show();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(MainHome.this, message != null ? message : "NO_MESSAGE", Toast.LENGTH_SHORT).show();
        }
    }

    private void refreshProfile(String title, String message) {
        try {
            builder = new AlertDialog.Builder(MainHome.this, R.style.SimpleBlackDialog);
            builder.setMessage(message != null ? message : "NO_MESSAGE")
                    .setTitle(title);
            // Add the buttons
            builder.setPositiveButton("Refresh", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    ProfileLoader profileLoader = new ProfileLoader(loginResponse.getAccess_token(), userDetails.getUser().getImage(), MainHome.this);
                    profileLoader.startLoading();
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dialog = builder.create();
            dialog.show();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(MainHome.this, message != null ? message : "NO_MESSAGE", Toast.LENGTH_SHORT).show();
        }
    }

    private void refreshMerchant(String title, String message) {
        try {
            builder = new AlertDialog.Builder(MainHome.this, R.style.SimpleBlackDialog);
            builder.setMessage(message != null ? message : "NO_MESSAGE")
                    .setTitle(title);
            builder.setCancelable(false);
            // Add the buttons
            builder.setPositiveButton("Refresh", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    mProgress("Loading merchant information");
                    MerchantLoader merchantLoader = new MerchantLoader(MainHome.this, MerchantServices.CMD_GET_MERCHANT, loginResponse.getAccess_token());
                    merchantLoader.startLoading();
                    dialog.dismiss();
                }
            });
            dialog = builder.create();
            dialog.show();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(MainHome.this, message != null ? message : "NO_MESSAGE", Toast.LENGTH_SHORT).show();
        }
    }

    private void mProgress(String message) {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();

        progressDialog = new ProgressDialog(MainHome.this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(true);
        progressDialog.setMessage(message != null ? message : "NO_MESSAGE");
        progressDialog.show();
    }

    private void dismissProgress() {
        if (progressDialog != null)
            if (progressDialog.isShowing())
                progressDialog.dismiss();
    }

    private void updateMessage(String message) {
        if (progressDialog != null)
            if (progressDialog.isShowing())
                progressDialog.setMessage(message != null ? message : "NO_MESSAGE");
    }

    @Override
    public void onHomeFragment(boolean isGoTo, Object goTo, String message) {

    }

    @Override
    public void onHomeWalletData(Wallet wallet) {
        if(wallet == null)
            return;
        this.wallet = wallet;
    }

    @Override
    public void onCustomer(Uri uri) {

    }

    @Override
    public void onGraphs(boolean isLiquidate, Object object) {

    }

    @Override
    public void onLiquidate(boolean isLiquidate, Object object) {

    }

    @Override
    public void onUserDetails(boolean isLoaded, Object object) {
        dismissProgress();
        if (!isLoaded) {
            refreshToGetUser("Alert", object + "");
            return;
        }

        View headerView = navigationView.findViewById(R.id.headerView);
        userDetails = (UserDetails) object;
        String userNm = userDetails.getUser().getFirstName() != null ? userDetails.getUser().getFirstName() :
                userDetails.getUser().getLastName() != null ? userDetails.getUser().getLastName() :
                        userDetails.getUser().getMiddleName() != null ? userDetails.getUser().getMiddleName() : "Name not availble";
        userName = (Label) headerView.findViewById(R.id.userName);
        userName.setText(userNm);

        mProgress("Loading merchant information");
        MerchantLoader merchantLoader = new MerchantLoader(MainHome.this, MerchantServices.CMD_GET_MERCHANT, loginResponse.getAccess_token());
        merchantLoader.startLoading();

        mProgress("Preparing profile");
        ProfileLoader profileLoader = new ProfileLoader(loginResponse.getAccess_token(), userDetails.getUser().getImage(), MainHome.this);
        profileLoader.startLoading();

        profilePic = (ImageView) headerView.findViewById(R.id.profilePic);
        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshProfile("Alert", "Do you really want to reload the profile picture?");
            }
        });
    }

    @Override
    public void onProfileLoader(boolean isLoaded, Object object) {
        dismissProgress();
        if (!isLoaded) {
            return;
        }
        View headerView = navigationView.findViewById(R.id.headerView);
        profilePic = (ImageView) headerView.findViewById(R.id.profilePic);
        userProfile = (UserProfile) object;
        byte[] decodedString = Base64.decode(userProfile.getFile(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        profilePic.setImageBitmap(decodedByte);
    }

    @Override
    public void onMerchant(boolean isLoaded, Object object) {
        dismissProgress();
        if (!isLoaded) {
            refreshMerchant("Alert", "Failed to load merchant information due to: " + object + ", please refresh the process");
            return;
        }
        View headerView = navigationView.findViewById(R.id.headerView);
        merchantDetails = (MerchantDetails) object;
        String merchantName = merchantDetails.getMerchantName() != null ? merchantDetails.getMerchantName() : "Merchant";
        Label merchantLabel = (Label) headerView.findViewById(R.id.merchantName);
        merchantLabel.setText(merchantName);
        fragmentHandler(getHomeFragment());
    }
}
