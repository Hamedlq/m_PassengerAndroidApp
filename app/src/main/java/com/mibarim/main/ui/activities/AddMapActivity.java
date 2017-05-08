package com.mibarim.main.ui.activities;


import android.Manifest;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.accounts.OperationCanceledException;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

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
import com.mibarim.main.models.Address.Location;
import com.mibarim.main.models.Address.LocationPoint;
import com.mibarim.main.models.Address.PathPoint;
import com.mibarim.main.models.ApiResponse;
import com.mibarim.main.models.CityLocation;
import com.mibarim.main.models.RouteRequest;
import com.mibarim.main.models.EventResponse;
import com.mibarim.main.models.enums.AddRouteStates;
import com.mibarim.main.models.enums.ServiceTypes;
import com.mibarim.main.services.AddressService;
import com.mibarim.main.services.AuthenticateService;
import com.mibarim.main.services.RegisterService;
import com.mibarim.main.services.RouteRequestService;
import com.mibarim.main.services.UserInfoService;
import com.mibarim.main.ui.BootstrapActivity;
import com.mibarim.main.ui.HandleApiMessages;
import com.mibarim.main.ui.fragments.addRouteFragments.AddMapFragment;
import com.mibarim.main.ui.fragments.addRouteFragments.MainAddMapFragment;
import com.mibarim.main.util.SafeAsyncTask;
import com.mibarim.main.util.Toaster;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

//import org.osmdroid.tileprovider.constants.OpenStreetMapTileProviderConstants;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.fabric.sdk.android.Fabric;
import timber.log.Timber;


/**
 * Initial activity for the application.
 * * <p/>
 * If you need to remove the authentication from the application please see
 */
public class AddMapActivity extends BootstrapActivity implements AddMapFragment.OnMapClickedListener {
    static final String TAG = "AddMapActivity";

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

    private CharSequence title;
    private Toolbar toolbar;

    private String srcAdd;
    private String dstAdd;

    public RouteRequest routeRequest;
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
    private boolean isPriceSet=false;

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
        mTracker.setScreenName("AddMapActivity");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        mTracker.send(new HitBuilders.EventBuilder().setCategory("Activity").setAction("AddMapActivity").build());

        setContentView(R.layout.main_activity);

        // View injection with Butterknife
        ButterKnife.bind(this);

        if (getWindow().getDecorView().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }


        if (getIntent() != null && getIntent().getExtras() != null) {
            stateSelector = (AddRouteStates) getIntent().getExtras().getSerializable("AddRouteStates");
        } else {
            stateSelector = AddRouteStates.SelectOriginState;
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

        routeRequest = new RouteRequest();
        if (stateSelector == AddRouteStates.SelectEventOriginState || stateSelector == AddRouteStates.SelectEventDestinationState) {
            _eventResponse = (EventResponse) getIntent().getExtras().getSerializable("EventResponse");
        }
        checkAuth();

//        initScreen();
    }


