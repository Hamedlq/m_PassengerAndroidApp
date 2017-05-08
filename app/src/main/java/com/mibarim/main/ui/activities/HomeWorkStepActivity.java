

package com.mibarim.main.ui.activities;


import android.accounts.OperationCanceledException;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.mibarim.main.BootstrapApplication;
import com.mibarim.main.BootstrapServiceProvider;
import com.mibarim.main.R;
import com.mibarim.main.authenticator.TokenRefreshActivity;
import com.mibarim.main.core.LocationService;
import com.mibarim.main.data.UserData;
import com.mibarim.main.events.NetworkErrorEvent;
import com.mibarim.main.events.UnAuthorizedErrorEvent;
import com.mibarim.main.models.Address.AddressComponent;
import com.mibarim.main.models.Address.AddressObject;
import com.mibarim.main.models.Address.AddressResult;
import com.mibarim.main.models.Address.DetailPlaceResult;
import com.mibarim.main.models.Address.LocationPoint;
import com.mibarim.main.models.Address.PathPoint;
import com.mibarim.main.models.ApiResponse;
import com.mibarim.main.models.EventResponse;
import com.mibarim.main.models.RouteRequest;
import com.mibarim.main.models.enums.AddRouteStates;
import com.mibarim.main.models.enums.PricingOptions;
import com.mibarim.main.models.enums.ServiceTypes;
import com.mibarim.main.services.AddressService;
import com.mibarim.main.services.AuthenticateService;
import com.mibarim.main.services.RouteRequestService;
import com.mibarim.main.services.UserInfoService;
import com.mibarim.main.ui.BootstrapActivity;
import com.mibarim.main.ui.HandleApiMessages;
import com.mibarim.main.ui.fragments.addRouteFragments.AddMapFragment;
import com.mibarim.main.ui.fragments.addRouteFragments.MainAddMapFragment;
import com.mibarim.main.ui.fragments.addRouteStepper.DriverPassengerStep;
import com.mibarim.main.ui.fragments.addRouteStepper.MainRouteStepFragment;
import com.mibarim.main.ui.fragments.addRouteStepper.StepAddressFlagFragment;
import com.mibarim.main.ui.fragments.addRouteStepper.StepSrcDstFragment;
import com.mibarim.main.ui.fragments.addRouteStepper.TimeStep;
import com.mibarim.main.util.SafeAsyncTask;
import com.mibarim.main.util.Toaster;
import com.squareup.otto.Subscribe;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import io.fabric.sdk.android.Fabric;

import static com.mibarim.main.models.enums.AddRouteStates.SelectDestinationState;
import static com.mibarim.main.models.enums.AddRouteStates.SelectGoWorkState;
import static com.mibarim.main.models.enums.AddRouteStates.SelectTime;


/**
 * Initial activity for the application.
 * * <p/>
 * If you need to remove the authentication from the application please see
 */
public class HomeWorkStepActivity extends BootstrapActivity implements AddMapFragment.OnMapClickedListener, StepAddressFlagFragment.AddressFlagFragmentListener, DriverPassengerStep.DriverPassFragmentListener, TimeStep.TimeStepFragmentListener, StepperLayout.StepperListener, StepSrcDstFragment.SrcDstStepFragmentListener {

    @Inject
    AddressService addressService;
    @Inject
    RouteRequestService routeRequestService;
    @Inject
    UserInfoService userInfoService;
    @Inject
    BootstrapServiceProvider serviceProvider;
    @Inject
    UserData userData;

    private int Location_SET = 2010;
    private int REFRESH_TOKEN_REQUEST = 3456;

    private Toolbar toolbar;
    private boolean refreshingToken = false;

    public int SelectedPathRouteInRecommendPath = 1;
    private Handler mHandler;
    private int mInterval = 600000;
    private int getInfoDelay = 1000;
    protected AddRouteStates stateSelector;
    private String srcAdd;
    private String dstAdd;
    private String srcLatitude;
    private String srcLongitude;
    private String dstLatitude;
    private String dstLongitude;

    private String lastLatitude;
    private String lastLongitude;

    private ApiResponse response;
    public EventResponse _eventResponse;
    private String pathPrice;
    private boolean isPriceSet = false;
    private String authToken;

    private Tracker mTracker;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        BootstrapApplication.component().inject(this);

        BootstrapApplication application = (BootstrapApplication) getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("RouteStepActivity");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        mTracker.send(new HitBuilders.EventBuilder().setCategory("Activity").setAction("RouteStepActivity").build());

