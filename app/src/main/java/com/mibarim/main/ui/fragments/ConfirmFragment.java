package com.mibarim.main.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.mibarim.main.BootstrapApplication;
import com.mibarim.main.R;
import com.mibarim.main.ui.activities.AddMapActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Hamed on 3/5/2016.
 */
public class ConfirmFragment extends Fragment {

    @Bind(R.id.final_confirm)
    protected BootstrapButton final_confirm;
    @Bind(R.id.final_confirm_no)
    protected BootstrapButton final_confirm_no;


    private LinearLayout layout;

    private WebView taxiLineWebView;
    private TextView messageView;
    private String message;

    public ConfirmFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BootstrapApplication.component().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = (LinearLayout) inflater.inflate(R.layout.fragment_confirm, container, false);
        messageView = (TextView) layout.findViewById(R.id.messageView);
        //message = ((AddMapActivity) getActivity()).getConfirmMessage();
        messageView.setText(message);

        return layout;
    }

    public void ReEnableBtn() {
        final_confirm.setText(R.string.confirm);
        final_confirm.setEnabled(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ButterKnife.bind(this, getView());
        final_confirm.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                    ((AddMapActivity) getActivity()).confirmRoute();
                    final_confirm.setEnabled(false);
                    final_confirm.setText(R.string.do_wait);
                    return true;
                }
                return false;
            }
        });
        final_confirm_no.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //((AddMapActivity) getActivity()).NotConfirmedRoute();
                return true;
            }
        });
    }

}
