package com.mibarim.main.ui.activities;


import android.accounts.OperationCanceledException;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;
import com.mibarim.main.BootstrapApplication;
import com.mibarim.main.BootstrapServiceProvider;
import com.mibarim.main.R;
import com.mibarim.main.authenticator.LogoutService;
import com.mibarim.main.authenticator.TokenRefreshActivity;
import com.mibarim.main.core.LocationService;
import com.mibarim.main.data.UserData;
import com.mibarim.main.events.UnAuthorizedErrorEvent;
import com.mibarim.main.models.Address.AddressComponent;
import com.mibarim.main.models.Address.AddressObject;
import com.mibarim.main.models.Address.AddressResult;
import com.mibarim.main.models.Address.DetailPlaceResult;
import com.mibarim.main.models.Address.LocationPoint;
import com.mibarim.main.models.Address.PathPoint;
import com.mibarim.main.models.ApiResponse;
import com.mibarim.main.models.CityLocation;
import com.mibarim.main.models.EventResponse;
import com.mibarim.main.models.LocalRoute;
import com.mibarim.main.models.RouteRequest;
import com.mibarim.main.models.enums.AddRouteStates;
import com.mibarim.main.models.enums.ServiceTypes;
import com.mibarim.main.services.AddressService;
import com.mibarim.main.services.AuthenticateService;
import com.mibarim.main.services.RouteRequestService;
import com.mibarim.main.services.UserInfoService;
import com.mibarim.main.ui.BootstrapActivity;
import com.mibarim.main.ui.HandleApiMessages;
import com.mibarim.main.ui.fragments.addRouteFragments.AddMapFragment;
import com.mibarim.main.ui.fragments.addRouteFragments.MainAddMapFragment;
import com.mibarim.main.ui.fragments.rideRequestFragment.RideRequestMainFragment;
import com.mibarim.main.ui.fragments.rideRequestFragment.RideRequestMapFragment;
import com.mibarim.main.util.SafeAsyncTask;
import com.mibarim.main.util.Toaster;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import butterknife.ButterKnife;
import io.fabric.sdk.android.Fabric;

//import org.osmdroid.tileprovider.constants.OpenStreetMapTileProviderConstants;


/**
 * Initial activity for the application.
 * * <p/>
 * If you need to remove the authentication from the application please see
 */
public class RideRequestMapActivity extends BootstrapActivity implements RideRequestMapFragment.OnMapListener {
    static final String TAG = "RideRequestMapActivity";

    @Inject
    BootstrapServiceProvider serviceProvider;
    @Inject
    AddressService addressService;
    @Inject
    RouteRequestService routeRequestService;
    @Inject
    LogoutService getLogoutService;
    @Inject
    UserInfoService userInfoService;
    @Inject
    UserData userData;


    // The minimum distance to change Updates in meters
//    private static final long LOCATION_REFRESH_DISTANCE = 50; // 10 meters
//
//    // The minimum time between updates in milliseconds
//    private static final long LOCATION_REFRESH_TIME = 1000; // 1 minute

    private Handler mHandler;
    private int mInterval = 600000;
    private int getInfoDelay = 1000;

    private static final String PERSONAL_INFO = "personalInfo";
    private static final String LICENSE_INFO = "licenseInfo";
    private static final String CAR_INFO = "carInfo";
    private static final int GET_PERSONAL_INFO = 256;
    private int RELOAD_REQUEST = 1234;
    private int TIME_SET = 1024;
    private int Drive_SET = 8191;
    private int Location_SET = 2010;
    private LocalRoute theLocalRoute;

    private CharSequence title;
    private Toolbar toolbar;

    private String srcAdd;
    private String dstAdd;

    //public RouteRequest routeRequest;
    public int sequenceNo;
    ApiResponse recommendRoutes;
    List<PathPoint> recommendPathPointList;
    public int SelectedPathRouteInRecommendPath = 1;

    private String srcLatitude;
    private String srcLongitude;
    private String dstLatitude;
    private String dstLongitude;

    private String lastLatitude;
    private String lastLongitude;

    private ApiResponse cityLocations;
    private ApiResponse response;
    private String pathPrice;

    private List<Location> wayPoints;

    private String authToken;

