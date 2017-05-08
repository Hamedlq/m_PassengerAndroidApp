package com.mibarim.main.ui.fragments.userInfoFragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.mibarim.main.BootstrapApplication;
import com.mibarim.main.R;
import com.mibarim.main.models.AboutMeModel;
import com.mibarim.main.models.ImageResponse;
import com.mibarim.main.models.UserInfoModel;
import com.mibarim.main.models.enums.UserCardTypes;
import com.mibarim.main.ui.activities.MainActivity;
import com.mibarim.main.ui.activities.UserInfoActivity;
import com.mibarim.main.ui.activities.UserInfoDetailActivity;

import butterknife.ButterKnife;

/**
 * Created by Hamed on 3/5/2016.
 */
public class UserInfoDetailMainFragment extends Fragment {

    private UserInfoModel userInfo;
    private UserCardTypes selectedType;

    private RelativeLayout layout;
    private Tracker mTracker;

    public UserInfoDetailMainFragment() {
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
        layout = (RelativeLayout) inflater.inflate(R.layout.fragment_main_user_detail_info, container, false);
        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ButterKnife.bind(this, getView());
/*        SharedPreferences prefs = getActivity().getSharedPreferences(
                "com.mibarim.main", Context.MODE_PRIVATE);
        String Mobile = prefs.getString("UserMobile", "");*/
        if (getActivity() instanceof UserInfoDetailActivity) {
            userInfo = ((UserInfoDetailActivity) getActivity()).getUserInfo();
            selectedType = ((UserInfoDetailActivity) getActivity()).getSelectedFragment();
        }
        initScreen();
        mTracker.setScreenName("UserInfoDetailMainFragment");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        mTracker.send(new HitBuilders.EventBuilder().setCategory("Fragment").setAction("UserInfoDetailMainFragment").build());
    }


    private void initScreen() {
        final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        switch (selectedType) {
            case InviteFriend:
                getActivity().setTitle(R.string.invite_friends);
                fragmentManager.beginTransaction()
                        .replace(R.id.invite_fragment, new InviteFragment())
                        .commit();
                break;
            case UserInfo:
                getActivity().setTitle(R.string.user_info);
                fragmentManager.beginTransaction()
                        .replace(R.id.user_person_fragment, new UserPersonFragment())
                        .commit();
                break;
            case AboutMe:
                getActivity().setTitle(R.string.about_me);
                fragmentManager.beginTransaction()
                        .replace(R.id.about_me_fragment, new AboutMeMainFragment())
                        .commit();
                break;
            case DiscountCode:
                getActivity().setTitle(R.string.discount_code);
                fragmentManager.beginTransaction()
                        .replace(R.id.discount_fragment, new DiscountMainFragment())
                        .commit();
                break;
            case LicenseInfo:
                getActivity().setTitle(R.string.license_info);
                fragmentManager.beginTransaction()
                        .replace(R.id.license_fragment, new LicenseFragment())
                        .commit();
                break;
            case CarInfo:
                getActivity().setTitle(R.string.car_info);
                fragmentManager.beginTransaction()
                        .replace(R.id.car_info_fragment, new CarInfoFragment())
                        .commit();
                break;
            case BankInfo:
                getActivity().setTitle(R.string.bank_info);
                fragmentManager.beginTransaction()
                        .replace(R.id.bank_info_fragment, new BankFragment())
                        .commit();
                break;
            case WithDraw:
                getActivity().setTitle(R.string.money_request);
                fragmentManager.beginTransaction()
                        .replace(R.id.withdraw_fragment, new WithdrawMainFragment())
                        .commit();
                break;
        }
    }

    public UserInfoModel getUserInfo() {
        final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        switch (selectedType) {
            case UserInfo:
                Fragment fragment = fragmentManager.findFragmentById(R.id.user_person_fragment);
                return  ((UserPersonFragment) fragment).getUserInfo();
            case CarInfo:
                Fragment carfragment = fragmentManager.findFragmentById(R.id.car_info_fragment);
                return ((CarInfoFragment) carfragment).getCarInfo();
            case BankInfo:
                Fragment bankfragment = fragmentManager.findFragmentById(R.id.bank_info_fragment);
                return ((BankFragment) bankfragment).getBankInfo();
        }
        return null;
    }

    public void setImage(ImageResponse imageResponse) {
        final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        switch (imageResponse.ImageType) {
            case UserPic:
                //((UserInfoActivity) getActivity()).setHeadImage(imageResponse.Base64ImageFile);
                break;
            case UserNationalCard:
                Fragment fragment = fragmentManager.findFragmentById(R.id.user_person_fragment);
                ((UserPersonFragment) fragment).setNationalImage(imageResponse);
                break;
            case LicensePic:
                Fragment lifragment = fragmentManager.findFragmentById(R.id.license_fragment);
                ((LicenseFragment) lifragment).setLicenseImage(imageResponse);
                break;
            case CarPic:
                Fragment carfragment = fragmentManager.findFragmentById(R.id.car_info_fragment);
                ((CarInfoFragment) carfragment).setCarImage(imageResponse);
                break;
            case CarBckPic:
                Fragment car2fragment = fragmentManager.findFragmentById(R.id.car_info_fragment);
                ((CarInfoFragment) car2fragment).setCarBckImage(imageResponse);
                break;
            /*case BankPic:
                Fragment bankfragment = fragmentManager.findFragmentById(R.id.bank_info_fragment);
                ((BankFragment)bankfragment).setBankCardImage(imageResponse);
                break;*/
        }
    }

    public void ClearCode() {
        final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.discount_fragment);
        if(fragment instanceof DiscountMainFragment){
            ((DiscountMainFragment) fragment).ClearCode();
        }
    }

    public AboutMeModel getAboutMeInfo() {
        final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.about_me_fragment);
        return  ((AboutMeMainFragment) fragment).getAboutMeInfo();

    }

    public void reloadInvite() {
        final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.invite_fragment);
        if(fragment instanceof InviteFragment){
            ((InviteFragment) fragment).reloadInvite();
        }
    }
}
