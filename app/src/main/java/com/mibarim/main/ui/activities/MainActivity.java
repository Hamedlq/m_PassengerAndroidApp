package com.mibarim.main.ui.activities;


import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.OperationCanceledException;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.google.gson.Gson;
import com.mibarim.main.BootstrapApplication;
import com.mibarim.main.BootstrapServiceProvider;
import com.mibarim.main.R;
import com.mibarim.main.authenticator.LogoutService;
import com.mibarim.main.authenticator.TokenRefreshActivity;
import com.mibarim.main.core.ImageUtils;
import com.mibarim.main.core.LocationService;
import com.mibarim.main.data.UserData;
import com.mibarim.main.events.NetworkErrorEvent;
import com.mibarim.main.events.UnAuthorizedErrorEvent;
import com.mibarim.main.models.AboutMeModel;
import com.mibarim.main.models.ApiResponse;
import com.mibarim.main.models.ContactModel;
import com.mibarim.main.models.EventResponse;
import com.mibarim.main.models.ImageResponse;
import com.mibarim.main.models.LocalRoute;
import com.mibarim.main.models.PersonalInfoModel;
import com.mibarim.main.models.Route.RouteResponse;
import com.mibarim.main.models.ScoreModel;
import com.mibarim.main.models.ShareResponse;
import com.mibarim.main.models.UserInfoModel;
import com.mibarim.main.models.enums.MainTabs;
import com.mibarim.main.services.AuthenticateService;
import com.mibarim.main.services.ContactService;
import com.mibarim.main.services.RouteRequestService;
import com.mibarim.main.services.RouteResponseService;
import com.mibarim.main.services.UserInfoService;
import com.mibarim.main.ui.BootstrapActivity;
import com.mibarim.main.ui.HandleApiMessages;
import com.mibarim.main.ui.fragments.mainFragments.MainFragment;
import com.mibarim.main.ui.fragments.mainFragments.MainMapFragment;
import com.mibarim.main.util.SafeAsyncTask;
//import com.mibarim.main.util.Toaster;
import com.squareup.otto.Subscribe;

//import org.osmdroid.tileprovider.constants.OpenStreetMapTileProviderConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.inject.Inject;

import butterknife.ButterKnife;
import io.fabric.sdk.android.Fabric;


/**
 * Initial activity for the application.
 * * <p/>
 * If you need to remove the authentication from the application please see
 */
public class MainActivity extends BootstrapActivity implements MainMapFragment.OnMainMapClickedListener {

    @Inject
    BootstrapServiceProvider serviceProvider;
    @Inject
    UserInfoService userInfoService;
    @Inject
    LogoutService getLogoutService;
    @Inject
    RouteRequestService routeRequestService;
    @Inject
    protected ContactService contactService;
    @Inject
    UserData userData;
    @Inject
    protected RouteResponseService routeResponseService;


    private static final String PERSONAL_INFO = "personalInfo";
    private static final String USER_INFO = "userInfoModel";

    private CharSequence title;
    private Toolbar toolbar;

    private String authToken;
    private boolean refreshingToken = false;
    private boolean netErrorMsg = false;

    private int REFRESH_TOKEN_REQUEST = 3456;
    private int RELOAD_MAP = 9133;
    private int RELOAD_CONTACT = 8585;
    private int RELOAD_SUGGEST_CONTACT = 8691;
    private int VALIDATE_MOBILE = 2121;
    private int RELOAD_USER_INFO = 4873;
    private int FINISH_USER_INFO = 5649;
    private int SPLASH_DONE = 1111;
    //private PersonalInfoModel personalInfoModel;
    private UserInfoModel userInfoModel;
    private ApiResponse cityLocations;
    private ApiResponse localRoutes;
    private ApiResponse routeRequestRes;
    private ApiResponse contactRes;
    private AboutMeModel aboutMe;
    private ImageResponse imageResponse = new ImageResponse();

    private ScoreModel scoreModel;
    private ApiResponse deleteRes;
    private ApiResponse shareRes;
    private ApiResponse routeInfo;
    private LocalRoute localRouteInfo;
    private String lastLatitude;
    private String lastLongitude;
    private Handler mHandler;
    private int getInfoDelay = 2000;
    private int appVersion = 0;
    private long tripId = 0;
    private int selectedTab = MainTabs.Route.toInt();
    private int selectedContactId = 0;
    /*private boolean showUser = false;
    private boolean showRoute = true;*/
    private Menu thisMenu;
    private boolean profileVisible = true;
    private int selectedRouteId;
    private List<LocalRoute> localRouteList;
    boolean doubleBackToExitPressedOnce = false;
    String googletoken="";

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static final String SENT_TOKEN_TO_SERVER = "sentTokenToServer";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());


