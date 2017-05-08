package com.mibarim.main.ui.fragments.addRouteStepper;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mibarim.main.BootstrapApplication;
import com.mibarim.main.R;
import com.mibarim.main.models.Address.PathPoint;
import com.mibarim.main.models.enums.AddRouteStates;
import com.mibarim.main.ui.activities.AddMapActivity;
import com.mibarim.main.ui.activities.RideRequestMapActivity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Hamed on 3/4/2016.
 */
public class StepSrcDstFragment extends Fragment implements View.OnTouchListener {

    @Bind(R.id.driver_label)
    protected TextView driver_label;
    @Bind(R.id.passenger_label)
    protected TextView passenger_label;

    @Bind(R.id.blue_icon)
    protected TextView blue_icon;
    @Bind(R.id.green_icon)
    protected TextView green_icon;
    @Bind(R.id.yellow_icon)
    protected TextView yellow_icon;
    @Bind(R.id.red_icon)
    protected TextView red_icon;
    @Bind(R.id.black_icon)
    protected TextView black_icon;
    @Bind(R.id.no_one_icon)
    protected TextView no_one_icon;

    @Bind(R.id.price)
    protected TextView price;

    @Bind(R.id.time_layout)
    protected LinearLayout time_layout;
    @Bind(R.id.driver_pass_layout)
    protected LinearLayout driver_pass_layout;
    @Bind(R.id.price_layout)
    protected LinearLayout price_layout;
    @Bind(R.id.route_path)
    protected LinearLayout route_path;
    @Bind(R.id.my_location)
    protected ImageView my_location;

    private boolean isDriver;

    SrcDstStepFragmentListener mCallback;


