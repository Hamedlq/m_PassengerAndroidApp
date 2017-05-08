package com.mibarim.main.ui.activities;


import android.accounts.OperationCanceledException;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.gson.Gson;
import com.mibarim.main.BootstrapApplication;
import com.mibarim.main.BootstrapServiceProvider;
import com.mibarim.main.R;
import com.mibarim.main.authenticator.LogoutService;
import com.mibarim.main.authenticator.TokenRefreshActivity;
import com.mibarim.main.core.LocationService;
import com.mibarim.main.core.UserService;
import com.mibarim.main.data.UserData;
import com.mibarim.main.events.UnAuthorizedErrorEvent;
import com.mibarim.main.locationServices.GoogleLocationService;
import com.mibarim.main.models.Address.AddressComponent;
import com.mibarim.main.models.Address.AddressObject;
import com.mibarim.main.models.Address.AddressResult;
import com.mibarim.main.models.Address.DetailPlaceResult;
import com.mibarim.main.models.Address.Location;
import com.mibarim.main.models.Address.LocationPoint;
import com.mibarim.main.models.Address.PathPoint;
import com.mibarim.main.models.ApiResponse;
import com.mibarim.main.models.CityLocation;
import com.mibarim.main.models.EventResponse;
import com.mibarim.main.models.ImageResponse;
import com.mibarim.main.models.RouteRequest;
import com.mibarim.main.models.Trip.TripResponse;
import com.mibarim.main.models.enums.AddRouteStates;
import com.mibarim.main.models.enums.ServiceTypes;
import com.mibarim.main.services.AddressService;
import com.mibarim.main.services.AuthenticateService;
import com.mibarim.main.services.RouteRequestService;
import com.mibarim.main.services.TripService;
import com.mibarim.main.services.UserInfoService;
import com.mibarim.main.ui.BootstrapActivity;
import com.mibarim.main.ui.HandleApiMessages;
import com.mibarim.main.ui.fragments.addRouteFragments.AddMapFragment;
import com.mibarim.main.ui.fragments.addRouteFragments.MainAddMapFragment;
import com.mibarim.main.ui.fragments.tripFragments.MainTripFragment;
import com.mibarim.main.util.SafeAsyncTask;
import com.mibarim.main.util.Toaster;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import io.fabric.sdk.android.Fabric;

//import org.osmdroid.tileprovider.constants.OpenStreetMapTileProviderConstants;


/**
 * Initial activity for the application.
 * * <p/>
 * If you need to remove the authentication from the application please see
 */
public class TripActivity extends BootstrapActivity {
    static final String TAG = "TripActivity";

    @Inject
    BootstrapServiceProvider serviceProvider;
    @Inject
    TripService tripService;
    @Inject
    UserInfoService userInfoService;



    Intent googleServiceIntent;
    Intent serviceIntent;
    GoogleLocationService mService;
    boolean mBound = false;
    private Tracker mTracker;

    private String authToken;
    private int RELOAD_REQUEST = 1234;
    private Toolbar toolbar;
    private long tripId;
    private Handler mHandler;
    private Runnable mRunnable;
    private int delay=10000;
    private ApiResponse tripApiResponse;
    private ApiResponse endTripApiResponse;
    private TripResponse tripResponse;
    private int endTripResult;
    private int LocationServiceInUse=1;// GOOGLE=1 LOCAL_SERVICE=2
    private ImageResponse imageResponse;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        super.onCreate(savedInstanceState);

        if (getIntent() != null && getIntent().getExtras() != null) {
            tripId = getIntent().getExtras().getLong("TripId");
        }

        Fabric.with(this, new Crashlytics());
        BootstrapApplication.component().inject(this);

        BootstrapApplication application = (BootstrapApplication) getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("TripActivity");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        mTracker.send(new HitBuilders.EventBuilder().setCategory("Activity").setAction("TripActivity").build());


        setContentView(R.layout.main_activity);

        // View injection with Butterknife
        ButterKnife.bind(this);

        if (getWindow().getDecorView().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        serviceIntent = new Intent(this, LocationService.class);
        googleServiceIntent = new Intent(this, GoogleLocationService.class);

        checkAuth();
//        initScreen();
    }


