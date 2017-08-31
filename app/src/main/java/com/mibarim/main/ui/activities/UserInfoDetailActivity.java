package com.mibarim.main.ui.activities;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.AppCompatButton;
import android.util.Base64;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.mibarim.main.BootstrapApplication;
import com.mibarim.main.BootstrapServiceProvider;
import com.mibarim.main.R;
import com.mibarim.main.core.Constants;
import com.mibarim.main.core.ImageUtils;
import com.mibarim.main.data.UserData;
import com.mibarim.main.models.ApiResponse;
import com.mibarim.main.models.ImageResponse;
import com.mibarim.main.models.Plus.PassRouteModel;
import com.mibarim.main.models.UserInfoModel;
import com.mibarim.main.services.UserImageService;
import com.mibarim.main.services.UserInfoService;
import com.mibarim.main.ui.BootstrapActivity;
import com.mibarim.main.ui.HandleApiMessages;
import com.mibarim.main.ui.HandleApiMessagesBySnackbar;
import com.mibarim.main.util.SafeAsyncTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.inject.Inject;

import butterknife.Bind;

/**
 * Created by Alireza on 8/8/2017.
 */

public class UserInfoDetailActivity extends BootstrapActivity implements View.OnClickListener {

    private static final int RESULT_LOAD_IMAGE = 1;
    ImageButton uploadButton;
    ImageButton cameraButton;

    ImageView imageToUpload;
    ImageView imageTemp;
    private int USER_REQUEST_CAMERA = 2;
    private int USER_SELECT_FILE = 3;
    private boolean refreshingToken = false;
    private String authToken;
    private UserInfoModel newUserInfoModel;
    private Bitmap image;
    @Bind(R.id.progressBar)
    protected ProgressBar progressBar;

    @Bind(R.id.continue_btn)
    protected AppCompatButton continueButton;


    private ApiResponse response;

    private String encodedImage;

    private PassRouteModel imageResponse;

    private View parentLayout;
    private String id;




    @Inject
    BootstrapServiceProvider serviceProvider;

    @Inject
    UserImageService userImageService;

    @Inject
    UserInfoService userInfoService;

    @Inject
    UserData userData;
    private int CROP_PIC_REQUEST_CODE = 2;
    private int REQUEST_TAKE_PICTURE = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_info_detail_activity);

        BootstrapApplication.component().inject(this);
        progressBar.setVisibility(View.GONE);


        imageToUpload = (ImageView) findViewById(R.id.image_to_upload);
        uploadButton = (ImageButton) findViewById(R.id.camera_button);
        cameraButton = (ImageButton) findViewById(R.id.gallery_button);


        imageToUpload.setOnClickListener(this);

        uploadButton.setOnClickListener(this);
        cameraButton.setOnClickListener(this);
        continueButton.setOnClickListener(this);

        if (getIntent() != null && getIntent().getExtras() != null) {
            authToken = getIntent().getExtras().getString(Constants.Auth.AUTH_TOKEN);
        }

        SharedPreferences prefs = getApplicationContext().getSharedPreferences(
                "com.mibarim.main", Context.MODE_PRIVATE);
        prefs.edit().putInt("UserPhotoUploadedFirstTry",1).apply();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.gallery_button:
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);

                break;

            case R.id.camera_button:
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PICTURE);



/*

                UploadImage uploadImage = new UploadImage(image, "name");
                uploadImage.execute();
*/


//                getImageById(imageResponse.UserImageId);


