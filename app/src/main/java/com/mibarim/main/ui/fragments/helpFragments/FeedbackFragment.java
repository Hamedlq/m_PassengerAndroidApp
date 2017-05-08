package com.mibarim.main.ui.fragments.helpFragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.mibarim.main.BootstrapApplication;
import com.mibarim.main.R;
import com.mibarim.main.ui.activities.HelpActivity;
import com.mibarim.main.ui.activities.HelpingActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Hamed on 3/4/2016.
 */
public class FeedbackFragment extends Fragment {


    @Bind(R.id.untouch_layot)
    protected LinearLayout untouch;

    @Bind(R.id.send_button)
    protected Button sentbtn;

    @Bind(R.id.send_text)
    protected EditText senttxt;



    public FeedbackFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BootstrapApplication.component().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.fragment_feedback, container, false);
        return layout;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ButterKnife.bind(this, getView());

        sentbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((HelpingActivity) getActivity()).sendfeedback(senttxt.getText().toString());


            }
        });

        untouch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                ((HelpingActivity) getActivity()).hideFeedback();
                return true;
            }
        });

    }


}
