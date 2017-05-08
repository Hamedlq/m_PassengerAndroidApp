package com.mibarim.main.ui.fragments.userInfoFragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.mibarim.main.BootstrapApplication;
import com.mibarim.main.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import ernestoyaquello.com.verticalstepperform.VerticalStepperFormLayout;
import ernestoyaquello.com.verticalstepperform.interfaces.VerticalStepperForm;

/**
 * Created by Hamed on 3/5/2016.
 */
public class VerifyStepperFragment extends Fragment {

    private RelativeLayout layout;
    private Tracker mTracker;
    VerticalStepperForm vform;

    @Bind(R.id.vertical_stepper_form)
    protected VerticalStepperFormLayout vertical_stepper_form;




    public VerifyStepperFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BootstrapApplication.component().inject(this);
        // Obtain the shared Tracker instance.
        BootstrapApplication application = (BootstrapApplication) getActivity().getApplication();
        mTracker = application.getDefaultTracker();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = (RelativeLayout) inflater.inflate(R.layout.fragment_verify_stepper, container, false);

        //initScreen();
        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ButterKnife.bind(this, getView());

        String[] mySteps = {"کارت ملی", "کارت ماشین", "گواهینامه"};
        String[] myStepsSubtitles = {"تایید شده", "ثبت شده", ""};
        int colorPrimary = ContextCompat.getColor(getActivity().getApplicationContext(), R.color.colorPrimary);
        int colorPrimaryDark = ContextCompat.getColor(getActivity().getApplicationContext(), R.color.colorPrimaryDark);
        //vertical_stepper_form.setNextButtonText("حامد");
        // Finding the view
        //verticalStepperForm = (VerticalStepperFormLayout) findViewById(R.id.vertical_stepper_form);

        // Setting up and initializing the form
        VerticalStepperFormLayout.Builder.newInstance(vertical_stepper_form, mySteps, vform, getActivity())
                .primaryColor(colorPrimary)
                .primaryDarkColor(colorPrimaryDark)
                .stepsSubtitles(myStepsSubtitles)
                .displayBottomNavigation(false) // It is true by default, so in this case this line is not necessary
                .init();


        mTracker.setScreenName("VerifyStepperFragment");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        mTracker.send(new HitBuilders.EventBuilder().setCategory("Fragment").setAction("VerifyStepperFragment").build());
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            vform = (VerticalStepperForm) activity;
        } catch (ClassCastException e) {
        }

    }

    public void setActiveStepAsCompleted() {
        vertical_stepper_form.setActiveStepAsCompleted();
    }

    public void setStepAsCompleted(int i) {
        vertical_stepper_form.setStepAsCompleted(i);
    }
}