//                selectImage();

                break;

            case R.id.continue_btn:
                finish();


        }

    }


    private void doCrop(Uri picUri) {
        try {

            Intent cropIntent = new Intent("com.android.camera.action.CROP");

            cropIntent.setDataAndType(picUri, "image/*");
            cropIntent.putExtra("crop", "true");
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            cropIntent.putExtra("outputX", 128);
            cropIntent.putExtra("outputY", 128);
            cropIntent.putExtra("return-data", true);
            startActivityForResult(cropIntent, CROP_PIC_REQUEST_CODE);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            // display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }


    private Intent getCropIntent(Intent intent) {
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 320);
        intent.putExtra("outputY", 320);
        intent.putExtra("return-data", true);
        return intent;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {

            Uri selectedImageURI = data.getData();

            final Uri imageUri = data.getData();
            InputStream imageStream = null;
            try {
                imageStream = getContentResolver().openInputStream(imageUri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            final Bitmap selectedImage1 = BitmapFactory.decodeStream(imageStream);

            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setClassName("com.google.android.gallery3d", "com.android.gallery3d.app.CropImage");
//            File file = new File(filePath);
//            Uri uri = Uri.fromFile(file);
            intent.setData(imageUri);
            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("outputX", 96);
            intent.putExtra("outputY", 96);
            intent.putExtra("noFaceDetection", true);
            intent.putExtra("return-data", true);
//            startActivityForResult(intent, CROP_PIC_REQUEST_CODE);

            CropImage.activity(imageUri).setCropShape(CropImageView.CropShape.OVAL).setFixAspectRatio(true)
                    .start(this);

//            String encodedImage = encodeImage(selectedImage);
            
/*

            imageToUpload.setImageURI(selectedImage);
            image = ((BitmapDrawable) imageToUpload.getDrawable()).getBitmap();
*/
//            image = selectedImage1;
//            saveProfileImage();
        }

        if (requestCode == CROP_PIC_REQUEST_CODE) {
/*

            Intent intent = new Intent("com.android.camera.action.CROP");
            Uri uri = Uri.fromFile(new File(cameraFileName));
            intent.setDataAndType(uri, "image*/
/*");
            startActivityForResult(getCropIntent(intent), CROP_PIC_REQUEST_CODE);
*/

        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                InputStream imageStream = null;
                try {
                    imageStream = getContentResolver().openInputStream(resultUri);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                final Bitmap selectedImage1 = BitmapFactory.decodeStream(imageStream);
                image = selectedImage1;
                progressBar.setVisibility(View.VISIBLE);
                saveProfileImage();



            }
            if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }


        if (requestCode == REQUEST_TAKE_PICTURE) {
            if (data != null) {
//                Uri photo = (Uri) data.getExtras().get("data");
//                Bitmap photo = (Bitmap) data.getExtras().get("data");
                Uri uri = data.getData();

                CropImage.activity(uri).setCropShape(CropImageView.CropShape.OVAL).setFixAspectRatio(true)
                        .start(this);


//                image = photo;
//                saveProfileImage();

            }


        }
    }

/*
    public class UploadImage extends AsyncTask<String, String, String> {

        private final Bitmap image;
        String name;

        public UploadImage(Bitmap image, String name) {

            this.image = image;
            this.name = name;

        }

        @Override
        protected String doInBackground(String... params) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }


    }
*/


    private void saveProfileImage() {
        new SafeAsyncTask<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                if (authToken == null) {
                    authToken = serviceProvider.getAuthToken(UserInfoDetailActivity.this);
                }

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);

                response = userImageService.SaveImage(authToken, encodedImage);
                if ((response.Errors == null || response.Errors.size() == 0) && response.Status.equals("OK")) {

                    if (response != null) {
                        for (String tripJson : response.Messages) {
                            id = tripJson.replace("\"", "");
                        }
                    }

//                    id = response.Messages.get(0);

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
//                getImageById(imageResponse.UserImageId);
                getImageById(id);
                reloadUserInfo();
            }
        }.execute();
    }

    private void reloadUserInfo() {
        new SafeAsyncTask<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                if (authToken == null) {
//                    serviceProvider.invalidateAuthToken();
//                    authToken = serviceProvider.getAuthToken(UserInfoDetailActivity.this);
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
//                    getImages(newUserInfoModel);
                }
            }
        }.execute();

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
                    String path = ImageUtils.saveImageToInternalStorage(getApplicationContext(), decodedByte, imageResponse.ImageId);
                    userData.insertImage(imageResponse, path);
//                    imageToUpload.setImageURI();
                    imageToUpload.setImageBitmap(decodedByte);
                    progressBar.setVisibility(View.GONE);

/*

                    hideProgress();
                    if (imageLoaded) {
                        gotoBankPayPage(paymentDetailModel);
                    }
*/

                    new HandleApiMessagesBySnackbar(parentLayout, response).showMessages();
//                    setImage(imageResponse);
                }
            }
        }.execute();
    }







/*

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
                        // Callback onRequestPermissionsResult interceptado na Activity MainActivity0
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
                    intent.setType("image*/
/*");
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

*/

/*

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
*/


/*


    private void sendUserInfoToServer() {
        new SafeAsyncTask<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                if (authToken == null) {
                    authToken = serviceProvider.getAuthToken(UserInfoDetailActivity.this);
                }
                UserInfoModel u = userData.tempUserInfoQuery();
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

*/


}
