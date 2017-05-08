

package com.mibarim.main.ui.activities;

import android.Manifest;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.beardedhen.androidbootstrap.BootstrapCircleThumbnail;
import com.crashlytics.android.Crashlytics;
import com.mibarim.main.BootstrapApplication;
import com.mibarim.main.BootstrapServiceProvider;
import com.mibarim.main.R;
import com.mibarim.main.adapters.ViewPagerAdapter;
import com.mibarim.main.core.ImageUtils;
import com.mibarim.main.data.UserData;
import com.mibarim.main.models.AboutMeModel;
import com.mibarim.main.models.ApiResponse;
import com.mibarim.main.models.ImageResponse;
import com.mibarim.main.models.UserInfoModel;
import com.mibarim.main.models.enums.ImageTypes;
import com.mibarim.main.models.enums.UserCardTypes;
import com.mibarim.main.services.AuthenticateService;
import com.mibarim.main.services.RouteRequestService;
import com.mibarim.main.services.UserImageService;
import com.mibarim.main.services.UserInfoService;
import com.mibarim.main.ui.BootstrapActivity;
import com.mibarim.main.ui.HandleApiMessages;
import com.mibarim.main.ui.fragments.addRouteFragments.SrcDstFragment;
import com.mibarim.main.ui.fragments.userInfoFragments.AboutMeMainFragment;
import com.mibarim.main.ui.fragments.userInfoFragments.CommentContactFragment;
import com.mibarim.main.ui.fragments.userInfoFragments.ProfileFragment;
import com.mibarim.main.ui.fragments.userInfoFragments.UserInfoMainFragment;
import com.mibarim.main.util.SafeAsyncTask;

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
public class UserInfoActivity extends BootstrapActivity {

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
    private String tempImageDir;

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
    private TabLayout tabLayout;
    private ViewPager viewPager;

    protected String authToken;
    protected ApiResponse response;

    private boolean userHasAuthenticated = false;
    private UserInfoModel userInfoModel;
    private UserInfoModel newUserInfoModel;

    private static final String USER_INFO = "userInfoModel";
    private static final String SELECTED_FRAGMENT = "theFragment";
    private String profileImagePath;