/*
        Intent intent = new Intent(MainActivity.this, SplashActivity.class);
        //Intent intent = new Intent(MainActivity.this, HomeWorkStepActivity.class);
        this.startActivityForResult(intent, SPLASH_DONE);
*/

        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        BootstrapApplication.component().inject(this);

        setContentView(R.layout.main_activity);

        // View injection with Butterknife
        ButterKnife.bind(this);

        if (getWindow().getDecorView().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }

        if (getIntent() != null && getIntent().getExtras() != null) {
            selectedTab = getIntent().getExtras().getInt("selectedTab");
            selectedContactId = getIntent().getExtras().getInt("contactId");
        }

        title = getTitle();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //toolbar.setLogo(R.mipmap.ic_logo_white);
        localRouteList = new ArrayList<>();

        checkAuth();

        mHandler = new Handler();
        //initScreen();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

/*
    @Override
    protected void onRestart() {
        super.onRestart();
    }
*/

    private void initScreen() {
        SharedPreferences prefs = this.getSharedPreferences(
                "com.mibarim.main", Context.MODE_PRIVATE);
        //authentication passed so It's not first launch anymore
        prefs.edit().putInt("FirstLaunch", 1).apply();
        loadUserData();
        loadData();
        final FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.main_container, new MainFragment())
                .commitAllowingStateLoss();
