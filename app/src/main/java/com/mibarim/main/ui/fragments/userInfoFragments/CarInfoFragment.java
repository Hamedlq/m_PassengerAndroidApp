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
import com.mibarim.main.models.CarInfoModel;
import com.mibarim.main.ui.activities.UserInfoDetailActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Hamed on 3/5/2016.
 */
public class CarInfoFragment extends Fragment {

    private LinearLayout layout;

    private static final int CAR_REQUEST_CAMERA = 5;
    private static final int CAR_SELECT_FILE = 6;

    private static final int CARBK_REQUEST_CAMERA = 7;
    private static final int CARBK_SELECT_FILE = 8;

    private UserInfoModel infoModel;


    @Bind(R.id.carImage)
    protected BootstrapThumbnail carImage;
    @Bind(R.id.carBkImage)
    protected BootstrapThumbnail carBkImage;
    @Bind(R.id.carType)
    protected EditText carType;
    @Bind(R.id.car_no)
    protected EditText car_no;
    @Bind(R.id.car_color)
    protected EditText car_color;


    public CarInfoFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BootstrapApplication.component().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = (LinearLayout) inflater.inflate(R.layout.fragment_car_info, container, false);
        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ButterKnife.bind(this, getView());
        infoModel = ((UserInfoDetailActivity) getActivity()).getUserInfo();
        if (infoModel != null) {
            carType.setText(infoModel.CarType);
            car_no.setText(infoModel.CarPlateNo);
            car_color.setText(infoModel.CarColor);
            carImage.setImageBitmap(((UserInfoDetailActivity) getActivity()).getImageById(infoModel.CarCardImageId));
            carBkImage.setImageBitmap(((UserInfoDetailActivity) getActivity()).getImageById(infoModel.CarCardBckImageId));
            /*if (carInfo.Base64CarCardPic != null && !carInfo.Base64CarCardPic.equals("")) {
                byte[] decodedString = Base64.decode(carInfo.Base64CarCardPic, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                carImage.setImageBitmap(decodedByte);
            }
            if (carInfo.Base64CarCardBckPic != null && !carInfo.Base64CarCardBckPic.equals("")) {
                byte[] decodedString = Base64.decode(carInfo.Base64CarCardBckPic, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                carBkImage.setImageBitmap(decodedByte);
            }*/
            carImage.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        selectImage();
                    }
                    return true;
                }
            });
            carBkImage.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        selectBKImage();

                    }
                    return true;
                }
            });

        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        /*if (carType != null && !isVisibleToUser) {
            saveCarInfo();
        }*/
    }
/*
    public void saveCarInfo() {
        ((UserInfoActivity) getActivity()).setNewCarInfoModel(getCarInfo());
    }*/

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
                                CAR_REQUEST_CAMERA);
                    } else {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        getActivity().startActivityForResult(intent, CAR_REQUEST_CAMERA);
                    }
                } else if (items[item].equals(getString(R.string.fromGallery))) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    getActivity().startActivityForResult(
                            Intent.createChooser(intent, getString(R.string.choose_pic)),
                            CAR_SELECT_FILE);
                } else if (items[item].equals(getString(R.string.later))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void selectBKImage() {
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
                                CARBK_REQUEST_CAMERA);
                    } else {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        getActivity().startActivityForResult(intent, CARBK_REQUEST_CAMERA);
                    }
                } else if (items[item].equals(getString(R.string.fromGallery))) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    getActivity().startActivityForResult(
                            Intent.createChooser(intent, getString(R.string.choose_pic)),
                            CARBK_SELECT_FILE);
                } else if (items[item].equals(getString(R.string.later))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public UserInfoModel getCarInfo() {
        UserInfoModel model = new UserInfoModel();
        model.CarType = carType.getText().toString();
        model.CarColor = car_color.getText().toString();
        model.CarPlateNo = car_no.getText().toString();
        return model;
    }

    public void setCarImage(ImageResponse imageResponse) {
        carImage.setImageBitmap(((UserInfoDetailActivity)getActivity()).getImageById(imageResponse.ImageId));
        /*byte[] decodedString = Base64.decode(imageResponse.Base64ImageFile, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        carImage.setImageBitmap(decodedByte);*/
    }

    public void setCarBckImage(ImageResponse imageResponse) {
        carBkImage.setImageBitmap(((UserInfoDetailActivity)getActivity()).getImageById(imageResponse.ImageId));
        /*byte[] decodedString = Base64.decode(imageResponse.Base64ImageFile, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        carBkImage.setImageBitmap(decodedByte);*/
    }
}