    @Bind(R.id.select_user_image)
    protected FloatingActionButton select_user_image;
    @Bind(R.id.header)
    protected ImageView header;
    /*@Bind(R.id.user_image)
    protected BootstrapCircleThumbnail user_image;*/

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };


    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        BootstrapApplication.component().inject(this);

        setContentView(R.layout.user_info_activity);
        //setContentView(R.layout.user_info_tab_activity);

        // View injection with Butterknife
        ButterKnife.bind(this);

        if (getWindow().getDecorView().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
        userInfoModel = userData.userInfoQuery();
        /*if (getIntent() != null && getIntent().getExtras() != null) {
            userInfoModel = (UserInfoModel) getIntent().getExtras().getSerializable(USER_INFO);
        }*/

        toolbar = (Toolbar) findViewById(R.id.anim_toolbar);
        this.setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_menu_forward);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setTitle(userInfoModel.Name + " " + userInfoModel.Family);
        }

        /*viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        NestedScrollView scrollView = (NestedScrollView) findViewById (R.id.nest_scrollview);
        scrollView.setFillViewport(true);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);*/

        checkAuth();

        verifyStoragePermissions(this);

    }

    /**
     * Checks if the app has permission to write to device storage
     * <p>
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }


//    @Override
//    public void onConfigurationChanged(final Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//
//    }


    private void initScreen() {
        if (userHasAuthenticated) {
            final FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.main_container, new UserInfoMainFragment())
                    .commitAllowingStateLoss();
            select_user_image.setOnTouchListener(new View.OnTouchListener() {
                                                     @Override
                                                     public boolean onTouch(View v, MotionEvent event) {
                                                         if (event.getAction() == MotionEvent.ACTION_DOWN) {
                                                             selectImage();
                                                         }
                                                         return true;
                                                     }
                                                 }
            );
            //getImageFromServer(userInfoModel.UserImageId);
            /*BitmapDrawable background = new BitmapDrawable();
            //background.setTileModeX(android.graphics.Shader.TileMode.REPEAT);*/
            header.setImageBitmap(getImage(userInfoModel.UserImageId));
            //user_image.setImageBitmap(getImage(userInfoModel.UserImageId));
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new AboutMeMainFragment(), getString(R.string.about_me));
        adapter.addFragment(new CommentContactFragment(), getString(R.string.user_comments));
        //adapter.addFragment(new UserInfoMainFragment(), "THREE");
        viewPager.setAdapter(adapter);
    }

    /*private void setEmail() {
        if (userInfoModel.Email == null) {
            userInfoModel = userData.userInfoQuery();
            try {
                Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
                Account[] accounts = AccountManager.get(getApplicationContext()).getAccounts();
                for (Account account : accounts) {
                    if (emailPattern.matcher(account.name).matches()) {
                        userInfoModel.Email = account.name;
                    }
                }
            } catch (Exception e) {

            }
            userData.insertUserInfo(userInfoModel);
        }
    }*/

    private void checkAuth() {
        new SafeAsyncTask<Boolean>() {

            @Override
            public Boolean call() throws Exception {
                final AuthenticateService svc = serviceProvider.getService(UserInfoActivity.this);
                if (svc != null) {
                    authToken = serviceProvider.getAuthToken(UserInfoActivity.this);
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

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_route_btn:
                //saveUserInfo();
                finishIt();
                return true;
            case android.R.id.home:
                //saveUserInfo();
                finishIt();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*private void saveUserInfo() {
        newUserInfoModel = getNewUserInfo();
        if (newUserInfoModel != null && newUserInfoModel.Name.equals("") || newUserInfoModel.Family.equals("")) {
            Toaster.showLong(UserInfoActivity.this, getString(R.string.empty_user_info));
        } else {
            sendNewUserInfoToServer();
        }*//* else {
            finish();
        }*//*
    }

    private UserInfoModel getNewUserInfo() {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.main_container);
        return ((UserInfoMainFragment) fragment).getNewUserInfo();
    }

    private boolean isUserInfoChanged() {
        if (newUserInfoModel.Gender.equals(userInfoModel.Gender) &&
                newUserInfoModel.Name.equals(userInfoModel.Name) &&
                newUserInfoModel.Family.equals(userInfoModel.Family) &&
                (newUserInfoModel.Email == null || newUserInfoModel.Email.isEmpty() || userInfoModel.Email.equals(newUserInfoModel.Email)) &&
                (newUserInfoModel.Code == null || newUserInfoModel.Code.isEmpty() || userInfoModel.Code.equals(newUserInfoModel.Code)) &&
                (newUserInfoModel.CarType == null || newUserInfoModel.CarType.isEmpty() || userInfoModel.CarType.equals(newUserInfoModel.CarType)) &&
                (newUserInfoModel.CarPlateNo == null || newUserInfoModel.CarPlateNo.isEmpty() || userInfoModel.CarPlateNo.equals(newUserInfoModel.CarPlateNo)) &&
                (newUserInfoModel.CarColor == null || newUserInfoModel.CarColor.isEmpty() || userInfoModel.CarColor.equals(newUserInfoModel.CarColor)) &&
                (newUserInfoModel.BankShaba == null || newUserInfoModel.BankShaba.isEmpty() || userInfoModel.BankShaba.equals(newUserInfoModel.BankShaba)) &&
                (newUserInfoModel.BankName == null || newUserInfoModel.BankName.isEmpty() || userInfoModel.BankName.equals(newUserInfoModel.BankName)) &&
                (newUserInfoModel.BankAccountNo == null || newUserInfoModel.BankAccountNo.isEmpty() || userInfoModel.BankAccountNo.equals(newUserInfoModel.BankAccountNo)) &&
                (newUserInfoModel.NationalCode == null || newUserInfoModel.NationalCode.isEmpty() || userInfoModel.NationalCode.equals(newUserInfoModel.NationalCode))) {
            return false;
        } else {
            return true;
        }
    }

    private void sendNewUserInfoToServer() {
        new SafeAsyncTask<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                if (authToken == null) {
                    authToken = serviceProvider.getAuthToken(UserInfoActivity.this);
                }
                response = userInfoService.SaveUserInfo(authToken, newUserInfoModel);
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
                new HandleApiMessages(UserInfoActivity.this, response).showMessages();
                if (uploadSuccess) {
                    finishIt();
                }
            }
        }.execute();
    }
