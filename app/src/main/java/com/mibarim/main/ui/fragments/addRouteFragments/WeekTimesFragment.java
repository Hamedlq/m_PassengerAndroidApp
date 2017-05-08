package com.mibarim.main.ui.fragments.addRouteFragments;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.mibarim.main.BootstrapApplication;
import com.mibarim.main.R;
import com.mibarim.main.models.RouteRequest;
import com.mibarim.main.models.enums.TimingOptions;
import com.mibarim.main.ui.activities.AddMapActivity;
import com.mibarim.main.ui.activities.WeekTimeActivity;
import com.mibarim.main.util.JalaliCalendar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Hamed on 3/5/2016.
 */
public class WeekTimesFragment extends Fragment implements View.OnTouchListener, TimePickerDialog.OnTimeSetListener {

    private RelativeLayout layout;
    private int currentDialog;
    private TimePickerDialog timeDialog;
    private static final String DATEPICKER = "DatePickerDialog";
    private RouteRequest routeRequest;
    int hour;
    int min;


    @Bind(R.id.time_saturday)
    protected TextView time_saturday;
    @Bind(R.id.time_saturday_lbl)
    protected TextView time_saturday_lbl;
    @Bind(R.id.time_sunday)
    protected TextView time_sunday;
    @Bind(R.id.time_sunday_lbl)
    protected TextView time_sunday_lbl;
    @Bind(R.id.time_monday)
    protected TextView time_monday;
    @Bind(R.id.time_monday_lbl)
    protected TextView time_monday_lbl;
    @Bind(R.id.time_tuesday)
    protected TextView time_tuesday;
    @Bind(R.id.time_tuesday_lbl)
    protected TextView time_tuesday_lbl;
    @Bind(R.id.time_wednesday)
    protected TextView time_wednesday;
    @Bind(R.id.time_wednesday_lbl)
    protected TextView time_wednesday_lbl;
    @Bind(R.id.time_thursday)
    protected TextView time_thursday;
    @Bind(R.id.time_thursday_lbl)
    protected TextView time_thursday_lbl;
    @Bind(R.id.time_friday)
    protected TextView time_friday;
    @Bind(R.id.time_friday_lbl)
    protected TextView time_friday_lbl;

    @Bind(R.id.do_continue_btn)
    protected TextView do_continue_btn;
//    @Bind(R.id.label_title)
//    protected TextView label_title;

