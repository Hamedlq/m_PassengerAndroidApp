package com.mibarim.main.ui.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;
import com.mibarim.main.BootstrapApplication;
import com.mibarim.main.BootstrapServiceProvider;
import com.mibarim.main.R;
import com.mibarim.main.authenticator.LogoutService;
import com.mibarim.main.core.ImageUtils;
import com.mibarim.main.data.UserData;
import com.mibarim.main.models.ApiResponse;
import com.mibarim.main.models.ImageResponse;
import com.mibarim.main.models.Route.BriefRouteModel;
import com.mibarim.main.models.Route.RouteGroupModel;
import com.mibarim.main.models.Route.RouteResponse;
import com.mibarim.main.models.UserInfo.UserRouteModel;
import com.mibarim.main.services.RouteResponseService;
import com.mibarim.main.services.UserInfoService;
import com.mibarim.main.ui.BootstrapActivity;
import com.mibarim.main.ui.ThrowableLoader;
import com.mibarim.main.ui.fragments.MapFragment;
import com.mibarim.main.ui.fragments.TripProfileFragments.TripProfileMainFragment;
import com.mibarim.main.util.SafeAsyncTask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

import static android.app.PendingIntent.getActivity;
import static com.mibarim.main.R.id.map_fragment;
import static com.mibarim.main.R.id.select_dialog_listview;

/**
 * Created by Sina on 4/7/2017.
 */

public class TripProfileActivity extends BootstrapActivity
        implements LoaderManager.LoaderCallbacks<UserRouteModel> {

    @Inject
    UserData userData;
    @Inject
    protected BootstrapServiceProvider serviceProvider;
    @Inject
    protected RouteResponseService routeResponseService;
    @Inject
    UserInfoService userInfoService;

    UserRouteModel item;
    UserRouteModel latest;
    private Toolbar toolbar;
    private ApiResponse suggestRouteResponse;
    private BriefRouteModel selfRoute;
    private RouteResponse routeResponse;
    private Tracker mTracker;
    private boolean isInitial = true;


/*
    @Bind(R.id.picture_profile)
    protected ImageView picture_profile;

    @Bind(R.id.name_profile)
    protected TextView name_profile;

    @Bind(R.id.age_profile)
    protected TextView age_profile;

    @Bind(R.id.about_me)
    protected TextView about_me;

    @Bind(R.id.map_fragment)
    protected FrameLayout map_fragment;
*/


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BootstrapApplication.component().inject(this);

        BootstrapApplication application = (BootstrapApplication) getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("SuggestRouteActivity");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        mTracker.send(new HitBuilders.EventBuilder().setCategory("Activity").setAction("SuggestRouteActivity").build());


        setContentView(R.layout.container_activity);
        ButterKnife.bind(this);

        if (getWindow().getDecorView().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }

        if (getIntent() != null && getIntent().getExtras() != null) {
            //theSuggestRoute = (ApiResponse) getIntent().getExtras().getSerializable("SuggestRoutes");
            selfRoute = (BriefRouteModel) getIntent().getExtras().getSerializable("SelectedBriefRouteModel");
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
        getSupportLoaderManager().initLoader(0, null, this);
        /*final FragmentManager fragmentManager = this.getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .add(R.id.map_fragment, new MapFragment())
                .commit();*/
    }

    private void initScreen() {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.container, new TripProfileMainFragment())
                .commit();
    }


    public UserRouteModel getProfileTripInfo() {
        if (isInitial) {
            UserRouteModel u = new UserRouteModel();
            u.RouteId = selfRoute.RouteId;
            u.Name = selfRoute.Name;
            u.Family = selfRoute.Family;
            //u.UserAboutme = "دانشجوی ارشد مخابرات";
            u.SrcAddress = selfRoute.SrcAddress;
            u.UserRating = 3;
            u.DstAddress = selfRoute.DstAddress;
            u.TimingString = selfRoute.TimingString;
            u.UserImageId = selfRoute.UserImageId;
            u.Sat = true;
            u.Sun = true;
            u.Mon = true;
            u.Tue = true;
            u.Wed = true;
            u.Thu = false;
            u.Fri = false;
            u.TripCount = 48;
            return u;
        } else {
            return latest;
        }
    }

    @Override
    public Loader<UserRouteModel> onCreateLoader(int id, Bundle args) {
        showProgress();
        item = new UserRouteModel();
        return new ThrowableLoader<UserRouteModel>(this, item) {
            @Override
            public UserRouteModel loadData() throws Exception {
                latest = new UserRouteModel();
                String authToken = serviceProvider.getAuthToken(TripProfileActivity.this);
                latest = routeResponseService.GetTripProfileInfo(authToken, selfRoute.RouteId);
                return latest;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<UserRouteModel> loader, UserRouteModel data) {
        //close dialog
        hideProgress();
        isInitial = false;
        latest=data;
        setValues();
    }

    private void setValues() {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.container);
        ((TripProfileMainFragment) fragment).setValues();
    }

    @Override
    public void onLoaderReset(Loader<UserRouteModel> loader) {
    }
    /*public UserRouteModel getRoute() {
        UserRouteModel umodel=new UserRouteModel();
        umodel.Name=selfRoute.Name;
        umodel.Family=selfRoute.Family;
        umodel.UserImageId=selfRoute.UserImageId;
        return umodel;
    }*/

    public Bitmap getImageById(String imageId, int defaultDrawableId) {
        Bitmap icon = ImageUtils.getBitmapFromVectorDrawable(this, defaultDrawableId);
        /*Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(icon, 50, 50, true));
        icon = ((BitmapDrawable) d).getBitmap();*/
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
                String token = serviceProvider.getAuthToken(TripProfileActivity.this);
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

}