/*        if(selectedContactId!=0){
            showProgress();
        }*/
        if (authToken != null && prefs.getInt("MobileValidated", 2) == 0) {
            logout();
            //gotoMobileValidationActivity(prefs.getString("UserMobile", ""), authToken);
        } else {
            checkVersion();
/*            if (prefs.getInt("HomeWorkShown", 1) != 1) {
                Log.d("avali", "avali");
                prefs.edit().putInt("HomeWorkShown", 1).apply();
                Log.d("dovomi", "dovomi");
                final Intent i = new Intent(this, HomeWorkStepActivity.class);
                Log.d("dovomi2", "dovomi2");
                startActivityForResult(i, RELOAD_MAP);
            }*/
            /*Intent i=new Intent(this,RouteStepActivity.class);
            startActivity(i);*/
            getTripId();
            /*if (IsTripState()) {
                final Intent in = new Intent(this, TripActivity.class);
                in.putExtra("TripId", prefs.getLong("TripId", 0));
                startActivity(in);
            }*/
            String lastLatitude = prefs.getString("SrcLatitude", "35.717110");
            String lastLongitude = prefs.getString("SrcLongitude", "51.426830");
            getLocalRoutes(lastLatitude, lastLongitude);
        }

    }


    @Subscribe
    public void onNetworkErrorEvent(NetworkErrorEvent event) {
        if (!netErrorMsg) {
            netErrorMsg = true;
            //Toaster.showLong(MainActivity.this, getString(R.string.network_error), R.drawable.toast_warn);
            ShowSnackBar(R.string.network_error);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    netErrorMsg = false;
                }
            }, 5000);
        }
    }


    @Subscribe
    public void onUnAuthorizedErrorEvent(UnAuthorizedErrorEvent event) {
        refreshToken();
    }

    private void refreshToken() {
        if (!refreshingToken) {
            refreshingToken = true;
            final Intent i = new Intent(this, TokenRefreshActivity.class);
            startActivityForResult(i, REFRESH_TOKEN_REQUEST);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.new_route, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        thisMenu = menu;
        thisMenu.findItem(R.id.new_route_btn).setVisible(false);
        thisMenu.findItem(R.id.edit_user).setVisible(false);
        thisMenu.findItem(R.id.helping).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                return true;
            case R.id.new_route_btn:
                gotoAddMapActivity();
                return true;
            case R.id.edit_user:
                goToUserActivity();
                return true;
            case R.id.helping:
                goToHelpingActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void gotoAddMapActivity() {
        //Intent intent = new Intent(MainActivity.this, AddMainActivity.class);
        Intent intent = new Intent(MainActivity.this, RouteStepActivity.class);
        //Intent intent = new Intent(MainActivity.this, HomeWorkStepActivity.class);
        this.startActivityForResult(intent, RELOAD_MAP);
    }


    private void checkAuth() {
        new SafeAsyncTask<Boolean>() {

            @Override
            public Boolean call() throws Exception {
                final AuthenticateService svc = serviceProvider.getService(MainActivity.this);
                if (svc != null) {
                    authToken = serviceProvider.getAuthToken(MainActivity.this);
                    return true;
                }
                return false;
            }

            @Override
            protected void onException(final Exception e) throws RuntimeException {
                super.onException(e);
                if (e instanceof OperationCanceledException) {
                    // User cancelled the authentication process (back button, etc).
                    // Since auth could not take place, lets finish this activity.
                    finish();
                }
            }

            @Override
            protected void onSuccess(final Boolean hasAuthenticated) throws Exception {
                super.onSuccess(hasAuthenticated);
                //userHasAuthenticated = true;
                initScreen();
                sendRegistrationToServer();
            }
        }.execute();
    }

    public String getAuthToken() {
        return authToken;
    }

    private void sendRegistrationToServer() {
        if (checkPlayServices()) {
            final InstanceID instanceID = InstanceID.getInstance(this);
            new SafeAsyncTask<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    googletoken = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                            GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
                    return true;
                }

                @Override
                protected void onException(final Exception e) throws RuntimeException {
                    super.onException(e);
                    sendTokenToServer();
                }

                @Override
                protected void onSuccess(final Boolean state) throws Exception {
                    super.onSuccess(state);
                    sendTokenToServer();
                }
            }.execute();
        }
    }
    private void sendTokenToServer() {
        new SafeAsyncTask<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                if (authToken == null) {
                    serviceProvider.invalidateAuthToken();
                    authToken = serviceProvider.getAuthToken(MainActivity.this);
                }
                userInfoService.SaveGoogleToken(authToken, googletoken);
                return true;
            }

            @Override
            protected void onException(final Exception e) throws RuntimeException {
                super.onException(e);
            }

            @Override
            protected void onSuccess(final Boolean state) throws Exception {
                super.onSuccess(state);
            }
        }.execute();
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                finish();
            }
            return false;
        }
        return true;
    }


    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        //Toaster.showLong(MainActivity.this, getString(R.string.press_again_to_exit), R.drawable.toast_info);
        ShowSnackBar(R.string.press_again_to_exit);
        this.doubleBackToExitPressedOnce = true;
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 4000);
    }

    private void ShowSnackBar(int resourceId) {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.main_container);
        ((MainFragment) fragment).ShowSnackBar(resourceId);
    }


    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        /*if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.finishAffinity();
            return true;
        }*/
        return super.onKeyUp(keyCode, event);
    }

    /**
     * Hide progress dialog
     */
    @SuppressWarnings("deprecation")
    protected void hideProgress() {
        dismissDialog(0);
    }

    /**
     * Show progress dialog
     */
    @SuppressWarnings("deprecation")
    protected void showProgress() {
        showDialog(0);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage(getText(R.string.please_wait));
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
//        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
//            public void onCancel(final DialogInterface dialog) {
//            }
//        });
        return dialog;
    }

    @Override
    public void onMapStartDrag() {

    }

    @Override
    public void onMapStopDrag(String latitude, String longitude) {
        StopDelayedTask();
        lastLatitude = latitude;
        lastLongitude = longitude;
        StartDelayedTask();
        //getlocalPoints(latitude, longitude);
        //getLocalRoutes(latitude, longitude);
    }

    @Override
    public void onMarkerClicked(LocalRoute localRoute) {
        if (localRoute != null) {
            //getSupportActionBar().hide();
            final FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment fragment = fragmentManager.findFragmentById(R.id.main_container);
            ((MainFragment) fragment).ShowHeadBar();
            getRouteInfo(localRoute);
        }
    }

    void StartDelayedTask() {
        mHandler.postDelayed(mGettingInfo, getInfoDelay);
    }

    void StopDelayedTask() {
        if (mGettingInfo != null) {
            mHandler.removeCallbacks(mGettingInfo);
        }
    }

    Runnable mGettingInfo = new Runnable() {
        @Override
        public void run() {
            getLocalRoutes(lastLatitude, lastLongitude);
        }
    };

    /*@Override
    public void setCityLocations(String latitude, String longitude) {
        getlocalPoints(latitude, longitude);
    }*/


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RELOAD_MAP && resultCode == RESULT_OK) {
            finish();
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("selectedTab", MainTabs.Route.toInt());
            startActivity(intent);
            /*getRouteRequestsFromServer();
            SharedPreferences prefs = getSharedPreferences(
                    "com.mibarim.main", Context.MODE_PRIVATE);
            String lastLatitude = prefs.getString("SrcLatitude", "35.717110");
            String lastLongitude = prefs.getString("SrcLongitude", "51.426830");
            getLocalRoutes(lastLatitude, lastLongitude);
            final FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment fragment = fragmentManager.findFragmentById(R.id.main_container);
            ((MainFragment) fragment).MoveMap(lastLatitude, lastLongitude);*/

        }
        if (requestCode == VALIDATE_MOBILE && resultCode == RESULT_OK) {
            checkAuth();
        }
        if (requestCode == SPLASH_DONE) {
            checkAuth();
        }
        if (requestCode == RELOAD_CONTACT && resultCode == RESULT_OK) {
            loadData();
        }
        if (requestCode == RELOAD_SUGGEST_CONTACT && resultCode == RESULT_OK) {
            selectedContactId = data.getIntExtra("contactId", 0);
            Intent intent = getIntent();
            intent.putExtra("selectedTab", MainTabs.Message.toInt());
            intent.putExtra("contactId", selectedContactId);
            finish();
            startActivity(intent);
        }
        if (requestCode == RELOAD_USER_INFO && resultCode == RESULT_OK) {
            String action = data.getStringExtra("ActionType");
            if (action != null && action.equals("Exit")) {
                logout();
            } else {
                loadUserData();
            }
        }
        if (requestCode == REFRESH_TOKEN_REQUEST && resultCode == RESULT_OK) {
            authToken = null;
            serviceProvider.invalidateAuthToken();
            getTripId();
            loadData();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    refreshingToken = false;
                }
            }, 5000);
        }
        if (requestCode == FINISH_USER_INFO && resultCode == RESULT_OK) {
            SharedPreferences prefs = this.getSharedPreferences(
                    "com.mibarim.main", Context.MODE_PRIVATE);
            /*if (prefs.getInt("UserInfoRegistered", 0) != 1) {
                final Intent i = new Intent(this, RegisterActivity.class);
                startActivityForResult(i, FINISH_USER_INFO);
            } else {
                loadUserData();
            }*/
            prefs.edit().putInt("HomeWorkShown", 1).apply();
            final Intent i = new Intent(this, HomeWorkStepActivity.class);
            startActivityForResult(i, RELOAD_MAP);
        }
    }


    private void getLocalRoutes(final String latitude, final String longitude) {
        new SafeAsyncTask<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                localRoutes = routeRequestService.getLocalRoutes(latitude, longitude);
                return true;
            }

            @Override
            protected void onException(final Exception e) throws RuntimeException {
                super.onException(e);
            }

            @Override
            protected void onSuccess(final Boolean state) throws Exception {
                super.onSuccess(state);
                showRoutesOnMap();
            }
        }.execute();

    }

    private void showRoutesOnMap() {
        if (localRoutes != null) {
            if (localRouteList.size() > 200) {
                localRouteList = new ArrayList<>();
            }
            Gson gson = new Gson();
            for (String json : localRoutes.Messages) {
                LocalRoute point = gson.fromJson(json, LocalRoute.class);
                localRouteList.add(point);
            }
            showRouteListOnMap();
        }
    }

    public void showRouteListOnMap() {
        if (localRouteList != null && localRouteList.size() > 0) {
            final FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment fragment = fragmentManager.findFragmentById(R.id.main_container);
            ((MainFragment) fragment).AddLocalRoutes(localRouteList);
        }
    }

    public UserInfoModel getUserInfo() {
        return userData.userInfoQuery();
    }

    private void loadUserData() {
        getUserInfoFromServer();
        getAboutMeFromServer();
    }

    private void loadData() {
        getRouteRequestsFromServer();
        getContactsFromServer();
    }

    private void getUserInfoFromServer() {
        new SafeAsyncTask<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                if (authToken == null) {
                    serviceProvider.invalidateAuthToken();
                    authToken = serviceProvider.getAuthToken(MainActivity.this);
                }
                userInfoModel = userInfoService.getUserInfo(authToken);
                return true;
            }

            @Override
            protected void onException(final Exception e) throws RuntimeException {
                super.onException(e);
            }

            @Override
            protected void onSuccess(final Boolean state) throws Exception {
                super.onSuccess(state);
                userData.insertUserInfo(userInfoModel);
                getImageById(userInfoModel.UserImageId, R.mipmap.ic_camera);
                setInfoValues(userInfoModel.IsUserRegistered);
                //setEmail();
            }
        }.execute();
    }

    private void setInfoValues(boolean IsUserRegistered) {
        SharedPreferences prefs = this.getSharedPreferences(
                "com.mibarim.main", Context.MODE_PRIVATE);
        if (IsUserRegistered) {
            prefs.edit().putInt("UserInfoRegistered", 1).apply();
        } else {
            prefs.edit().putInt("UserInfoRegistered", 0).apply();
            final Intent i = new Intent(this, RegisterActivity.class);
            startActivityForResult(i, FINISH_USER_INFO);
        }
    }

    private void getAboutMeFromServer() {
        new SafeAsyncTask<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                if (authToken == null) {
                    serviceProvider.invalidateAuthToken();
                    authToken = serviceProvider.getAuthToken(MainActivity.this);
                }
                aboutMe = userInfoService.getAboutMe(authToken);
                return true;
            }

            @Override
            protected void onException(final Exception e) throws RuntimeException {
                super.onException(e);
            }

            @Override
            protected void onSuccess(final Boolean state) throws Exception {
                super.onSuccess(state);
                userData.insertAboutMeInfo(aboutMe);
            }
        }.execute();
    }

    private void setUserImage() {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.main_container);
        ((MainFragment) fragment).reloadUserImage();
    }

    private void setCompanyImage(Bitmap decodedByte) {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.main_container);
        ((MainFragment) fragment).setCompanyImage(decodedByte);
    }


    public void goToUserActivity() {
        final Intent i = new Intent(this, UserInfoActivity.class);
        UserInfoModel model = userData.userInfoQuery();
        i.putExtra(USER_INFO, model);
        this.startActivityForResult(i, RELOAD_USER_INFO);
    }
    public void goToHelpingActivity() {
        final Intent i = new Intent(this, HelpingActivity.class);
        this.startActivityForResult(i, RELOAD_USER_INFO);
    }

    public void profileVisible() {
        profileVisible = true;
    }

    public void profileInVisible() {
        profileVisible = false;
    }

    /*public void hideActionBar() {
        //if (profileVisible) {
            getSupportActionBar().hide();
        //}
    }*/

    /*public void showActionBar() {
        getSupportActionBar().show();
    }*/

    public void profileMenu() {
        if (thisMenu != null) {
            thisMenu.findItem(R.id.new_route_btn).setVisible(false);
            thisMenu.findItem(R.id.edit_user).setVisible(true);
            thisMenu.findItem(R.id.helping).setVisible(true);
        }
    }

    public void routeMenu() {
        if (thisMenu != null) {
            thisMenu.findItem(R.id.new_route_btn).setVisible(true);
            thisMenu.findItem(R.id.edit_user).setVisible(false);
            thisMenu.findItem(R.id.helping).setVisible(false);
        }
    }

    public void mapMenu() {
        if (thisMenu != null) {
            thisMenu.findItem(R.id.new_route_btn).setVisible(false);
            thisMenu.findItem(R.id.edit_user).setVisible(false);
            thisMenu.findItem(R.id.helping).setVisible(true);
        }
    }

    public void contactMenu() {
        if (thisMenu != null) {
            thisMenu.findItem(R.id.new_route_btn).setVisible(false);
            thisMenu.findItem(R.id.edit_user).setVisible(false);
            thisMenu.findItem(R.id.helping).setVisible(true);
        }
    }

    public void gotoMyLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            android.location.Location location = LocationService.getLocationManager(this).getLocation();
            if (location != null) {
                final FragmentManager fragmentManager = getSupportFragmentManager();
                Fragment fragment = fragmentManager.findFragmentById(R.id.main_container);
                ((MainFragment) fragment).MoveMap(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));
            }
        } else {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }
    }

    public void getUserScore() {
        new SafeAsyncTask<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                if (authToken == null) {
                    serviceProvider.invalidateAuthToken();
                    authToken = serviceProvider.getAuthToken(MainActivity.this);
                }
                scoreModel = userInfoService.getUserScores(authToken);
                return true;
            }

            @Override
            protected void onException(final Exception e) throws RuntimeException {
                super.onException(e);
            }

            @Override
            protected void onSuccess(final Boolean state) throws Exception {
                super.onSuccess(state);
                setUserScore();
            }
        }.execute();
    }

    private void refreshRouteList() {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.main_container);
        ((MainFragment) fragment).reloadRouteList();
    }

    private void refreshContactList() {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.main_container);
        ((MainFragment) fragment).reloadContactList();
    }


    private void setUserScore() {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.main_container);
        ((MainFragment) fragment).setUserScores(scoreModel);
    }


    private void checkVersion() {
        new SafeAsyncTask<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                ApiResponse ver = userInfoService.AppVersion();
                if (ver.Messages.size() > 0 && ver.Messages.get(0) != null) {
                    String version = ver.Messages.get(0);
                    appVersion = Integer.valueOf(version);
                }
                return true;
            }

            @Override
            protected void onException(final Exception e) throws RuntimeException {
                super.onException(e);
            }

            @Override
            protected void onSuccess(final Boolean state) throws Exception {
                super.onSuccess(state);
                if (appVersion > getVersion()) {
                    showUpdatePanel();
                }
            }
        }.execute();
    }

    private void getTripId() {
        new SafeAsyncTask<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                if (authToken == null) {
                    serviceProvider.invalidateAuthToken();
                    authToken = serviceProvider.getAuthToken(MainActivity.this);
                }
                ApiResponse ver = userInfoService.GetTripId(authToken);
                if (ver.Messages.size() > 0 && ver.Messages.get(0) != null) {
                    String tripVal = ver.Messages.get(0);
                    tripId = Long.valueOf(tripVal);
                }
                return true;
            }

            @Override
            protected void onException(final Exception e) throws RuntimeException {
                super.onException(e);
            }

            @Override
            protected void onSuccess(final Boolean state) throws Exception {
                super.onSuccess(state);
                setTripId(tripId);
            }
        }.execute();
    }

    private void setTripId(long tripId) {
        SharedPreferences prefs = this.getSharedPreferences(
                "com.mibarim.main", Context.MODE_PRIVATE);
        if (tripId > 0) {
            prefs.edit().putLong("TripId", tripId).apply();
            setTripState();
            //gotoTripActivity();
        } else {
            prefs.edit().putLong("TripId", 0).apply();
        }
    }

    private void setTripState() {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.main_container);
        ((MainFragment) fragment).setTripState();
    }

    public void gotoTripActivity() {
        SharedPreferences prefs = this.getSharedPreferences(
                "com.mibarim.main", Context.MODE_PRIVATE);
        final Intent i = new Intent(this, TripActivity.class);
        i.putExtra("TripId", prefs.getLong("TripId", 0));
        startActivity(i);
    }


    public int getVersion() {
        int v = 100;
        try {
            v = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
        }
        return v;
    }

    private void showUpdatePanel() {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.main_container);
        ((MainFragment) fragment).showUpdateMsg();
    }

    public void closeUpdatePanel() {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.main_container);
        ((MainFragment) fragment).hideUpdateMsg();
    }

    public void gotoUpdate() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.update_link)));
        startActivity(browserIntent);
    }


    public int getSelectedTab() {
        return selectedTab;
    }

    public void deleteRoute(final String routeId) {
        new SafeAsyncTask<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                if (authToken == null) {
                    serviceProvider.invalidateAuthToken();
                    authToken = serviceProvider.getAuthToken(MainActivity.this);
                }
                deleteRes = routeRequestService.deleteRoute(authToken, routeId);
                return true;
            }

            @Override
            protected void onException(final Exception e) throws RuntimeException {
                super.onException(e);
            }

            @Override
            protected void onSuccess(final Boolean state) throws Exception {
                super.onSuccess(state);
                new HandleApiMessages(MainActivity.this, deleteRes).showMessages();
                getRouteRequestsFromServer();
            }
        }.execute();
    }

    public void shareRoute(final String routeId) {
        new SafeAsyncTask<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                if (authToken == null) {
                    serviceProvider.invalidateAuthToken();
                    authToken = serviceProvider.getAuthToken(MainActivity.this);
                }
                shareRes = routeRequestService.shareRoute(authToken, routeId);
                return true;
            }

            @Override
            protected void onException(final Exception e) throws RuntimeException {
                super.onException(e);
            }

            @Override
            protected void onSuccess(final Boolean state) throws Exception {
                super.onSuccess(state);
                new HandleApiMessages(MainActivity.this, shareRes).showMessages();
                shareRouteMsg();
            }
        }.execute();
    }

    public void getRouteInfo(final LocalRoute theRoute) {
        new SafeAsyncTask<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                if (authToken == null) {
                    serviceProvider.invalidateAuthToken();
                    authToken = serviceProvider.getAuthToken(MainActivity.this);
                }
                routeInfo = routeRequestService.getRouteInfo(authToken, theRoute.RouteUId);
                return true;
            }

            @Override
            protected void onException(final Exception e) throws RuntimeException {
                super.onException(e);
            }

            @Override
            protected void onSuccess(final Boolean state) throws Exception {
                super.onSuccess(state);
                new HandleApiMessages(MainActivity.this, routeInfo).showMessages();
                showRouteInfo(routeInfo);
            }
        }.execute();
    }

    private void showRouteInfo(ApiResponse routeInfo) {
        Gson gson = new Gson();
        localRouteInfo = new LocalRoute();
        for (String routeJson : routeInfo.Messages) {
            localRouteInfo = gson.fromJson(routeJson, LocalRoute.class);
        }
        final FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.main_container);
        ((MainFragment) fragment).ShowRouteInfo(localRouteInfo);
    }


    private void shareRouteMsg() {
        Gson gson = new Gson();
        ShareResponse shareResponse = new ShareResponse();
        for (String shareJson : shareRes.Messages) {
            shareResponse = gson.fromJson(shareJson, ShareResponse.class);
        }
        // Construct a ShareIntent with link to image
        Uri imageUri = Uri.parse(shareResponse.ImagePath);
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        //shareIntent.putExtra(Intent.EXTRA_TEXT, imageUri.toString());
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareResponse.ImageCaption + "\r\n" + imageUri.toString());
        shareIntent.setType("text/plain");

        // Launch sharing dialog for image
        startActivity(Intent.createChooser(shareIntent, "اشتراک مسیر"));
    }


    public void goToSuggestActivity(RouteResponse routeResponse) {
        Intent intent = new Intent(this, SuggestRouteCardActivity.class);
        intent.putExtra("RouteResponse", routeResponse);
        this.startActivityForResult(intent, RELOAD_SUGGEST_CONTACT);

    }


    public Bitmap getImageById(String imageId, int defaultImageResourceId) {
        Bitmap icon = BitmapFactory.decodeResource(getResources(),
                defaultImageResourceId);
        if (imageId == null || imageId.equals("") || imageId.equals("00000000-0000-0000-0000-000000000000")) {
            return icon;
        }
        ImageResponse imageResponse = userData.imageQuery(imageId);
        if (imageResponse != null) {
            Bitmap b = ImageUtils.loadImageFromStorage(imageResponse.ImageFilePath, imageResponse.ImageId);
            if (b != null) {
                return b;
            } else {
                getImageFromServer(imageId);
            }
        } else {
            getImageFromServer(imageId);
        }
        return icon;
    }

    private void getImageFromServer(final String imageId) {
        new SafeAsyncTask<Boolean>() {
            ImageResponse imageResponse = new ImageResponse();
            Bitmap decodedByte;

            @Override
            public Boolean call() throws Exception {
                String token = serviceProvider.getAuthToken(MainActivity.this);
                imageResponse = userInfoService.GetImageById(token, imageId);
                if (imageResponse != null && imageResponse.Base64ImageFile != null) {
                    return true;
                }
                return false;
            }

            @Override
            protected void onException(final Exception e) throws RuntimeException {
                super.onException(e);
                if (e instanceof android.os.OperationCanceledException) {
                    // User cancelled the authentication process (back button, etc).
                    // Since auth could not take place, lets finish this activity.
//                    finish();
                }
            }

            @Override
            protected void onSuccess(final Boolean imageLoaded) throws Exception {
                if (imageLoaded) {
                    byte[] decodedString = Base64.decode(imageResponse.Base64ImageFile, Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    String path = ImageUtils.saveImageToInternalStorage(getApplicationContext(), decodedByte, imageResponse.ImageId);
                    userData.insertImage(imageResponse, path);
                    setUserImage();
                }
            }
        }.execute();
    }


    public void getCompanyImage(final String imageId) {
        new SafeAsyncTask<Boolean>() {
            ImageResponse imageResponse = new ImageResponse();
            Bitmap decodedByte;

            @Override
            public Boolean call() throws Exception {
                String token = serviceProvider.getAuthToken(MainActivity.this);
                imageResponse = userInfoService.GetImageById(token, imageId);
                if (imageResponse != null && imageResponse.Base64ImageFile != null) {
                    return true;
                }
                return false;
            }

            @Override
            protected void onException(final Exception e) throws RuntimeException {
                super.onException(e);
                if (e instanceof android.os.OperationCanceledException) {
                    // User cancelled the authentication process (back button, etc).
                    // Since auth could not take place, lets finish this activity.
//                    finish();
                }
            }

            @Override
            protected void onSuccess(final Boolean imageLoaded) throws Exception {
                if (imageLoaded) {
                    if (imageResponse != null) {
                        byte[] decodedString = Base64.decode(imageResponse.Base64ImageFile, Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        setCompanyImage(decodedByte);
                    }
                }
            }
        }.execute();
    }

    public void goToContactActivity(ContactModel contactModel) {
        Intent intent;
        if (contactModel.IsSupport == 1) {
            intent = new Intent(this, MessagingActivity.class);
        } else {
            intent = new Intent(this, UserContactActivity.class);
            intent.putExtra("OriginActivity", "MainActivity");
        }
        intent.putExtra("ContactModel", contactModel);
        this.startActivityForResult(intent, RELOAD_CONTACT);
    }

    public void gotoEventActivity(EventResponse eventResponse) {
        Intent intent = new Intent(MainActivity.this, EventMapActivity.class);
        intent.putExtra("EventResponse", eventResponse);
        this.startActivityForResult(intent, RELOAD_MAP);
    }

    private void getRouteRequestsFromServer() {
        new SafeAsyncTask<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                if (authToken == null) {
                    serviceProvider.invalidateAuthToken();
                    authToken = serviceProvider.getAuthToken(MainActivity.this);
                }
                routeRequestRes = routeResponseService.GetRoutes(authToken);
                if (routeRequestRes != null) {
                    return true;
                }
                return false;
            }

            @Override
            protected void onException(final Exception e) throws RuntimeException {
                super.onException(e);
            }

            @Override
            protected void onSuccess(final Boolean state) throws Exception {
                super.onSuccess(state);
                if (state) {
                    insertRouteResponses(routeRequestRes);
                }
            }
        }.execute();
    }

    private void insertRouteResponses(ApiResponse routeRequestApi) {
        Gson gson = new Gson();
        List<RouteResponse> latest = new ArrayList<RouteResponse>();
        for (String routeJson : routeRequestApi.Messages) {
            RouteResponse route = gson.fromJson(routeJson, RouteResponse.class);
            latest.add(route);
        }
        userData.insertRouteResponse(latest);
        new HandleApiMessages(MainActivity.this, routeRequestApi).showMessages();
        refreshRouteList();
    }

    private void getContactsFromServer() {
        new SafeAsyncTask<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                if (authToken == null) {
                    serviceProvider.invalidateAuthToken();
                    authToken = serviceProvider.getAuthToken(MainActivity.this);
                }
                contactRes = contactService.GetContacts(authToken);
                if (contactRes != null) {
                    return true;
                }
                return false;
            }

            @Override
            protected void onException(final Exception e) throws RuntimeException {
                super.onException(e);
                //hideProgress();
            }

            @Override
            protected void onSuccess(final Boolean state) throws Exception {
                super.onSuccess(state);
                if (state) {
                    insertContacts(contactRes);
                    gotoContact();
                }
            }
        }.execute();
    }


    private void gotoContact() {
        if (selectedContactId != 0) {
            List<ContactModel> latest;
            latest = userData.contactModelListQuery();
            for (ContactModel contactModel : latest) {
                if (contactModel.ContactId == selectedContactId) {
                    selectedContactId = 0;
                    goToContactActivity(contactModel);
                }
            }
        }
        //hideProgress();
    }


    private void insertContacts(ApiResponse contactRes) {
        Gson gson = new Gson();
        List<ContactModel> latest = new ArrayList<ContactModel>();
        for (String routeJson : contactRes.Messages) {
            ContactModel contactModel = gson.fromJson(routeJson, ContactModel.class);
            latest.add(contactModel);
        }
        userData.insertContacts(latest);
        new HandleApiMessages(MainActivity.this, contactRes).showMessages();
        refreshContactList();
    }


    public void getContactImageFromServer(final ImageView imageView, final String imageId) {
        imageView.setImageBitmap(getImageById(imageId, R.mipmap.ic_camera));
    }

    public void gotoRideRequestActivity() {
        Intent intent = new Intent(MainActivity.this, RideMainActivity.class);
        intent.putExtra("LocalRoute", localRouteInfo);
        this.startActivityForResult(intent, RELOAD_MAP);
    }

    public void gotoMibarimWebsite() {
        SharedPreferences prefs = this.getSharedPreferences(
                "com.mibarim.main", Context.MODE_PRIVATE);
        String mobile = prefs.getString("UserMobile", "");
        String uri = "http://mibarim.ir/Pay?id=" + mobile;
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        startActivity(browserIntent);
    }

    private void logout() {
        getLogoutService.logout(new Runnable() {
            @Override
            public void run() {
                //forceRefresh();
                userData.DeleteUserInfo();
                ReloadActivity();
            }
        });

    }

    private void ReloadActivity() {
        finish();
        startActivity(getIntent());
    }

    /*private void setEmail() {
        if (userInfoModel.Email == null) {
            userInfoModel = userData.userInfoQuery();
            try {
                Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
                Account[] accounts = AccountManager.get(getApplicationContext()).getAccounts();
                for (Account account : accounts) {
                    if (emailPattern.matcher(account.name).matches()) {
                        userInfoModel.Email = account.name;
                    }
                }
            } catch (Exception e) {

            }
            sendUserInfoToServer(userInfoModel);
        }
    }*/

    private void sendUserInfoToServer(final UserInfoModel userInfoModel) {
        new SafeAsyncTask<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                if (authToken == null) {
                    authToken = serviceProvider.getAuthToken(MainActivity.this);
                }
                userInfoService.SaveUserPersonalInfo(authToken, userInfoModel);
                /*if ((response.Errors == null || response.Errors.size() == 0) && response.Status.equals("OK")) {
                    return true;
                }*/
                return true;
            }

            @Override
            protected void onException(final Exception e) throws RuntimeException {
                super.onException(e);
            }

            @Override
            protected void onSuccess(final Boolean uploadSuccess) throws Exception {
                super.onSuccess(uploadSuccess);
                if (uploadSuccess) {
                }
            }
        }.execute();
    }

    public boolean IsTripState() {
        SharedPreferences prefs = this.getSharedPreferences(
                "com.mibarim.main", Context.MODE_PRIVATE);
        return (prefs.getLong("TripId", 0) != 0);
    }

    public int getSelectedContactId() {
        return selectedContactId;
    }

    public AboutMeModel getAboutMe() {
        return userData.aboutMeQuery();
    }

    public void gotoVerify() {
        final Intent intent = new Intent(this, VerifyStepperActivity.class);
        this.startActivity(intent);
    }

    public void goToMessaging(ContactModel contactModel) {
        Intent intent = new Intent(this, MessagingActivity.class);
        intent.putExtra("ContactModel", contactModel);
        this.startActivityForResult(intent, RELOAD_CONTACT);
    }

    /*@Override
    protected void onStop() {
        LocationService.destroy();
        super.onStop();
    }*/
}