    public WeekTimesFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BootstrapApplication.component().inject(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ButterKnife.bind(this, getView());
        Calendar calendar = Calendar.getInstance();
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        min = calendar.get(Calendar.MINUTE);

        //label_title.setText(((WeekTimeActivity) getActivity()).getTimeTitle());
        routeRequest = ((WeekTimeActivity) getActivity()).getRouteRequest();
        time_saturday.setText(routeRequest.SatDatetimeString());
        time_sunday.setText(routeRequest.SunDatetimeString());
        time_monday.setText(routeRequest.MonDatetimeString());
        time_tuesday.setText(routeRequest.TueDatetimeString());
        time_wednesday.setText(routeRequest.WedDatetimeString());
        time_thursday.setText(routeRequest.ThuDatetimeString());
        time_friday.setText(routeRequest.FriDatetimeString());

        time_saturday.setOnTouchListener(this);
        time_saturday_lbl.setOnTouchListener(this);
        time_sunday.setOnTouchListener(this);
        time_sunday_lbl.setOnTouchListener(this);
        time_monday.setOnTouchListener(this);
        time_monday_lbl.setOnTouchListener(this);
        time_tuesday.setOnTouchListener(this);
        time_tuesday_lbl.setOnTouchListener(this);
        time_wednesday.setOnTouchListener(this);
        time_wednesday_lbl.setOnTouchListener(this);
        time_thursday.setOnTouchListener(this);
        time_thursday_lbl.setOnTouchListener(this);
        time_friday.setOnTouchListener(this);
        time_friday_lbl.setOnTouchListener(this);
        do_continue_btn.setOnTouchListener(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = (RelativeLayout) inflater.inflate(R.layout.fragment_week_times, container, false);
        return layout;
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            switch (v.getId()) {
                case R.id.do_continue_btn:
                    ((WeekTimeActivity) getActivity()).AllDone();
                    break;
                case R.id.time_saturday:
                case R.id.time_saturday_lbl:
                    if (routeRequest.SatDatetime != null) {
                        hour = routeRequest.SatDatetime.getTime().getHours();
                        min = routeRequest.SatDatetime.getTime().getMinutes();
                    }
                    timeDialog = new TimePickerDialog(getActivity(), this, hour, min, true);
                    timeDialog.show();
                    currentDialog = R.id.time_saturday;
                    break;
                case R.id.time_sunday:
                case R.id.time_sunday_lbl:
                    if (routeRequest.SunDatetime != null) {
                        hour = routeRequest.SunDatetime.getTime().getHours();
                        min = routeRequest.SunDatetime.getTime().getMinutes();
                    }
                    timeDialog = new TimePickerDialog(getActivity(), this, hour, min, true);
                    timeDialog.show();
                    currentDialog = R.id.time_sunday;
                    break;
                case R.id.time_monday:
                case R.id.time_monday_lbl:
                    if (routeRequest.MonDatetime != null) {
                        hour = routeRequest.MonDatetime.getTime().getHours();
                        min = routeRequest.MonDatetime.getTime().getMinutes();
                    }
                    timeDialog = new TimePickerDialog(getActivity(), this, hour, min, true);
                    timeDialog.show();
                    currentDialog = R.id.time_monday;
                    break;
                case R.id.time_tuesday:
                case R.id.time_tuesday_lbl:
                    if (routeRequest.TueDatetime != null) {
                        hour = routeRequest.TueDatetime.getTime().getHours();
                        min = routeRequest.TueDatetime.getTime().getMinutes();
                    }
                    timeDialog = new TimePickerDialog(getActivity(), this, hour, min, true);
                    timeDialog.show();
                    currentDialog = R.id.time_tuesday;
                    break;
                case R.id.time_wednesday:
                case R.id.time_wednesday_lbl:
                    if (routeRequest.WedDatetime != null) {
                        hour = routeRequest.WedDatetime.getTime().getHours();
                        min = routeRequest.WedDatetime.getTime().getMinutes();
                    }
                    timeDialog = new TimePickerDialog(getActivity(), this, hour, min, true);
                    timeDialog.show();
                    currentDialog = R.id.time_wednesday;
                    break;
                case R.id.time_thursday:
                case R.id.time_thursday_lbl:
                    if (routeRequest.ThuDatetime != null) {
                        hour = routeRequest.ThuDatetime.getTime().getHours();
                        min = routeRequest.ThuDatetime.getTime().getHours();
                    }
                    timeDialog = new TimePickerDialog(getActivity(), this, hour, min, true);
                    timeDialog.show();
                    currentDialog = R.id.time_thursday;
                    break;
                case R.id.time_friday:
                case R.id.time_friday_lbl:
                    if (routeRequest.FriDatetime != null) {
                        hour = routeRequest.FriDatetime.getTime().getHours();
                        min = routeRequest.FriDatetime.getTime().getHours();
                    }
                    timeDialog = new TimePickerDialog(getActivity(), this, hour, min, true);
                    timeDialog.show();
                    currentDialog = R.id.time_friday;
                    break;

            }
            return true;
        }
        return false;
    }


    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        switch (currentDialog) {
            case R.id.time_saturday:
                ((WeekTimeActivity) getActivity()).routeRequest.SatDatetime = calendar;
                time_saturday.setText(hourOfDay + ":" + minute);
                break;
            case R.id.time_sunday:
                ((WeekTimeActivity) getActivity()).routeRequest.SunDatetime = calendar;
                time_sunday.setText(hourOfDay + ":" + minute);
                break;
            case R.id.time_monday:
                ((WeekTimeActivity) getActivity()).routeRequest.MonDatetime = calendar;
                time_monday.setText(hourOfDay + ":" + minute);
                break;
            case R.id.time_tuesday:
                ((WeekTimeActivity) getActivity()).routeRequest.TueDatetime = calendar;
                time_tuesday.setText(hourOfDay + ":" + minute);
                break;
            case R.id.time_wednesday:
                ((WeekTimeActivity) getActivity()).routeRequest.WedDatetime = calendar;
                time_wednesday.setText(hourOfDay + ":" + minute);
                break;
            case R.id.time_thursday:
                ((WeekTimeActivity) getActivity()).routeRequest.ThuDatetime = calendar;
                time_thursday.setText(hourOfDay + ":" + minute);
                break;
            case R.id.time_friday:
                ((WeekTimeActivity) getActivity()).routeRequest.FriDatetime = calendar;
                time_friday.setText(hourOfDay + ":" + minute);
                break;
        }
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
}
