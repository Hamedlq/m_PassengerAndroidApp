

package com.mibarim.main.ui.activities;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.OperationCanceledException;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import com.crashlytics.android.Crashlytics;
import com.mibarim.main.BootstrapApplication;
import com.mibarim.main.BootstrapServiceProvider;
import com.mibarim.main.R;
import com.mibarim.main.authenticator.TokenRefreshActivity;
import com.mibarim.main.data.UserData;
import com.mibarim.main.events.NetworkErrorEvent;
import com.mibarim.main.events.UnAuthorizedErrorEvent;
import com.mibarim.main.models.ApiResponse;
import com.mibarim.main.models.EventResponse;
import com.mibarim.main.models.RouteRequest;
import com.mibarim.main.models.enums.AddRouteStates;
import com.mibarim.main.models.enums.PricingOptions;
import com.mibarim.main.models.enums.ServiceTypes;
import com.mibarim.main.models.enums.TimingOptions;
import com.mibarim.main.services.RouteRequestService;
import com.mibarim.main.ui.BootstrapActivity;
import com.mibarim.main.ui.HandleApiMessages;
import com.mibarim.main.ui.fragments.eventFragments.EventMainFragment;
import com.mibarim.main.ui.fragments.mainFragments.AddMainFragment;
import com.mibarim.main.util.SafeAsyncTask;
import com.mibarim.main.util.Toaster;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;

import butterknife.ButterKnife;
import io.fabric.sdk.android.Fabric;


/**
 * Initial activity for the application.
 * * <p/>
 * If you need to remove the authentication from the application please see
 */
public class EventMapActivity extends BootstrapActivity implements EventMainFragment.FragmentInterface {

    @Inject
    BootstrapServiceProvider serviceProvider;
    @Inject
    RouteRequestService routeRequestService;
    @Inject
    UserData userData;

    private CharSequence title;
    private Toolbar toolbar;
    private int RELOAD_REQUEST = 1234;
    private int SET_EVENT_ORIGIN= 2345;
    private int SET_EVENT_DEST= 5432;
    private int SET_TIME_DETAIL = 3917;
    private int REFRESH_TOKEN_REQUEST = 3456;
    private int Drive_SET = 8191;
    private RouteRequest routeRequest;
    private ApiResponse response;
    private EventResponse eventResponse;
    private boolean refreshingToken = false;
/*
    private float goPrice;
    private float returnPrice;
*/

    private String authToken;
    private boolean week_time_set = false;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        BootstrapApplication.component().inject(this);

        setContentView(R.layout.main_activity);

        // View injection with Butterknife
        ButterKnife.bind(this);

        if (getWindow().getDecorView().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }


        if (getIntent() != null && getIntent().getExtras() != null) {
            eventResponse = (EventResponse) getIntent().getExtras().getSerializable("EventResponse");
        }

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
        initScreen();

    }

    private void initScreen() {
        userData.DeleteRouteRequest();
        final FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.main_container, new EventMainFragment())
                .commit();
        //gotoDriverActivity();
