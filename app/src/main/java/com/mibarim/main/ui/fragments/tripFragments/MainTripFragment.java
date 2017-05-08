package com.mibarim.main.ui.fragments.tripFragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.mibarim.main.BootstrapApplication;
import com.mibarim.main.R;
import com.mibarim.main.models.CityLocation;
import com.mibarim.main.models.Trip.TripResponse;
import com.mibarim.main.ui.activities.TripActivity;
import com.mibarim.main.ui.fragments.InfoMessageFragment;
import com.mibarim.main.ui.fragments.addRouteFragments.AddMapFragment;
import com.mibarim.main.ui.fragments.addRouteFragments.AddressFlagFragment;
import com.mibarim.main.ui.fragments.addRouteFragments.SrcDstFragment;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Hamed on 3/5/2016.
 */
public class MainTripFragment extends Fragment {

    private RelativeLayout layout;
    @Bind(R.id.info_message_fragment)
    protected FrameLayout info_message_fragment;

    public MainTripFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BootstrapApplication.component().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = (RelativeLayout) inflater.inflate(R.layout.fragment_main_trip, container, false);
        initScreen();
        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ButterKnife.bind(this, layout);
        hideLocationMsg();
    }

    private void initScreen() {
        final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .replace(R.id.map_fragment, new TripMapFragment())
                .commit();
        fragmentManager.beginTransaction()
                .replace(R.id.trip_info_fragment, new TripInfoFragment())
                .commit();
        fragmentManager.beginTransaction()
                .replace(R.id.info_message_fragment, new InfoMessageFragment())
                .commit();
    }

    public void hideLocationMsg() {
        info_message_fragment.setVisibility(View.GONE);
    }
    public void showLocationMsg(String msg){
        info_message_fragment.setVisibility(View.VISIBLE);
        final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.info_message_fragment);
        ((InfoMessageFragment) fragment).setActionBtn(getString(R.string.ok));
        ((InfoMessageFragment) fragment).setMsg(msg);
    }




    public void showTripInfo(TripResponse tripResponse) {
        final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.map_fragment);
        ((TripMapFragment) fragment).showInfoOnMap(tripResponse);
        fragment = fragmentManager.findFragmentById(R.id.trip_info_fragment);
        ((TripInfoFragment) fragment).showInfo(tripResponse);
    }

    public void MoveMap(String lat, String lng) {
        final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.map_fragment);

        if (fragment != null) {
            ((TripMapFragment) fragment).MoveMap(lat, lng);
        }
    }
}