*/

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //saveUserInfo();
            finishIt();
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    public UserInfoModel getUserInfo() {
        return userInfoModel;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == USER_REQUEST_CAMERA || requestCode == LICENSE_REQUEST_CAMERA || requestCode == CAR_REQUEST_CAMERA || requestCode == CARBK_REQUEST_CAMERA
                    || requestCode == NATIONAL_CARD_REQUEST_CAMERA) {
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
                } /*else if (requestCode == LICENSE_REQUEST_CAMERA) {
                    saveLicenseImage(typedFile);
                } else if (requestCode == CAR_REQUEST_CAMERA) {
                    saveCarImage(typedFile);
                } else if (requestCode == CARBK_REQUEST_CAMERA) {
                    saveCarBckImage(typedFile);
                } else if (requestCode == NATIONAL_CARD_REQUEST_CAMERA) {
                    saveNationalCardImage(typedFile);
                }*/
                /*else if (requestCode == BANK_CARD_REQUEST_CAMERA) {
                    saveBankCardImage(typedFile);
                }*/
            } else if (requestCode == USER_SELECT_FILE || requestCode == LICENSE_SELECT_FILE || requestCode == CAR_SELECT_FILE || requestCode == CARBK_SELECT_FILE
                    || requestCode == NATIONAL_CARD_SELECT_FILE) {
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
                final int REQUIRED_SIZE = 300;
                int scale = 1;
                while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                        && options.outHeight / scale / 2 >= REQUIRED_SIZE)
                    scale *= 2;
                options.inSampleSize = scale;
                options.inJustDecodeBounds = false;
                bm = BitmapFactory.decodeFile(selectedImagePath, options);
                TypedFile typedFile = new TypedFile("image/jpeg", new File(selectedImagePath));
                if (requestCode == USER_SELECT_FILE) {
                    saveUserImage(typedFile);
                } /*else if (requestCode == LICENSE_SELECT_FILE) {
                    saveLicenseImage(typedFile);
                } else if (requestCode == CAR_SELECT_FILE) {
                    saveCarImage(typedFile);
                } else if (requestCode == CARBK_SELECT_FILE) {
                    saveCarBckImage(typedFile);
                } else if (requestCode == NATIONAL_CARD_SELECT_FILE) {
                    saveNationalCardImage(typedFile);
                *//*} else if (requestCode == BANK_CARD_SELECT_FILE) {
                    saveBankCardImage(typedFile);*//*
                }*/
            }
        }
    }

    private void reloadUserInfo() {
        new SafeAsyncTask<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                if (authToken == null) {
                    serviceProvider.invalidateAuthToken();
                    authToken = serviceProvider.getAuthToken(UserInfoActivity.this);
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
        if (userInfoModel.UserImageId != null && newUserInfoModel.UserImageId != null &&
                !userInfoModel.UserImageId.equals(newUserInfoModel.UserImageId)) {
            getImageFromServer(newUserInfoModel.UserImageId);
        }
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
                    authToken = serviceProvider.getAuthToken(UserInfoActivity.this);
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
                    new HandleApiMessages(UserInfoActivity.this, response).showMessages();
                }
                reloadUserInfo();
            }
        }.execute();
    }

    /*private void saveLicenseImage(final TypedFile pic) {
        new SafeAsyncTask<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                if (authToken == null) {
                    authToken = serviceProvider.getAuthToken(UserInfoActivity.this);
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
                    new HandleApiMessages(UserInfoActivity.this, response).showMessages();
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
                    authToken = serviceProvider.getAuthToken(UserInfoActivity.this);
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
                    new HandleApiMessages(UserInfoActivity.this, response).showMessages();
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
                    authToken = serviceProvider.getAuthToken(UserInfoActivity.this);
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
                    new HandleApiMessages(UserInfoActivity.this, response).showMessages();
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
                    authToken = serviceProvider.getAuthToken(UserInfoActivity.this);
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
                    new HandleApiMessages(UserInfoActivity.this, response).showMessages();
                }
                reloadUserInfo();
            }
        }.execute();
    }*/

    /*private void saveBankCardImage(final TypedFile pic) {
        new SafeAsyncTask<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                if (authToken == null) {
                    authToken = serviceProvider.getAuthToken(UserInfoActivity.this);
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
                    new HandleApiMessages(UserInfoActivity.this, response).showMessages();
                }
                reloadUserInfo();
            }
        }.execute();
    }*/

    private void selectImage() {
        final CharSequence[] items = {getString(R.string.camera), getString(R.string.fromGallery), getString(R.string.later)};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals(getString(R.string.camera))) {
                    // Check permission for CAMERA
                    if (ActivityCompat.checkSelfPermission(UserInfoActivity.this, Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {
                        // Check Permissions Now
                        // Callback onRequestPermissionsResult interceptado na Activity MainActivity
                        ActivityCompat.requestPermissions(UserInfoActivity.this,
                                new String[]{Manifest.permission.CAMERA},
                                USER_REQUEST_CAMERA);
                    } else {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        UserInfoActivity.this.startActivityForResult(intent, USER_REQUEST_CAMERA);
                    }
                } else if (items[item].equals(getString(R.string.fromGallery))) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    UserInfoActivity.this.startActivityForResult(
                            Intent.createChooser(intent, getString(R.string.choose_pic)),
                            USER_SELECT_FILE);
                } else if (items[item].equals(getString(R.string.later))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void finishIt() {
        //Toaster.showLong(UserInfoActivity.this, R.string.info_saved);
        Intent intent = getIntent();
        setResult(RESULT_OK, intent);
        this.finish();
    }

    /*public Bitmap getImageById(String imageId) {
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
    }*/

    public Bitmap getImage(String imageId) {
        ImageResponse imageResponse = userData.imageQuery(imageId);
        if (imageResponse != null) {
            Bitmap b = ImageUtils.loadImageFromStorage(imageResponse.ImageFilePath, imageResponse.ImageId);
            return b;
        }
        return null;
    }

    private void getImageFromServer(final String imageId) {
        new SafeAsyncTask<Boolean>() {
            ImageResponse imageResponse = new ImageResponse();
            Bitmap decodedByte;

            @Override
            public Boolean call() throws Exception {
                String token = serviceProvider.getAuthToken(UserInfoActivity.this);
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
                    if (imageResponse != null && imageResponse.ImageType == ImageTypes.UserPic) {
                        byte[] decodedString = Base64.decode(imageResponse.Base64ImageFile, Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        profileImagePath = ImageUtils.saveImageToInternalStorage(getApplicationContext(), decodedByte, imageResponse.ImageId);
                        userData.insertImage(imageResponse, profileImagePath);
                        setHeadImage(imageResponse);
                    }
                }
            }
        }.execute();
    }

    /*private void setImage(ImageResponse imageResponse) {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.main_container);
        ((UserInfoMainFragment) fragment).setImage(imageResponse);
    }*/

    private void reloadFragments() {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.main_container, new UserInfoMainFragment())
                .commitAllowingStateLoss();
    }

    public void setHeadImage(ImageResponse imageResponse) {
        Bitmap decodedByte = getImage(imageResponse.ImageId);
        header.setImageBitmap(decodedByte);
    }

    public void goToUserDetailActivity(UserCardTypes userCardModel) {
        switch (userCardModel) {
            case Exit:
                Intent resultIntent = getIntent();
                resultIntent.putExtra("ActionType", "Exit");
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
                break;
            default:
                final Intent i = new Intent(this, UserInfoDetailActivity.class);
                //UserInfoModel model = userData.userInfoQuery();
                i.putExtra(USER_INFO, userInfoModel);
                i.putExtra(SELECTED_FRAGMENT, userCardModel);
                this.startActivity(i);
        }
    }


    public AboutMeModel getAboutMe() {
        return userData.aboutMeQuery();
    }
}