    private void initScreen() {
        //userData.DeleteTime();
        SharedPreferences prefs = getSharedPreferences(
                "com.mibarim.main", Context.MODE_PRIVATE);
        lastLatitude = prefs.getString("SrcLatitude", "35.717110");
        lastLongitude = prefs.getString("SrcLongitude", "51.426830");
        srcLatitude = lastLatitude;
        srcLongitude = lastLongitude;
        gettingPathInfo();
        getlocalPoints(lastLatitude, lastLongitude);
        final FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.main_container, new MainAddMapFragment())
                .commitAllowingStateLoss();
        mHandler = new Handler();
        if (stateSelector == AddRouteStates.SelectEventOriginState || stateSelector == AddRouteStates.SelectEventDestinationState) {
            selectEvent();
        }

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


    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        switch (item.getItemId()) {
            case android.R.id.home:
                switch (getSrcDstStateSelector()) {
                    case SelectOriginState:
                        finish();
                        break;
                    case SelectDestinationState:
                        switch (getSrcDstStateSelector()) {
                            case SelectDestinationState:
                                setSrcDstStateSelector(AddRouteStates.SelectOriginState);
                                break;
                            case SelectGoWorkState:
                                setSrcDstStateSelector(AddRouteStates.SelectGoHomeState);
                                break;
                            case SelectReturnHomeState:
                                setSrcDstStateSelector(AddRouteStates.SelectReturnWorkState);
                                break;
                        }
                        fragmentManager.beginTransaction()
                                .replace(R.id.main_container, new MainAddMapFragment())
                                .commitAllowingStateLoss();
                        break;
                    case SelectDriveRouteState:
//                    case "REQUESTING":
//                        goToDestination();
//                        break;
//                    case "EVENT_LIST_SELECTED":
//                        goToSource();
//                        break;
                    default:
                        finish();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void checkAuth() {
        new SafeAsyncTask<Boolean>() {

            @Override
            public Boolean call() throws Exception {
                final AuthenticateService svc = serviceProvider.getService(AddMapActivity.this);
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

/*
    private void navigateToContactUs() {
        final Intent i = new Intent(this, ContactUsActivity.class);
        startActivity(i);
    }
*/

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            final FragmentManager fragmentManager = getSupportFragmentManager();
            switch (getSrcDstStateSelector()) {
                case SelectOriginState:
                    finish();
                    break;
                case SelectDestinationState:
                    switch (getSrcDstStateSelector()) {
                        case SelectDestinationState:
                            setSrcDstStateSelector(AddRouteStates.SelectOriginState);
                            break;
                        case SelectGoWorkState:
                            setSrcDstStateSelector(AddRouteStates.SelectGoHomeState);
                            break;
                        case SelectReturnHomeState:
                            setSrcDstStateSelector(AddRouteStates.SelectReturnWorkState);
                            break;
                    }
                    fragmentManager.beginTransaction()
                            .replace(R.id.main_container, new MainAddMapFragment())
                            .commitAllowingStateLoss();
                    break;
                case SelectDriveRouteState:
//                    case "REQUESTING":
//                        goToDestination();
//                        break;
//                    case "EVENT_LIST_SELECTED":
//                        goToSource();
//                        break;
                default:
                    finish();
            }
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    public void setSrcDstStateSelector(AddRouteStates state) {
        stateSelector = state;
    }

    public AddRouteStates getSrcDstStateSelector() {
        return stateSelector;
    }

    public void setSrcAddress(String address) {
        srcAdd = address;
        routeRequest.SrcGAddress = address;
    }

    public String getSrcAddress() {
        return srcAdd;
    }

    @Override
    public void setSrcLatLng(String latitude, String longitude) {
        srcLatitude = latitude;
        srcLongitude = longitude;
    }

    @Override
    public LocationPoint getSrcLatLng() {
        LocationPoint location = new LocationPoint();
        location.Lat = srcLatitude;
        location.Lng = srcLongitude;
        return location;
    }

    @Override
    public void setDstLatLng(String latitude, String longitude) {
        dstLatitude = latitude;
        dstLongitude = longitude;
    }

    @Override
    public LocationPoint getDstLatLng() {
        LocationPoint location = new LocationPoint();
        location.Lat = dstLatitude;
        location.Lng = dstLongitude;
        return location;
    }

    public void setDstAddress(String address) {
        dstAdd = address;
        routeRequest.DstGAddress = address;
    }

    public String getDstAddress() {
        return dstAdd;
    }


    @Override
    public AddRouteStates getRouteStates() {
        return getSrcDstStateSelector();
    }

    @Override
    public void onMapStartDrag() {
        /*StopDelayedTask();
        StartSetAddress();*/
        if (stateSelector == AddRouteStates.SelectOriginState) {
//            StartSetAddress();
        } else {

        }
    }

    @Override
    public void onMapStopDrag(String latitude, String longitude) {
        StopDelayedTask();
        StartSetAddress();
        if (IsPriceVisible()) {
            isPriceSet=false;
        }
        lastLatitude = latitude;
        lastLongitude = longitude;
        StartDelayedTask();
    }

    private void gettingPathInfo() {
        String latitude = lastLatitude;
        String longitude = lastLongitude;
        if (stateSelector == AddRouteStates.SelectOriginState || stateSelector == AddRouteStates.SelectEventOriginState
                || stateSelector == AddRouteStates.SelectGoHomeState || stateSelector == AddRouteStates.SelectReturnWorkState) {
            srcLatitude = latitude;
            srcLongitude = longitude;
            routeRequest.SrcLatitude = latitude;
            routeRequest.SrcLongitude = longitude;
            getAddress("SOURCE");
        } else {
            dstLatitude = latitude;
            dstLongitude = longitude;
            routeRequest.DstLatitude = latitude;
            routeRequest.DstLongitude = longitude;
            getAddress("DESTINATION");
        }
        if (IsPriceVisible()) {
            getPathPrice();
        }
    }

    Runnable mGettingInfo = new Runnable() {
        @Override
        public void run() {
            gettingPathInfo();
            getlocalPoints(lastLatitude, lastLongitude);
        }
    };

    private void getlocalPoints(final String latitude, final String longitude) {
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

    }

    private void showPointsOnMap() {
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
    }


    void StartDelayedTask() {
        mHandler.postDelayed(mGettingInfo, getInfoDelay);
    }

    void StopDelayedTask() {
        if (mGettingInfo != null) {
            mHandler.removeCallbacks(mGettingInfo);
        }
    }


    private void getPathPrice() {
        isPriceSet=false;
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
        isPriceSet=true;
        final FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.main_container);
        ((MainAddMapFragment) fragment).setPrice(pathPrice);
    }

    @Override
    public List<PathPoint> getRecommendPathPointList() {
        if (recommendPathPointList == null) {
            return new ArrayList<>();
        }
        return recommendPathPointList;
    }

    @Override
    public int getSelectedPathPoint() {
        return SelectedPathRouteInRecommendPath;
    }

    public void setSelectedPathPoint(int i) {
        SelectedPathRouteInRecommendPath = i;
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
        if (fragment != null && fragment instanceof MainAddMapFragment) {
            ((MainAddMapFragment) fragment).RebuildAddressFragment();
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

    /*public void sendTaxiRequest() {
        showProgress();
        new SafeAsyncTask<Boolean>() {

            @Override
            public Boolean call() throws Exception {
                if (authToken == null) {
                    serviceProvider.invalidateAuthToken();
                    authToken = serviceProvider.getAuthToken(AddMapActivity.this);
                }
                response = routeRequestService.SubmitNewRoute(authToken, routeRequest);
                if ((response.Errors == null || response.Errors.size() == 0) && response.Status.equals("OK")) {
                    //routeRequest.RequestId = response.Messages.get(0);
                    return true;
                }
                //}
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
                hideProgress();
            }

            @Override
            protected void onSuccess(final Boolean isRouteSubmitted) throws Exception {
                super.onSuccess(isRouteSubmitted);
                hideProgress();
                if (isRouteSubmitted) {
                    showResultMessage();
                }
//                    RequestingTaxi();
//                } else {

                new HandleApiMessages(AddMapActivity.this, response).showMessages();
//                }
            }
        }.execute();

    }*/

    private void showResultMessage() {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.main_container);
        if (routeRequest.ServiceType == ServiceTypes.EventRide) {
            //((MainAddMapFragment) fragment).showResultMessage(String.format(getString(R.string.submit_event), routeRequest.RequestId));
        } else {
            //((MainAddMapFragment) fragment).showResultMessage(String.format(getString(R.string.submit_taxi), routeRequest.RequestId));
        }
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
            Toaster.showLong(AddMapActivity.this, getString(R.string.retry), R.drawable.toast_warn);
        }
        if (requestCode == Drive_SET && resultCode == RESULT_OK) {
            RebuildSrcDstFragment();
        }
        if (requestCode == Location_SET && resultCode == RESULT_OK) {
            String PlaceId = data.getStringExtra("PlaceId");
            getPlaceDetail(PlaceId);
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
        ((MainAddMapFragment) fragment).MoveMap(lat, lng);
    }

    private void getPlaceDetail(final String placeId) {
        new SafeAsyncTask<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                if (getSrcDstStateSelector() == AddRouteStates.SelectOriginState
                        || getSrcDstStateSelector() == AddRouteStates.SelectGoHomeState) {
                    DetailPlaceResult response = addressService.getPlaceDetail(placeId);
                    srcLatitude = response.result.geometry.location.lat;
                    srcLongitude = response.result.geometry.location.lng;
                    routeRequest.SrcLatitude = srcLatitude;
                    routeRequest.SrcLongitude = srcLongitude;
                    return true;
                }
                if (getSrcDstStateSelector() == AddRouteStates.SelectDestinationState ||
                        getSrcDstStateSelector() == AddRouteStates.SelectGoWorkState) {
                    DetailPlaceResult response = addressService.getPlaceDetail(placeId);
                    dstLatitude = response.result.geometry.location.lat;
                    dstLongitude = response.result.geometry.location.lng;
                    routeRequest.DstLatitude = dstLatitude;
                    routeRequest.DstLongitude = dstLongitude;
                    return true;
                }
                return false;
            }

            @Override
            protected void onException(final Exception e) throws RuntimeException {
                super.onException(e);
                if (e instanceof OperationCanceledException) {
                    //finish();
                }
            }

            @Override
            protected void onSuccess(final Boolean res) throws Exception {
                super.onSuccess(res);
                //Toaster.showLong(AddMapActivity.this, srcLatitude + " " + srcLongitude);
                if (getSrcDstStateSelector() == AddRouteStates.SelectOriginState
                        || getSrcDstStateSelector() == AddRouteStates.SelectEventOriginState
                        || getSrcDstStateSelector() == AddRouteStates.SelectGoHomeState) {
                    getAddress("SOURCE");
                    MoveMapFragment(srcLatitude, srcLongitude);
                } else if (getSrcDstStateSelector() == AddRouteStates.SelectDestinationState
                        || getSrcDstStateSelector() == AddRouteStates.SelectEventDestinationState
                        || getSrcDstStateSelector() == AddRouteStates.SelectGoWorkState) {
                    getAddress("DESTINATION");
                    MoveMapFragment(dstLatitude, dstLongitude);
                }
            }
        }.execute();
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

    public void selectEvent() {
        routeRequest.ServiceType = ServiceTypes.EventRide;
        routeRequest.EventId = _eventResponse.EventId;
        srcLatitude = _eventResponse.Latitude;
        srcLongitude = _eventResponse.Longitude;
        dstLatitude = _eventResponse.Latitude;
        dstLongitude = _eventResponse.Longitude;
        switch (stateSelector) {
            case SelectEventOriginState:
                //goToEventSource();
                break;
            case SelectEventDestinationState:
                RouteRequest dataRouteRequest=userData.routeRequestQuery();
                if(dataRouteRequest.SrcLatitude!=null){
                    dstLatitude = dataRouteRequest.SrcLatitude;
                    dstLongitude = dataRouteRequest.SrcLongitude;
                }
                break;
        }
    }

    private void goToEventDestination() {
        setSrcDstStateSelector(AddRouteStates.SelectEventDestinationState);
        final FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.main_container);
        ((MainAddMapFragment) fragment).EventDestinationFragment();
        SetAddress();
    }

    public void RebuildAddMapFragment() {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.main_container);
        ((MainAddMapFragment) fragment).ReDrawAddMapFragment();
    }

    private void goToEventSource() {
        //setSrcDstStateSelector(AddRouteStates.SelectEventOriginState);
        final FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.main_container);
        if (fragment != null) {
            ((MainAddMapFragment) fragment).EventSourceFragment();
        }
        //SetAddress();
    }


    public String getEventAddress() {
        return _eventResponse.Address;
    }

    public EventResponse getEvent() {
        return _eventResponse;
    }

    /*public void hideMsg() {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.main_container);
        ((MainAddMapFragment) fragment).hideSubmitMsg();
        goToSource();
    }*/

    public void doBtnClicked() {
        if (IsPriceVisible()) {
            if(!isPriceSet){
                Toaster.showLong(AddMapActivity.this, getString(R.string.price_not_set), R.drawable.toast_warn);
                return;
            }
        }
        SharedPreferences prefs = getSharedPreferences(
                "com.mibarim.main", Context.MODE_PRIVATE);
        switch (getSrcDstStateSelector()) {
            case SelectGoHomeState:
                routeRequest.ServiceType = ServiceTypes.RideShare;
                routeRequest.EventId = 0;
                routeRequest.SrcLatitude = srcLatitude;
                routeRequest.SrcLongitude = srcLongitude;
                routeRequest.SrcGAddress = srcAdd;
                userData.insertNewRouteSrc(routeRequest);
                setSrcDstStateSelector(AddRouteStates.SelectGoWorkState);
                RebuildDstFragment();
                break;
            case SelectReturnWorkState:
                routeRequest.ServiceType = ServiceTypes.RideShare;
                routeRequest.EventId = 0;
                routeRequest.SrcLatitude = srcLatitude;
                routeRequest.SrcLongitude = srcLongitude;
                routeRequest.SrcGAddress = srcAdd;
                userData.insertNewRouteSrc(routeRequest);
                setSrcDstStateSelector(AddRouteStates.SelectReturnHomeState);
                lastLatitude = prefs.getString("DstLatitude", "35.717110");
                lastLongitude = prefs.getString("DstLongitude", "51.426830");
                RebuildDstFragment(lastLatitude, lastLongitude);
                break;
            case SelectGoWorkState:
            case SelectReturnHomeState:
                routeRequest.DstLatitude = dstLatitude;
                routeRequest.DstLongitude = dstLongitude;
                routeRequest.DstGAddress = dstAdd;
                routeRequest.CostMinMax = Float.parseFloat(getPrice());
                routeRequest.IsDrive = prefs.getBoolean("IsDriver", false);
                userData.insertNewRouteDst(routeRequest);
                if (prefs.getBoolean("IsDriver", false)) {
                    getGRoutesFromServer();
                } else {
                    returnOk();
                }
                break;
            case SelectDriveRouteState:
                if (SelectedPathRouteInRecommendPath != 0) {
                    PathPoint selectedPathPoint = recommendPathPointList.get(SelectedPathRouteInRecommendPath - 1);
                    userData.insertNewRouteIndex(selectedPathPoint.metadata.name);
                }
                returnOk();
                break;

            case SelectOriginState:
                routeRequest.ServiceType = ServiceTypes.RideShare;
                routeRequest.EventId = 0;
                routeRequest.SrcLatitude = srcLatitude;
                routeRequest.SrcLongitude = srcLongitude;
                routeRequest.SrcGAddress = srcAdd;
                userData.insertNewRouteSrc(routeRequest);
                setSrcDstStateSelector(AddRouteStates.SelectDestinationState);
                RebuildDstFragment();
                break;
            case SelectDestinationState:
                routeRequest.DstLatitude = dstLatitude;
                routeRequest.DstLongitude = dstLongitude;
                routeRequest.DstGAddress = dstAdd;
                routeRequest.CostMinMax = Float.parseFloat(getPrice());
                routeRequest.IsDrive = prefs.getBoolean("IsDriver", false);
                userData.insertNewRouteDst(routeRequest);
                if (prefs.getBoolean("IsDriver", false)) {
                    getGRoutesFromServer();
                } else {
                    returnOk();
                }
                break;
            case SelectEventOriginState:
                routeRequest.ServiceType = ServiceTypes.EventRide;
                routeRequest.EventId = _eventResponse.EventId;
                routeRequest.SrcLatitude = srcLatitude;
                routeRequest.SrcLongitude = srcLongitude;
                routeRequest.SrcGAddress = srcAdd;
                routeRequest.CostMinMax = Float.parseFloat(getPrice());
                userData.insertNewRouteSrc(routeRequest);
                returnOk();
                break;
            case SelectEventDestinationState:
                routeRequest.ServiceType = ServiceTypes.EventRide;
                routeRequest.EventId = _eventResponse.EventId;
                routeRequest.DstLatitude = dstLatitude;
                routeRequest.DstLongitude = dstLongitude;
                routeRequest.DstGAddress = dstAdd;
                routeRequest.CostMinMax = Float.parseFloat(getPrice());
                routeRequest.IsDrive = prefs.getBoolean("IsDriver", false);
                /*if(userData.routeRequestQuery().SrcLatitude==null|| userData.routeRequestQuery().SrcLatitude.equals("")){
                    userData.insertNewRouteSrc(routeRequest);
                }*/
                userData.insertNewRouteEventDst(routeRequest);
                returnOk();
                break;
        }
    }

    private void returnOk() {
        Intent intent = getIntent();
        setResult(RESULT_OK, intent);
        finish();
    }

    private void getGRoutesFromServer() {
        showProgress();
        new SafeAsyncTask<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                recommendRoutes = routeRequestService.getGoogleRoute(srcLatitude, srcLongitude, dstLatitude, dstLongitude, wayPoints);
                if (recommendRoutes.Messages.size() > 0) {
                    return true;
                }
                return false;
            }

            @Override
            protected void onException(final Exception e) throws RuntimeException {
                super.onException(e);
                hideProgress();
                Toaster.showLong(AddMapActivity.this, R.string.message_timeout);
            }

            @Override
            protected void onSuccess(final Boolean res) throws Exception {
                super.onSuccess(res);
                hideProgress();
                if (res) {
                    showRecommendRoutes();
                } else {
                    Toaster.showLong(AddMapActivity.this, R.string.error_occured);
                }
            }
        }.execute();
    }

    private void showRecommendRoutes() {
        recommendPathPointList = new ArrayList<>();
        Gson gson = new Gson();
        for (String eventJson : recommendRoutes.Messages) {
            PathPoint pathPoint = gson.fromJson(eventJson, PathPoint.class);
            recommendPathPointList.add(pathPoint);
        }
        new HandleApiMessages(this, recommendRoutes).showMessages();
        setSrcDstStateSelector(AddRouteStates.SelectDriveRouteState);
        RebuildMainAddMapFragment();
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

    private boolean IsPriceVisible(){
        SharedPreferences prefs = getSharedPreferences(
                "com.mibarim.main", Context.MODE_PRIVATE);
        if(!prefs.getBoolean("IsDriver", false)){
        if (stateSelector == AddRouteStates.SelectDestinationState || stateSelector == AddRouteStates.SelectEventDestinationState
                || stateSelector == AddRouteStates.SelectGoWorkState || stateSelector == AddRouteStates.SelectReturnHomeState
                || stateSelector == AddRouteStates.SelectEventOriginState || stateSelector == AddRouteStates.SelectEventDestinationState) {
            return true;
        }
        }
        return false;
    }
}
