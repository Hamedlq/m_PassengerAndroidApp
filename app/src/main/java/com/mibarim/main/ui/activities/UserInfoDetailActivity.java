

package com.mibarim.main.ui.activities;

import android.Manifest;
import android.accounts.OperationCanceledException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;

import com.crashlytics.android.Crashlytics;
import com.mibarim.main.BootstrapApplication;
import com.mibarim.main.BootstrapServiceProvider;
import com.mibarim.main.R;
import com.mibarim.main.authenticator.TokenRefreshActivity;
import com.mibarim.main.core.ImageUtils;
import com.mibarim.main.data.UserData;
import com.mibarim.main.events.UnAuthorizedErrorEvent;
import com.mibarim.main.models.AboutMeModel;
import com.mibarim.main.models.ApiResponse;
import com.mibarim.main.models.ImageResponse;
import com.mibarim.main.models.InviteModel;
import com.mibarim.main.models.UserInfoModel;
import com.mibarim.main.models.enums.UserCardTypes;
import com.mibarim.main.services.AuthenticateService;
import com.mibarim.main.services.RouteRequestService;
import com.mibarim.main.services.UserImageService;
import com.mibarim.main.services.UserInfoService;
import com.mibarim.main.ui.BootstrapActivity;
import com.mibarim.main.ui.HandleApiMessages;
import com.mibarim.main.ui.fragments.userInfoFragments.UserInfoDetailMainFragment;
import com.mibarim.main.ui.fragments.userInfoFragments.UserInfoMainFragment;
import com.mibarim.main.util.SafeAsyncTask;
import com.mibarim.main.util.Toaster;
import com.squareup.otto.Subscribe;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.fabric.sdk.android.Fabric;
import retrofit.mime.TypedFile;


/**
 * Initial activity for the application.
 * <p/>
 * If you need to remove the authentication from the application please see
 * {@link com.mibarim.main.authenticator.ApiKeyProvider#getAuthKey(android.app.Activity)}
 */
public class UserInfoDetailActivity extends BootstrapActivity {

    private static final int USER_REQUEST_CAMERA = 1;
    private static final int USER_SELECT_FILE = 2;
    private static final int LICENSE_REQUEST_CAMERA = 3;
    private static final int LICENSE_SELECT_FILE = 4;
    private static final int CAR_REQUEST_CAMERA = 5;
    private static final int CAR_SELECT_FILE = 6;
    private static final int CARBK_REQUEST_CAMERA = 7;
    private static final int CARBK_SELECT_FILE = 8;
    private static final int NATIONAL_CARD_REQUEST_CAMERA = 9;
    private static final int NATIONAL_CARD_SELECT_FILE = 10;
/*
    private static final int BANK_CARD_REQUEST_CAMERA = 11;
    private static final int BANK_CARD_SELECT_FILE = 12;
*/


    @Inject
    BootstrapServiceProvider serviceProvider;
    @Inject
    RouteRequestService routeRequestService;
    @Inject
    UserInfoService userInfoService;
    @Inject
    UserImageService userImageService;
    @Inject
    UserData userData;

    private CharSequence title;
    private Toolbar toolbar;

    protected String authToken;
    protected ApiResponse response;
    private InviteModel inviteModel;

    private boolean userHasAuthenticated = false;
    private UserInfoModel userInfoModel;
    private UserInfoModel newUserInfoModel;

    private static final String USER_INFO = "userInfoModel";
    private static final String SELECTED_FRAGMENT = "theFragment";
    private ActionBar actionBar;
    private UserCardTypes selectedFragment;

    private int REFRESH_TOKEN_REQUEST = 3456;

    private boolean refreshingToken = false;

    /*private PersonalInfoModel personalInfoModel;
    private CarInfoModel carInfoModel;
    private LicenseInfoModel licenseInfoModel;

    private PersonalInfoModel newPersonalInfoModel;
    private CarInfoModel newCarInfoModel;
    private LicenseInfoModel newLicenseInfoModel;*/


/*
    private static final String PERSONAL_INFO = "personalInfo";
    private static final String LICENSE_INFO = "licenseInfo";
    private static final String CAR_INFO = "carInfo";

    private boolean isPersonalInfoSaved = false;
    private boolean isCarInfoSaved = false;
    private boolean isLicenseInfoSaved = false;
*/

/*
    @Bind(R.id.select_user_image)
    protected FloatingActionButton select_user_image;
*/


