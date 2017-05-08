package com.mibarim.main.ui.fragments.addRouteStepper;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mibarim.main.R;
import com.mibarim.main.models.enums.AddRouteStates;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.mibarim.main.models.enums.AddRouteStates.SelectDriverPassenger;

/**
 * Created by Hamed on 3/5/2016.
 */
public class DriverPassengerStep extends Fragment implements Step, View.OnTouchListener {
    private RelativeLayout layout;
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

    DriverPassFragmentListener mCallback;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = (RelativeLayout) inflater.inflate(R.layout.step_drive_pass, container, false);
        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ButterKnife.bind(this, layout);
        SharedPreferences prefs = getActivity().getSharedPreferences(
                "com.mibarim.main", Context.MODE_PRIVATE);
        driver_rdo.setOnCheckedChangeListener(null);
        passenger_rdo.setOnCheckedChangeListener(null);
        if (prefs.getBoolean("IsDriver", false)) {
            radio_passenger.clearCheck();
            driver_rdo.setChecked(true);
        } else {
            radio_driver.clearCheck();
            passenger_rdo.setChecked(true);
        }
        driver_desc_1.setOnTouchListener(this);
        passenger_desc_1.setOnTouchListener(this);
        driver_rdo.setOnCheckedChangeListener(listener1);
        passenger_rdo.setOnCheckedChangeListener(listener1);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {

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
            }
            return false;
        }
        return false;
    }


    private CompoundButton.OnCheckedChangeListener listener1 = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                SharedPreferences prefs = getActivity().getSharedPreferences(
                        "com.mibarim.main", Context.MODE_PRIVATE);
                if (buttonView.getId() == R.id.driver_rdo) {
                    radio_passenger.clearCheck();
                    driver_rdo.setChecked(true);
                    if(!prefs.getBoolean("IsDriver", false)){
                        mCallback.doBtnClicked();
                    }
                    prefs.edit().putBoolean("IsDriver", true).apply();
                    //mCallback.showSnackBar(R.string.driver_label);
                    //Snackbar.make(layout, R.string.driver_passenger, Snackbar.LENGTH_LONG).show();
                }
                if (buttonView.getId() == R.id.passenger_rdo) {
                    radio_driver.clearCheck();
                    passenger_rdo.setChecked(true);
                    if(prefs.getBoolean("IsDriver", false)){
                        mCallback.doBtnClicked();
                    }
                    prefs.edit().putBoolean("IsDriver", false).apply();
                    //mCallback.showSnackBar(R.string.passenger_label);
                    //Snackbar.make(layout, R.string.driver_passenger, Snackbar.LENGTH_LONG).show();
                }
            }
        }
    };


    @Override
    public VerificationError verifyStep() {
        //return null if the user can go to the next step, create a new VerificationError instance otherwise
        return null;
    }

    @Override
    public void onSelected() {
        mCallback.setRouteStates(SelectDriverPassenger);
    }

    @Override
    public void onError(@NonNull VerificationError error) {
        //handle error inside of the fragment, e.g. show error on EditText
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (DriverPassFragmentListener) activity;
        } catch (ClassCastException e) {
        }

    }

    public interface DriverPassFragmentListener {
        //public void showSnackBar(int resId);
        public void setRouteStates(AddRouteStates state);

        public void doBtnClicked();
    }

}