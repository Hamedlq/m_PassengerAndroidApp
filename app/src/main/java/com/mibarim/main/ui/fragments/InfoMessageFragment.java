package com.mibarim.main.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.mibarim.main.BootstrapApplication;
import com.mibarim.main.R;
import com.mibarim.main.ui.activities.TripActivity;
import com.mibarim.main.ui.activities.UserContactActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Hamed on 3/4/2016.
 */
public class InfoMessageFragment extends Fragment {

    private Context context;
    private RelativeLayout layout;

//    @Bind(R.id.later_btn)
//    protected BootstrapButton later_btn;
    @Bind(R.id.action_btn)
    protected BootstrapButton action_btn;
    @Bind(R.id.messageView)
    protected TextView messageView;


    public InfoMessageFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BootstrapApplication.component().inject(this);
        context = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.fragment_info_message, container, false);
        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ButterKnife.bind(this, getView());
/*
        later_btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (getActivity() instanceof RouteListActivity) {
                        ((RouteListActivity) getActivity()).hideMsg();
                    }
                    return true;
                }
                return false;
            }
        });
*/
        action_btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (getActivity() instanceof TripActivity) {
                        ((TripActivity) getActivity()).turnOnGps();
                    }
                    if (getActivity() instanceof UserContactActivity) {
                        ((UserContactActivity) getActivity()).gotoMibarimWebsite();
                    }
                    return true;
                }
                return false;
            }
        });

    }

    public void setActionBtn(String actoinText) {
        action_btn.setText(actoinText);
    }

/*
    public void setCancelBtn(String cancelText) {
        later_btn.setText(cancelText);
    }
*/

    public void setMsg(String msg) {
        messageView.setText(msg);
    }

}
