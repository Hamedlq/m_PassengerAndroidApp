package com.mibarim.main.ui.fragments.messagingFragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.beardedhen.androidbootstrap.AwesomeTextView;
import com.mibarim.main.BootstrapApplication;
import com.mibarim.main.R;
import com.mibarim.main.ui.activities.MessagingActivity;
import com.mibarim.main.util.Toaster;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Hamed on 3/4/2016.
 */
public class SendMessageFragment extends Fragment {


    @Bind(R.id.messageBodyField)
    protected EditText messageBodyField;
    @Bind(R.id.sendButton)
    protected AwesomeTextView sendButton;
    private String theMsg;

    public SendMessageFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BootstrapApplication.component().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.fragment_send_message, container, false);

        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ButterKnife.bind(this, getView());
        sendButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    String msg = messageBodyField.getText().toString();
                    if (msg.equals("")) {
                        Toaster.showLong(getActivity(), R.string.msg_empty_error);
                    } else {
                        ((MessagingActivity) getActivity()).sendMessage(msg);
                        theMsg = msg;
                        messageBodyField.setText("");
                        sendButton.setEnabled(false);
                        return true;
                    }
                }
                return false;
            }
        });

    }


    public void enableSendBtn() {
        messageBodyField.setText(theMsg);
        sendButton.setEnabled(true);
    }

    public void clearMsg() {
        sendButton.setEnabled(true);
        theMsg = "";
        messageBodyField.setText("");
    }
}
