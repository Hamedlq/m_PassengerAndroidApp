package com.mibarim.main.ui.fragments.addRouteStepper;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.mibarim.main.BootstrapApplication;
import com.mibarim.main.R;
import com.mibarim.main.adapters.RouteStepperAdapter;
import com.mibarim.main.models.Address.LocationPoint;
import com.mibarim.main.models.RouteRequest;
import com.mibarim.main.models.enums.AddRouteStates;
import com.mibarim.main.ui.activities.HomeWorkStepActivity;
import com.mibarim.main.ui.activities.RouteStepActivity;
import com.mibarim.main.ui.fragments.addRouteFragments.AddMapFragment;
import com.mibarim.main.ui.fragments.addRouteFragments.AddressFlagFragment;
import com.mibarim.main.util.Toaster;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;
import com.stepstone.stepper.internal.TabsContainer;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Hamed on 3/5/2016.
 */
public class MainRouteStepFragment extends Fragment {

    private RelativeLayout layout;
    private StepperLayout mStepperLayout;
    private RouteStepperAdapter stepperAdapter;
    @Bind(R.id.map_fragment)
    protected FrameLayout map_fragment;
    @Bind(R.id.address_flag_fragment)
    protected FrameLayout address_flag_fragment;
    @Bind(R.id.route_src_dst_fragment)
    protected FrameLayout route_src_dst_fragment;