    public Menu theMenu;
    public EventResponse _eventResponse;
    private Tracker mTracker;
    //private TrafficAddressResponse trafficAddress;
    private String trafficAddress;
/*
    private TaxiPriceModel taxiPriceModel;

    private List<RoutePriceModel> routePriceModel;
*/

//    public boolean dstShow=false;
//    public boolean calcShow=false;


    protected AddRouteStates stateSelector;//SOURCE_SELECTED DESTINATION_SELECTED REQUESTING EVENT_LIST_SELECTED EVENT_SOURCE_SELECT EVENT_DESTINATION_SELECT

    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        super.onCreate(savedInstanceState);
        /*if (getCacheDir() != null) {
            OpenStreetMapTileProviderConstants.setCachePath(getCacheDir().getAbsolutePath());
        }*/
        Fabric.with(this, new Crashlytics());
        BootstrapApplication.component().inject(this);

        BootstrapApplication application = (BootstrapApplication) getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("RideRequestMapActivity");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        mTracker.send(new HitBuilders.EventBuilder().setCategory("Activity").setAction("RideRequestMapActivity").build());

        setContentView(R.layout.main_activity);

        // View injection with Butterknife
        ButterKnife.bind(this);

        if (getWindow().getDecorView().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }


        if (getIntent() != null && getIntent().getExtras() != null) {
            theLocalRoute = (LocalRoute) getIntent().getExtras().getSerializable("LocalRoute");
            Location src=getNearLocation(theLocalRoute.SrcPoint.Lat,theLocalRoute.SrcPoint.Lng,50);
            srcLatitude=String.valueOf(src.getLatitude());
            srcLongitude=String.valueOf(src.getLongitude());
            getAddress("SOURCE");
        }

