package com.mibarim.main.ui.fragments.rideRequestFragment;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import com.beardedhen.androidbootstrap.AwesomeTextView;
import com.mibarim.main.BootstrapApplication;
import com.mibarim.main.R;
import com.mibarim.main.models.RouteRequest;
import com.mibarim.main.models.enums.TimingOptions;
import com.mibarim.main.ui.fragments.InfoMessageFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Hamed on 3/5/2016.
 */
public class RideMainFragment extends Fragment implements View.OnTouchListener, TimePickerDialog.OnTimeSetListener {

    @Bind(R.id.do_done_btn)
    protected TextView do_done_btn;

    @Bind(R.id.home_editText)
    protected EditText home_editText;

    /*@Bind(R.id.home_editText)
    protected EditText home_editText;*/
    @Bind(R.id.work_editText)
    protected TextView work_editText;
    @Bind(R.id.fa_src)
    protected AwesomeTextView fa_src;
    @Bind(R.id.fa_dst)
    protected AwesomeTextView fa_dst;

    @Bind(R.id.home_work_time)
    protected EditText home_work_time;
    /*

    @Bind(R.id.home_work_tlabel)
    protected TextView home_work_tlabel;
*/

    @Bind(R.id.home_work_layout)
    protected LinearLayout home_work_layout;

    @Bind(R.id.weekly_txt)
    protected TextView weekly_txt;
    @Bind(R.id.weekly_chk_bx)
    protected CheckBox weekly_chk_bx;


    @Bind(R.id.message_fragment)
    protected FrameLayout message_fragment;

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

    private RelativeLayout layout;
    private TimePickerDialog timeDialog;
    private int currentDialog;
    int hour;
    int min;
    RouteRequest routeRequest;

    FragmentInterface mCallback;
    Calendar TheTime = null;
    Calendar TheReturnTime = null;

