package com.mibarim.main.ui.fragments.messagingFragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.mibarim.main.BootstrapApplication;
import com.mibarim.main.R;
import com.mibarim.main.models.ContactModel;
import com.mibarim.main.ui.activities.MessagingActivity;

/**
 * Created by Hamed on 3/5/2016.
 */
public class MainMessagingFragment extends Fragment {

    private RelativeLayout layout;

    public MainMessagingFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BootstrapApplication.component().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = (RelativeLayout) inflater.inflate(R.layout.fragment_main_messaging, container, false);
        initScreen();
        return layout;
    }

    private void initScreen() {
        final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        ContactModel contactModel=((MessagingActivity)getActivity()).getContactModel();

        fragmentManager.beginTransaction()
                .replace(R.id.message_list_fragment, new MessageListFragment())
                .commit();
        fragmentManager.beginTransaction()
                .replace(R.id.send_message_fragment, new SendMessageFragment())
                .commit();
        if(contactModel.IsSupport!=1) {
            fragmentManager.beginTransaction()
                    .replace(R.id.toggle_fragment, new ToggleContactTripFragment())
                    .commit();
        }
    }

}
