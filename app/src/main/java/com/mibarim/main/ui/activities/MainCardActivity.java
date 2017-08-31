

package com.mibarim.main.ui.activities;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.os.OperationCanceledException;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
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
import com.mibarim.main.core.Constants;
import com.mibarim.main.core.ImageUtils;
import com.mibarim.main.data.UserData;
import com.mibarim.main.events.NetworkErrorEvent;
import com.mibarim.main.events.RestAdapterErrorEvent;
import com.mibarim.main.events.UnAuthorizedErrorEvent;
import com.mibarim.main.models.ApiResponse;
import com.mibarim.main.models.ImageResponse;
import com.mibarim.main.models.InviteModel;
import com.mibarim.main.models.Plus.PassRouteModel;
import com.mibarim.main.models.Plus.PaymentDetailModel;
import com.mibarim.main.models.RouteResponse;
import com.mibarim.main.models.UserInfoModel;
import com.mibarim.main.models.enums.TripStates;
import com.mibarim.main.services.AuthenticateService;
import com.mibarim.main.services.RouteRequestService;
import com.mibarim.main.services.RouteResponseService;
import com.mibarim.main.services.UserInfoService;
import com.mibarim.main.ui.BootstrapActivity;
import com.mibarim.main.ui.HandleApiMessages;

import com.mibarim.main.ui.HandleApiMessagesBySnackbar;
import com.mibarim.main.ui.fragments.PlusFragments.PassengerCardFragment;
import com.mibarim.main.util.SafeAsyncTask;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;

import butterknife.ButterKnife;

//import org.osmdroid.tileprovider.constants.OpenStreetMapTileProviderConstants;


/**
 * Initial activity for the application.
 * * <p/>
 * If you need to remove the authentication from the application please see
 */
public class MainCardActivity extends BootstrapActivity {

    @Inject
    protected BootstrapServiceProvider serviceProvider;
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
    ImageView invite_btn;

    private String authToken;
    private String url;
    private ApiResponse response;
    private ApiResponse userTrip;
    private int appVersion = 0;
    private ApiResponse theSuggestRoute;
    //private RouteResponse selfRoute;

    private Tracker mTracker;
    protected Bitmap result;//concurrency must be considered
    private int REFRESH_TOKEN_REQUEST = 3456;
    private boolean refreshingToken = false;
    String googletoken = "";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private int FINISH_USER_INFO = 5649;
    private int FINISH_PAYMENT = 5659;
    private int FINISH_TRIP = 5669;
    private View parentLayout;
    private boolean netErrorMsg = false;
    boolean doubleBackToExitPressedOnce = false;
    private UserInfoModel userInfoModel;
    private InviteModel inviteModel;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        super.onCreate(savedInstanceState);
  /*      if (getCacheDir() != null) {
            OpenStreetMapTileProviderConstants.setCachePath(getCacheDir().getAbsolutePath());
        }*/
        //Fabric.with(this, new Crashlytics());
        BootstrapApplication.component().inject(this);

        BootstrapApplication application = (BootstrapApplication) getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("MainCardActivity");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        mTracker.send(new HitBuilders.EventBuilder().setCategory("Activity").setAction("MainCardActivity").build());

        setContentView(R.layout.main_activity);
        if (getIntent() != null && getIntent().getExtras() != null) {
            url = getIntent().getExtras().getString(Constants.GlobalConstants.URL);
        }

        parentLayout = findViewById(R.id.main_activity_root);
        // View injection with Butterknife
        ButterKnife.bind(this);

