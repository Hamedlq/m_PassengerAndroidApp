package com.mibarim.main.ui.fragments.addRouteFragments;

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

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by Hamed on 3/5/2016.
 */
public class MainAddMapFragment extends Fragment {

    private RelativeLayout layout;
/*
    @Bind(R.id.info_submit_fragment)
    protected FrameLayout info_submit_fragment;
*/

    public MainAddMapFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BootstrapApplication.component().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = (RelativeLayout) inflater.inflate(R.layout.fragment_main_add_map, container, false);
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
                .replace(R.id.map_fragment, new AddMapFragment())
                .commit();
        fragmentManager.beginTransaction()
                .replace(R.id.route_src_dst_fragment, new SrcDstFragment())
                .commit();
        fragmentManager.beginTransaction()
                .replace(R.id.address_flag_fragment, new AddressFlagFragment())
                .commit();
//        fragmentManager.beginTransaction()
//                .replace(R.id.info_submit_fragment, new InfoMessageFragment())
//                .commit();
    }

    public void RebuildAddressFragment() {
        final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.address_flag_fragment, new AddressFlagFragment())
                .addToBackStack(null)
                .commitAllowingStateLoss();
        fragmentManager.executePendingTransactions();
    }

    public void waitingAddress() {
        final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.address_flag_fragment);
        if (fragment instanceof AddressFlagFragment) {
            ((AddressFlagFragment) fragment).waitingState();
        }

    }

