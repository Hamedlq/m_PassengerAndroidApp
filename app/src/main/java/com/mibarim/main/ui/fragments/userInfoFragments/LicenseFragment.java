package com.mibarim.main.ui.fragments.userInfoFragments;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.beardedhen.androidbootstrap.BootstrapThumbnail;
import com.mibarim.main.BootstrapApplication;
import com.mibarim.main.R;
import com.mibarim.main.models.ImageResponse;
import com.mibarim.main.models.UserInfoModel;
import com.mibarim.main.ui.activities.UserInfoActivity;
import com.mibarim.main.models.LicenseInfoModel;
import com.mibarim.main.ui.activities.UserInfoDetailActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Hamed on 3/5/2016.
 */
public class LicenseFragment extends Fragment {

    private LinearLayout layout;
    private static final int LICENSE_REQUEST_CAMERA = 3;
    private static final int LICENSE_SELECT_FILE = 4;


    @Bind(R.id.licenseImage)
    protected BootstrapThumbnail licenseImage;
    /*@Bind(R.id.licenseNo)
    protected EditText licenseNo;*/


    public LicenseFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BootstrapApplication.component().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = (LinearLayout) inflater.inflate(R.layout.fragment_license_info, container, false);
        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ButterKnife.bind(this, getView());

        UserInfoModel licenseInfo = ((UserInfoDetailActivity) getActivity()).getUserInfo();
        if (licenseInfo != null) {
            licenseImage.setImageBitmap(((UserInfoDetailActivity)getActivity()).getImageById(licenseInfo.LicenseImageId));

            //licenseNo.setText(licenseInfo.LicenseNo);
            /*if (licenseInfo.Base64LicensePic != null && !licenseInfo.Base64LicensePic.equals("")) {
                byte[] decodedString = Base64.decode(licenseInfo.Base64LicensePic, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                licenseImage.setImageBitmap(decodedByte);
            }*/
            licenseImage.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        selectImage();
                    }
                    return true;
                }
            });

        }

    }

    private void selectImage() {
        final CharSequence[] items = {getString(R.string.camera), getString(R.string.fromGallery), getString(R.string.later)};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals(getString(R.string.camera))) {
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {
                        // Check Permissions Now
                        // Callback onRequestPermissionsResult interceptado na Activity MainActivity
                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{Manifest.permission.CAMERA},
                                LICENSE_REQUEST_CAMERA);
                    } else {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        getActivity().startActivityForResult(intent, LICENSE_REQUEST_CAMERA);
                    }
                } else if (items[item].equals(getString(R.string.fromGallery))) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    getActivity().startActivityForResult(
                            Intent.createChooser(intent, getString(R.string.choose_pic)),
                            LICENSE_SELECT_FILE);
                } else if (items[item].equals(getString(R.string.later))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    public LicenseInfoModel getLicenseInfo() {
        LicenseInfoModel model=new LicenseInfoModel();
        //model.LicenseNo=licenseNo.getText().toString();
        return model;
    }

    /*public void saveLicenseInfo(){
        ((UserInfoActivity) getActivity()).setNewLicenseInfoModel(getLicenseInfo());
    }*/

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        /*if (licenseNo!=null && !isVisibleToUser) {
            saveLicenseInfo();
        }*/
    }

    public void setLicenseImage(ImageResponse imageResponse) {
        licenseImage.setImageBitmap(((UserInfoDetailActivity)getActivity()).getImageById(imageResponse.ImageId));
        /*byte[] decodedString = Base64.decode(imageResponse.Base64ImageFile, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        licenseImage.setImageBitmap(decodedByte);*/
    }
}
