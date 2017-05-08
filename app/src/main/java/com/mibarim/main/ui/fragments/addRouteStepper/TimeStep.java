package com.mibarim.main.ui.fragments.addRouteStepper;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.mibarim.main.R;
import com.mibarim.main.models.RouteRequest;
import com.mibarim.main.models.enums.AddRouteStates;
import com.mibarim.main.models.enums.TimingOptions;
import com.mibarim.main.ui.activities.HomeWorkStepActivity;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.mibarim.main.R.id.return_checkBox;
import static com.mibarim.main.R.id.switch_for_trip_state;
import static com.mibarim.main.R.id.weekly_chk_bx;
import static com.mibarim.main.models.enums.AddRouteStates.SelectDriverPassenger;

/**
 * Created by Hamed on 3/5/2016.
 */
public class TimeStep extends Fragment implements Step, View.OnTouchListener {

    private RelativeLayout layout;
    String[] hours;
    String[] allhours;
    String[] minutes;

    @Bind(R.id.info_text)
    protected TextView info_text;
    @Bind(R.id.minute_picker)
    protected NumberPicker minute_picker;
    @Bind(R.id.hour_picker)
    protected NumberPicker hour_picker;
    @Bind(R.id.minute_up)
    protected LinearLayout minute_up;
    @Bind(R.id.minute_down)
    protected LinearLayout minute_down;
    @Bind(R.id.hour_up)
    protected LinearLayout hour_up;
    @Bind(R.id.hour_down)
    protected LinearLayout hour_down;
    @Bind(R.id.sat)
    protected ToggleButton sat;
    @Bind(R.id.sun)
    protected ToggleButton sun;
    @Bind(R.id.mon)
    protected ToggleButton mon;
    @Bind(R.id.tue)
    protected ToggleButton tue;
    @Bind(R.id.wed)
    protected ToggleButton wed;
    @Bind(R.id.thu)
    protected ToggleButton thu;
    @Bind(R.id.fri)
    protected ToggleButton fri;
    @Bind(R.id.do_submit)
    protected AppCompatButton do_submit;