/*
        SharedPreferences prefs = this.getSharedPreferences(
                "com.mibarim.main", Context.MODE_PRIVATE);
        if (prefs.getInt("UserInitialShown", 0) != 1) {
            prefs.edit().putInt("UserInitialShown", 1).apply();
            final Intent i = new Intent(this, UserInitialActivity.class);
            startActivity(i);
        }
*/

    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //Toaster.showLong(this, R.string.please_fill);
            //return true;
        }
        return super.onKeyUp(keyCode, event);
    }


    @Subscribe
    public void onNetworkErrorEvent(NetworkErrorEvent event) {
        Toaster.showLong(EventMapActivity.this, getString(R.string.network_error), R.drawable.toast_warn);
    }


    @Subscribe
    public void onUnAuthorizedErrorEvent(UnAuthorizedErrorEvent event) {
        refreshToken();
    }

    private void refreshToken() {
        /*if (authToken != null) {
            final Intent i = new Intent(this, TokenRefreshActivity.class);
            startActivityForResult(i, RELOAD_REQUEST);
        }*/
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
//        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
//            public void onCancel(final DialogInterface dialog) {
//            }
//        });
        return dialog;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                //Toaster.showLong(this, R.string.please_fill);
                //return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
        if (requestCode == SET_EVENT_ORIGIN && resultCode == RESULT_OK) {
            routeRequest = userData.routeRequestQuery();
            //kesafat kari- baiad gheimato negah daram:((
            userData.insertNewRouteIndex(String.valueOf(routeRequest.CostMinMax));
            setAddresses();
        }
        if (requestCode == SET_EVENT_DEST && resultCode == RESULT_OK) {
            routeRequest = userData.routeRequestQuery();
            setAddresses();
        }
        /*if (requestCode == SET_TIME_DETAIL && resultCode == RESULT_OK) {
            week_time_set = true;
            saveRouteRequest();

        }*/
        if (requestCode == Drive_SET && resultCode == RESULT_OK) {
            userData.DeleteRouteRequest();
            routeRequest=new RouteRequest();
            setAddresses();
            setDriver();
        }
    }

    public void saveRouteRequest(final RouteRequest theRequest) {
        showProgress();
        new SafeAsyncTask<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                if (authToken == null) {
                    serviceProvider.invalidateAuthToken();
                    authToken = serviceProvider.getAuthToken(EventMapActivity.this);
                }
                response = routeRequestService.SubmitNewEventRoute(authToken, theRequest);
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
                Toaster.showLong(EventMapActivity.this, getString(R.string.network_error), R.drawable.toast_warn);
            }

            @Override
            protected void onSuccess(final Boolean isRouteSubmitted) throws Exception {
                super.onSuccess(isRouteSubmitted);
                hideProgress();
                if (isRouteSubmitted) {
                    Toaster.showLong(EventMapActivity.this, getString(R.string.saved_route), R.drawable.toast_info);
                    setResult(RESULT_OK);
                    finish();
                } else {
                    new HandleApiMessages(EventMapActivity.this, response).showMessages();
                }
            }
        }.execute();
    }



    private void setAddresses() {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.main_container);
        ((EventMainFragment) fragment).setAddresses();
    }

    private void setDriver() {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.main_container);
        ((EventMainFragment) fragment).setDriver();
    }

    @Override
    public String getTitleDescription() {
        return "";
    }

    @Override
    public String getOriginLabel() {
        return getString(R.string.pre_event_src);
    }

    @Override
    public String getDestinationLabel() {
        return getString(R.string.post_event_dst);
    }

    @Override
    public RouteRequest getRouteRequest() {
        routeRequest = userData.routeRequestQuery();
        return routeRequest;
    }


    @Override
    public void gotoOriginAddMapActivity() {
        Intent intent = new Intent(EventMapActivity.this, AddMapActivity.class);
        intent.putExtra("AddRouteStates", AddRouteStates.SelectEventOriginState);
        intent.putExtra("EventResponse", eventResponse);
        this.startActivityForResult(intent, SET_EVENT_ORIGIN);
    }

    @Override
    public void gotoDestinationAddMapActivity() {
        Intent intent = new Intent(EventMapActivity.this, AddMapActivity.class);
        intent.putExtra("AddRouteStates", AddRouteStates.SelectEventDestinationState);
        intent.putExtra("EventResponse", eventResponse);
        this.startActivityForResult(intent, SET_EVENT_DEST);
    }

    @Override
    public void gotoDriverActivity() {
        Intent intent = new Intent(this, DriveActivity.class);
        this.startActivityForResult(intent, Drive_SET);

    }

    @Override
    public void Done(RouteRequest routeRequest) {
        SharedPreferences prefs = getSharedPreferences(
                "com.mibarim.main", Context.MODE_PRIVATE);
        /*routeRequest.TimingOption = TimingOptions.InDateAndTime;
        userData.insertNewRouteDate(routeRequest);*/
        routeRequest = userData.routeRequestQuery();
        if(prefs.getBoolean("IsDriver", false)){
            routeRequest.IsDrive = true;
            routeRequest.CostMinMax=0;
            routeRequest.RecommendPathId=0;
        }else {
            routeRequest.IsDrive = false;
        }
        routeRequest.PricingOption = PricingOptions.MinMax;
        routeRequest.EventId=eventResponse.EventId;
        routeRequest.ServiceType=ServiceTypes.EventRide;
        //if (validateRouteRequest(routeRequest)) {
//            if (week_time_set) {
                saveRouteRequest(routeRequest);
            /*} else {
                final Intent i = new Intent(this, WeekTimeActivity.class);
                String theTitle = getResources().getString(R.string.home_work_time);
                i.putExtra("TimeTitle", theTitle);
                this.startActivityForResult(i, SET_TIME_DETAIL);
            }*/
        //}
    }

    private boolean validateRouteRequest(RouteRequest routeRequest) {
        boolean res = true;
        if (routeRequest.SrcLatitude == null || routeRequest.SrcLatitude.equals("") ||
                routeRequest.DstLatitude == null || routeRequest.DstLatitude.equals("")) {
            Toaster.showLong(EventMapActivity.this, getString(R.string.src_dest_not_set), R.drawable.toast_warn);
            res = false;
        }
        if (routeRequest.TheTimeString() == null || routeRequest.TheTimeString().equals("")) {
            Toaster.showLong(EventMapActivity.this, getString(R.string.time_not_set), R.drawable.toast_warn);
            res = false;
        }
        return res;
    }


    @Override
    public String getOriginAddress() {
        return routeRequest.SrcGAddress;
    }

    @Override
    public String getDriverPassenger() {
        SharedPreferences prefs = getSharedPreferences(
                "com.mibarim.main", Context.MODE_PRIVATE);
        if (prefs.getBoolean("IsDriver", false)) {
            return getString(R.string.driver_label);
        } else {
            return getString(R.string.passenger_label);
        }
    }

    @Override
    public String getDestinationAddress() {
        return routeRequest.DstGAddress;
    }

    @Override
    public Boolean isShowWeeklyChkBx() {
        return false;
    }

    @Override
    public String getDriverPassIcon() {
        SharedPreferences prefs = getSharedPreferences(
                "com.mibarim.main", Context.MODE_PRIVATE);
        if (prefs.getBoolean("IsDriver", false)) {
            return "fa_cab";
        } else {
            return "fa_male";
        }
    }

    @Override
    public String getOriginIcon() {
        return "fa_home";
    }

    @Override
    public String getDestinationIcon() {
        return "fa_home";
    }

    private void showMustMessage() {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.main_container);
        ((AddMainFragment) fragment).setAddresses();
    }
    public EventResponse getEventResponse(){
        return eventResponse;
    }

    public String getEventAddress() {
        return eventResponse.Address;
    }
}