    private void initScreen() {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.main_container, new MainTripFragment())
                .commitAllowingStateLoss();
        periodicReLoading();
    }


    @Subscribe
    public void onUnAuthorizedErrorEvent(UnAuthorizedErrorEvent event) {
        refreshToken();
    }

    private void refreshToken() {
        if (authToken != null) {
            final Intent i = new Intent(this, TokenRefreshActivity.class);
            startActivityForResult(i, RELOAD_REQUEST);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        return super.onOptionsItemSelected(item);
    }


    private void checkAuth() {
        new SafeAsyncTask<Boolean>() {

            @Override
            public Boolean call() throws Exception {
                final AuthenticateService svc = serviceProvider.getService(TripActivity.this);
                return svc != null;
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
            }
        }.execute();
    }

    private void periodicReLoading() {
        mHandler = new Handler();
        mRunnable = new Runnable() {
            @Override
            public void run() {
                reloadThread();
                mHandler.postDelayed(this, delay);
            }
        };
        mRunnable.run();
    }

    public void reloadThread() {
        new SafeAsyncTask<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return true;
            }

            @Override
            protected void onException(final Exception e) throws RuntimeException {
                super.onException(e);
            }

            @Override
            protected void onSuccess(final Boolean isMsgSubmitted) throws Exception {
                super.onSuccess(isMsgSubmitted);
                TripReloading();
            }
        }.execute();

    }

    private void TripReloading() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            android.location.Location location = LocationService.getLocationManager(this).getLocation();
            if (location != null ) {
                if(!mBound){
                    startLocationService();
                    delay=30000;
                }else {
                    getTripInfo();
                }
            }

        }else {
            final FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment fragment = fragmentManager.findFragmentById(R.id.main_container);
            ((MainTripFragment) fragment).showLocationMsg(getString(R.string.LocationMustBeOn));
        }

    }

    private void getTripInfo() {
        new SafeAsyncTask<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                if (authToken == null) {
                    serviceProvider.invalidateAuthToken();
                    authToken = serviceProvider.getAuthToken(TripActivity.this);
                }
                tripApiResponse = tripService.getTripInfo(authToken,tripId);
                if (tripApiResponse != null) {
                    for (String tripJson : tripApiResponse.Messages) {
                        tripResponse = new Gson().fromJson(tripJson, TripResponse.class);
                    }
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
                showTripInfo(tripResponse);
            }
        }.execute();

    }

    private void showTripInfo(TripResponse tripResponse) {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.main_container);
        ((MainTripFragment) fragment).showTripInfo(tripResponse);
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
        return dialog;
    }

    public void turnOnGps() {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);
        final FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.main_container);
        ((MainTripFragment) fragment).hideLocationMsg();
    }
    private void startLocationService() {
        boolean IsGoogleServiceSupported = false;
        if (isPlayServicesConfigured()) {
            //bind to google location
            try {
                startService(googleServiceIntent);
                bindService(googleServiceIntent, mConnection, Context.BIND_AUTO_CREATE);
                IsGoogleServiceSupported = true;
                LocationServiceInUse=1;
            } catch (Exception e) {
                IsGoogleServiceSupported = false;
            }
        } else {
            IsGoogleServiceSupported = false;
        }
        if (!IsGoogleServiceSupported) {
            //bind to manual location provider
            startService(serviceIntent);
            bindService(serviceIntent, mConnection, Context.BIND_AUTO_CREATE);
            LocationServiceInUse=2;
        }
    }

    public void stopService() {
        unbindService(mConnection);
        if(LocationServiceInUse==1){
            stopService(googleServiceIntent);
        }else if(LocationServiceInUse==2){
            stopService(serviceIntent);
        }
    }

    /**
     * Defines callbacks for service binding, passed to bindService()
     */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            GoogleLocationService.LocalBinder binder = (GoogleLocationService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    private boolean isPlayServicesConfigured() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this.getApplicationContext());
        if (status == ConnectionResult.SUCCESS)
            return true;
        else {
            Log.d(TAG, "Error connecting with Google Play services. Code: " + String.valueOf(status));
            return false;
        }
    }

    public void gotoMyLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            android.location.Location location = LocationService.getLocationManager(this).getLocation();
            if (location != null) {
                final FragmentManager fragmentManager = getSupportFragmentManager();
                Fragment fragment = fragmentManager.findFragmentById(R.id.main_container);
                ((MainTripFragment) fragment).MoveMap(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));
            }
        } else {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }
    }

    public void EndTrip() {
        showProgress();
        new SafeAsyncTask<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                if (authToken == null) {
                    serviceProvider.invalidateAuthToken();
                    authToken = serviceProvider.getAuthToken(TripActivity.this);
                }
                endTripApiResponse = tripService.endTrip(authToken, tripId);
                if (endTripApiResponse != null) {
                    for (String tripJson : endTripApiResponse.Messages) {
                        endTripResult = new Gson().fromJson(tripJson, int.class);
                    }
                }
                return true;
            }

            @Override
            protected void onException(final Exception e) throws RuntimeException {
                super.onException(e);
                hideProgress();
            }

            @Override
            protected void onSuccess(final Boolean state) throws Exception {
                super.onSuccess(state);
                hideProgress();
                new HandleApiMessages(TripActivity.this,endTripApiResponse).showMessages();
                if(endTripResult==1){
                    finishTrip();
                }

            }
        }.execute();
    }

    private void finishTrip() {
        SharedPreferences prefs = this.getSharedPreferences(
                "com.mibarim.main", Context.MODE_PRIVATE);
        prefs.edit().putLong("TripId", 0).apply();
        stopService();
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    public void gotoMain() {
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
    }



    public void getRouteImage(final ImageView imageView, final String imageId) {
        new SafeAsyncTask<Boolean>() {
            Bitmap decodedByte;
            @Override
            public Boolean call() throws Exception {
                String token = serviceProvider.getAuthToken(TripActivity.this);
                imageResponse = userInfoService.GetImageById(token, imageId);
                if (imageResponse != null && imageResponse.Base64ImageFile != null) {
                    byte[] decodedString = Base64.decode(imageResponse.Base64ImageFile, Base64.DEFAULT);
                    decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
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
                    imageView.setImageBitmap(decodedByte);
                }
            }
        }.execute();
    }
}
