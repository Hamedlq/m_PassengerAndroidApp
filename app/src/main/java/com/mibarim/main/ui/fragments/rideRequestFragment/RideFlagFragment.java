package com.mibarim.main.ui.fragments.rideRequestFragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mibarim.main.BootstrapApplication;
import com.mibarim.main.R;
import com.mibarim.main.models.enums.AddRouteStates;
import com.mibarim.main.ui.activities.AddMapActivity;
import com.mibarim.main.ui.activities.RideRequestMapActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Hamed on 3/4/2016.
 */
public class RideFlagFragment extends Fragment {


    @Bind(R.id.address_layout)
    protected RelativeLayout address_layout;

    @Bind(R.id.src_address_editText)
    protected TextView src_address_editText;
    @Bind(R.id.dst_address_editText)
    protected TextView dst_address_editText;

    @Bind(R.id.flag_layout)
    protected RelativeLayout flag_layout;

    @Bind(R.id.wait_layout)
    protected LinearLayout wait_layout;

    @Bind(R.id.src_mid_flag)
    protected TextView src_mid_flag;
    @Bind(R.id.dst_mid_flag)
    protected TextView dst_mid_flag;
    @Bind(R.id.home_mid_flag)
    protected TextView home_mid_flag;
    @Bind(R.id.work_mid_flag)
    protected TextView work_mid_flag;
    @Bind(R.id.src_dst_divide)
    protected View src_dst_divide;


    public RideFlagFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BootstrapApplication.component().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.fragment_ride_flag, container, false);
        return layout;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ButterKnife.bind(this, getView());
        RideRequestState();
        src_address_editText.setText(((RideRequestMapActivity) getActivity()).getSrcAddress());
        dst_address_editText.setText(((RideRequestMapActivity) getActivity()).getDstAddress());

        address_layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    ((RideRequestMapActivity) getActivity()).gotoLocationActivity();
                    return true;
                }
                return false;
            }
        });

    }

    private void RideRequestState() {
        wait_layout.setVisibility(View.GONE);
        flag_layout.setVisibility(View.VISIBLE);
        src_dst_divide.setVisibility(View.VISIBLE);
        dst_address_editText.setVisibility(View.VISIBLE);
        src_address_editText.setTypeface(null, Typeface.NORMAL);
        src_address_editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
        dst_address_editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
        dst_address_editText.setTypeface(null, Typeface.BOLD);
        InvisibleAllFlag();
        dst_mid_flag.setVisibility(View.VISIBLE);
    }

    private void InvisibleAllFlag(){
        src_mid_flag.setVisibility(View.GONE);
        dst_mid_flag.setVisibility(View.GONE);
        home_mid_flag.setVisibility(View.GONE);
        work_mid_flag.setVisibility(View.GONE);
    }

    public void waitingState() {
        InvisibleAllFlag();
        flag_layout.setVisibility(View.VISIBLE);
        wait_layout.setVisibility(View.VISIBLE);
    }

}
