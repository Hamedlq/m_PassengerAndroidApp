

package com.mibarim.main.ui.activities;


import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;
import com.mibarim.main.BootstrapApplication;
import com.mibarim.main.BootstrapServiceProvider;
import com.mibarim.main.R;
import com.mibarim.main.authenticator.LogoutService;
import com.mibarim.main.authenticator.TokenRefreshActivity;
import com.mibarim.main.core.ImageUtils;
import com.mibarim.main.data.UserData;
import com.mibarim.main.events.NetworkErrorEvent;
import com.mibarim.main.events.RestAdapterErrorEvent;
import com.mibarim.main.events.UnAuthorizedErrorEvent;
import com.mibarim.main.models.Address.Location;
import com.mibarim.main.models.Address.PathPoint;
import com.mibarim.main.models.ApiResponse;
import com.mibarim.main.models.ContactModel;
import com.mibarim.main.models.ImageResponse;
import com.mibarim.main.models.PersonalInfoModel;
import com.mibarim.main.models.Route.BriefRouteModel;
import com.mibarim.main.models.Route.RouteResponse;
import com.mibarim.main.services.RouteRequestService;
import com.mibarim.main.services.RouteResponseService;
import com.mibarim.main.services.UserInfoService;
import com.mibarim.main.ui.BootstrapActivity;
import com.mibarim.main.ui.HandleApiMessages;
import com.mibarim.main.ui.fragments.MainGroupFragment;
import com.mibarim.main.ui.fragments.MapFragment;
import com.mibarim.main.ui.fragments.routeFragments.SuggestRouteCardFragment;
import com.mibarim.main.util.SafeAsyncTask;
import com.mibarim.main.util.Toaster;
import com.squareup.otto.Subscribe;

//import org.osmdroid.tileprovider.constants.OpenStreetMapTileProviderConstants;

import javax.inject.Inject;

import butterknife.ButterKnife;
import io.fabric.sdk.android.Fabric;


/**
 * Initial activity for the application.
 * * <p/>
 * If you need to remove the authentication from the application please see
 */
public class SuggestRouteActivity extends BootstrapActivity {

    @Inject
    protected BootstrapServiceProvider serviceProvider;
    @Inject
    RouteRequestService routeRequestService;
    @Inject
    LogoutService getLogoutService;
    @Inject
    RouteResponseService routeResponseService;
    @Inject
    UserInfoService userInfoService;
    @Inject
    UserData userData;


    private CharSequence title;
    private Toolbar toolbar;

    private String authToken;
    private ApiResponse response;

    private ApiResponse theSuggestRoute;
    private RouteResponse selfRoute;
    private BriefRouteModel selectedRoute;
    private Tracker mTracker;
    protected Bitmap result;//concurrency must be considered
    private int REFRESH_TOKEN_REQUEST = 3456;
    private boolean refreshingToken = false;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        super.onCreate(savedInstanceState);
  /*      if (getCacheDir() != null) {
            OpenStreetMapTileProviderConstants.setCachePath(getCacheDir().getAbsolutePath());
        }*/
        Fabric.with(this, new Crashlytics());
        BootstrapApplication.component().inject(this);

        BootstrapApplication application = (BootstrapApplication) getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("SuggestRouteActivity");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        mTracker.send(new HitBuilders.EventBuilder().setCategory("Activity").setAction("SuggestRouteActivity").build());


        setContentView(R.layout.main_activity);

        // View injection with Butterknife
        ButterKnife.bind(this);

        if (getWindow().getDecorView().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }

        if (getIntent() != null && getIntent().getExtras() != null) {
            //theSuggestRoute = (ApiResponse) getIntent().getExtras().getSerializable("SuggestRoutes");
            selfRoute = (RouteResponse) getIntent().getExtras().getSerializable("RouteResponse");
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_menu_forward);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
        initScreen();
    }

    private void initScreen() {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.main_container, new MainGroupFragment())
                .commit();

    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Subscribe
    public void onNetworkErrorEvent(NetworkErrorEvent event) {
        Toaster.showLong(SuggestRouteActivity.this, getString(R.string.network_error), R.drawable.toast_warn);
    }

    @Subscribe
    public void onRestAdapterErrorEvent(RestAdapterErrorEvent event) {
        Toaster.showLong(SuggestRouteActivity.this, getString(R.string.network_error), R.drawable.toast_warn);
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

    public void getRouteImage(final ImageView imageView, final int routeId) {
        new SafeAsyncTask<Boolean>() {
            Bitmap decodedByte;

            @Override
            public Boolean call() throws Exception {
                if (authToken == null) {
                    serviceProvider.invalidateAuthToken();
                    authToken = serviceProvider.getAuthToken(SuggestRouteActivity.this);
                }
                PersonalInfoModel res = routeResponseService.GetRouteImage(authToken, routeId);
                if (res != null && res.Base64UserPic != null) {
                    byte[] decodedString = Base64.decode(res.Base64UserPic, Base64.DEFAULT);
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

    public Location getRouteSource() {
        Location loc = new Location();
        if (selfRoute != null) {
            loc.lat = selfRoute.SrcLatitude;
            loc.lng = selfRoute.SrcLongitude;

        }
        return loc;
    }

    public Location getRouteDestination() {
        Location loc = new Location();
        if (selfRoute != null) {
            loc.lat = selfRoute.DstLatitude;
            loc.lng = selfRoute.DstLongitude;

        }
        return loc;
    }

    public void setRouteSrcDst(String srcLat, String srcLng, String dstLat, String dstLng, PathPoint pathRoute, int position) {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.map_fragment);
        ((MapFragment) fragment).setRoute(srcLat, srcLng, dstLat, dstLng, pathRoute, position % 4);

    }

    public ApiResponse getSuggestRoutes() {
        return theSuggestRoute;
    }

    public void RequestRideShare() {
        if (selectedRoute == null || selectedRoute.RouteId == 0) {
            Toaster.showLong(SuggestRouteActivity.this, R.string.please_select);
        } else {
            showProgress();
            new SafeAsyncTask<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    if (authToken == null) {
                        authToken = serviceProvider.getAuthToken(SuggestRouteActivity.this);
                    }
                    response = routeRequestService.requestRideShare(authToken, String.valueOf(selectedRoute.RouteId),
                            String.valueOf(selfRoute.RouteId));
                    if ((response.Errors == null || response.Errors.size() == 0) && response.Status.equals("OK")) {
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
                    hideProgress();
                }

                @Override
                protected void onSuccess(final Boolean succees) throws Exception {
                    hideProgress();
                    if (succees) {
                        getContactandFinish(response);
                    }
                    new HandleApiMessages(SuggestRouteActivity.this, response).showMessages();
                    //finishIt();
                }
            }.execute();
        }
    }

    private void getContactandFinish(ApiResponse response) {
        ContactModel contact = new ContactModel();
        Gson gson = new Gson();
        for (String routeJson : this.response.Messages) {
            contact = gson.fromJson(routeJson, ContactModel.class);
        }
        Intent resultIntent = getIntent();
        resultIntent.putExtra("contactId", contact.ContactId);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    private void finishIt() {
        final Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

    public void setSelectedRoute(BriefRouteModel selectedItem) {
        selectedRoute = selectedItem;
    }

    public void DeleteRoute() {
        if (selectedRoute == null || selectedRoute.RouteId == 0) {
            Toaster.showLong(SuggestRouteActivity.this, R.string.please_select);
        } else {
            new SafeAsyncTask<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    if (authToken == null) {
                        serviceProvider.invalidateAuthToken();
                        authToken = serviceProvider.getAuthToken(SuggestRouteActivity.this);
                    }
                    response = routeRequestService.deleteRouteSuggestion(authToken, String.valueOf(selectedRoute.RouteId),
                            String.valueOf(selfRoute.RouteId));
                    if ((response.Errors == null || response.Errors.size() == 0) && response.Status.equals("OK")) {
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
                protected void onSuccess(final Boolean succees) throws Exception {
                    if (succees) {
                        refreshList();
                    }
                    new HandleApiMessages(SuggestRouteActivity.this, response).showMessages();

                }
            }.execute();
        }
    }

    private void refreshList() {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.group_route_fragment);
        ((SuggestRouteCardFragment) fragment).refresh();
    }

    public Bitmap getImageById(String imageId, int defaultDrawableId) {
        Bitmap icon = BitmapFactory.decodeResource(getResources(), defaultDrawableId);
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
                String token = serviceProvider.getAuthToken(SuggestRouteActivity.this);
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
                }
            }
        }.execute();
    }

    public RouteResponse getRoute() {
        return selfRoute;
    }

    public void goToContactActivity(ContactModel contactModel) {
        Intent intent;
        intent = new Intent(this, UserContactActivity.class);
        intent.putExtra("OriginActivity", "SuggestRouteActivity");
        intent.putExtra("ContactModel", contactModel);
        this.startActivity(intent);
    }


}