    public RideMainFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BootstrapApplication.component().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = (RelativeLayout) inflater.inflate(R.layout.fragment_main_ride, container, false);
        initScreen();
        return layout;
    }

    private void initScreen() {
        final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.message_fragment, new InfoMessageFragment())
                .commit();
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (FragmentInterface) activity;
        } catch (ClassCastException e) {
        }

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ButterKnife.bind(this, getView());

        fa_src.setFontAwesomeIcon(mCallback.getOriginIcon());
        fa_dst.setFontAwesomeIcon(mCallback.getDestinationIcon());

        message_fragment.setVisibility(View.GONE);
        routeRequest = mCallback.getRouteRequest();
        //home_work_time.setText(routeRequest.TheTimeString());
        if (routeRequest.SatDatetime != null) {
            setColors(sat);
            sat.setChecked(true);
        }
        if (routeRequest.SunDatetime != null) {
            setColors(sun);
            sun.setChecked(true);
        }
        if (routeRequest.MonDatetime != null) {
            setColors(mon);
            mon.setChecked(true);
        }
        if (routeRequest.TueDatetime != null) {
            setColors(tue);
            tue.setChecked(true);
        }
        if (routeRequest.WedDatetime != null) {
            setColors(wed);
            wed.setChecked(true);
        }
        if (routeRequest.ThuDatetime != null) {
            setColors(thu);
            thu.setChecked(true);
        }
        if (routeRequest.FriDatetime != null) {
            setColors(fri);
            fri.setChecked(true);
        }
        setAddresses();
        if (mCallback.isShowWeeklyChkBx())
            weekly_chk_bx.setVisibility(View.VISIBLE);
        else
            weekly_chk_bx.setVisibility(View.GONE);

        home_editText.setHint(mCallback.getOriginLabel());
        work_editText.setHint(mCallback.getDestinationLabel());
        home_work_time.setHint(mCallback.getTimeLabel());
        home_editText.setOnTouchListener(this);
        work_editText.setOnTouchListener(this);

        //work_editText.setOnTouchListener(this);
        //home_work_layout.setOnTouchListener(this);
        home_work_time.setOnTouchListener(this);
        //work_home_layout.setOnTouchListener(this);
        weekly_txt.setOnTouchListener(this);
        //weekly_chk_bx.setOnTouchListener(this);
        do_done_btn.setOnTouchListener(this);

        sat.setOnTouchListener(this);
        sun.setOnTouchListener(this);
        mon.setOnTouchListener(this);
        tue.setOnTouchListener(this);
        wed.setOnTouchListener(this);
        thu.setOnTouchListener(this);
        fri.setOnTouchListener(this);


        Calendar calendar = Calendar.getInstance();
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        min = calendar.get(Calendar.MINUTE);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            switch (v.getId()) {
                case R.id.weekly_txt:
                    weekly_chk_bx.toggle();
                    break;
                case R.id.do_done_btn:
                    routeRequest = getDateParams();
                    mCallback.Done(routeRequest);
                    break;
                case R.id.home_editText:
                    //case R.id.home_editText:
                    mCallback.gotoOriginAddMapActivity();
                    break;
                case R.id.work_editText:
                    //case R.id.work_editText:
                    mCallback.gotoDestinationAddMapActivity();
                    break;
                case R.id.home_work_time:
                    //case R.id.home_work_time:
                    timeDialog = new TimePickerDialog(getActivity(), this, hour, min, true);
                    timeDialog.show();
                    currentDialog = R.id.home_work_time;
                    break;
                case R.id.work_home_time:
                    //case R.id.home_work_time:
                    timeDialog = new TimePickerDialog(getActivity(), this, hour, min, true);
                    timeDialog.show();
                    currentDialog = R.id.work_home_time;
                    break;
                case R.id.sat:
                    setColors(sat);
                    break;
                case R.id.sun:
                    setColors(sun);
                    break;
                case R.id.mon:
                    setColors(mon);
                    break;
                case R.id.tue:
                    setColors(tue);
                    break;
                case R.id.wed:
                    setColors(wed);
                    break;
                case R.id.thu:
                    setColors(thu);
                    break;
                case R.id.fri:
                    setColors(fri);
                    break;

            }
            //for toggleButtons
            return false;
        }
        return false;
    }


    // Container Activity must implement this interface
    public interface FragmentInterface {

        public String getOriginLabel();

        public String getDestinationLabel();

        public String getTimeLabel();

        public RouteRequest getRouteRequest();

        public void gotoOriginAddMapActivity();

        public void gotoDestinationAddMapActivity();

        public void Done(RouteRequest routeRequest);

        public String getOriginAddress();

        public String getDestinationAddress();

        public Boolean isShowWeeklyChkBx();

        public Boolean isShowReturnChkBx();

        public String getOriginIcon();

        public String getDestinationIcon();
    }


    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar calendar = Calendar.getInstance();
        switch (currentDialog) {
            case R.id.home_work_time:
                home_work_time.setText(hourOfDay + ":" + minute);
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                TheTime = calendar;
                break;
        }
    }

    private void setColors(ToggleButton tb) {
        if (tb.isChecked()) {
            tb.setTextColor(getResources().getColor(R.color.week_time_text_off));
            tb.setBackgroundDrawable(getResources().getDrawable(R.drawable.border));
        } else {
            tb.setTextColor(getResources().getColor(R.color.week_time_text_on));
            tb.setBackgroundColor(getResources().getColor(R.color.week_time_on));
        }

    }

    public void setAddresses() {
        home_editText.setText(mCallback.getOriginAddress());
        work_editText.setText(mCallback.getDestinationAddress());
    }


    public RouteRequest getDateParams() {
        RouteRequest routeRequest = new RouteRequest();
        if (weekly_chk_bx.isChecked()) {
            routeRequest.TimingOption = TimingOptions.InWeek;
        } else {
            routeRequest.TimingOption = TimingOptions.Weekly;
        }
        routeRequest.TheTime = TheTime; //castStringToCal(home_work_time.getText().toString());
        routeRequest.TheReturnTime= TheReturnTime;
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

    private Calendar castStringToCal(String dateString) {
        Calendar cal = Calendar.getInstance();
        if (dateString == null || dateString.equals("")) {
            return null;
        }
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        try {
            Date date = format.parse(dateString);
            cal.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return cal;
    }

    public void showMsg(String msg) {
        final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.message_fragment);
        ((InfoMessageFragment) fragment).setActionBtn(getString(R.string.ok));
        ((InfoMessageFragment) fragment).setMsg(msg);
        message_fragment.setVisibility(View.VISIBLE);
    }
}