        setContentView(R.layout.main_activity);

        // View injection with Butterknife~
        ButterKnife.bind(this);

        if (getWindow().getDecorView().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
        if (getIntent() != null && getIntent().getExtras() != null) {
            stateSelector = (AddRouteStates) getIntent().getExtras().getSerializable("AddRouteStates");
        } else {
            stateSelector = AddRouteStates.SelectGoHomeState;
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setHomeButtonEnabled(false);
        }
        //checkAuth();
        initScreen();
        Log.d("sevomi", "sevomi");
    }

    private void initScreen() {

        final FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.main_container, new MainRouteStepFragment())
                .commitAllowingStateLoss();
        Log.d("4omi", "4omi");
        SharedPreferences prefs = getSharedPreferences(
                "com.mibarim.main", Context.MODE_PRIVATE);
        srcLatitude = prefs.getString("SrcLatitude", "35.717110");
        srcLongitude = prefs.getString("SrcLongitude", "51.426830");
        mHandler = new Handler();
    }

    private void checkAuth() {
        new SafeAsyncTask<Boolean>() {

            @Override
            public Boolean call() throws Exception {
                final AuthenticateService svc = serviceProvider.getService(HomeWorkStepActivity.this);
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
                initScreen();
            }
        }.execute();
    }

    @Override
    public void onBackPressed() {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.main_container);
        if (fragment != null) {
            ((MainRouteStepFragment) fragment).backStayButton();
        }
    }

    @Subscribe
    public void onNetworkErrorEvent(NetworkErrorEvent event) {
        //showSnackBar(R.string.network_error);
        Toaster.showLong(HomeWorkStepActivity.this, getString(R.string.network_error), R.drawable.toast_warn);
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

    /*@Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Location_SET && resultCode == RESULT_OK) {
            String PlaceId = data.getStringExtra("PlaceId");
            getPlaceDetail(PlaceId);
        }
        if (requestCode == REFRESH_TOKEN_REQUEST && resultCode == RESULT_OK) {
            authToken = null;
            serviceProvider.invalidateAuthToken();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    refreshingToken = false;
                }
            }, 5000);
        }
    }

    @Override
    public AddRouteStates getRouteStates() {
        return stateSelector;
    }

    @Override
    public void gotoMyLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            android.location.Location location = LocationService.getLocationManager(this).getLocation();
            if (location != null) {
                MoveMapFragment(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));
            }
        } else {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }
    }


    @Override
    public void setSelectedPathPoint(int i) {
        SelectedPathRouteInRecommendPath = i;
    }

    @Override
    public void setRouteStates(AddRouteStates state) {
        stateSelector = state;
        final FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.main_container);
        if (fragment != null) {
            ((MainRouteStepFragment) fragment).setState(state);
        }
    }

    @Override
    public String getInfoText() {
        return getString(R.string.morning_sher);
    }

    @Override
    public void saveRoute() {
        SharedPreferences prefs = getSharedPreferences(
                "com.mibarim.main", Context.MODE_PRIVATE);
        RouteRequest routeRequest = new RouteRequest();
        routeRequest.ServiceType = ServiceTypes.RideShare;
        routeRequest.EventId = 0;
        routeRequest.SrcGAddress = srcAdd;
        routeRequest.SrcLatitude = srcLatitude;
        routeRequest.SrcLongitude = srcLongitude;
        routeRequest.DstGAddress = dstAdd;
        routeRequest.DstLatitude = dstLatitude;
        routeRequest.DstLongitude = dstLongitude;
        routeRequest.CostMinMax = Float.parseFloat(getPrice());
        routeRequest.IsDrive = prefs.getBoolean("IsDriver", false);
        RouteRequest tempRouteRequest = getDateParams();
        routeRequest.IsReturn = tempRouteRequest.IsReturn;
        routeRequest.TheTime = tempRouteRequest.TheTime;
        routeRequest.SatDatetime = tempRouteRequest.SatDatetime;
        routeRequest.SunDatetime = tempRouteRequest.SunDatetime;
        routeRequest.MonDatetime = tempRouteRequest.MonDatetime;
        routeRequest.TueDatetime = tempRouteRequest.TueDatetime;
        routeRequest.WedDatetime = tempRouteRequest.WedDatetime;
        routeRequest.ThuDatetime = tempRouteRequest.ThuDatetime;
        routeRequest.FriDatetime = tempRouteRequest.FriDatetime;
        routeRequest.TimingOption = tempRouteRequest.TimingOption;
        routeRequest.PricingOption = PricingOptions.MinMax;
        if (validateRouteRequest(routeRequest)) {
            saveRouteRequest(routeRequest);
        }
    }


    @Override
    public String getSrcAddress() {
        return srcAdd;
    }

    @Override
    public String getDstAddress() {
        return dstAdd;
    }

    @Override
    public String getEventAddress() {
        return _eventResponse.Address;
    }

    @Override
    public void gotoLocationActivity() {
        Intent intent = new Intent(this, LocationSearchActivity.class);
        this.startActivityForResult(intent, Location_SET);
    }

    @Override
    public void doBtnClicked() {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.main_container);
        if (fragment != null) {
            ((MainRouteStepFragment) fragment).setNextState();
        }
    }

    @Override
    public void onMapStartDrag() {

    }

    @Override
    public void onMapStopDrag(String latitude, String longitude) {
        lastLatitude = latitude;
        lastLongitude = longitude;
        StopDelayedTask();
        StartSetAddress();
        if (IsPriceVisible()) {
            isPriceSet = false;
        }
        StartDelayedTask();
    }


    @Override
    public void setSrcLatLng(String latitude, String longitude) {
        srcLatitude = latitude;
        srcLongitude = longitude;
    }

    @Override
    public void setDstLatLng(String latitude, String longitude) {
        dstLatitude = latitude;
        dstLongitude = longitude;
    }

    @Override
    public LocationPoint getSrcLatLng() {
        LocationPoint location = new LocationPoint();
        location.Lat = srcLatitude;
        location.Lng = srcLongitude;
        return location;
    }

    @Override
    public LocationPoint getDstLatLng() {
        LocationPoint location = new LocationPoint();
        location.Lat = dstLatitude;
        location.Lng = dstLongitude;
        return location;
    }

    @Override
    public List<PathPoint> getRecommendPathPointList() {
        return null;
    }

    @Override
    public int getSelectedPathPoint() {
        return 0;
    }

    private void getPlaceDetail(final String placeId) {
        new SafeAsyncTask<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                if (getRouteStates() == AddRouteStates.SelectOriginState
                        || getRouteStates() == AddRouteStates.SelectGoHomeState) {
                    DetailPlaceResult response = addressService.getPlaceDetail(placeId);
                    srcLatitude = response.result.geometry.location.lat;
                    srcLongitude = response.result.geometry.location.lng;
                    /*routeRequest.SrcLatitude = srcLatitude;
                    routeRequest.SrcLongitude = srcLongitude;*/
                    return true;
                }
                if (getRouteStates() == SelectDestinationState ||
                        getRouteStates() == SelectGoWorkState) {
                    DetailPlaceResult response = addressService.getPlaceDetail(placeId);
                    dstLatitude = response.result.geometry.location.lat;
                    dstLongitude = response.result.geometry.location.lng;
                    /*routeRequest.DstLatitude = dstLatitude;
                    routeRequest.DstLongitude = dstLongitude;*/
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
                if (getRouteStates() == AddRouteStates.SelectOriginState
                        || getRouteStates() == AddRouteStates.SelectEventOriginState
                        || getRouteStates() == AddRouteStates.SelectGoHomeState) {
                    getAddress("SOURCE");
                    MoveMapFragment(srcLatitude, srcLongitude);
                } else if (getRouteStates() == SelectDestinationState
                        || getRouteStates() == AddRouteStates.SelectEventDestinationState
                        || getRouteStates() == SelectGoWorkState) {
                    getAddress("DESTINATION");
                    MoveMapFragment(dstLatitude, dstLongitude);
                }
            }
        }.execute();
    }

    private void MoveMapFragment(String lat, String lng) {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.main_container);
        if (fragment != null) {
            ((MainRouteStepFragment) fragment).MoveMap(lat, lng);
        }
    }

    private void StartSetAddress() {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.main_container);
        if (fragment != null) {
            ((MainRouteStepFragment) fragment).waitingAddress();
        }
    }

    void StopDelayedTask() {
        if (mGettingInfo != null) {
            mHandler.removeCallbacks(mGettingInfo);
        }
    }

    void StartDelayedTask() {
        mHandler.postDelayed(mGettingInfo, getInfoDelay);
    }

    Runnable mGettingInfo = new Runnable() {
        @Override
        public void run() {
            gettingPathInfo(lastLatitude, lastLongitude);
        }
    };

    private void gettingPathInfo(String latitude, String longitude) {
        if (stateSelector == AddRouteStates.SelectOriginState || stateSelector == AddRouteStates.SelectEventOriginState
                || stateSelector == AddRouteStates.SelectGoHomeState || stateSelector == AddRouteStates.SelectReturnWorkState) {
            srcLatitude = latitude;
            srcLongitude = longitude;
            /*routeRequest.SrcLatitude = latitude;
            routeRequest.SrcLongitude = longitude;*/
            getAddress("SOURCE");
        } else {
            dstLatitude = latitude;
            dstLongitude = longitude;
            /*routeRequest.DstLatitude = latitude;
            routeRequest.DstLongitude = longitude;*/
            getAddress("DESTINATION");
        }
        if (IsPriceVisible()) {
            getPathPrice();
        }
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

    private void SetAddress() {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.main_container);
        if (fragment != null) {
            ((MainRouteStepFragment) fragment).RebuildAddressFragment();
        }
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

    public void setSrcAddress(String address) {
        srcAdd = address;
        //routeRequest.SrcGAddress = address;
    }

    public void setDstAddress(String address) {
        dstAdd = address;
        //routeRequest.DstGAddress = address;
    }

    private boolean IsPriceVisible() {
        SharedPreferences prefs = getSharedPreferences(
                "com.mibarim.main", Context.MODE_PRIVATE);
        if (!prefs.getBoolean("IsDriver", false)) {
            if (stateSelector == SelectDestinationState || stateSelector == AddRouteStates.SelectEventDestinationState
                    || stateSelector == SelectGoWorkState || stateSelector == AddRouteStates.SelectReturnHomeState
                    || stateSelector == AddRouteStates.SelectEventOriginState || stateSelector == AddRouteStates.SelectEventDestinationState) {
                return true;
            }
        }
        return false;
    }

    private void getPathPrice() {
        isPriceSet = false;
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
        isPriceSet = true;
        final FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.main_container);
        if (fragment != null) {
            ((MainRouteStepFragment) fragment).setPrice(pathPrice);
        }
    }

    public String getPrice() {
        if (pathPrice == null || pathPrice.equals("")) {
            return "0";
        } else {
            return pathPrice;
        }
    }

    private boolean validateRouteRequest(RouteRequest routeRequest) {
        boolean res = true;
        if (routeRequest.SrcLatitude == null || routeRequest.SrcLatitude.equals("") ||
                routeRequest.DstLatitude == null || routeRequest.DstLatitude.equals("")) {
            Toaster.showLong(HomeWorkStepActivity.this, getString(R.string.src_dest_not_set), R.drawable.toast_warn);
            res = false;
        }
        if (routeRequest.TheTimeString() == null || routeRequest.TheTimeString().equals("")) {
            Toaster.showLong(HomeWorkStepActivity.this, getString(R.string.time_not_set), R.drawable.toast_warn);
            res = false;
        }
        return res;
    }

    public void saveRouteRequest(final RouteRequest routeRequest) {
        showProgress();
        new SafeAsyncTask<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                if (authToken == null) {
                    serviceProvider.invalidateAuthToken();
                    authToken = serviceProvider.getAuthToken(HomeWorkStepActivity.this);
                }
                response = routeRequestService.SubmitNewRoute(authToken, routeRequest);
                if ((response.Errors == null || response.Errors.size() == 0) && response.Status.equals("OK")) {
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
                Toaster.showLong(HomeWorkStepActivity.this, getString(R.string.network_error), R.drawable.toast_warn);
            }

            @Override
            protected void onSuccess(final Boolean isRouteSubmitted) throws Exception {
                super.onSuccess(isRouteSubmitted);
                hideProgress();
                if (isRouteSubmitted) {
                    Toaster.showLong(HomeWorkStepActivity.this, getString(R.string.saved_route), R.drawable.toast_info);
                    setResult(RESULT_OK);
                    finish();
                } else {
                    new HandleApiMessages(HomeWorkStepActivity.this, response).showMessages();
                }
            }
        }.execute();
    }

    @Override
    public void onCompleted(View completeButton) {
        saveRoute();
    }

    private RouteRequest getDateParams() {
        RouteRequest r = new RouteRequest();
        final FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.main_container);
        if (fragment != null) {
            r = ((MainRouteStepFragment) fragment).getDateParams();
        }
        return r;
    }

    @Override
    public void onError(VerificationError verificationError) {

    }

    @Override
    public void onStepSelected(int newStepPosition) {

    }

    @Override
    public void onReturn() {
    }
}
