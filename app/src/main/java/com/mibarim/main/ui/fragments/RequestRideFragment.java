package com.mibarim.main.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.mibarim.main.BootstrapApplication;
import com.mibarim.main.R;
import com.mibarim.main.models.LocalRoute;
import com.mibarim.main.models.enums.LocalRouteTypes;
import com.mibarim.main.ui.activities.MainActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Hamed on 3/4/2016.
 */
public class RequestRideFragment extends Fragment {

    private Context context;
    private RelativeLayout layout;

    @Bind(R.id.ride_info_layout)
    protected LinearLayout ride_info_layout;
    @Bind(R.id.info_loading)
    protected ProgressBar info_loading;
    @Bind(R.id.ride_btn_layout)
    protected LinearLayout ride_btn_layout;
    @Bind(R.id.request_ride_share)
    protected BootstrapButton request_ride_share;
    /*@Bind(R.id.username)
    protected TextView username;*/
    @Bind(R.id.timing)
    protected TextView timing;
    private Animation slideUp;
    private Animation slideDown;

    public RequestRideFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BootstrapApplication.component().inject(this);
        context = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = (RelativeLayout) inflater.inflate(R.layout.fragment_request_ride, container, false);
        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ButterKnife.bind(this, getView());
        slideUp = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_up_hide);
        slideDown = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_down_show);
        //layout.setAnimation(slideDown);
        layout.setVisibility(View.INVISIBLE);
        showSelect(false);

        request_ride_share.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    layout.startAnimation(slideUp);
                    ((MainActivity) getActivity()).gotoRideRequestActivity();
                    return true;
                }
                return false;
            }
        });
        /*update_btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    ((MainActivity) getActivity()).gotoUpdate();
                    return true;
                }
                return false;
            }
        });*/

    }

    private void showSelect(boolean selector){
        if(selector){
            ride_info_layout.setVisibility(View.VISIBLE);
            ride_btn_layout.setVisibility(View.VISIBLE);
            info_loading.setVisibility(View.GONE);
            //layout.startAnimation(slideUp);
        }else {
            ride_info_layout.setVisibility(View.GONE);
            ride_btn_layout.setVisibility(View.GONE);
            info_loading.setVisibility(View.VISIBLE);
            //layout.startAnimation(slideDown);
        }
    }


    public void ShowHeadBar() {
        layout.setVisibility(View.VISIBLE);
        showSelect(false);
        layout.startAnimation(slideDown);
    }

    public void ShowRouteInfo(LocalRoute localRoute) {
        //username.setText(localRoute.Name + " " + localRoute.Family);
        timing.setText(localRoute.RouteStartTime);
        if (localRoute.LocalRouteType == LocalRouteTypes.Driver &&localRoute.PathRoute.path.size()>0) {
            request_ride_share.setVisibility(View.VISIBLE);
        }else {
            request_ride_share.setVisibility(View.GONE);
        }
        showSelect(true);
    }

    public void HideHeadBar() {
        layout.setVisibility(View.GONE);
    }
}