    TimeStepFragmentListener mCallback;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = (RelativeLayout) inflater.inflate(R.layout.step_time, container, false);
        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ButterKnife.bind(this, layout);
        info_text.setText(mCallback.getInfoText());
        hours = new String[]{"5", "6", "7", "8", "9"};
        allhours = new String[]{"5", "6", "7", "8", "9","10","11","12","13","14","15","16","17","18","19","20","21","22"};
        hour_picker.setMinValue(1);
        if(getActivity() instanceof HomeWorkStepActivity){
            hour_picker.setMaxValue(5);
            hour_picker.setDisplayedValues(hours);
        }else {
            hour_picker.setMaxValue(18);
            hour_picker.setDisplayedValues(allhours);
        }
        hour_picker.setWrapSelectorWheel(false);
        hour_picker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        minutes = new String[]{"00", "15", "30", "45"};
        minute_picker.setMinValue(1);
        minute_picker.setMaxValue(4);
        minute_picker.setDisplayedValues(minutes);
        minute_picker.setWrapSelectorWheel(true);
        minute_picker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        minute_down.setOnTouchListener(this);
        minute_up.setOnTouchListener(this);
        hour_up.setOnTouchListener(this);
        hour_down.setOnTouchListener(this);
        sat.setOnTouchListener(this);
        sun.setOnTouchListener(this);
        mon.setOnTouchListener(this);
        tue.setOnTouchListener(this);
        wed.setOnTouchListener(this);
        thu.setOnTouchListener(this);
        fri.setOnTouchListener(this);
        do_submit.setOnTouchListener(this);
    }


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
            mCallback = (TimeStepFragmentListener) activity;
        } catch (ClassCastException e) {
        }

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        boolean returnVal = false;
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int val = 0;
            switch (v.getId()) {
                case R.id.do_submit:
                    mCallback.saveRoute();
                    break;
                case R.id.minute_up:
                    val = minute_picker.getValue();
                    /*if(minutes.length>val){*/
                    minute_picker.setValue(val + 1);
                    //}
                    returnVal = true;
                    break;
                case R.id.minute_down:
                    val = minute_picker.getValue();
                    /*if(val>1){*/
                    minute_picker.setValue(val - 1);
                    //}
                    returnVal = true;
                    break;
                case R.id.hour_up:
                    val = hour_picker.getValue();
                    /*if(hours.length>val){*/
                    hour_picker.setValue(val + 1);
                    //}
                    returnVal = true;
                    break;
                case R.id.hour_down:
                    val = hour_picker.getValue();
                    /*if(val>1){*/
                    hour_picker.setValue(val - 1);
                    //}
                    returnVal = true;
                    break;
                case R.id.sat:
                    setColors(sat);
                    returnVal = false;
                    break;
                case R.id.sun:
                    setColors(sun);
                    returnVal = false;
                    break;
                case R.id.mon:
                    setColors(mon);
                    returnVal = false;
                    break;
                case R.id.tue:
                    setColors(tue);
                    returnVal = false;
                    break;
                case R.id.wed:
                    setColors(wed);
                    returnVal = false;
                    break;
                case R.id.thu:
                    setColors(thu);
                    returnVal = false;
                    break;
                case R.id.fri:
                    setColors(fri);
                    returnVal = false;
                    break;
            }
            return returnVal;
        }
        return returnVal;
    }

    private void setColors(ToggleButton tb) {
        if (tb.isChecked()) {
            tb.setTextColor(getResources().getColor(R.color.week_time_text_off));
            tb.setBackgroundDrawable(getResources().getDrawable(R.drawable.roundedwhitebutton));
        } else {
            tb.setTextColor(getResources().getColor(R.color.week_time_text_on));
            tb.setBackgroundDrawable(getResources().getDrawable(R.drawable.roundedbutton));
        }
    }

    public RouteRequest getDateParams() {
        RouteRequest routeRequest = new RouteRequest();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, getHour());
        calendar.set(Calendar.MINUTE, getMinute());
        Calendar TheTime = calendar;
        routeRequest.TimingOption = TimingOptions.Weekly;
        /*if (weekly_chk_bx.isChecked()) {
            routeRequest.TimingOption = TimingOptions.InWeek;
        } else {
            routeRequest.TimingOption = TimingOptions.Weekly;
        }
        if(return_checkBox.isChecked()){
            routeRequest.IsReturn=true;
        }else {*/
        routeRequest.IsReturn = false;
        //}
        routeRequest.TheTime = TheTime; //castStringToCal(home_work_time.getText().toString());
        //routeRequest.TheReturnTime= TheReturnTime;
        if (sat.isChecked()) {
            routeRequest.SatDatetime = TheTime;//castStringToCal(home_work_time.getText().toString());
        }
        if (sun.isChecked()) {
            routeRequest.SunDatetime = TheTime;// castStringToCal(home_work_time.getText().toString());
        }
        if (mon.isChecked()) {
            routeRequest.MonDatetime = TheTime;//castStringToCal(home_work_time.getText().toString());
        }
        if (tue.isChecked()) {
            routeRequest.TueDatetime = TheTime;//castStringToCal(home_work_time.getText().toString());
        }
        if (wed.isChecked()) {
            routeRequest.WedDatetime = TheTime;// castStringToCal(home_work_time.getText().toString());
        }
        if (thu.isChecked()) {
            routeRequest.ThuDatetime = TheTime;//castStringToCal(home_work_time.getText().toString());
        }
        if (fri.isChecked()) {
            routeRequest.FriDatetime = TheTime;//castStringToCal(home_work_time.getText().toString());
        }
        return routeRequest;
    }

    private int getHour() {
        int hour=5;
        switch (hour_picker.getValue()){
            case 1:
                hour=5;
                break;
            case 2:
                hour=6;
                break;
            case 3:
                hour=7;
                break;
            case 4:
                hour=8;
                break;
            case 5:
                hour=9;
                break;
            case 6:
                hour=10;
                break;
            case 7:
                hour=11;
                break;
            case 8:
                hour=12;
                break;
            case 9:
                hour=13;
                break;
            case 10:
                hour=14;
                break;
            case 11:
                hour=15;
                break;
            case 12:
                hour=16;
                break;
            case 13:
                hour=17;
                break;
            case 14:
                hour=18;
                break;
            case 15:
                hour=19;
                break;
            case 16:
                hour=20;
                break;
            case 17:
                hour=21;
                break;
            case 18:
                hour=22;
                break;
            case 19:
                hour=23;
                break;
        }
        return hour;
    }

    private int getMinute() {
        int minute=0;
        switch (minute_picker.getValue()){
            case 1:
                minute=0;
                break;
            case 2:
                minute=15;
                break;
            case 3:
                minute=30;
                break;
            case 4:
                minute=45;
                break;
        }
        return minute;
    }

    public interface TimeStepFragmentListener {

        public void setRouteStates(AddRouteStates state);

        public String getInfoText();

        public void saveRoute();

    }


}