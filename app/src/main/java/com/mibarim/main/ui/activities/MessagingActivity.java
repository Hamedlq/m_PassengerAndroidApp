

package com.mibarim.main.ui.activities;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;
import com.mibarim.main.BootstrapApplication;
import com.mibarim.main.BootstrapServiceProvider;
import com.mibarim.main.R;
import com.mibarim.main.authenticator.LogoutService;
import com.mibarim.main.core.ImageUtils;
import com.mibarim.main.data.UserData;
import com.mibarim.main.events.NetworkErrorEvent;
import com.mibarim.main.events.RestAdapterErrorEvent;
import com.mibarim.main.models.ApiResponse;
import com.mibarim.main.models.ContactModel;
import com.mibarim.main.models.ContactStateModel;
import com.mibarim.main.models.ImageResponse;
import com.mibarim.main.models.PersonalInfoModel;
import com.mibarim.main.models.Route.RouteResponse;
import com.mibarim.main.services.GroupService;
import com.mibarim.main.services.RouteRequestService;
import com.mibarim.main.services.UserInfoService;
import com.mibarim.main.ui.BootstrapActivity;
import com.mibarim.main.ui.HandleApiMessages;
import com.mibarim.main.ui.fragments.messagingFragments.MainMessagingFragment;
import com.mibarim.main.ui.fragments.messagingFragments.MessageListFragment;
import com.mibarim.main.ui.fragments.messagingFragments.SendMessageFragment;
import com.mibarim.main.ui.fragments.messagingFragments.ToggleContactTripFragment;
import com.mibarim.main.util.SafeAsyncTask;
import com.mibarim.main.util.Toaster;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.ButterKnife;
import io.fabric.sdk.android.Fabric;


/**
 * Initial activity for the application.
 * * <p/>
 * If you need to remove the authentication from the application please see
 */
public class MessagingActivity extends BootstrapActivity {

    @Inject
    protected BootstrapServiceProvider serviceProvider;
    @Inject
    RouteRequestService routeRequestService;
    @Inject
    LogoutService getLogoutService;
    @Inject
    GroupService groupService;
    @Inject
    UserInfoService userInfoService;
    @Inject
    UserData userData;

    private Tracker mTracker;

    private int RELOAD_REQUEST = 1234;
    private int RELOAD_CONTACT = 8585;
    private CharSequence title;
    private Toolbar toolbar;

    private Handler mHandler;
    private Runnable mRunnable;

    private String authToken;
    private ApiResponse response;
    private ApiResponse toggleStateResponse;

    private ContactModel contactModel;
    private static final ScheduledExecutorService worker =
            Executors.newSingleThreadScheduledExecutor();
    List<Pair<Long, Bitmap>> picMap;


    protected Bitmap result;//concurrency must be considered

    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        BootstrapApplication.component().inject(this);

        BootstrapApplication application = (BootstrapApplication) getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("MessagingActivity");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        mTracker.send(new HitBuilders.EventBuilder().setCategory("Activity").setAction("MessagingActivity").build());


        setContentView(R.layout.main_activity);

        // View injection with Butterknife
        ButterKnife.bind(this);

        if (getWindow().getDecorView().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }

        if (getIntent() != null && getIntent().getExtras() != null) {
            contactModel = (ContactModel) getIntent().getExtras().getSerializable("ContactModel");
            setTitle(contactModel.Name + " " + contactModel.Family);
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
        periodicReLoading();
        picMap = new ArrayList<>();
    }

    private void periodicReLoading() {
        mHandler = new Handler();
        mRunnable = new Runnable() {
            @Override
            public void run() {
                reloadThread();
                mHandler.postDelayed(this, 10000);
            }
        };
        worker.schedule(mRunnable, 10, TimeUnit.SECONDS);
    }


    @Override
    public void onDestroy() {
        mHandler.removeCallbacks(mRunnable);
        super.onDestroy();
    }

