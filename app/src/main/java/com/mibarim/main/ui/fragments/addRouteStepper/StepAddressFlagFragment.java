package com.mibarim.main.ui.fragments.addRouteStepper;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
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
import com.mibarim.main.models.enums.AddRouteStates;
import com.mibarim.main.ui.fragments.addRouteFragments.AddressFlagFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Hamed on 3/4/2016.
 */
public class StepAddressFlagFragment extends Fragment {


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
    @Bind(R.id.flagImage)
    protected ImageView flagImage;

    AddressFlagFragmentListener mCallback;


    public StepAddressFlagFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BootstrapApplication.component().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.step_address_flag, container, false);
        return layout;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ButterKnife.bind(this, getView());
        if (mCallback.getRouteStates() == AddRouteStates.SelectOriginState) {
            sourceState();
            src_address_editText.setText(mCallback.getSrcAddress());
        } else if (mCallback.getRouteStates() == AddRouteStates.SelectDestinationState) {
            destinationState();
            src_address_editText.setText(mCallback.getSrcAddress());
            dst_address_editText.setText(mCallback.getDstAddress());
        } else if (mCallback.getRouteStates() == AddRouteStates.SelectEventOriginState) {
            eventSourceState();
            src_address_editText.setText(mCallback.getSrcAddress());
            dst_address_editText.setText(mCallback.getEventAddress());
        } else if (mCallback.getRouteStates() == AddRouteStates.SelectEventDestinationState) {
            destinationState();
            src_address_editText.setText(mCallback.getEventAddress());
            dst_address_editText.setText(mCallback.getDstAddress());
        } else if (mCallback.getRouteStates() == AddRouteStates.SelectGoHomeState) {
            HomeState();
            src_address_editText.setText(mCallback.getSrcAddress());
        } else if (mCallback.getRouteStates() == AddRouteStates.SelectGoWorkState) {
            WorkState();
            src_address_editText.setText(mCallback.getSrcAddress());
            dst_address_editText.setText(mCallback.getDstAddress());
        } else if (mCallback.getRouteStates() == AddRouteStates.SelectReturnWorkState) {
            ReturnWorkState();
            src_address_editText.setText(mCallback.getSrcAddress());
        } else if (mCallback.getRouteStates() == AddRouteStates.SelectReturnHomeState) {
            ReturnHomeState();
            src_address_editText.setText(mCallback.getSrcAddress());
            dst_address_editText.setText(mCallback.getDstAddress());
        } else if (mCallback.getRouteStates() == AddRouteStates.SelectDriveRouteState) {
            DriveRouteState();
        }
        address_layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    mCallback.gotoLocationActivity();
                    return true;
                }
                return false;
            }
        });
        flagImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    mCallback.doBtnClicked();
                    return true;
                }
                return false;
            }
        });

    }

    private void ReturnHomeState() {
        wait_layout.setVisibility(View.GONE);
        flag_layout.setVisibility(View.VISIBLE);
        src_dst_divide.setVisibility(View.VISIBLE);
        dst_address_editText.setVisibility(View.VISIBLE);
        src_address_editText.setTypeface(null, Typeface.NORMAL);
        src_address_editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
        dst_address_editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
        dst_address_editText.setTypeface(null, Typeface.BOLD);
        InvisibleAllFlag();
        home_mid_flag.setVisibility(View.VISIBLE);
    }

    private void ReturnWorkState() {
        wait_layout.setVisibility(View.GONE);
        flag_layout.setVisibility(View.VISIBLE);
        src_dst_divide.setVisibility(View.GONE);
        dst_address_editText.setVisibility(View.GONE);
        src_address_editText.setTypeface(null, Typeface.BOLD);
        src_address_editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
        InvisibleAllFlag();
        work_mid_flag.setVisibility(View.VISIBLE);
    }

    private void DriveRouteState() {
        flag_layout.setVisibility(View.GONE);
        wait_layout.setVisibility(View.GONE);
        address_layout.setVisibility(View.GONE);
    }

    private void InvisibleAllFlag() {
        src_mid_flag.setVisibility(View.GONE);
        dst_mid_flag.setVisibility(View.GONE);
        home_mid_flag.setVisibility(View.GONE);
        work_mid_flag.setVisibility(View.GONE);
    }

    private void HomeState() {
        wait_layout.setVisibility(View.GONE);
        flag_layout.setVisibility(View.VISIBLE);
        src_dst_divide.setVisibility(View.GONE);
        dst_address_editText.setVisibility(View.GONE);
        src_address_editText.setTypeface(null, Typeface.BOLD);
        src_address_editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
        InvisibleAllFlag();
        home_mid_flag.setVisibility(View.VISIBLE);
    }

    private void WorkState() {
        wait_layout.setVisibility(View.GONE);
        flag_layout.setVisibility(View.VISIBLE);
        src_dst_divide.setVisibility(View.VISIBLE);
        dst_address_editText.setVisibility(View.VISIBLE);
        src_address_editText.setTypeface(null, Typeface.NORMAL);
        src_address_editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
        dst_address_editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
        dst_address_editText.setTypeface(null, Typeface.BOLD);
        InvisibleAllFlag();
        work_mid_flag.setVisibility(View.VISIBLE);

    }

    public void waitingState() {
        InvisibleAllFlag();
        flag_layout.setVisibility(View.VISIBLE);
        wait_layout.setVisibility(View.VISIBLE);
    }

    private void requestingState() {
        flag_layout.setVisibility(View.GONE);
        wait_layout.setVisibility(View.GONE);
    }

    private void sourceState() {
        wait_layout.setVisibility(View.GONE);
        flag_layout.setVisibility(View.VISIBLE);
        src_dst_divide.setVisibility(View.GONE);
        dst_address_editText.setVisibility(View.GONE);
        src_address_editText.setTypeface(null, Typeface.BOLD);
        src_address_editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
        InvisibleAllFlag();
        src_mid_flag.setVisibility(View.VISIBLE);
    }

    private void destinationState() {
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

    private void eventSourceState() {
        wait_layout.setVisibility(View.GONE);
        flag_layout.setVisibility(View.VISIBLE);
        src_dst_divide.setVisibility(View.VISIBLE);
        dst_address_editText.setVisibility(View.VISIBLE);
        src_address_editText.setTypeface(null, Typeface.BOLD);
        src_address_editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
        dst_address_editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
        dst_address_editText.setTypeface(null, Typeface.NORMAL);
        InvisibleAllFlag();
        src_mid_flag.setVisibility(View.VISIBLE);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (AddressFlagFragmentListener) activity;
        } catch (ClassCastException e) {
        }

    }
    public interface AddressFlagFragmentListener {
        public AddRouteStates getRouteStates();

        public String getSrcAddress();

        public String getDstAddress();

        public String getEventAddress();

        public void gotoLocationActivity();

        public void doBtnClicked();

    }
}
