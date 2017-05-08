package com.mibarim.main.ui.fragments.tripFragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapCircleThumbnail;
import com.mibarim.main.BootstrapApplication;
import com.mibarim.main.R;
import com.mibarim.main.models.Trip.TripResponse;
import com.mibarim.main.models.Trip.TripRouteModel;
import com.mibarim.main.ui.activities.AddMapActivity;
import com.mibarim.main.ui.activities.TripActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Hamed on 3/4/2016.
 */
public class TripInfoFragment extends Fragment {

    @Bind(R.id.driver_fragment)
    protected LinearLayout driver_fragment;
    @Bind(R.id.driverUserImage)
    protected BootstrapCircleThumbnail driverUserImage;
    @Bind(R.id.driver_name)
    protected TextView driver_name;
    @Bind(R.id.car_info)
    protected TextView car_info;
    @Bind(R.id.return_from_trip)
    protected ImageView return_from_trip;


    @Bind(R.id.user_1_fragment)
    protected LinearLayout user_1_fragment;
    @Bind(R.id.user_1_image)
    protected BootstrapCircleThumbnail user_1_image;
    @Bind(R.id.user_1_name)
    protected TextView user_1_name;

    @Bind(R.id.user_2_fragment)
    protected LinearLayout user_2_fragment;
    @Bind(R.id.user_2_image)
    protected BootstrapCircleThumbnail user_2_image;
    @Bind(R.id.user_2_name)
    protected TextView user_2_name;

    @Bind(R.id.user_3_fragment)
    protected LinearLayout user_3_fragment;
    @Bind(R.id.user_3_image)
    protected BootstrapCircleThumbnail user_3_image;
    @Bind(R.id.user_3_name)
    protected TextView user_3_name;

    @Bind(R.id.user_4_fragment)
    protected LinearLayout user_4_fragment;
    @Bind(R.id.user_4_image)
    protected BootstrapCircleThumbnail user_4_image;
    @Bind(R.id.user_4_name)
    protected TextView user_4_name;

    @Bind(R.id.my_location)
    protected ImageView my_location;

    @Bind(R.id.do_source_btn)
    protected BootstrapButton do_source_btn;


    public TripInfoFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BootstrapApplication.component().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.fragment_info_trip, container, false);
        return layout;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ButterKnife.bind(this, getView());
        ResetAll();
        do_source_btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (getActivity() instanceof TripActivity) {
                        ((TripActivity) getActivity()).EndTrip();
                    }
                    return true;
                }
                return false;
            }
        });
        my_location.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (getActivity() instanceof TripActivity) {
                        ((TripActivity) getActivity()).gotoMyLocation();
                    }
                    return true;
                }
                return false;
            }
        });
        return_from_trip.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    ((TripActivity) getActivity()).gotoMain();
                    return true;
                }
                return false;
            }
        });
        /*Animation blink = AnimationUtils.loadAnimation(getActivity(), R.anim.blink);
        return_from_trip.startAnimation(blink);*/

    }


    public void showInfo(TripResponse tripResponse) {
        int co = 0;
        ResetAll();
        do_source_btn.setEnabled(true);
        for (TripRouteModel tripRouteModel : tripResponse.TripRoutes) {
            if (tripRouteModel.IsDriver) {
                driver_fragment.setVisibility(View.VISIBLE);
                driver_name.setText(tripRouteModel.UserName + " " + tripRouteModel.UserFamily);
                ((TripActivity) getActivity()).getRouteImage(driverUserImage, tripRouteModel.UserImageId);
                car_info.setText(tripResponse.CarInfo);
                if(tripRouteModel.IsMe){
                    do_source_btn.setText(R.string.finishTrip);
                }
            } else {
                co++;
                switch (co) {
                    case 1:
                        user_1_fragment.setVisibility(View.VISIBLE);
                        user_1_name.setText(tripRouteModel.UserName + " " + tripRouteModel.UserFamily);
                        ((TripActivity) getActivity()).getRouteImage(user_1_image, tripRouteModel.UserImageId);
                        if(tripRouteModel.IsMe){
                            do_source_btn.setText(R.string.payAndFinish);
                        }
                        break;
                    case 2:
                        user_2_fragment.setVisibility(View.VISIBLE);
                        user_2_name.setText(tripRouteModel.UserName + " " + tripRouteModel.UserFamily);
                        ((TripActivity) getActivity()).getRouteImage(user_2_image, tripRouteModel.UserImageId);
                        if(tripRouteModel.IsMe){
                            do_source_btn.setText(R.string.payAndFinish);
                        }
                        break;
                    case 3:
                        user_3_fragment.setVisibility(View.VISIBLE);
                        user_3_name.setText(tripRouteModel.UserName + " " + tripRouteModel.UserFamily);
                        ((TripActivity) getActivity()).getRouteImage(user_3_image, tripRouteModel.UserImageId);
                        if(tripRouteModel.IsMe){
                            do_source_btn.setText(R.string.payAndFinish);
                        }
                        break;
                    case 4:
                        user_4_fragment.setVisibility(View.VISIBLE);
                        user_4_name.setText(tripRouteModel.UserName + " " + tripRouteModel.UserFamily);
                        ((TripActivity) getActivity()).getRouteImage(user_4_image, tripRouteModel.UserImageId);
                        if(tripRouteModel.IsMe){
                            do_source_btn.setText(R.string.payAndFinish);
                        }
                        break;
                }
            }

        }
    }

    private void ResetAll() {
        driver_fragment.setVisibility(View.GONE);
        user_1_fragment.setVisibility(View.GONE);
        user_2_fragment.setVisibility(View.GONE);
        user_3_fragment.setVisibility(View.GONE);
        user_4_fragment.setVisibility(View.GONE);
        do_source_btn.setText(R.string.wait_state);
        do_source_btn.setEnabled(false);
    }
}