    private void messagingReloading() {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.message_list_fragment);
        if (fragment != null) {
            ((MessageListFragment) fragment).refresh();
        }
    }


    private void initScreen() {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.main_container, new MainMessagingFragment())
                .commit();
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = getIntent();
            setResult(RESULT_OK, intent);
            finish();
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    public ContactModel getContactModel() {
        return contactModel;
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        if (contactModel.IsSupport != 1) {
            inflater.inflate(R.menu.chat_menu, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }


    public void toggleTripState(final boolean state) {
        showProgress();
        new SafeAsyncTask<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                if (authToken == null) {
                    authToken = serviceProvider.getAuthToken(MessagingActivity.this);
                }
                String gId = String.valueOf(contactModel.ContactId);
                toggleStateResponse = userInfoService.toggleTripState(authToken, gId, state);
                if ((toggleStateResponse.Errors == null || toggleStateResponse.Errors.size() == 0) && toggleStateResponse.Status.equals("OK")) {
                    return true;
                }
                return false;
            }

            @Override
            protected void onException(final Exception e) throws RuntimeException {
                hideProgress();
                super.onException(e);
                setSwitchState(!state);
            }

            @Override
            protected void onSuccess(final Boolean isSubmitted) throws Exception {
                super.onSuccess(isSubmitted);
                hideProgress();
                toggleTrip(toggleStateResponse);
            }
        }.execute();

    }

    private void toggleTrip(ApiResponse toggleStateResponse) {
        Gson gson = new Gson();
        ContactStateModel contactStateModel = new ContactStateModel();
        for (String routeJson : toggleStateResponse.Messages) {
            contactStateModel = gson.fromJson(routeJson, ContactStateModel.class);
        }
        setSwitchState(contactStateModel.State);
        if (contactStateModel.Msg != null && !contactStateModel.Msg.equals("")) {
            showHelpDialog(contactStateModel.Msg);
        }
    }

    public void showHelp() {
        if (contactModel.IsDriver == 1) {
            String msg = getString(R.string.trip_help_2) + "\r\n" + getString(R.string.trip_help_3);
            showHelpDialog(msg);
        } else {
            String msg = getString(R.string.trip_help_1) + "\r\n" + getString(R.string.trip_help_2) + "\r\n" + getString(R.string.trip_help_3);
            showHelpDialog(msg);
        }
    }

    private void showHelpDialog(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(msg).setPositiveButton("باشه", dialogClickListener).show();
    }

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    break;
            }
        }
    };

    private void setSwitchState(boolean state) {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.toggle_fragment);
        if (fragment != null) {
            ((ToggleContactTripFragment) fragment).setSwitchState(state);
        }
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = getIntent();
                setResult(RESULT_OK, intent);
                this.finish();
                return true;
            case R.id.action_user:
                goToContactActivity(contactModel);
                return true;
            case R.id.action_similar_route:
                goToSimilarRoutes(contactModel);
                return true;
            /*case R.id.myswitch:
                handleTripSwitch();
                return true;*/
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void goToSimilarRoutes(ContactModel contactModel) {
        Intent intent = new Intent(this, CheckSuggestRouteActivity.class);
        intent.putExtra("ContactModel", contactModel);
        this.startActivity(intent);
    }


    public void goToContactActivity(ContactModel contactModel) {
        Intent intent;
        if (contactModel.IsSupport == 1) {
            intent = new Intent(this, MessagingActivity.class);
        } else {
            intent = new Intent(this, UserContactActivity.class);
        }
        intent.putExtra("ContactModel", contactModel);
        this.startActivityForResult(intent, RELOAD_CONTACT);
    }

    @Subscribe
    public void onNetworkErrorEvent(NetworkErrorEvent event) {
        Toaster.showLong(MessagingActivity.this, getString(R.string.network_error), R.drawable.toast_warn);
    }

    @Subscribe
    public void onRestAdapterErrorEvent(RestAdapterErrorEvent event) {
        Toaster.showLong(MessagingActivity.this, getString(R.string.network_error), R.drawable.toast_warn);
    }


  /*  public RouteResponse getRouteResponse() {
        return theRoute;
    }*/

    public void sendMessage(final String s) {
        new SafeAsyncTask<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                if (authToken == null) {
                    authToken = serviceProvider.getAuthToken(MessagingActivity.this);
                }
                String gId = String.valueOf(contactModel.ContactId);
                response = groupService.sendMessage(authToken, s, gId);
                if ((response.Errors == null || response.Errors.size() == 0) && response.Status.equals("OK")) {
                    return true;
                }
                return false;
            }

            @Override
            protected void onException(final Exception e) throws RuntimeException {
                enableSendBtn();
                super.onException(e);
            }

            @Override
            protected void onSuccess(final Boolean isMsgSubmitted) throws Exception {
                super.onSuccess(isMsgSubmitted);
                if (!isMsgSubmitted) {
                    new HandleApiMessages(MessagingActivity.this, response).showMessages();
                } else {
                    messagingReloading();
                    final FragmentManager fragmentManager = getSupportFragmentManager();
                    Fragment fragment = fragmentManager.findFragmentById(R.id.send_message_fragment);
                    ((SendMessageFragment) fragment).clearMsg();
                    /*
                    final FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.route_container, new MainMessagingFragment())
                            .addToBackStack(null)
                            .commit();*/
                }

            }
        }.execute();

    }

    private void enableSendBtn() {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.send_message_fragment);
        ((SendMessageFragment) fragment).enableSendBtn();
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
                String token = serviceProvider.getAuthToken(MessagingActivity.this);
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

    public void getMesssageImage(final ImageView imageView, final long commentId) {
        Bitmap localDecodedByte = searchForCurrentPics(commentId);
        if (localDecodedByte != null) {
            imageView.setImageBitmap(localDecodedByte);
        } else {
            new SafeAsyncTask<Boolean>() {
                Bitmap decodedByte;

                @Override
                public Boolean call() throws Exception {
                    String token = serviceProvider.getAuthToken(MessagingActivity.this);
                    PersonalInfoModel res = groupService.GetMsgImage(token, commentId);
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
                        picMap.add(new Pair<>(commentId, decodedByte));
                        imageView.setImageBitmap(decodedByte);
                    }
                }
            }.execute();
        }
    }

    private Bitmap searchForCurrentPics(long commentId) {
        for (Pair<Long, Bitmap> pair : picMap) {
            if (pair.first == commentId) {
                return pair.second;
            }
        }
        return null;
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
                messagingReloading();
            }
        }.execute();

    }

}
