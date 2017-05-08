package com.mibarim.main.ui.fragments.addRouteStepper;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.mibarim.main.R;
import com.mibarim.main.ui.activities.RouteStepActivity;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import static com.mibarim.main.models.enums.AddRouteStates.SelectGoWorkState;
import static com.mibarim.main.models.enums.AddRouteStates.SelectWeek;

/**
 * Created by Hamed on 3/5/2016.
 */
public class WeekStep extends Fragment implements Step {
    private RelativeLayout layout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = (RelativeLayout) inflater.inflate(R.layout.step_week, container, false);
        return layout;
    }


    @Override
    public VerificationError verifyStep() {
        //return null if the user can go to the next step, create a new VerificationError instance otherwise
        return null;
    }

    @Override
    public void onSelected() {
        ((RouteStepActivity)getActivity()).setRouteStates(SelectWeek);
        //update UI when selected
    }

    @Override
    public void onError(@NonNull VerificationError error) {
        //handle error inside of the fragment, e.g. show error on EditText
    }


}
