package com.mibarim.main.ui.fragments.routeFragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.mibarim.main.BootstrapApplication;
import com.mibarim.main.R;
import com.mibarim.main.models.Route.RouteResponse;
import com.mibarim.main.ui.activities.CheckSuggestRouteActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Hamed on 3/5/2016.
 */
public class CheckButtonsFragment extends Fragment {

    private LinearLayout layout;

    public CheckButtonsFragment() {
    }

    @Bind(R.id.appointment)
    protected BootstrapButton appointment;
/*
    @Bind(R.id.reject)
    protected BootstrapButton reject;
*/


    private RouteResponse routeResponse;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BootstrapApplication.component().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = (LinearLayout) inflater.inflate(R.layout.fragment_check_buttons, container, false);
        return layout;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ButterKnife.bind(this, getView());
        /*reject.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    ((CheckSuggestRouteActivity) getActivity()).rejectRouteSuggestion();
                    return true;
                }
                return false;
            }
        });*/
        appointment.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    ((CheckSuggestRouteActivity) getActivity()).gotoMessaging();
                    return true;
                }
                return false;
            }
        });


    }

}
