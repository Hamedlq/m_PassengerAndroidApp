package com.mibarim.main.ui.fragments.userInfoFragments;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.AwesomeTextView;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapCircleThumbnail;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.firebase.auth.UserInfo;
import com.mibarim.main.BootstrapApplication;
import com.mibarim.main.R;
import com.mibarim.main.adapters.ViewPagerAdapter;
import com.mibarim.main.core.ImageUtils;
import com.mibarim.main.models.AboutMeModel;
import com.mibarim.main.models.PersonalInfoModel;
import com.mibarim.main.models.ScoreModel;
import com.mibarim.main.models.UserInfoModel;
import com.mibarim.main.ui.activities.MainActivity;
import com.mibarim.main.ui.activities.UserInfoActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Hamed on 3/5/2016.
 */
public class ProfileFragment extends Fragment {

    private CoordinatorLayout layout;
    private Tracker mTracker;
    private Context context;
    //private boolean isViewShown = false;

    @Bind(R.id.header)
    protected ImageView header;
    @Bind(R.id.title_container)
    protected TextView title_container;
/*
    @Bind(R.id.viewpager)
    protected ViewPager viewPager;
*/
    @Bind(R.id.nest_scrollview)
    protected NestedScrollView scrollView;
/*
    @Bind(R.id.tabs)
    protected TabLayout tabLayout;
*/

    @Bind(R.id.user_image)
    protected BootstrapCircleThumbnail user_image;

    /*@Bind(R.id.verify_level)
    protected ImageView verify_level;*/
    /*@Bind(R.id.fa_edit)
    protected AwesomeTextView fa_edit;*/
    @Bind(R.id.credit_money)
    protected TextView credit_money;
    @Bind(R.id.score_tree)
    protected TextView score_tree;
    /*@Bind(R.id.company_image)
    protected ImageView company_image;
    @Bind(R.id.company_name)
    protected TextView company_name;*/
    @Bind(R.id.charge_account)
    protected BootstrapButton charge_account;

/*    @Bind(R.id.name_family)
    protected TextView name_family;*/
    @Bind(R.id.about_me)
    protected TextView about_me;

    public ProfileFragment() {
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
        layout = (CoordinatorLayout) inflater.inflate(R.layout.fragment_profile_2, container, false);
       /* final FragmentManager fragmentManager = getChildFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.scores_fragment, new ScoresFragment())
                .commitAllowingStateLoss();*/
        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getActivity();
        ButterKnife.bind(this, getView());
/*        setupViewPager(viewPager);
        scrollView.setFillViewport(true);*/
        //tabLayout = (TabLayout) findViewById(R.id.tabs);
        //tabLayout.setupWithViewPager(viewPager);
/*        SharedPreferences prefs = getActivity().getSharedPreferences(
                "com.mibarim.main", Context.MODE_PRIVATE);
        String Mobile = prefs.getString("UserMobile", "");*/
        UserInfoModel userInfo = null;
        AboutMeModel aboutMeModel = null;
        ((MainActivity) getActivity()).getUserScore();
        if (getActivity() instanceof MainActivity) {
            userInfo = ((MainActivity) getActivity()).getUserInfo();
            aboutMeModel=((MainActivity) context).getAboutMe();
            about_me.setText(aboutMeModel.Desc);
            //((MainActivity) context).hideActionBar();
        }
        if (userInfo != null && userInfo.Gender != null) {
            //name_family.setText(userInfo.Name + " " + userInfo.Family);
            Bitmap icon = BitmapFactory.decodeResource(getResources(),
                    R.mipmap.ic_camera);
            Bitmap img=((MainActivity) getActivity()).getImageById(userInfo.UserImageId,R.mipmap.ic_camera);
            if(!img.sameAs(icon)){
                user_image.setImageBitmap(img);
                header.setImageBitmap(ImageUtils.darkenBitMap(img));
            }else {
                user_image.setImageBitmap(img);
                header.setBackgroundResource(R.color.primary_light);
            }
            title_container.setText(userInfo.Name+" "+ userInfo.Family);
            /*if (userInfo.Base64UserPic != null && !userInfo.Base64UserPic.equals("")) {
                byte[] decodedString = Base64.decode(userInfo.Base64UserPic, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                user_image.setImageBitmap(decodedByte);
            }*/
        }
        /*company_image.setVisibility(View.GONE);
        company_name.setVisibility(View.GONE);*/
        /*if (userInfo != null && userInfo.CompanyName != null) {
            company_image.setVisibility(View.VISIBLE);
            company_name.setVisibility(View.VISIBLE);
            company_name.setText(userInfo.CompanyName);
            ((MainActivity) context).getCompanyImage(userInfo.CompanyImageId);
        }*/
        /*fa_edit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    ((MainActivity) getActivity()).goToUserActivity();
                    return true;
                }
                return false;
            }
        });*/
        charge_account.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    ((MainActivity) getActivity()).gotoMibarimWebsite();
                    return true;
                }
                return false;
            }
        });
        /*verify_level.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    ((MainActivity) getActivity()).gotoVerify();
                    return true;
                }
                return false;
            }
        });*/
        mTracker.setScreenName("ProfileFragment");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        mTracker.send(new HitBuilders.EventBuilder().setCategory("Fragment").setAction("ProfileFragment").build());
    }
/*
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (context != null && context instanceof MainActivity) {
            if (isVisibleToUser) {
                ((MainActivity) context).profileVisible();
                ((MainActivity) context).hideActionBar();
            } else {
                ((MainActivity) context).profileInVisible();
                ((MainActivity) context).showActionBar();
            }
        }
    }
*/

    public void SetScores(ScoreModel scoreModel) {
        credit_money.setText(scoreModel.CreditMoney);
        score_tree.setText(scoreModel.Score);
    }

    public void reloadUserImage() {
        UserInfoModel userInfo = ((MainActivity) getActivity()).getUserInfo();
        if (userInfo != null && userInfo.Gender != null) {
            user_image.setImageBitmap(((MainActivity) getActivity()).getImageById(userInfo.UserImageId,R.mipmap.ic_camera));
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new OperationFragment(), getString(R.string.user_operations));
        adapter.addFragment(new AboutMeMainFragment(), getString(R.string.about_me));
        //adapter.addFragment(new CommentContactFragment(), getString(R.string.user_comments));
        //adapter.addFragment(new UserInfoMainFragment(), "THREE");
        viewPager.setAdapter(adapter);
    }

    public void setCompanyImage(Bitmap decodedByte) {
        //company_image.setImageBitmap(decodedByte);
    }
}
