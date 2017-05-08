package com.mibarim.main.ui.fragments.addRouteFragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mibarim.main.BootstrapApplication;
import com.mibarim.main.R;
import com.mibarim.main.ui.activities.DriveActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Hamed on 3/5/2016.
 */
public class DriveFragment extends Fragment implements View.OnTouchListener {

    private RelativeLayout layout;
    /*
        @Bind(R.id.driver_checkBox)
        protected CheckBox driver_checkBox;
        @Bind(R.id.passenger_checkBox)
        protected CheckBox passenger_checkBox;*/
    @Bind(R.id.radio_passenger)
    protected RadioGroup radio_passenger;
    @Bind(R.id.radio_driver)
    protected RadioGroup radio_driver;
    @Bind(R.id.driver_rdo)
    protected RadioButton driver_rdo;
    @Bind(R.id.passenger_rdo)
    protected RadioButton passenger_rdo;
    @Bind(R.id.driver_desc_1)
    protected TextView driver_desc_1;
    @Bind(R.id.passenger_desc_1)
    protected TextView passenger_desc_1;
    @Bind(R.id.d_submit)
    protected AppCompatButton d_submit;

    public DriveFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BootstrapApplication.component().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = (RelativeLayout) inflater.inflate(R.layout.fragment_drive, container, false);
        return layout;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ButterKnife.bind(this, layout);
        SharedPreferences prefs = getActivity().getSharedPreferences(
                "com.mibarim.main", Context.MODE_PRIVATE);
        if (prefs.getBoolean("IsDriver", false)) {
            radio_passenger.clearCheck();
            driver_rdo.setChecked(true);
        } else {
            radio_driver.clearCheck();
            passenger_rdo.setChecked(true);
        }
        driver_desc_1.setOnTouchListener(this);
        passenger_desc_1.setOnTouchListener(this);
        d_submit.setOnTouchListener(this);
        driver_rdo.setOnCheckedChangeListener(listener1);
        passenger_rdo.setOnCheckedChangeListener(listener1);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            SharedPreferences prefs = getActivity().getSharedPreferences(
                    "com.mibarim.main", Context.MODE_PRIVATE);
            switch (v.getId()) {
                case R.id.driver_desc_1:
                    radio_passenger.clearCheck();
                    driver_rdo.setChecked(true);
                    //((DriveActivity)getActivity()).done();
                    break;
                case R.id.passenger_desc_1:
                    radio_driver.clearCheck();
                    passenger_rdo.setChecked(true);
                    //((DriveActivity)getActivity()).done();
                    break;
                case R.id.d_submit:

                    int selectedId = radio_driver.getCheckedRadioButtonId();
                    if (selectedId == R.id.driver_rdo) {
                        prefs.edit().putBoolean("IsDriver", true).apply();
                    }
                    selectedId = radio_passenger.getCheckedRadioButtonId();
                    if (selectedId == R.id.passenger_rdo) {
                        prefs.edit().putBoolean("IsDriver", false).apply();
                    }
                    ((DriveActivity) getActivity()).done();
                    break;
            }
            return false;
        }
        return false;
    }

    private CompoundButton.OnCheckedChangeListener listener1 = new CompoundButton.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                if(buttonView.getId()==R.id.driver_rdo){
                    radio_passenger.clearCheck();
                    driver_rdo.setChecked(true);
                }
                if(buttonView.getId()==R.id.passenger_rdo){
                    radio_driver.clearCheck();
                    passenger_rdo.setChecked(true);
                }
            }
        }
    };



}