    public StepSrcDstFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BootstrapApplication.component().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.step_src_dst, container, false);
        return layout;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ButterKnife.bind(this, getView());
        time_layout.setOnTouchListener(this);
        driver_pass_layout.setOnTouchListener(this);
        blue_icon.setOnTouchListener(this);
        green_icon.setOnTouchListener(this);
        yellow_icon.setOnTouchListener(this);
        red_icon.setOnTouchListener(this);
        black_icon.setOnTouchListener(this);
        no_one_icon.setOnTouchListener(this);
        my_location.setOnTouchListener(this);
        SharedPreferences prefs = getActivity().getSharedPreferences(
                "com.mibarim.main", Context.MODE_PRIVATE);
        isDriver = prefs.getBoolean("IsDriver", false);
        if (isDriver) {
            driver_label.setVisibility(View.VISIBLE);
            passenger_label.setVisibility(View.GONE);
        } else {
            driver_label.setVisibility(View.GONE);
            passenger_label.setVisibility(View.VISIBLE);
        }
        setAllBlocksInvisible();
        switch (mCallback.getRouteStates()) {
            case SelectGoHomeState:
                break;
            case SelectGoWorkState:
                if (!isDriver) {
                    price_layout.setVisibility(View.VISIBLE);
                }
                break;
            case SelectReturnWorkState:
                break;
            case SelectReturnHomeState:
                if (!isDriver) {
                    price_layout.setVisibility(View.VISIBLE);
                }
                break;
            case SelectOriginState:
                break;
            case SelectEventOriginState:
                if (!isDriver) {
                    price_layout.setVisibility(View.VISIBLE);
                }
                break;
            case SelectEventDestinationState:
                if (!isDriver) {
                    price_layout.setVisibility(View.VISIBLE);
                }
                break;
            case SelectDestinationState:
                if (!isDriver) {
                    price_layout.setVisibility(View.VISIBLE);
                }
                break;
            case SelectDriveRouteState:
                route_path.setVisibility(View.VISIBLE);
                //setIconsVisibility(mCallback.getRecommendPathPointList().size());
                break;
            default:
                break;
        }
    }

    private void setAllBlocksInvisible() {
        price_layout.setVisibility(View.GONE);
        route_path.setVisibility(View.GONE);
        driver_pass_layout.setVisibility(View.GONE);
        time_layout.setVisibility(View.GONE);
    }


    private void setIconsVisibility(int size) {
        unVisibleAll();
        switch (size) {
            case 5:
                black_icon.setVisibility(View.VISIBLE);
            case 4:
                red_icon.setVisibility(View.VISIBLE);
            case 3:
                yellow_icon.setVisibility(View.VISIBLE);
            case 2:
                green_icon.setVisibility(View.VISIBLE);
            case 1:
                blue_icon.setVisibility(View.VISIBLE);
        }
    }

    private void unVisibleAll() {
        blue_icon.setVisibility(View.GONE);
        green_icon.setVisibility(View.GONE);
        yellow_icon.setVisibility(View.GONE);
        red_icon.setVisibility(View.GONE);
        black_icon.setVisibility(View.GONE);
    }

    public void setPrice(String thePrice) {
        if (thePrice == null || thePrice.equals("")) {
            price.setText("-");
        } else {
            price.setText(thePrice);
        }
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            switch (v.getId()) {
                case R.id.time_layout:
                    break;
                case R.id.blue_icon:
                    mCallback.setSelectedPathPoint(1);
                    //mCallback.RebuildAddMapFragment();
                    setSelected(blue_icon);
                    break;
                case R.id.green_icon:
                    mCallback.setSelectedPathPoint(2);
                    //mCallback.RebuildAddMapFragment();
                    setSelected(green_icon);
                    break;
                case R.id.yellow_icon:
                    mCallback.setSelectedPathPoint(3);
                    //mCallback.RebuildAddMapFragment();
                    setSelected(yellow_icon);
                    break;
                case R.id.red_icon:
                    mCallback.setSelectedPathPoint(4);
                    //mCallback.RebuildAddMapFragment();
                    setSelected(red_icon);
                    break;
                case R.id.black_icon:
                    mCallback.setSelectedPathPoint(5);
                    //mCallback.RebuildAddMapFragment();
                    setSelected(black_icon);
                    break;
                case R.id.no_one_icon:
                    mCallback.setSelectedPathPoint(0);
                    //mCallback.RebuildAddMapFragment();
                    setSelected(no_one_icon);
                    break;
                case R.id.my_location:
                    mCallback.gotoMyLocation();

                    break;
            }
            return true;
        }
        return false;
    }

    private void setSelected(TextView icon) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        unSelectAll();
        int pixels = (int) (50 * scale + 0.5f);
        icon.getLayoutParams().height = pixels;
        icon.getLayoutParams().width = pixels;
        icon.setLayoutParams(icon.getLayoutParams());
    }

    private void unSelectAll() {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        int pixels = (int) (40 * scale + 0.5f);

        blue_icon.getLayoutParams().height = pixels;
        blue_icon.getLayoutParams().width = pixels;
        blue_icon.setLayoutParams(blue_icon.getLayoutParams());
        green_icon.getLayoutParams().height = pixels;
        green_icon.getLayoutParams().width = pixels;
        green_icon.setLayoutParams(green_icon.getLayoutParams());
        yellow_icon.getLayoutParams().height = pixels;
        yellow_icon.getLayoutParams().width = pixels;
        yellow_icon.setLayoutParams(yellow_icon.getLayoutParams());
        red_icon.getLayoutParams().height = pixels;
        red_icon.getLayoutParams().width = pixels;
        red_icon.setLayoutParams(red_icon.getLayoutParams());
        black_icon.getLayoutParams().height = pixels;
        black_icon.getLayoutParams().width = pixels;
        black_icon.setLayoutParams(black_icon.getLayoutParams());
        no_one_icon.getLayoutParams().height = pixels;
        no_one_icon.getLayoutParams().width = pixels;
        no_one_icon.setLayoutParams(black_icon.getLayoutParams());
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (SrcDstStepFragmentListener) activity;
        } catch (ClassCastException e) {
        }

    }
    public interface SrcDstStepFragmentListener {
        public AddRouteStates getRouteStates();

        public void gotoMyLocation();

        public void setSelectedPathPoint(int i);

    }

}

