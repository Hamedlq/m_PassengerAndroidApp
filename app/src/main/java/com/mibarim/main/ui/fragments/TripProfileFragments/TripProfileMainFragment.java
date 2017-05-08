package com.mibarim.main.ui.fragments.TripProfileFragments;

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
import com.mibarim.main.models.UserInfo.UserRouteModel;
import com.mibarim.main.ui.fragments.MapFragment;
import com.mibarim.main.ui.fragments.RequestRideFragment;
import com.mibarim.main.ui.fragments.UpdateMessageFragment;

import butterknife.ButterKnife;

/**
 * Created by Hamed on 3/5/2016.
 */
public class TripProfileMainFragment extends Fragment {

    private RelativeLayout layout;


    public TripProfileMainFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BootstrapApplication.component().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = (RelativeLayout) inflater.inflate(R.layout.fragment_trip_profile_main, container, false);
        final FragmentManager fragmentManager = getChildFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.user_info_fragment, new ProfileUserInfoFragment())
                .commit();
        fragmentManager.beginTransaction()
                .add(R.id.map_fragment, new MapFragment())
                .commit();
        fragmentManager.beginTransaction()
                .replace(R.id.route_info_fragment, new ProfileRouteInfoFragment())
                .commit();

        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ButterKnife.bind(this, layout);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public void setValues() {
        final FragmentManager fragmentManager = getChildFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.user_info_fragment);
        if (fragment instanceof ProfileUserInfoFragment) {
            ((ProfileUserInfoFragment) fragment).setValues();
        }
        Fragment fragment1 = fragmentManager.findFragmentById(R.id.route_info_fragment);
        if (fragment1 instanceof ProfileRouteInfoFragment) {
            ((ProfileRouteInfoFragment) fragment1).setValues();
        }
    }
}
