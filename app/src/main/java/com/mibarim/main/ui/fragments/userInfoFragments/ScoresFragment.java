package com.mibarim.main.ui.fragments.userInfoFragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapCircleThumbnail;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.mibarim.main.BootstrapApplication;
import com.mibarim.main.R;
import com.mibarim.main.adapters.ViewPagerAdapter;
import com.mibarim.main.core.ImageUtils;
import com.mibarim.main.models.AboutMeModel;
import com.mibarim.main.models.ScoreModel;
import com.mibarim.main.models.UserInfoModel;
import com.mibarim.main.ui.activities.MainActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Hamed on 3/5/2016.
 */
public class ScoresFragment extends Fragment {

    private RelativeLayout layout;
    private Tracker mTracker;
    private Context context;
    //private boolean isViewShown = false;

    /*@Bind(R.id.header)
    protected ImageView header;
    @Bind(R.id.title_container)
    protected TextView title_container;
    @Bind(R.id.viewpager)
    protected ViewPager viewPager;
    @Bind(R.id.nest_scrollview)
    protected NestedScrollView scrollView;
    @Bind(R.id.tabs)
    protected TabLayout tabLayout;

    @Bind(R.id.user_image)
    protected BootstrapCircleThumbnail user_image;*/

    /*@Bind(R.id.verify_level)
    protected ImageView verify_level;*/
/*    @Bind(R.id.fa_edit)
    protected AwesomeTextView fa_edit;
    @Bind(R.id.credit_money)
    protected TextView credit_money;
    @Bind(R.id.score_tree)
    protected TextView score_tree;*/
    /*@Bind(R.id.company_image)
    protected ImageView company_image;*/
    /*@Bind(R.id.company_name)
    protected TextView company_name;*/
    /*@Bind(R.id.charge_account)
    protected BootstrapButton charge_account;

    @Bind(R.id.name_family)
    protected TextView name_family;
    @Bind(R.id.about_me)
    protected TextView about_me;*/

    public ScoresFragment() {
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
        layout = (RelativeLayout) inflater.inflate(R.layout.fragment_scores, container, false);
        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getActivity();
        ButterKnife.bind(this, getView());
        mTracker.setScreenName("ScoresFragment");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        mTracker.send(new HitBuilders.EventBuilder().setCategory("Fragment").setAction("ScoresFragment").build());
    }

    public void SetScores(ScoreModel scoreModel) {
        /*credit_money.setText(scoreModel.CreditMoney);
        score_tree.setText(scoreModel.Score);*/
    }

}
