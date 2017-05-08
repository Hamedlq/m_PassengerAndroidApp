package com.mibarim.main.ui.fragments.rideRequestFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.mibarim.main.BootstrapApplication;
import com.mibarim.main.R;
import com.mibarim.main.models.CityLocation;
import com.mibarim.main.ui.fragments.addRouteFragments.AddMapFragment;
import com.mibarim.main.ui.fragments.addRouteFragments.AddressFlagFragment;
import com.mibarim.main.ui.fragments.addRouteFragments.SrcDstFragment;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by Hamed on 3/5/2016.
 */
public class RideRequestMainFragment extends Fragment {

    private RelativeLayout layout;
/*
    @Bind(R.id.info_submit_fragment)
    protected FrameLayout info_submit_fragment;
*/

    public RideRequestMainFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BootstrapApplication.component().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = (RelativeLayout) inflater.inflate(R.layout.fragment_main_ride_request, container, false);
        initScreen();
        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ButterKnife.bind(this, layout);
        //hideSubmitMsg();
    }

    private void initScreen() {
        final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .replace(R.id.map_fragment, new RideRequestMapFragment())
                .commit();
        fragmentManager.beginTransaction()
                .replace(R.id.ride_flag_fragment, new RideFlagFragment())
                .commit();
        fragmentManager.beginTransaction()
                .replace(R.id.route_src_dst_fragment, new SrcDstFragment())
                .commit();
    }


    public void MoveMap(String lat, String lng) {
        final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.executePendingTransactions();
        Fragment mapFragment = fragmentManager.findFragmentById(R.id.map_fragment);
        ((RideRequestMapFragment) mapFragment).MoveMap(lat, lng);
    }

    public void RebuildAddressFragment() {
        final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.ride_flag_fragment, new RideFlagFragment())
                .addToBackStack(null)
                .commitAllowingStateLoss();
        fragmentManager.executePendingTransactions();
    }

    public void setPrice(String pathPrice) {
        final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.route_src_dst_fragment);
        if (fragment instanceof SrcDstFragment) {
            ((SrcDstFragment) fragment).setPrice(pathPrice);
        }
    }

}
