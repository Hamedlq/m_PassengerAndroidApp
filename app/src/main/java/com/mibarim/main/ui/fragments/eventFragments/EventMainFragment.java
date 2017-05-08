package com.mibarim.main.ui.fragments.eventFragments;

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
import com.mibarim.main.models.EventResponse;
import com.mibarim.main.models.RouteRequest;
import com.mibarim.main.models.enums.TimingOptions;
import com.mibarim.main.ui.activities.EventMapActivity;
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
public class EventMainFragment extends Fragment implements View.OnTouchListener {

    @Bind(R.id.do_done_btn)
    protected TextView do_done_btn;

    @Bind(R.id.home_textView)
    protected EditText home_textView;
    @Bind(R.id.end_time_textView)
    protected TextView end_time_textView;
    @Bind(R.id.start_time_textView)
    protected TextView start_time_textView;
    /*@Bind(R.id.home_editText)
    protected EditText home_editText;*/
    @Bind(R.id.work_textView)
    protected EditText work_textView;
    @Bind(R.id.fa_src)
    protected AwesomeTextView fa_src;
    @Bind(R.id.fa_dst)
    protected AwesomeTextView fa_dst;
    @Bind(R.id.fa_driver_pass)
    protected AwesomeTextView fa_driver_pass;

    @Bind(R.id.driver_pass)
    protected EditText driver_pass;
    /*

    @Bind(R.id.home_work_tlabel)
    protected TextView home_work_tlabel;
*/

    @Bind(R.id.desc_title)
    protected TextView desc_title;


    @Bind(R.id.message_fragment)
    protected FrameLayout message_fragment;


    private RelativeLayout layout;
    RouteRequest routeRequest;
    EventResponse eventResponse;

    FragmentInterface mCallback;
    Calendar TheTime = null;

    public EventMainFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BootstrapApplication.component().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = (RelativeLayout) inflater.inflate(R.layout.fragment_main_event, container, false);
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
        if (getActivity() instanceof EventMapActivity) {
            eventResponse = ((EventMapActivity) getActivity()).getEventResponse();
            start_time_textView.setText(eventResponse.StartTimeString);
            end_time_textView.setText(eventResponse.EndTimeString);
        }
        fa_src.setFontAwesomeIcon(mCallback.getOriginIcon());
        fa_dst.setFontAwesomeIcon(mCallback.getDestinationIcon());
        fa_driver_pass.setFontAwesomeIcon(mCallback.getDriverPassIcon());

        message_fragment.setVisibility(View.GONE);
        routeRequest = mCallback.getRouteRequest();
        setAddresses();
        desc_title.setText(mCallback.getTitleDescription());
        driver_pass.setText(mCallback.getDriverPassenger());

        home_textView.setHint(mCallback.getOriginLabel());
        work_textView.setHint(mCallback.getDestinationLabel());
        home_textView.setOnTouchListener(this);
        driver_pass.setOnTouchListener(this);
        work_textView.setOnTouchListener(this);
        //work_editText.setOnTouchListener(this);
        //weekly_txt.setOnTouchListener(this);
        //weekly_chk_bx.setOnTouchListener(this);
        do_done_btn.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            switch (v.getId()) {
                case R.id.weekly_txt:

                    break;
                case R.id.do_done_btn:
                    //routeRequest = getDateParams();
                    mCallback.Done(routeRequest);
                    break;
                case R.id.home_textView:
                    //case R.id.home_editText:
                    mCallback.gotoOriginAddMapActivity();
                    break;
                case R.id.work_textView:
                    //case R.id.work_editText:
                    mCallback.gotoDestinationAddMapActivity();
                    break;
                case R.id.driver_pass:
                    mCallback.gotoDriverActivity();
                    break;

            }
            //for toggleButtons
            return false;
        }
        return false;
    }


    // Container Activity must implement this interface
    public interface FragmentInterface {
        public String getTitleDescription();

        public String getOriginLabel();

        public String getDestinationLabel();

        public RouteRequest getRouteRequest();

        public void gotoOriginAddMapActivity();

        public void gotoDestinationAddMapActivity();

        public void gotoDriverActivity();

        public void Done(RouteRequest routeRequest);

        public String getOriginAddress();

        public String getDriverPassenger();

        public String getDestinationAddress();

        public Boolean isShowWeeklyChkBx();

        public String getDriverPassIcon();

        public String getOriginIcon();

        public String getDestinationIcon();
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
        home_textView.setText(mCallback.getOriginAddress());
        work_textView.setText(mCallback.getDestinationAddress());
    }

    public void setDriver() {
        driver_pass.setText(mCallback.getDriverPassenger());
        fa_driver_pass.setFontAwesomeIcon(mCallback.getDriverPassIcon());
    }

    public RouteRequest getDateParams() {
        RouteRequest routeRequest = new RouteRequest();
        routeRequest.TheTime = TheTime; //castStringToCal(home_work_time.getText().toString());

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