    @Override
    protected void onCreate(final Bundle savedInstanceState) {

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
            //userInfoModel = (UserInfoModel) getIntent().getExtras().getSerializable(USER_INFO);
            selectedFragment= (UserCardTypes)getIntent().getExtras().getSerializable(SELECTED_FRAGMENT);
        }
        userInfoModel = userData.userInfoQuery();
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

        checkAuth();
    }

    private void initScreen() {
        if (userHasAuthenticated) {
            final FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.main_container, new UserInfoDetailMainFragment())
                    .commitAllowingStateLoss();
            //checkSave();
        }
    }

    private void checkAuth() {
        new SafeAsyncTask<Boolean>() {

            @Override
            public Boolean call() throws Exception {
                final AuthenticateService svc = serviceProvider.getService(UserInfoDetailActivity.this);
                if (svc != null) {
                    authToken = serviceProvider.getAuthToken(UserInfoDetailActivity.this);
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
                userHasAuthenticated = true;
                initScreen();
            }
        }.execute();
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


    private void checkSave() {
        UserInfoModel u=userData.tempCarInfoQuery();
        if(u!=null){
            saveUserTempInfo();
        }
        UserInfoModel uc=userData.tempCarInfoQuery();
        if(uc!=null){
            saveCarTempInfo();
        }
        UserInfoModel ub=userData.tempBankInfoQuery();
        if(ub!=null){
            saveBankTempInfo();
        }
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                saveAndfinishIt();
                //saveUserInfo();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
/*
    private void saveUserInfo() {
        newUserInfoModel = getNewUserInfo();
        if (newUserInfoModel != null && newUserInfoModel.Name.equals("") || newUserInfoModel.Family.equals("")) {
            Toaster.showLong(UserInfoDetailActivity.this, getString(R.string.empty_user_info));
        } else{
            sendNewUserInfoToServer();
        }*//* else {
            finish();
        }*//*
    }*/

    private UserInfoModel getNewUserInfo() {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.main_container);
        return ((UserInfoDetailMainFragment) fragment).getUserInfo();
    }

    private AboutMeModel getAboutMeInfo() {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.main_container);
        return ((UserInfoDetailMainFragment) fragment).getAboutMeInfo();
    }

    /*private boolean isUserInfoChanged() {
        if (newUserInfoModel.Gender.equals(userInfoModel.Gender) &&
                newUserInfoModel.Name.equals(userInfoModel.Name) &&
                newUserInfoModel.Family.equals(userInfoModel.Family) &&
                (newUserInfoModel.Email==null || newUserInfoModel.Email.isEmpty() || userInfoModel.Email.equals(newUserInfoModel.Email)) &&
                (newUserInfoModel.Code==null || newUserInfoModel.Code.isEmpty() || userInfoModel.Code.equals(newUserInfoModel.Code)) &&
                (newUserInfoModel.CarType==null ||newUserInfoModel.CarType.isEmpty() || userInfoModel.CarType.equals(newUserInfoModel.CarType)) &&
                (newUserInfoModel.CarPlateNo==null ||newUserInfoModel.CarPlateNo.isEmpty() || userInfoModel.CarPlateNo.equals(newUserInfoModel.CarPlateNo)) &&
                (newUserInfoModel.CarColor==null ||newUserInfoModel.CarColor.isEmpty() || userInfoModel.CarColor.equals(newUserInfoModel.CarColor)) &&
                (newUserInfoModel.BankShaba==null ||newUserInfoModel.BankShaba.isEmpty() || userInfoModel.BankShaba.equals(newUserInfoModel.BankShaba)) &&
                (newUserInfoModel.BankName==null ||newUserInfoModel.BankName.isEmpty() || userInfoModel.BankName.equals(newUserInfoModel.BankName)) &&
                (newUserInfoModel.BankAccountNo==null ||newUserInfoModel.BankAccountNo.isEmpty() || userInfoModel.BankAccountNo.equals(newUserInfoModel.BankAccountNo)) &&
                (newUserInfoModel.NationalCode==null ||newUserInfoModel.NationalCode.isEmpty() || userInfoModel.NationalCode.equals(newUserInfoModel.NationalCode))) {
            return false;
        } else {
            return true;
        }
    }*/

    private void sendUserInfoToServer() {
        new SafeAsyncTask<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                if (authToken == null) {
                    authToken = serviceProvider.getAuthToken(UserInfoDetailActivity.this);
                }
                UserInfoModel u=userData.tempUserInfoQuery();
                response = userInfoService.SaveUserPersonalInfo(authToken, u);
                if ((response.Errors == null || response.Errors.size() == 0) && response.Status.equals("OK")) {
                    return true;
                }
                return false;
            }

            @Override
            protected void onException(final Exception e) throws RuntimeException {
                super.onException(e);
            }

            @Override
            protected void onSuccess(final Boolean uploadSuccess) throws Exception {
                super.onSuccess(uploadSuccess);
                new HandleApiMessages(UserInfoDetailActivity.this, response).showMessages();
                if (uploadSuccess) {
                    userData.DeleteTempUserInfo();
                    reloadUserInfo();
                }
            }
        }.execute();
    }

    private void saveAboutMe() {
        new SafeAsyncTask<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                if (authToken == null) {
                    authToken = serviceProvider.getAuthToken(UserInfoDetailActivity.this);
                }
                AboutMeModel a=userData.aboutMeQuery();
                response = userInfoService.saveAboutMe(authToken, a);
                if ((response.Errors == null || response.Errors.size() == 0) && response.Status.equals("OK")) {
                    return true;
                }
                return true;
            }

            @Override
            protected void onException(final Exception e) throws RuntimeException {
                super.onException(e);
            }

            @Override
            protected void onSuccess(final Boolean uploadSuccess) throws Exception {
                super.onSuccess(uploadSuccess);
                new HandleApiMessages(UserInfoDetailActivity.this, response).showMessages();
                if (uploadSuccess) {
                }
            }
        }.execute();
    }

    private void saveCarTempInfo() {
        new SafeAsyncTask<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                if (authToken == null) {
                    authToken = serviceProvider.getAuthToken(UserInfoDetailActivity.this);
                }
                UserInfoModel u=userData.tempCarInfoQuery();
                response = userInfoService.SaveCarInfo(authToken, u);
                if ((response.Errors == null || response.Errors.size() == 0) && response.Status.equals("OK")) {
                    return true;
                }
                return true;
            }

            @Override
            protected void onException(final Exception e) throws RuntimeException {
                super.onException(e);
            }

            @Override
            protected void onSuccess(final Boolean uploadSuccess) throws Exception {
                super.onSuccess(uploadSuccess);
                new HandleApiMessages(UserInfoDetailActivity.this, response).showMessages();
                if (uploadSuccess) {
                    userData.DeleteTempUserInfo();
                    reloadUserInfo();
                }
            }
        }.execute();
    }

    private void saveBankTempInfo() {
        new SafeAsyncTask<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                if (authToken == null) {
                    authToken = serviceProvider.getAuthToken(UserInfoDetailActivity.this);
                }
                UserInfoModel u=userData.tempBankInfoQuery();
                response = userInfoService.SaveBankInfo(authToken, u);
                if ((response.Errors == null || response.Errors.size() == 0) && response.Status.equals("OK")) {
                    return true;
                }
                return true;
            }

            @Override
            protected void onException(final Exception e) throws RuntimeException {
                super.onException(e);
            }

            @Override
            protected void onSuccess(final Boolean uploadSuccess) throws Exception {
                super.onSuccess(uploadSuccess);
                new HandleApiMessages(UserInfoDetailActivity.this, response).showMessages();
                if (uploadSuccess) {
                    userData.DeleteTempUserInfo();
                    reloadUserInfo();
                }
            }
        }.execute();
    }


    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            saveAndfinishIt();
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    public UserInfoModel getUserInfo() {
        return userInfoModel;
    }

    public UserCardTypes getSelectedFragment(){
        return selectedFragment;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REFRESH_TOKEN_REQUEST) {
                authToken = null;
                serviceProvider.invalidateAuthToken();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshingToken = false;
                    }
                }, 5000);
            }
            if (requestCode == USER_REQUEST_CAMERA || requestCode == LICENSE_REQUEST_CAMERA || requestCode == CAR_REQUEST_CAMERA || requestCode == CARBK_REQUEST_CAMERA
                    || requestCode == NATIONAL_CARD_REQUEST_CAMERA ) {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                File destination = new File(Environment.getExternalStorageDirectory(),
                        System.currentTimeMillis() + ".jpg");
                FileOutputStream fo;
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                TypedFile typedFile = new TypedFile("image/jpeg", destination);
                if (requestCode == USER_REQUEST_CAMERA) {
                    saveUserImage(typedFile);
                } else if (requestCode == LICENSE_REQUEST_CAMERA) {
                    saveLicenseImage(typedFile);
                } else if (requestCode == CAR_REQUEST_CAMERA) {
                    saveCarImage(typedFile);
                } else if (requestCode == CARBK_REQUEST_CAMERA) {
                    saveCarBckImage(typedFile);
                } else if (requestCode == NATIONAL_CARD_REQUEST_CAMERA) {
                    saveNationalCardImage(typedFile);
                }
                /*else if (requestCode == BANK_CARD_REQUEST_CAMERA) {
                    saveBankCardImage(typedFile);
                }*/
            } else if (requestCode == USER_SELECT_FILE || requestCode == LICENSE_SELECT_FILE || requestCode == CAR_SELECT_FILE || requestCode == CARBK_SELECT_FILE
                    || requestCode == NATIONAL_CARD_SELECT_FILE ) {
                Uri selectedImageUri = data.getData();
                String[] projection = {MediaStore.MediaColumns.DATA};
                CursorLoader cursorLoader = new CursorLoader(this, selectedImageUri, projection, null, null,
                        null);
                Cursor cursor = cursorLoader.loadInBackground();
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                cursor.moveToFirst();
                String selectedImagePath = cursor.getString(column_index);
                Bitmap bm;
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(selectedImagePath, options);
                final int REQUIRED_SIZE = 200;
                int scale = 1;
                while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                        && options.outHeight / scale / 2 >= REQUIRED_SIZE)
                    scale *= 2;
                options.inSampleSize = scale;
                options.inJustDecodeBounds = false;
                bm = BitmapFactory.decodeFile(selectedImagePath, options);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                File destination = new File(Environment.getExternalStorageDirectory(),
                        System.currentTimeMillis() + ".jpg");
                FileOutputStream fo;
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                TypedFile typedFile = new TypedFile("image/jpeg", destination);
                if (requestCode == USER_SELECT_FILE) {
                    saveUserImage(typedFile);
                } else if (requestCode == LICENSE_SELECT_FILE) {
                    saveLicenseImage(typedFile);
                } else if (requestCode == CAR_SELECT_FILE) {
                    saveCarImage(typedFile);
                } else if (requestCode == CARBK_SELECT_FILE) {
                    saveCarBckImage(typedFile);
                } else if (requestCode == NATIONAL_CARD_SELECT_FILE) {
                    saveNationalCardImage(typedFile);
                /*} else if (requestCode == BANK_CARD_SELECT_FILE) {
                    saveBankCardImage(typedFile);*/
                }
            }
        }
    }

    private void reloadUserInfo() {
        new SafeAsyncTask<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                if (authToken == null) {
                    serviceProvider.invalidateAuthToken();
                    authToken = serviceProvider.getAuthToken(UserInfoDetailActivity.this);
                }
                newUserInfoModel = userInfoService.getUserInfo(authToken);
                return true;
            }

            @Override
            protected void onException(final Exception e) throws RuntimeException {
                super.onException(e);
            }

            @Override
            protected void onSuccess(final Boolean state) throws Exception {
                super.onSuccess(state);
                if (state) {
                    userData.insertUserInfo(newUserInfoModel);
                    getImages(newUserInfoModel);
                }
            }
        }.execute();

    }

    private void getImages(UserInfoModel newUserInfoModel) {
        if (userInfoModel.NationalCardImageId != null && newUserInfoModel.NationalCardImageId != null &&
                !userInfoModel.NationalCardImageId.equals(newUserInfoModel.NationalCardImageId)) {
            getImageFromServer(newUserInfoModel.NationalCardImageId);
        }
        if (userInfoModel.LicenseImageId != null && newUserInfoModel.LicenseImageId != null &&
                !userInfoModel.LicenseImageId.equals(newUserInfoModel.LicenseImageId)) {
            getImageFromServer(newUserInfoModel.LicenseImageId);
        }
        if (userInfoModel.CarCardImageId != null && newUserInfoModel.CarCardImageId != null &&
                !userInfoModel.CarCardImageId.equals(newUserInfoModel.CarCardImageId)) {
            getImageFromServer(newUserInfoModel.CarCardImageId);
        }
        if (userInfoModel.CarCardBckImageId != null && newUserInfoModel.CarCardBckImageId != null &&
                !userInfoModel.CarCardBckImageId.equals(newUserInfoModel.CarCardBckImageId)) {
            getImageFromServer(newUserInfoModel.CarCardBckImageId);
        }
        if (userInfoModel.BankImageId != null && newUserInfoModel.BankImageId != null &&
                !userInfoModel.BankImageId.equals(newUserInfoModel.BankImageId)) {
            getImageFromServer(newUserInfoModel.BankImageId);
        }
        userInfoModel = newUserInfoModel;
    }


    private void saveUserImage(final TypedFile pic) {
        new SafeAsyncTask<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                if (authToken == null) {
                    authToken = serviceProvider.getAuthToken(UserInfoDetailActivity.this);
                }
                response = userImageService.SaveUserImage(authToken, pic);
                if ((response.Errors == null || response.Errors.size() == 0) && response.Status.equals("OK")) {
                    return true;
                }
                return true;
            }

            @Override
            protected void onException(final Exception e) throws RuntimeException {
                super.onException(e);
            }

            @Override
            protected void onSuccess(final Boolean uploadSuccess) throws Exception {
                super.onSuccess(uploadSuccess);
                if (!uploadSuccess) {
                    new HandleApiMessages(UserInfoDetailActivity.this, response).showMessages();
                }
                reloadUserInfo();
            }
        }.execute();
    }

    private void saveLicenseImage(final TypedFile pic) {
        new SafeAsyncTask<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                if (authToken == null) {
                    authToken = serviceProvider.getAuthToken(UserInfoDetailActivity.this);
                }
                response = userImageService.SaveLicenseImage(authToken, pic);
                if ((response.Errors == null || response.Errors.size() == 0) && response.Status.equals("OK")) {
                    return true;
                }
                return true;
            }

            @Override
            protected void onException(final Exception e) throws RuntimeException {
                super.onException(e);
            }

            @Override
            protected void onSuccess(final Boolean uploadSuccess) throws Exception {
                super.onSuccess(uploadSuccess);
                if (!uploadSuccess) {
                    new HandleApiMessages(UserInfoDetailActivity.this, response).showMessages();
                }
                reloadUserInfo();
            }
        }.execute();
    }

    private void saveCarImage(final TypedFile pic) {
        new SafeAsyncTask<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                if (authToken == null) {
                    authToken = serviceProvider.getAuthToken(UserInfoDetailActivity.this);
                }
                response = userImageService.SaveCarImage(authToken, pic);
                if ((response.Errors == null || response.Errors.size() == 0) && response.Status.equals("OK")) {
                    return true;
                }
                return true;
            }

            @Override
            protected void onException(final Exception e) throws RuntimeException {
                super.onException(e);
            }

            @Override
            protected void onSuccess(final Boolean uploadSuccess) throws Exception {
                super.onSuccess(uploadSuccess);
                if (!uploadSuccess) {
                    new HandleApiMessages(UserInfoDetailActivity.this, response).showMessages();
                }
                reloadUserInfo();
            }
        }.execute();
    }

    private void saveCarBckImage(final TypedFile pic) {
        new SafeAsyncTask<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                if (authToken == null) {
                    authToken = serviceProvider.getAuthToken(UserInfoDetailActivity.this);
                }
                response = userImageService.SaveCarBckImage(authToken, pic);
                if ((response.Errors == null || response.Errors.size() == 0) && response.Status.equals("OK")) {
                    return true;
                }
                return true;
            }

            @Override
            protected void onException(final Exception e) throws RuntimeException {
                super.onException(e);
            }

            @Override
            protected void onSuccess(final Boolean uploadSuccess) throws Exception {
                super.onSuccess(uploadSuccess);
                if (!uploadSuccess) {
                    new HandleApiMessages(UserInfoDetailActivity.this, response).showMessages();
                }
                reloadUserInfo();
            }
        }.execute();
    }


    private void saveNationalCardImage(final TypedFile pic) {
        new SafeAsyncTask<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                if (authToken == null) {
                    authToken = serviceProvider.getAuthToken(UserInfoDetailActivity.this);
                }
                response = userImageService.SaveNationalCardImage(authToken, pic);
                if ((response.Errors == null || response.Errors.size() == 0) && response.Status.equals("OK")) {
                    return true;
                }
                return true;
            }

            @Override
            protected void onException(final Exception e) throws RuntimeException {
                super.onException(e);
            }

            @Override
            protected void onSuccess(final Boolean uploadSuccess) throws Exception {
                super.onSuccess(uploadSuccess);
                if (!uploadSuccess) {
                    new HandleApiMessages(UserInfoDetailActivity.this, response).showMessages();
                }
                reloadUserInfo();
            }
        }.execute();
    }

    private void saveBankCardImage(final TypedFile pic) {
        new SafeAsyncTask<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                if (authToken == null) {
                    authToken = serviceProvider.getAuthToken(UserInfoDetailActivity.this);
                }
                response = userImageService.SaveBankCardImage(authToken, pic);
                if ((response.Errors == null || response.Errors.size() == 0) && response.Status.equals("OK")) {
                    return true;
                }
                return true;
            }

            @Override
            protected void onException(final Exception e) throws RuntimeException {
                super.onException(e);
            }

            @Override
            protected void onSuccess(final Boolean uploadSuccess) throws Exception {
                super.onSuccess(uploadSuccess);
                if (!uploadSuccess) {
                    new HandleApiMessages(UserInfoDetailActivity.this, response).showMessages();
                }
                reloadUserInfo();
            }
        }.execute();
    }

    private void selectImage() {
        final CharSequence[] items = {getString(R.string.camera), getString(R.string.fromGallery), getString(R.string.later)};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals(getString(R.string.camera))) {
                    // Check permission for CAMERA
                    if (ActivityCompat.checkSelfPermission(UserInfoDetailActivity.this, Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {
                        // Check Permissions Now
                        // Callback onRequestPermissionsResult interceptado na Activity MainActivity
                        ActivityCompat.requestPermissions(UserInfoDetailActivity.this,
                                new String[]{Manifest.permission.CAMERA},
                                USER_REQUEST_CAMERA);
                    } else {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        UserInfoDetailActivity.this.startActivityForResult(intent, USER_REQUEST_CAMERA);
                    }
                } else if (items[item].equals(getString(R.string.fromGallery))) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    UserInfoDetailActivity.this.startActivityForResult(
                            Intent.createChooser(intent, getString(R.string.choose_pic)),
                            USER_SELECT_FILE);
                } else if (items[item].equals(getString(R.string.later))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void saveAndfinishIt() {
        switch (selectedFragment)
        {
            case AboutMe:
                userData.insertAboutMeInfo(getAboutMeInfo());
                saveAboutMe();
                break;
            case UserInfo:
                userData.insertTempUserInfo(getNewUserInfo());
                saveUserTempInfo();
                break;
            case CarInfo:
                userData.insertTempCarInfo(getNewUserInfo());
                saveCarTempInfo();
                break;
            case BankInfo:
                userData.insertTempBankInfo(getNewUserInfo());
                saveBankTempInfo();
                break;
        }
        //Toaster.showLong(UserInfoDetailActivity.this, R.string.info_saved);
        Intent intent = getIntent();
        setResult(RESULT_OK, intent);
        this.finish();
    }

    private void saveUserTempInfo() {
        newUserInfoModel=userData.tempUserInfoQuery();
        if (newUserInfoModel != null && newUserInfoModel.Name.equals("") || newUserInfoModel.Family.equals("")) {
            Toaster.showLong(UserInfoDetailActivity.this, getString(R.string.empty_user_info));
        } else{
            sendUserInfoToServer();
        }
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
            @Override
            public Boolean call() throws Exception {
                String token = serviceProvider.getAuthToken(UserInfoDetailActivity.this);
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
                    String path=ImageUtils.saveImageToInternalStorage(getApplicationContext(), decodedByte, imageResponse.ImageId);
                    userData.insertImage(imageResponse,path);
                    setImage(imageResponse);
                }
            }
        }.execute();
    }

    private void setImage(ImageResponse imageResponse) {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.main_container);
        ((UserInfoDetailMainFragment) fragment).setImage(imageResponse);
    }

    private void reloadFragments() {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.main_container, new UserInfoDetailMainFragment())
                .commitAllowingStateLoss();
    }

    public void submitDiscountCode(final String discountCode) {
        new SafeAsyncTask<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                if (authToken == null) {
                    serviceProvider.invalidateAuthToken();
                    authToken = serviceProvider.getAuthToken(UserInfoDetailActivity.this);
                }
                response = userInfoService.submitDiscount(authToken, discountCode);
                return true;
            }

            @Override
            protected void onException(final Exception e) throws RuntimeException {
                super.onException(e);
            }

            @Override
            protected void onSuccess(final Boolean state) throws Exception {
                super.onSuccess(state);
                if (state) {
                    new HandleApiMessages(UserInfoDetailActivity.this, response).showMessages();
                    if(response.Messages.size()>0){
                        for (String msg: response.Messages){
                            Toaster.showLong(UserInfoDetailActivity.this, msg,R.drawable.toast_success);
                            clearCode();
                        }
                    }
                }
            }
        }.execute();

    }

    private void clearCode() {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.main_container);
        ((UserInfoDetailMainFragment) fragment).ClearCode();
    }

    public AboutMeModel getAboutMe() {
        return userData.aboutMeQuery();
    }

    public void submitWithdrawRequest(final String withdrawAmount) {
        new SafeAsyncTask<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                if (authToken == null) {
                    serviceProvider.invalidateAuthToken();
                    authToken = serviceProvider.getAuthToken(UserInfoDetailActivity.this);
                }
                response = userInfoService.submitWithdrawRequest(authToken, withdrawAmount);
                return true;
            }

            @Override
            protected void onException(final Exception e) throws RuntimeException {
                super.onException(e);
            }

            @Override
            protected void onSuccess(final Boolean state) throws Exception {
                super.onSuccess(state);
                if (state) {
                    new HandleApiMessages(UserInfoDetailActivity.this, response).showMessages();
                    if(response.Messages.size()>0){
                        for (String msg: response.Messages){
                            Toaster.showLong(UserInfoDetailActivity.this, msg,R.drawable.toast_success);
                            clearCode();
                        }
                    }
                }
            }
        }.execute();
    }

    public void getInviteFromServer() {
        new SafeAsyncTask<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                if (authToken == null) {
                    serviceProvider.invalidateAuthToken();
                    authToken = serviceProvider.getAuthToken(UserInfoDetailActivity.this);
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
                reloadInviteCode();
            }
        }.execute();
    }

    private void reloadInviteCode() {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.main_container);
        ((UserInfoDetailMainFragment) fragment).reloadInvite();
    }

    public InviteModel getInviteCode() {
        return userData.inviteQuery();
    }

    public void ShareInvite(String link) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        //shareIntent.putExtra(Intent.EXTRA_TEXT, imageUri.toString());
        shareIntent.putExtra(Intent.EXTRA_TEXT,link);
        shareIntent.setType("text/plain");

        startActivity(Intent.createChooser(shareIntent, "لینک دعوت را به اشتراک بگذارید"));
    }
}
