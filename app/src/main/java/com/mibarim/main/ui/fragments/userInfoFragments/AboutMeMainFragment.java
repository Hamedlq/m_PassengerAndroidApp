package com.mibarim.main.ui.fragments.userInfoFragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.mibarim.main.BootstrapApplication;
import com.mibarim.main.R;
import com.mibarim.main.models.AboutMeModel;
import com.mibarim.main.models.UserInfoModel;
import com.mibarim.main.ui.activities.MainActivity;
import com.mibarim.main.ui.activities.UserContactActivity;
import com.mibarim.main.ui.activities.UserInfoActivity;
import com.mibarim.main.ui.activities.UserInfoDetailActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Hamed on 3/5/2016.
 */
public class AboutMeMainFragment extends Fragment {

    private RelativeLayout layout;
    @Bind(R.id.about_me_desc)
    protected EditText about_me_desc;
    @Bind(R.id.about_me_txt_layout)
    protected LinearLayout about_me_txt_layout;
    @Bind(R.id.about_me_form)
    protected LinearLayout about_me_form;
    @Bind(R.id.about_me_txtview)
    protected TextView about_me_txtview;
    @Bind(R.id.edit_about_me)
    protected Button edit_about_me;
    private AboutMeModel model;
    private Tracker mTracker;

    public AboutMeMainFragment() {
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
        layout = (RelativeLayout) inflater.inflate(R.layout.fragment_main_about_me, container, false);
        initScreen();
        return layout;
    }

    private void initScreen() {
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ButterKnife.bind(this, getView());
        about_me_desc.requestFocus();
        if (getActivity() instanceof UserInfoDetailActivity) {
            model = ((UserInfoDetailActivity) getActivity()).getAboutMe();
            about_me_desc.setText(model.Desc);
            about_me_txt_layout.setVisibility(View.GONE);
            edit_about_me.setVisibility(View.GONE);
        }
        if (getActivity() instanceof UserInfoActivity) {
            model = ((UserInfoActivity) getActivity()).getAboutMe();
            about_me_txtview.setText(model.Desc);
            about_me_form.setVisibility(View.GONE);
            edit_about_me.setVisibility(View.GONE);
        }
        if (getActivity() instanceof MainActivity) {
            model = ((MainActivity) getActivity()).getAboutMe();
            about_me_txtview.setText(model.Desc);
            about_me_form.setVisibility(View.GONE);

        }
        mTracker.setScreenName("AboutMeMainFragment");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        mTracker.send(new HitBuilders.EventBuilder().setCategory("Fragment").setAction("AboutMeMainFragment").build());

    }

    public AboutMeModel getAboutMeInfo() {
        AboutMeModel model = new AboutMeModel();
        model.Desc = about_me_desc.getText().toString();
        return model;
    }
}