        if (getWindow().getDecorView().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }

        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        invite_btn = (ImageView) toolbar.findViewById(R.id.invite_btn);
        checkAuth();
        //initScreen();
    }

    private void initScreen() {
        checkVersion();
        getUserInfoFromServer();
        getInviteFromServer();
        final FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.main_container, new PassengerCardFragment())
                .commit();
        if(url!=null){
            gotoWebView(url);
        }
        invite_btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    gotoInviteActivity();
                    return true;
                }
                return false;
            }
        });

    }

    /*@Override
    protected void onResume() {
        checkAuth();
        super.onResume();
    }*/

    private void checkAuth() {
        new SafeAsyncTask<Boolean>() {

            @Override
            public Boolean call() throws Exception {
                final AuthenticateService svc = serviceProvider.getService(MainCardActivity.this);
                if (svc != null) {
                    authToken = serviceProvider.getAuthToken(MainCardActivity.this);
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

    public String getAuthToken() {
            return authToken;
    }

    @Subscribe
    public void onNetworkErrorEvent(NetworkErrorEvent event) {
        if (!netErrorMsg) {
            netErrorMsg = true;
            //Toaster.showLong(MainActivity.this, getString(R.string.network_error), R.drawable.toast_warn);
            Snackbar.make(parentLayout, R.string.network_error, Snackbar.LENGTH_LONG).show();
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

    @Subscribe
    public void onRestAdapterErrorEvent(RestAdapterErrorEvent event) {
        Snackbar.make(parentLayout, R.string.network_error, Snackbar.LENGTH_LONG).show();
        //Toaster.showLong(MainCardActivity.this, getString(R.string.network_error), R.drawable.toast_warn);
    }


    private void refreshToken() {
        if (!refreshingToken) {
            refreshingToken = true;
            final Intent i = new Intent(this, TokenRefreshActivity.class);
            startActivityForResult(i, REFRESH_TOKEN_REQUEST);
        }
    }


/*    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }*/

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
        if (requestCode ==FINISH_USER_INFO  && resultCode == RESULT_OK) {
            getUserInfoFromServer();
        }
        if (requestCode ==FINISH_PAYMENT  && resultCode == RESULT_OK) {
            refresh();
        }
        if (requestCode ==FINISH_TRIP  && resultCode == RESULT_OK) {
            refresh();
        }
    }


    private void refresh() {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.main_container);
        if (fragment != null) {
            ((PassengerCardFragment)fragment).refresh();
        }
    }


    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        Snackbar.make(parentLayout, R.string.press_again_to_exit, Snackbar.LENGTH_LONG).show();
        this.doubleBackToExitPressedOnce = true;
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 4000);
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

    private void gotoInviteActivity() {
        Intent intent = new Intent(this, InviteActivity.class);
        intent.putExtra(Constants.Auth.AUTH_TOKEN, authToken);
        this.startActivity(intent);
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
                authToken = serviceProvider.getAuthToken(MainCardActivity.this);
                imageResponse = userInfoService.GetImageById(authToken, imageId);
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
        RouteResponse selfRoute = new RouteResponse();
        selfRoute.RouteId = 127498;
        return selfRoute;
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

    private void sendTokenToServer() {
        new SafeAsyncTask<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                if (authToken == null) {
                    serviceProvider.invalidateAuthToken();
                    authToken = serviceProvider.getAuthToken(MainCardActivity.this);
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



    public void gotoPayActivity(final PassRouteModel selectedItem) {
        Intent intent = new Intent(this, PayActivity.class);
        intent.putExtra(Constants.GlobalConstants.PASS_ROUTE_MODEL, selectedItem);
        intent.putExtra(Constants.Auth.AUTH_TOKEN, authToken);
        this.startActivityForResult(intent,FINISH_PAYMENT);
    }




    public int getVersion() {
        int v = 1000;
        try {
            v = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
        }
        return v;
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
                    showUpdateDialog(getString(R.string.update_msg));
                }
            }
        }.execute();
    }

    public void showRidingActivity(PassRouteModel dm) {
        SharedPreferences prefs = this.getSharedPreferences(
                "com.mibarim.main", Context.MODE_PRIVATE);
        Long tripShown=prefs.getLong("FirstRidingShow",0);
        if(tripShown!=dm.TripId){
            gotoRidingActivity(dm);
        }
    }

    public void gotoRidingActivity(PassRouteModel dm) {
        Intent intent = new Intent(this, RidingActivity.class);
        intent.putExtra(Constants.GlobalConstants.PASS_ROUTE_MODEL, dm);
        intent.putExtra(Constants.Auth.AUTH_TOKEN, authToken);
        this.startActivityForResult(intent,FINISH_TRIP);
    }



    private void showUpdateDialog(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(msg).setPositiveButton("باشه", dialogClickListener).setNegativeButton("بستن برنامه", dialogClickListener).show();
    }

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.update_link)));
                    startActivity(browserIntent);
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    finishAffinity();
                    break;
            }
        }
    };

    private void getUserInfoFromServer() {
        new SafeAsyncTask<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                if (authToken == null) {
                    serviceProvider.invalidateAuthToken();
                    authToken = serviceProvider.getAuthToken(MainCardActivity.this);
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

    public void gotoWebView(String link) {
        Intent i = new Intent(MainCardActivity.this, WebViewActivity.class);
        i.putExtra("URL", link);
        startActivity(i);
    }

    public void getInviteFromServer() {
        new SafeAsyncTask<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                if (authToken == null) {
                    serviceProvider.invalidateAuthToken();
                    authToken = serviceProvider.getAuthToken(MainCardActivity.this);
                }
                inviteModel = userInfoService.getInvite(authToken);
                return true;
            }

            @Override
            protected void onException(final Exception e) throws RuntimeException {
                super.onException(e);
            }

            @Override
            protected void onSuccess(final Boolean state) throws Exception {
                super.onSuccess(state);
                userData.insertInvite(inviteModel);
            }
        }.execute();
    }
}
