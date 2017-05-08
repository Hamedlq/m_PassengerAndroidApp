package com.mibarim.main.ui.fragments.userInfoFragments;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapCircleThumbnail;
import com.beardedhen.androidbootstrap.BootstrapThumbnail;
import com.mibarim.main.BootstrapApplication;
import com.mibarim.main.R;
import com.mibarim.main.models.ImageResponse;
import com.mibarim.main.models.UserInfoModel;
import com.mibarim.main.ui.activities.UserInfoActivity;
import com.mibarim.main.models.PersonalInfoModel;
import com.mibarim.main.ui.activities.UserInfoDetailActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Hamed on 3/5/2016.
 */
public class UserPersonFragment extends Fragment {

    private static final int NATIONAL_CARD_REQUEST_CAMERA = 9;
    private static final int NATIONAL_CARD_SELECT_FILE = 10;
    private LinearLayout layout;

//    @Bind(R.id.user_image)
//    protected BootstrapCircleThumbnail user_image;
    @Bind(R.id.gender)
    protected TextView gender;
//    @Bind(R.id.user_mobile)
//    protected TextView user_mobile;
    @Bind(R.id.name)
    protected AutoCompleteTextView name;
    @Bind(R.id.family)
    protected AutoCompleteTextView family;
    @Bind(R.id.email)
    protected AutoCompleteTextView email;
    @Bind(R.id.the_code)
    protected AutoCompleteTextView the_code;
    @Bind(R.id.national_code)
    protected AutoCompleteTextView national_code;
    @Bind(R.id.nationalCardImage)
    protected BootstrapThumbnail nationalCardImage;


    public UserPersonFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BootstrapApplication.component().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = (LinearLayout) inflater.inflate(R.layout.fragment_user_person, container, false);
        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ButterKnife.bind(this, getView());
        /*SharedPreferences prefs = getActivity().getSharedPreferences(
                "com.mibarim.main", Context.MODE_PRIVATE);
        String Mobile = prefs.getString("UserMobile", "");*/
        //user_mobile.setText(Mobile);
        UserInfoModel userInfo = null;
        if (getActivity() instanceof UserInfoDetailActivity) {
            userInfo = ((UserInfoDetailActivity) getActivity()).getUserInfo();
            //go_on.setVisibility(View.GONE);
        }
        if (userInfo != null && userInfo.Gender!=null) {
            if (userInfo.Gender.equals("1")) {
                gender.setText(R.string.man);
            } else {
                gender.setText(R.string.woman);
            }
            //user_mobile.setText(userInfo.Mobile);
            name.setText(userInfo.Name);
            family.setText(userInfo.Family);
            email.setText(userInfo.Email);
            the_code.setText(userInfo.Code);
            national_code.setText(userInfo.NationalCode);
            nationalCardImage.setImageBitmap(((UserInfoDetailActivity)getActivity()).getImageById(userInfo.NationalCardImageId));
        }
        gender.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    selectGender();
                    return true;
                }
                return false;
            }
        });

        nationalCardImage.setOnTouchListener(new View.OnTouchListener() {
                                          @Override
                                          public boolean onTouch(View v, MotionEvent event) {
                                              if (event.getAction() == MotionEvent.ACTION_DOWN) {
                                                  selectNationalCardImage();
                                              }
                                              return true;
                                          }
                                      }
        );
    }



    public void setUserImage(Bitmap bm) {
//        user_image.setImageBitmap(bm);
    }

    public UserInfoModel getUserInfo() {
        UserInfoModel model = new UserInfoModel();
        if (gender.getText().equals(getString(R.string.man))) {
            model.Gender = "1";
        } else {
            model.Gender = "2";
        }
        model.Name = name.getText().toString();
        model.Family = family.getText().toString();
        model.Email = email.getText().toString();
        model.Code= the_code.getText().toString();
        model.NationalCode = national_code.getText().toString();
        return model;
    }

    /*@Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getActivity() instanceof UserInfoActivity) {
            if (name!=null && !isVisibleToUser) {
                saveUserInfo();
            }
        }
    }*/

   /* public void saveUserInfo(){
        ((UserInfoActivity) getActivity()).setNewPersonalInfoModel(getUserInfo());
    }*/

    private void selectGender() {
        final CharSequence[] items = {getString(R.string.man), getString(R.string.woman)};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals(getString(R.string.man))) {
                    gender.setText(getString(R.string.man));
                    dialog.dismiss();
                } else if (items[item].equals(getString(R.string.woman))) {
                    gender.setText(getString(R.string.woman));
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void selectNationalCardImage() {
        final CharSequence[] items = {getString(R.string.camera), getString(R.string.fromGallery), getString(R.string.later)};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals(getString(R.string.camera))) {
                    // Check permission for CAMERA
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {
                        // Check Permissions Now
                        // Callback onRequestPermissionsResult interceptado na Activity MainActivity
                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{Manifest.permission.CAMERA},
                                NATIONAL_CARD_REQUEST_CAMERA);
                    } else {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        getActivity().startActivityForResult(intent, NATIONAL_CARD_REQUEST_CAMERA);
                    }
                } else if (items[item].equals(getString(R.string.fromGallery))) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    getActivity().startActivityForResult(
                            Intent.createChooser(intent, getString(R.string.choose_pic)),
                            NATIONAL_CARD_SELECT_FILE);
                } else if (items[item].equals(getString(R.string.later))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public void setNationalImage(ImageResponse imageResponse) {
        nationalCardImage.setImageBitmap(((UserInfoDetailActivity)getActivity()).getImageById(imageResponse.ImageId));
        /*byte[] decodedString = Base64.decode(imageResponse.Base64ImageFile, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        nationalCardImage.setImageBitmap(decodedByte);*/
    }
}