//
//    public void SourceFragment() {
//        final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//        //removeDstFragment();
//        ShowSrcFragment();
//        RebuildSrcDstFragment();
//        fragmentManager.executePendingTransactions();
//        Fragment mapFragment = fragmentManager.findFragmentById(R.id.map_fragment);
//        ((AddMapFragment) mapFragment).SourceState();
//
//    }

    public void ShowSrcFragment() {
        final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        //removeDstFragment();
        fragmentManager.beginTransaction()
                .replace(R.id.map_fragment, new AddMapFragment())
                .replace(R.id.address_flag_fragment, new AddressFlagFragment())
                .addToBackStack(null)
                .commit();
        /*fragmentManager.executePendingTransactions();
        Fragment mapFragment = fragmentManager.findFragmentById(R.id.map_fragment);
        ((AddMapFragment) mapFragment).SourceState();*/

    }



    /*public void DestinationFragment() {
        final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        removeSrcFragment();
        removeRequestingFragment();
        Fragment mapFragment = fragmentManager.findFragmentById(R.id.map_fragment);
        fragmentManager.beginTransaction()
                .replace(R.id.newRoute_dst_fragment, new DstFragment())
                .addToBackStack(null)
                .commit();
        ((AddMapFragment) mapFragment).DestinationState();
        fragmentManager.executePendingTransactions();
    }*/


    /*public void RequestingFragment() {
        final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        //Fragment dstFragment = fragmentManager.findFragmentById(R.id.newRoute_dst_fragment);
        Fragment mapFragment = fragmentManager.findFragmentById(R.id.map_fragment);
        fragmentManager.beginTransaction()
                //.remove(dstFragment)
                //.replace(R.id.newRoute_requesting_fragment, new RequestingFragment())
                .addToBackStack(null)
                .commit();
        ((AddMapFragment) mapFragment).RequestingState();
        fragmentManager.executePendingTransactions();
    }
*/
   /* public void RebuildDestinationFragment() {
        final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.newRoute_dst_fragment, new DstFragment())
                .addToBackStack(null)
                .commitAllowingStateLoss();
        fragmentManager.executePendingTransactions();
    }*/

    public void RebuildSrcDstFragment() {
        final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.route_src_dst_fragment, new SrcDstFragment())
                .addToBackStack(null)
                .commitAllowingStateLoss();
        //fragmentManager.executePendingTransactions();
    }

    public void hideSourceFragment() {
        final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        removeMapFragment();
//        removeSrcFragment();
        removeAddressFlagFragment();
        /*fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_down, R.anim.slide_up)
                .replace(R.id.event_list_fragment, new EventListFragment())
                .addToBackStack(null)
                .commit();
        fragmentManager.executePendingTransactions();*/
    }

    public void EventSourceFragment() {
        final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        //removeEventListFragment();
        removeSrcFragment();
        fragmentManager.beginTransaction()
                .replace(R.id.map_fragment, new AddMapFragment())
                        //.replace(R.id.newRoute_dst_fragment, new DstFragment())
                .replace(R.id.address_flag_fragment, new AddressFlagFragment())
                .addToBackStack(null)
                .commit();
        fragmentManager.executePendingTransactions();
        Fragment mapFragment = fragmentManager.findFragmentById(R.id.map_fragment);
        ((AddMapFragment) mapFragment).SourceEventState();
    }

    public void EventDestinationFragment() {
        final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        //removeEventListFragment();
        fragmentManager.beginTransaction()
                .replace(R.id.map_fragment, new AddMapFragment())
                        //.replace(R.id.newRoute_dst_fragment, new DstFragment())
                .replace(R.id.address_flag_fragment, new AddressFlagFragment())
                .addToBackStack(null)
                .commit();
        fragmentManager.executePendingTransactions();
        Fragment mapFragment = fragmentManager.findFragmentById(R.id.map_fragment);
        ((AddMapFragment) mapFragment).DestinationEventState();
    }

    private void removeRequestingFragment() {
        final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        //Fragment requestingFragment = fragmentManager.findFragmentById(R.id.newRoute_requesting_fragment);
        //if (requestingFragment instanceof RequestingFragment) {
//            fragmentManager.beginTransaction()
//                    .remove(requestingFragment)
//                    .commit();
        //}
    }

    private void removeMapFragment() {
        final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        Fragment mapFragment = fragmentManager.findFragmentById(R.id.map_fragment);
        if (mapFragment instanceof AddMapFragment) {
            fragmentManager.beginTransaction()
                    .remove(mapFragment)
                    .commit();
        }
    }

    private void removeSrcFragment() {
        final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        //Fragment srcFragment = fragmentManager.findFragmentById(R.id.main_carousel_fragment);
        /*if (srcFragment instanceof MainCarouselFragment) {
            fragmentManager.beginTransaction()
                    .remove(srcFragment)
                    .commit();
        }*/
    }

    /*private void removeDstFragment() {
        final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        Fragment dstFragment = fragmentManager.findFragmentById(R.id.newRoute_dst_fragment);
        if (dstFragment instanceof DstFragment) {
            fragmentManager.beginTransaction()
                    .remove(dstFragment)
                    .commit();
        }
    }*/

   /* private void removeEventListFragment() {
        final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        Fragment eventFragment = fragmentManager.findFragmentById(R.id.event_list_fragment);
        if (eventFragment instanceof EventListFragment) {
            fragmentManager.beginTransaction()
                    .remove(eventFragment)
                    .commit();
        }
    }*/

    private void removeAddressFlagFragment() {
        final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        Fragment addressFlagFragment = fragmentManager.findFragmentById(R.id.address_flag_fragment);
        if (addressFlagFragment instanceof AddressFlagFragment) {
            fragmentManager.beginTransaction()
                    .remove(addressFlagFragment)
                    .commit();
        }
    }


    /*public void showResultMessage(String msg) {
        final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.info_submit_fragment);
        ((InfoMessageFragment) fragment).setActionBtn(getString(R.string.ok));
        ((InfoMessageFragment) fragment).setMsg(msg);
        info_submit_fragment.setVisibility(View.VISIBLE);
    }*/

    /*public void hideSubmitMsg() {
        info_submit_fragment.setVisibility(View.GONE);
    }*/

    public void MoveMap(String lat, String lng) {
        final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.executePendingTransactions();
        Fragment mapFragment = fragmentManager.findFragmentById(R.id.map_fragment);
        ((AddMapFragment) mapFragment).MoveMap(lat, lng);
    }

    public void RebuildFragment() {
        final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        /*Fragment addMapFragment = fragmentManager.findFragmentById(R.id.map_fragment);
        fragmentManager.beginTransaction()
                .remove(addMapFragment)
                .commit();*/
        fragmentManager.beginTransaction()
                .replace(R.id.map_fragment, new AddMapFragment())
                .replace(R.id.route_src_dst_fragment, new SrcDstFragment())
                .replace(R.id.address_flag_fragment, new AddressFlagFragment())
                .addToBackStack(null)
                .commitAllowingStateLoss();
    }

    public void ReDrawAddMapFragment() {
        final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.map_fragment);
        ((AddMapFragment) fragment).reDrawRoutePaths();
    }

    public void AddCityLocations(List<CityLocation> cityLocationList) {
        final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.map_fragment);
        ((AddMapFragment) fragment).setMapPoints(cityLocationList);
    }

    public void RebuildDstFragment() {
        final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.route_src_dst_fragment, new SrcDstFragment())
                .replace(R.id.address_flag_fragment, new AddressFlagFragment())
                .addToBackStack(null)
                .commitAllowingStateLoss();
        fragmentManager.executePendingTransactions();
        Fragment mapFragment = fragmentManager.findFragmentById(R.id.map_fragment);
        ((AddMapFragment) mapFragment).DestinationState();
    }

    public void RebuildDstFragment(String lat, String lng) {
        final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.route_src_dst_fragment, new SrcDstFragment())
                .replace(R.id.address_flag_fragment, new AddressFlagFragment())
                .addToBackStack(null)
                .commitAllowingStateLoss();
        fragmentManager.executePendingTransactions();
        Fragment mapFragment = fragmentManager.findFragmentById(R.id.map_fragment);
        ((AddMapFragment) mapFragment).DestinationState(lat, lng);
    }

    public void setPrice(String pathPrice) {
        final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.route_src_dst_fragment);
        if (fragment instanceof SrcDstFragment) {
            ((SrcDstFragment) fragment).setPrice(pathPrice);
        }
    }
}