        // Set up navigation drawer
        title = getTitle();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            if (Build.VERSION.SDK_INT >= 17) {
                actionBar.setHomeAsUpIndicator(R.mipmap.ic_menu_forward);
            }
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        //routeRequest = new RouteRequest();
        /*if (stateSelector == AddRouteStates.SelectEventOriginState || stateSelector == AddRouteStates.SelectEventDestinationState) {
            _eventResponse = (EventResponse) getIntent().getExtras().getSerializable("EventResponse");
        }*/
        checkAuth();
        mHandler = new Handler();

//        initScreen();
    }


    private void initScreen() {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.main_container, new RideRequestMainFragment())
                .commitAllowingStateLoss();
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
        theMenu = menu;
        return super.onCreateOptionsMenu(menu);
    }


    private void checkAuth() {
        new SafeAsyncTask<Boolean>() {

            @Override
            public Boolean call() throws Exception {
                final AuthenticateService svc = serviceProvider.getService(RideRequestMapActivity.this);
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

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        return true;
    }

    public void setDstAddress(String address) {
        dstAdd = address;
        //routeRequest.DstGAddress = address;
    }
    public void setSrcAddress(String address) {
        srcAdd = address;
    }

    public String getDstAddress() {
        return dstAdd;
    }

    public String getSrcAddress() {
        return srcAdd;
    }


    private void gettingPathInfo() {
        String latitude = lastLatitude;
        String longitude = lastLongitude;
        dstLatitude = latitude;
        dstLongitude = longitude;
        /*routeRequest.DstLatitude = latitude;
        routeRequest.DstLongitude = longitude;*/
        getAddress("DESTINATION");
        getPathPrice();
    }

    Runnable mGettingInfo = new Runnable() {
        @Override
        public void run() {
            gettingPathInfo();
            //getlocalPoints(lastLatitude, lastLongitude);
        }
    };

    /*private void getlocalPoints(final String latitude, final String longitude) {
        new SafeAsyncTask<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                cityLocations = routeRequestService.getCityLocations(latitude, longitude);
                return true;
            }

            @Override
            protected void onException(final Exception e) throws RuntimeException {
                super.onException(e);
            }

            @Override
            protected void onSuccess(final Boolean state) throws Exception {
                super.onSuccess(state);
                showPointsOnMap();
            }
        }.execute();

    }*/

    /*private void showPointsOnMap() {
        if (cityLocations != null) {
            Gson gson = new Gson();
            List<CityLocation> cityLocationList = new ArrayList<>();
            for (String eventJson : cityLocations.Messages) {
                CityLocation point = gson.fromJson(eventJson, CityLocation.class);
                cityLocationList.add(point);
            }
            final FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment fragment = fragmentManager.findFragmentById(R.id.main_container);
            ((MainAddMapFragment) fragment).AddCityLocations(cityLocationList);
        }
    }*/


    void StartDelayedTask() {
        mHandler.postDelayed(mGettingInfo, getInfoDelay);
    }

    void StopDelayedTask() {
        if (mGettingInfo != null) {
            mHandler.removeCallbacks(mGettingInfo);
        }
    }


    private void getPathPrice() {
        new SafeAsyncTask<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                ApiResponse response = routeRequestService.RequestPrice(srcLatitude, srcLongitude, dstLatitude, dstLongitude);
                for (String routeJson : response.Messages) {
                    pathPrice = routeJson;
                }
                return true;
            }

            @Override
            protected void onException(final Exception e) throws RuntimeException {
                super.onException(e);
                if (e instanceof OperationCanceledException) {
                    finish();
                }
            }

            @Override
            protected void onSuccess(final Boolean res) throws Exception {
                super.onSuccess(res);
                SetPathPrice();
            }
        }.execute();
    }


    private void SetPathPrice() {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.main_container);
        ((RideRequestMainFragment) fragment).setPrice(pathPrice);
    }

    private void getAddress(final String srcdst) {
        new SafeAsyncTask<Boolean>() {

            @Override
            public Boolean call() throws Exception {
                if (srcdst == "SOURCE") {
                    AddressResult response = addressService.getAddress(srcLatitude, srcLongitude);
                    List<String> add = purifyAddress(response);
                    setSrcAddress(add.get(0));
                    //List<String> bAdd = purifyBriefAddress(response);
                    //srcBriefAdd = bAdd.get(0);
                    return true;
                }
                if (srcdst == "DESTINATION") {
                    AddressResult response = addressService.getAddress(dstLatitude, dstLongitude);
                    List<String> add = purifyAddress(response);
                    setDstAddress(add.get(0));
//                    List<String> bAdd = purifyBriefAddress(response);
//                    dstBriefAdd = bAdd.get(0);
                    return true;
                }
                return false;
            }

            @Override
            protected void onException(final Exception e) throws RuntimeException {
                super.onException(e);
                if (e instanceof OperationCanceledException) {
                    finish();
                }
            }

            @Override
            protected void onSuccess(final Boolean res) throws Exception {
                super.onSuccess(res);
                SetAddress();
            }
        }.execute();
    }


    private void StartSetAddress() {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.main_container);
        ((MainAddMapFragment) fragment).waitingAddress();
    }

    private void SetAddress() {
//        if (srcDstStateSelector != "EVENT_LIST_SELECTED") {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.main_container);
        if (fragment != null && fragment instanceof RideRequestMainFragment) {
            ((RideRequestMainFragment) fragment).RebuildAddressFragment();
        }
//        }
    }

    private List<String> purifyAddress(AddressResult response) {
        String route;
        String neighborhood;
        String locality;
        List<String> result = new ArrayList<String>();
        for (AddressObject address : response.results) {
            route = "";
            neighborhood = "";
            locality = "";
            for (AddressComponent component : address.address_components) {
                for (String type : component.types) {
                    if (type.trim().equals("route")) {
                        route = component.long_name;
                    }
                    if (type.trim().equals("neighborhood")) {
                        neighborhood = component.long_name + "، ";
                    }
                    if (type.trim().equals("locality")) {
                        locality = component.long_name + "، ";
                    }
                }
            }
            result.add(locality + neighborhood + route);
        }
        return result;
    }

    /*private void RequestingTaxi() {
        setSrcDstStateSelector("REQUESTING");
        final FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.main_container);
        ((MainAddMapFragment) fragment).RequestingFragment();
        SetAddress();
        StartRequesting();
    }*/


    public void StartRequesting() {
        mHandler = new Handler();
        startRepeatingTask();
    }

    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            try {
//                requesting(); //this function can change value of mInterval.
            } catch (Exception e) {
                Log.d(TAG, "Runable task" + e);
            } finally {
                // 100% guarantee that this always happens, even if
                // your update method throws an exception
                mHandler.postDelayed(mStatusChecker, mInterval);
            }
        }
    };

    void startRepeatingTask() {
        mStatusChecker.run();
    }

    void stopRepeatingTask() {
        if (mStatusChecker != null) {
            mHandler.removeCallbacks(mStatusChecker);
        }
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /*if (requestCode == GET_PERSONAL_INFO && resultCode == RESULT_OK) {
            getPersonalInfoFromServer();
        }*/
        if (requestCode == RELOAD_REQUEST && resultCode == RESULT_OK) {
            authToken = null;
            Toaster.showLong(RideRequestMapActivity.this, getString(R.string.retry), R.drawable.toast_warn);
        }
        if (requestCode == Drive_SET && resultCode == RESULT_OK) {
            RebuildSrcDstFragment();
        }
        if (requestCode == Location_SET && resultCode == RESULT_OK) {
            String PlaceId = data.getStringExtra("PlaceId");
        }
    }

    private void RebuildSrcDstFragment() {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.main_container);
        ((MainAddMapFragment) fragment).RebuildSrcDstFragment();
    }

    private void RebuildMainAddMapFragment() {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.main_container);
        ((MainAddMapFragment) fragment).RebuildFragment();
    }

    private void RebuildDstFragment() {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.main_container);
        ((MainAddMapFragment) fragment).RebuildDstFragment();
    }

    private void RebuildDstFragment(String lat, String lng) {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.main_container);
        ((MainAddMapFragment) fragment).RebuildDstFragment(lat, lng);
    }

    private void MoveMapFragment(String lat, String lng) {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.main_container);
        ((RideRequestMainFragment) fragment).MoveMap(lat, lng);
    }


    public void sourceSet() {
        //if time  not set
//        if (isTimeSet()) {
//        goToDestination();
//        } else {
//            gotoTimeActivity();
//        }
        //if time set got to destination
        //
    }