    public MainRouteStepFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BootstrapApplication.component().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = (RelativeLayout) inflater.inflate(R.layout.fragment_main_stepper, container, false);
        initScreen();
        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ButterKnife.bind(this, layout);
        ViewPager mPager = (ViewPager) layout.findViewById(R.id.ms_stepPager);
        mPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return false;
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                HorizontalScrollView mTabsScrollView = (HorizontalScrollView) layout.findViewById(R.id.ms_stepTabsScrollView);
                mTabsScrollView.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
            }
        }, 2000);
        /*getActivity().postDelayed(new Runnable() {
            public void run() {
                HorizontalScrollView mTabsScrollView = (HorizontalScrollView) layout.findViewById(R.id.ms_stepTabsScrollView);
                mTabsScrollView.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
            }
        }, 100L);*/
        setLayoutsHeight();
        nonMap();
    }

    private void setLayoutsHeight() {
        ViewGroup mStepNavigation = (ViewGroup) layout.findViewById(R.id.ms_bottomNavigation);
        ViewGroup.LayoutParams bottom_params = mStepNavigation.getLayoutParams();
/*        LinearLayout mTabsInnerContainer = (LinearLayout) layout.findViewById(R.id.ms_stepTabsInnerContainer);
        ViewGroup.LayoutParams top_params = mTabsInnerContainer.getLayoutParams();*/

        RelativeLayout.LayoutParams map_params = (RelativeLayout.LayoutParams) map_fragment.getLayoutParams();
        map_params.setMargins(map_params.leftMargin,
                map_params.topMargin+dpToPx(80),
                map_params.rightMargin,
                map_params.bottomMargin + bottom_params.height);
        map_fragment.setLayoutParams(map_params);
        RelativeLayout.LayoutParams flag_params = (RelativeLayout.LayoutParams) address_flag_fragment.getLayoutParams();
        flag_params.setMargins(flag_params.leftMargin,
                flag_params.topMargin +dpToPx(80),
                flag_params.rightMargin,
                flag_params.bottomMargin + bottom_params.height);
        address_flag_fragment.setLayoutParams(flag_params);
        RelativeLayout.LayoutParams src_dst_params = (RelativeLayout.LayoutParams) route_src_dst_fragment.getLayoutParams();
        src_dst_params.setMargins(src_dst_params.leftMargin,
                src_dst_params.topMargin +dpToPx(80),
                src_dst_params.rightMargin,
                src_dst_params.bottomMargin + bottom_params.height);
        route_src_dst_fragment.setLayoutParams(src_dst_params);
    }

    private void initScreen() {
        final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        stepperAdapter=new RouteStepperAdapter(fragmentManager, getActivity());
        mStepperLayout = (StepperLayout) layout.findViewById(R.id.stepperLayout);
        mStepperLayout.setAdapter(stepperAdapter);
        if(getActivity() instanceof RouteStepActivity){
            mStepperLayout.setListener((RouteStepActivity)getActivity());
        }
        if(getActivity() instanceof HomeWorkStepActivity){
            mStepperLayout.setListener((HomeWorkStepActivity)getActivity());
        }


        fragmentManager.beginTransaction()
                .replace(R.id.map_fragment, new AddMapFragment())
                .commit();
        fragmentManager.beginTransaction()
                .replace(R.id.route_src_dst_fragment, new StepSrcDstFragment())
                .commit();
        fragmentManager.beginTransaction()
                .replace(R.id.address_flag_fragment, new StepAddressFlagFragment())
                .commit();

    }

    /*public void showSnackBar(int stringResId) {
        ViewPager mPager = (ViewPager) layout.findViewById(R.id.ms_stepPager);
        Snackbar.make(mPager, stringResId, Snackbar.LENGTH_LONG).show();

        //LinearLayout mTabsInnerContainer = (LinearLayout) layout.findViewById(R.id.ms_step_tab_container);
        //ViewPager stepPager = (ViewPager) layout.findViewById(R.id.ms_stepPager);
        //ViewGroup.LayoutParams params = mStepNavigation.getLayoutParams();

        *//*final View snackBarView = snackbar.getView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            snackBarView.setElevation(0);
        }
        snackBarView.setPadding(snackBarView.getPaddingLeft(),snackBarView.getPaddingTop(),snackBarView.getPaddingRight(),snackBarView.getPaddingBottom()+params.height);*//*
*//*        final FrameLayout.LayoutParams snack_params = (FrameLayout.LayoutParams) snackBarView.getLayoutParams();

        snack_params.setMargins(snack_params.leftMargin,
                snack_params.topMargin,
                snack_params.rightMargin,
                snack_params.bottomMargin + params.height);
        snackBarView.setLayoutParams(snack_params);*//*
        //snackbar.show();
    }*/

    public void nonMap() {
        map_fragment.setVisibility(View.GONE);
        address_flag_fragment.setVisibility(View.GONE);
        route_src_dst_fragment.setVisibility(View.GONE);
    }

    public void setState(AddRouteStates state) {
        switch (state) {
            case SelectDriverPassenger:
                nonMap();
                break;
            case SelectGoHomeState:
            case SelectOriginState:
                map_fragment.setVisibility(View.VISIBLE);
                address_flag_fragment.setVisibility(View.VISIBLE);
                route_src_dst_fragment.setVisibility(View.VISIBLE);
                RebuildSrcFragment();
                break;
            case SelectGoWorkState:
            case SelectDestinationState:
                map_fragment.setVisibility(View.VISIBLE);
                address_flag_fragment.setVisibility(View.VISIBLE);
                route_src_dst_fragment.setVisibility(View.VISIBLE);
                LocationPoint dst=new LocationPoint();
                if(getActivity() instanceof RouteStepActivity){
                    dst = ((RouteStepActivity) getActivity()).getDstLatLng();
                }
                if(getActivity() instanceof HomeWorkStepActivity){
                    dst = ((HomeWorkStepActivity) getActivity()).getDstLatLng();
                }
                if (dst.Lat != null) {
                    RebuildDstFragment(dst.Lat, dst.Lng);
                } else {
                    RebuildDstFragment();
                }
                break;
            case SelectTime:
                nonMap();
                break;
        }
    }

    public void RebuildSrcFragment() {
        final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.map_fragment, new AddMapFragment())
                .replace(R.id.route_src_dst_fragment, new StepSrcDstFragment())
                .replace(R.id.address_flag_fragment, new StepAddressFlagFragment())
                .addToBackStack(null)
                .commitAllowingStateLoss();
    }

    public void RebuildDstFragment() {
        final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.route_src_dst_fragment, new StepSrcDstFragment())
                .replace(R.id.address_flag_fragment, new StepAddressFlagFragment())
                .addToBackStack(null)
                .commitAllowingStateLoss();
        fragmentManager.executePendingTransactions();
        Fragment mapFragment = fragmentManager.findFragmentById(R.id.map_fragment);
        ((AddMapFragment) mapFragment).DestinationState();
    }

    public void RebuildDstFragment(String lat, String lng) {
        final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.route_src_dst_fragment, new StepSrcDstFragment())
                .replace(R.id.address_flag_fragment, new StepAddressFlagFragment())
                .addToBackStack(null)
                .commitAllowingStateLoss();
        fragmentManager.executePendingTransactions();
        Fragment mapFragment = fragmentManager.findFragmentById(R.id.map_fragment);
        ((AddMapFragment) mapFragment).DestinationState(lat, lng);
    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public void RebuildAddressFragment() {
        final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.address_flag_fragment, new StepAddressFlagFragment())
                .addToBackStack(null)
                .commitAllowingStateLoss();
        fragmentManager.executePendingTransactions();
    }

    public void setPrice(String pathPrice) {
        final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.route_src_dst_fragment);
        if (fragment instanceof StepSrcDstFragment) {
            ((StepSrcDstFragment) fragment).setPrice(pathPrice);
        }
    }

    public void waitingAddress() {
        final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.address_flag_fragment);
        if (fragment instanceof StepAddressFlagFragment) {
            ((StepAddressFlagFragment) fragment).waitingState();
        }
    }

    public void backStayButton() {
        final int currentStepPosition = mStepperLayout.getCurrentStepPosition();
        if (currentStepPosition > 0) {
            mStepperLayout.setCurrentStepPosition(currentStepPosition - 1);
        } else {
            Toaster.showLong(getActivity(),R.string.please_fill);
        }
    }
    public void backExitButton() {
        final int currentStepPosition = mStepperLayout.getCurrentStepPosition();
        if (currentStepPosition > 0) {
            mStepperLayout.setCurrentStepPosition(currentStepPosition - 1);
        } else {
            getActivity().finish();
        }
    }

    public void MoveMap(String lat, String lng) {
        final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.executePendingTransactions();
        Fragment mapFragment = fragmentManager.findFragmentById(R.id.map_fragment);
        ((AddMapFragment) mapFragment).MoveMap(lat, lng);
    }

    public RouteRequest getDateParams() {
        RouteRequest r=new RouteRequest();
        final FragmentManager fragmentManager = getFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag("android:switcher:" + R.id.ms_stepPager + ":"+ 3);
        if (fragment != null && fragment instanceof TimeStep) {
            r=((TimeStep) fragment).getDateParams();
        }
        return r;
    }

    public void setNextState() {
        mStepperLayout.setCurrentStepPosition(mStepperLayout.getCurrentStepPosition()+1);
    }
    public void setPreState() {
        mStepperLayout.setCurrentStepPosition(mStepperLayout.getCurrentStepPosition()-1);
    }


}