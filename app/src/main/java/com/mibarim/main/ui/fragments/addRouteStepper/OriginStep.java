package com.mibarim.main.ui.fragments.addRouteStepper;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import com.mibarim.main.R;
import com.mibarim.main.ui.activities.HomeWorkStepActivity;
import com.mibarim.main.ui.activities.RouteStepActivity;
import com.mibarim.main.ui.fragments.addRouteFragments.AddMapFragment;
import com.mibarim.main.ui.fragments.addRouteFragments.AddressFlagFragment;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import butterknife.ButterKnife;

import static com.mibarim.main.models.enums.AddRouteStates.SelectDriverPassenger;
import static com.mibarim.main.models.enums.AddRouteStates.SelectGoHomeState;
import static com.mibarim.main.models.enums.AddRouteStates.SelectOriginState;

/**
 * Created by Hamed on 3/5/2016.
 */
public class OriginStep extends Fragment implements Step {
    private RelativeLayout layout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = (RelativeLayout) inflater.inflate(R.layout.fragment_empty, container, false);
        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event){
                return false;
            }
        });
        return layout;
    }


    @Override
    public VerificationError verifyStep() {
        //return null if the user can go to the next step, create a new VerificationError instance otherwise
        return null;
    }

    @Override
    public void onSelected() {
        if(getActivity()instanceof RouteStepActivity){
            ((RouteStepActivity)getActivity()).setRouteStates(SelectOriginState);
        }else if(getActivity()instanceof HomeWorkStepActivity){
            ((HomeWorkStepActivity)getActivity()).setRouteStates(SelectGoHomeState);
        }

        //update UI when selected
    }

    @Override
    public void onError(@NonNull VerificationError error) {
        //handle error inside of the fragment, e.g. show error on EditText
    }



}