/*
    public void goToDestination() {

        setSrcDstStateSelector(AddRouteStates.SelectDestinationState);
        final FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.main_container);
        ((MainAddMapFragment) fragment).DestinationFragment();
        SetAddress();
    }
*/

    /*public void goToSource() {
        setSrcDstStateSelector(AddRouteStates.SelectOriginState);
        final FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.main_container);
        ((MainAddMapFragment) fragment).SourceFragment();
        SetAddress();
    }*/

//    public void showSource() {
//        setSrcDstStateSelector(AddRouteStates.SelectOriginState);
//        final FragmentManager fragmentManager = getSupportFragmentManager();
//        Fragment fragment = fragmentManager.findFragmentById(R.id.main_container);
//        ((MainAddMapFragment) fragment).ShowSrcFragment();
//        SetAddress();
//    }


    @Override
    public void onDestroy() {
        stopRepeatingTask();
        super.onDestroy();
    }

    public String getPrice() {
        if (pathPrice == null || pathPrice.equals("")) {
            return "0";
        } else {
            return pathPrice;
        }
    }

//    public void hideSource() {
//        setSrcDstStateSelector("EVENT_LIST_SELECTED");
//        final FragmentManager fragmentManager = getSupportFragmentManager();
//        Fragment fragment = fragmentManager.findFragmentById(R.id.main_container);
//        ((MainAddMapFragment) fragment).hideSourceFragment();
//    }


    private void returnOk() {
        Intent intent = getIntent();
        setResult(RESULT_OK, intent);
        finish();
    }


    public void gotoLocationActivity() {
        Intent intent = new Intent(this, LocationSearchActivity.class);
        this.startActivityForResult(intent, Location_SET);
    }

    public void gotoDriveActivity() {
        Intent intent = new Intent(this, DriveActivity.class);
        this.startActivityForResult(intent, Drive_SET);
    }

    public void gotoMyLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            android.location.Location location = LocationService.getLocationManager(this).getLocation();
            if (location != null) {
                final FragmentManager fragmentManager = getSupportFragmentManager();
                Fragment fragment = fragmentManager.findFragmentById(R.id.main_container);
                ((MainAddMapFragment) fragment).MoveMap(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));
            }
        } else {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }
    }

    @Override
    public LocalRoute getLocalRouteInfo() {
        return theLocalRoute;
    }

    @Override
    public LocalRoute returnToPath(String lat, String lng) {
        double coefficient = 1.5;
        Location nearestPoint1 = new Location("");
        Location nearestPoint2 = new Location("");
        float minDist = Float.MAX_VALUE;
        Location thePointer = new Location("");
        thePointer.setLatitude(Double.valueOf(lat));
        thePointer.setLongitude(Double.valueOf(lng));
        Location loc1 = new Location("");
        loc1.setLatitude(Double.valueOf(theLocalRoute.SrcPoint.Lat));
        loc1.setLongitude(Double.valueOf(theLocalRoute.SrcPoint.Lng));
        float distanceBetweenPoints;
        float distanceToThePoint1;
        Location loc2 = new Location("");
        float distanceToThePoint2;
        for (LocationPoint location : theLocalRoute.PathRoute.path) {
            if (location != null) {
                loc2 = new Location("");
                loc2.setLatitude(Double.valueOf(location.Lat));
                loc2.setLongitude(Double.valueOf(location.Lng));
                distanceBetweenPoints = loc1.distanceTo(loc2);
                distanceToThePoint1 = loc1.distanceTo(thePointer);
                distanceToThePoint2 = loc2.distanceTo(thePointer);
                if (distanceToThePoint1 + distanceToThePoint2 < minDist + distanceBetweenPoints) {
                    nearestPoint1 = loc1;
                    nearestPoint2 = loc2;
                    minDist = distanceToThePoint1 + distanceToThePoint2 - distanceBetweenPoints;
                }
                if (distanceBetweenPoints / 10000 > coefficient) {
                    coefficient = distanceBetweenPoints / 10000;
                }
                loc1 = new Location("");
                loc1.setLatitude(loc2.getLatitude());
                loc1.setLongitude(loc2.getLongitude());
            }
        }
        distanceBetweenPoints = nearestPoint1.distanceTo(nearestPoint2);
        distanceToThePoint1 = nearestPoint1.distanceTo(thePointer);
        distanceToThePoint2 = nearestPoint2.distanceTo(thePointer);
        if ((distanceToThePoint1 + distanceToThePoint2) > distanceBetweenPoints * coefficient) {
            Location midloc = new Location("");
            midloc.setLatitude((nearestPoint1.getLatitude() + nearestPoint2.getLatitude()) / 2);
            midloc.setLongitude((nearestPoint1.getLongitude() + nearestPoint2.getLongitude()) / 2);
            MoveMapFragment(String.valueOf(midloc.getLatitude()), String.valueOf(midloc.getLongitude()));
        }

        return null;
    }

    @Override
    public void onMapStopDragit(String latitude, String longitude) {
        StopDelayedTask();
        lastLatitude = latitude;
        lastLongitude = longitude;
        StartDelayedTask();
        //getlocalPoints(latitude, longitude);
        //getLocalRoutes(latitude, longitude);
    }

    public void doBtnClicked() {
        RouteRequest routeRequest = new RouteRequest();
        routeRequest.ServiceType = ServiceTypes.RideRequest;
        routeRequest.EventId = 0;
        routeRequest.SrcLatitude = srcLatitude;
        routeRequest.SrcLongitude = srcLongitude;
        routeRequest.SrcGAddress = srcAdd;
        userData.insertNewRouteSrc(routeRequest);
        routeRequest.DstLatitude = dstLatitude;
        routeRequest.DstLongitude = dstLongitude;
        routeRequest.DstGAddress = dstAdd;
        routeRequest.CostMinMax = Float.parseFloat(getPrice());
        routeRequest.IsDrive = false;
        userData.insertNewRouteDst(routeRequest);
        returnOk();
    }

    public Location getNearLocation(String lat, String lng, int radius) {
        double x0=Double.valueOf(lng);
        double y0=Double.valueOf(lat);
        Location location=new Location("");
        Random random = new Random();

        // Convert radius from meters to degrees
        double radiusInDegrees = radius / 111000f;

        double u = random.nextDouble();
        double v = random.nextDouble();
        double w = radiusInDegrees * Math.sqrt(u);
        double t = 2 * Math.PI * v;
        double x = w * Math.cos(t);
        double y = w * Math.sin(t);

        // Adjust the x-coordinate for the shrinking of the east-west distances
        double new_x = x / Math.cos(y0);

        double foundLongitude = new_x + x0;
        double foundLatitude = y + y0;
        location.setLatitude(foundLatitude);
        location.setLongitude(foundLongitude);
        return location;
    }
}
