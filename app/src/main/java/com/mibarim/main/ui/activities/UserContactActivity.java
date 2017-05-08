

package com.mibarim.main.ui.activities;

import android.accounts.OperationCanceledException;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapCircleThumbnail;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.mibarim.main.BootstrapApplication;
import com.mibarim.main.BootstrapServiceProvider;
import com.mibarim.main.R;
import com.mibarim.main.adapters.ViewPagerAdapter;
import com.mibarim.main.core.ImageUtils;
import com.mibarim.main.data.UserData;
import com.mibarim.main.models.ApiResponse;
import com.mibarim.main.models.ContactModel;
import com.mibarim.main.models.ImageResponse;
import com.mibarim.main.models.ScoreModel;
import com.mibarim.main.services.AuthenticateService;
import com.mibarim.main.services.RouteRequestService;
import com.mibarim.main.services.UserInfoService;
import com.mibarim.main.ui.BootstrapActivity;
import com.mibarim.main.ui.HandleApiMessages;
import com.mibarim.main.ui.fragments.userInfoFragments.AboutMeMainFragment;
import com.mibarim.main.ui.fragments.userInfoFragments.CommentContactFragment;
import com.mibarim.main.ui.fragments.userInfoFragments.UserContactFragment;
import com.mibarim.main.util.SafeAsyncTask;
import com.mibarim.main.util.Strings;
import com.mibarim.main.util.Toaster;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.fabric.sdk.android.Fabric;


/**
 * Initial activity for the application.
 * <p/>
 * If you need to remove the authentication from the application please see
 * {@link com.mibarim.main.authenticator.ApiKeyProvider#getAuthKey(android.app.Activity)}
 */
public class UserContactActivity extends BootstrapActivity {


    @Inject
    BootstrapServiceProvider serviceProvider;
    @Inject
    UserInfoService userInfoService;
    @Inject
    RouteRequestService routeRequestService;
    @Inject
    UserData userData;

    @Bind(R.id.header)
    protected ImageView header;
    @Bind(R.id.user_image)
    protected BootstrapCircleThumbnail user_image;
    @Bind(R.id.pb_loading)
    protected ProgressBar pb_loading;
    @Bind(R.id.rating)
    protected RatingBar rating;
    @Bind(R.id.do_rating)
    protected RatingBar do_rating;
    @Bind(R.id.about_me)
    protected TextView about_me;


    private Tracker mTracker;
    private CharSequence title;
    private Toolbar toolbar;
    private ScoreModel scoreModel;
    String OriginActivity;
    protected String authToken;
    protected ApiResponse response;
    protected ApiResponse ScoreResponse;

/*
    private TabLayout tabLayout;
    private ViewPager viewPager;
*/

    private boolean userHasAuthenticated = false;
    private ContactModel contactModel;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        BootstrapApplication.component().inject(this);

        BootstrapApplication application = (BootstrapApplication) getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("UserContactActivity");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        mTracker.send(new HitBuilders.EventBuilder().setCategory("Activity").setAction("UserContactActivity").build());

        //setContentView(R.layout.main_activity);
        setContentView(R.layout.user_info_tab_activity);

        // View injection with Butterknife
        ButterKnife.bind(this);

        if (getWindow().getDecorView().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }

        if (getIntent() != null && getIntent().getExtras() != null) {
            OriginActivity=getIntent().getExtras().getString("OriginActivity");
            contactModel = (ContactModel) getIntent().getExtras().getSerializable("ContactModel");
        }

