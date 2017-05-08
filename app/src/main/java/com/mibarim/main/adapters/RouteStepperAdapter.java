package com.mibarim.main.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;

import com.mibarim.main.R;
import com.mibarim.main.ui.activities.RouteStepActivity;
import com.mibarim.main.ui.fragments.addRouteStepper.DestinationStep;
import com.mibarim.main.ui.fragments.addRouteStepper.DriverPassengerStep;
import com.mibarim.main.ui.fragments.addRouteStepper.OriginStep;
import com.mibarim.main.ui.fragments.addRouteStepper.TimeStep;
import com.mibarim.main.ui.fragments.addRouteStepper.WeekStep;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter;
import com.stepstone.stepper.viewmodel.StepViewModel;

/**
 * Adapter to display a list of traffic items
 */
public class RouteStepperAdapter extends AbstractFragmentStepAdapter {
    private Context _context;

    public RouteStepperAdapter(FragmentManager fm, Context context) {
        super(fm, context);
        _context=context;
    }

    @Override
    public Step createStep(int position) {

        switch (position) {
            case 0:
                DriverPassengerStep step = new DriverPassengerStep();
                Bundle b = new Bundle();
                b.putInt("CURRENT_STEP_POSITION_KEY", position);
                step.setArguments(b);
                return step;
            case 1:
                OriginStep step2 = new OriginStep();
                Bundle b2 = new Bundle();
                b2.putInt("CURRENT_STEP_POSITION_KEY", position);
                step2.setArguments(b2);
                return step2;
            case 2:
                DestinationStep step3 = new DestinationStep();
                Bundle b3 = new Bundle();
                b3.putInt("CURRENT_STEP_POSITION_KEY", position);
                step3.setArguments(b3);
                return step3;
            case 3:
                TimeStep step4 = new TimeStep();
                Bundle b4 = new Bundle();
                b4.putInt("CURRENT_STEP_POSITION_KEY", position);
                step4.setArguments(b4);
                return step4;
            default:
                DriverPassengerStep dstep = new DriverPassengerStep();
                Bundle db = new Bundle();
                db.putInt("CURRENT_STEP_POSITION_KEY", position);
                dstep.setArguments(db);
                return dstep;
        }
    }

    @Override
    public int getCount() {
        return 4;
    }



    @NonNull
    @Override
    public StepViewModel getViewModel(@IntRange(from = 0) int position) {
        //Override this method to set Step title for the Tabs, not necessary for other stepper types

        switch (position) {
            case 0:

                return new StepViewModel.Builder(context)
                        .setTitle(R.string.driver_passenger)
                        .setNextButtonLabel(R.string.origin)
                        .create();
            case 1:
                if(_context instanceof RouteStepActivity){
                    return new StepViewModel.Builder(context)
                            .setTitle(R.string.origin) //can be a CharSequence instead
                            .setNextButtonLabel(R.string.destination)
                            .setBackButtonLabel(R.string.driver_passenger)
                            .create();
                }else {
                    return new StepViewModel.Builder(context)
                            .setTitle(R.string.home_place) //can be a CharSequence instead
                            .setNextButtonLabel(R.string.destination)
                            .setBackButtonLabel(R.string.driver_passenger)
                            .create();
                }
            case 2:
                if(_context instanceof RouteStepActivity){
                    return new StepViewModel.Builder(context)
                            .setTitle(R.string.destination) //can be a CharSequence instead
                            .setNextButtonLabel(R.string.leave_time)
                            .setBackButtonLabel(R.string.origin)
                            .create();
                }else {
                    return new StepViewModel.Builder(context)
                            .setTitle(R.string.work_place) //can be a CharSequence instead
                            .setNextButtonLabel(R.string.leave_time)
                            .setBackButtonLabel(R.string.origin)
                            .create();
                }

            case 3:

                return new StepViewModel.Builder(context)
                        .setBackButtonLabel(R.string.destination)
                        .setTitle(R.string.leave_time) //can be a CharSequence instead
                        .create();
            default:

                return new StepViewModel.Builder(context)
                        .setTitle(R.string.driver_passenger) //can be a CharSequence instead
                        .create();
        }

    }
}