        toolbar = (Toolbar) findViewById(R.id.anim_toolbar);
        //toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_menu_forward);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setTitle(contactModel.Name + " " + contactModel.Family);
        }
        //pb_loading.setVisibility(View.GONE);
        /*TextView title=(TextView) findViewById(R.id.title_container);
        title.setText(contactModel.Name + " " + contactModel.Family);*/


        /*viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        NestedScrollView scrollView = (NestedScrollView) findViewById (R.id.nest_scrollview);
        scrollView.setFillViewport(true);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);*/

        checkAuth();
    }

    private void initScreen() {
        if (userHasAuthenticated) {
            do_rating.setVisibility(View.GONE);
            Bitmap icon = BitmapFactory.decodeResource(getResources(),
                    R.mipmap.ic_camera);
            Bitmap img = getImageById(contactModel.UserImageId);
            if (!img.sameAs(icon)) {
                user_image.setImageBitmap(img);
                header.setImageBitmap(ImageUtils.darkenBitMap(img));
            } else {
                user_image.setImageBitmap(img);
                header.setBackgroundResource(R.color.secondary);
            }
            if (OriginActivity.equals("MainActivity")){
                getUserScoreByContact(contactModel);
            }else {
                getUserScoreByRoute(contactModel);
            }


            do_rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                public void onRatingChanged(RatingBar ratingBar, float rating,
                                            boolean fromUser) {
                    setRatingForUser(rating);
                }
            });


            /*final FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.main_container, new UserContactFragment())
                    .commitAllowingStateLoss();*/
            //getUserScore();
        }
    }

    private void getUserScoreByRoute(final ContactModel routeModel) {
        new SafeAsyncTask<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                if (authToken == null) {
                    serviceProvider.invalidateAuthToken();
                    authToken = serviceProvider.getAuthToken(UserContactActivity.this);
                }
                scoreModel = userInfoService.getUserScoresByRoute(authToken, routeModel.ContactId);
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

    private void setRatingForUser(final float rating) {
        new SafeAsyncTask<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                if (authToken == null) {
                    serviceProvider.invalidateAuthToken();
                    authToken = serviceProvider.getAuthToken(UserContactActivity.this);
                }
                ScoreResponse = userInfoService.SetUserScore(authToken, contactModel.ContactId, rating);
                return true;
            }

            @Override
            protected void onException(final Exception e) throws RuntimeException {
                super.onException(e);
            }

            @Override
            protected void onSuccess(final Boolean state) throws Exception {
                super.onSuccess(state);
                if (ScoreResponse.Messages != null) {
                    for (String scoreRes : ScoreResponse.Messages) {
                        if (scoreRes!=null &&!scoreRes.equals("\"\"")) {
                            Toaster.showLong(UserContactActivity.this, scoreRes, R.drawable.toast_success);
                        }
                    }
                }
                new HandleApiMessages(UserContactActivity.this, ScoreResponse).showMessages();
            }
        }.execute();
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new AboutMeMainFragment(), getString(R.string.about_me));
        adapter.addFragment(new CommentContactFragment(), getString(R.string.user_comments));
        //adapter.addFragment(new UserInfoMainFragment(), "THREE");
        viewPager.setAdapter(adapter);
    }


    private void checkAuth() {
        new SafeAsyncTask<Boolean>() {

            @Override
            public Boolean call() throws Exception {
                final AuthenticateService svc = serviceProvider.getService(UserContactActivity.this);
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
                userHasAuthenticated = true;
                initScreen();
            }
        }.execute();
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


    public void getUserScoreByContact(final ContactModel contactModel) {
        new SafeAsyncTask<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                if (authToken == null) {
                    serviceProvider.invalidateAuthToken();
                    authToken = serviceProvider.getAuthToken(UserContactActivity.this);
                }
                scoreModel = userInfoService.getUserScoresByContact(authToken, contactModel.ContactId);
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

    private void setUserScore() {
        rating.setRating(scoreModel.ContactScore);
        about_me.setText(scoreModel.AboutMe);
        pb_loading.setVisibility(View.GONE);
        //do_rating.setVisibility(View.VISIBLE);
        if (OriginActivity.equals("MainActivity")){
            do_rating.setVisibility(View.VISIBLE);
        }else {
            do_rating.setVisibility(View.GONE);
        }
        /*final FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.main_container);
        ((UserContactFragment) fragment).setUserScores(scoreModel);*/
    }

    private void hideAcceptButton() {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.main_container);
        ((UserContactFragment) fragment).hideDriverAcceptBtn();
    }

    public ContactModel getContactModel() {
        return contactModel;
    }


    public void goToMessaging() {
        Intent intent = new Intent(this, MessagingActivity.class);
        intent.putExtra("ContactModel", contactModel);
        this.startActivity(intent);

    }

    public void goToSimilarRoutes() {
        Intent intent = new Intent(this, CheckSuggestRouteActivity.class);
        intent.putExtra("ContactModel", contactModel);
        this.startActivity(intent);
    }

    public void acceptRideShare() {
        showProgress();
        new SafeAsyncTask<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                if (authToken == null) {
                    authToken = serviceProvider.getAuthToken(UserContactActivity.this);
                }
                response = routeRequestService.acceptRideShare(authToken, String.valueOf(contactModel.ContactId));
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
                if (!succees) {
                    new HandleApiMessages(UserContactActivity.this, response).showMessages();

                } else {
                    if (response.Messages == null) {
                        hideAcceptButton();
                    } else {
                        for (String routeJson : response.Messages) {
                            Toaster.showLong(UserContactActivity.this, routeJson, R.drawable.toast_info);
                        }
                    }
                }
            }
        }.execute();
    }

    public void gotoMibarimWebsite() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://mibarim.ir/Pay"));
        startActivity(browserIntent);
        final FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.main_container);
        ((UserContactFragment) fragment).hideMsg();
    }

    public Bitmap getImageById(String imageId) {
        Bitmap icon = BitmapFactory.decodeResource(getResources(),
                R.mipmap.ic_camera);
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
                String token = serviceProvider.getAuthToken(UserContactActivity.this);
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
                    //setUserImage();
                }
            }
        }.execute();
    }
}
