package com.mibarim.main;

import com.mibarim.main.authenticator.AuthenticatorActivity;
import com.mibarim.main.authenticator.TokenRefreshActivity;
import com.mibarim.main.ui.BootstrapActivity;
import com.mibarim.main.ui.BootstrapFragmentActivity;
import com.mibarim.main.ui.activities.ChargeAccountActivity;
import com.mibarim.main.ui.activities.InviteActivity;
import com.mibarim.main.ui.activities.MainActivity;
import com.mibarim.main.ui.activities.MainCardActivity;
import com.mibarim.main.ui.activities.MobileActivity;
import com.mibarim.main.ui.activities.MobileValidationActivity;
import com.mibarim.main.ui.activities.PayActivity;
import com.mibarim.main.ui.activities.RatingActivity;
import com.mibarim.main.ui.activities.RegisterActivity;
import com.mibarim.main.ui.activities.RidingActivity;
import com.mibarim.main.ui.activities.SearchStationActivity;
import com.mibarim.main.ui.activities.SmsValidationActivity;
import com.mibarim.main.ui.activities.SplashActivity;
import com.mibarim.main.ui.activities.UserInfoDetailActivity;
import com.mibarim.main.ui.activities.UserProfileActivity;
import com.mibarim.main.ui.fragments.FabFragment;
import com.mibarim.main.ui.fragments.MapFragment;
import com.mibarim.main.ui.fragments.PlusFragments.InviteFragment;
import com.mibarim.main.ui.fragments.PlusFragments.PassengerCardFragment;
import com.mibarim.main.ui.fragments.PlusFragments.PayFragment;
import com.mibarim.main.ui.fragments.RouteDetailsFragment;
import com.mibarim.main.ui.fragments.RouteFilterFragment;
import com.mibarim.main.ui.fragments.SubstationListFragment;
import com.mibarim.main.ui.fragments.SuggestedTimesFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(
        modules = {
                AndroidModule.class,
                BootstrapModule.class
        }
)
public interface BootstrapComponent {

    void inject(BootstrapApplication target);

    void inject(AuthenticatorActivity target);

    void inject(TokenRefreshActivity target);

    void inject(MobileActivity target);

    void inject(RidingActivity target);

    void inject(InviteActivity target);

    void inject(PayActivity target);

    void inject(InviteFragment target);

    void inject(PayFragment target);

    void inject(PassengerCardFragment target);

    void inject(MainCardActivity target);

    void inject(SmsValidationActivity target);

    void inject(MobileValidationActivity target);

    void inject(RegisterActivity target);

    void inject(SplashActivity target);

    void inject(BootstrapFragmentActivity target);

    void inject(MapFragment target);

    void inject(BootstrapActivity target);

    void inject(UserInfoDetailActivity target);

    void inject(RatingActivity target);

    void inject(FabFragment target);

    void inject(SearchStationActivity target);

    void inject(SubstationListFragment target);

    void inject(MainActivity target);

    void inject(RouteFilterFragment target);

    void inject(RouteDetailsFragment target);

    void inject(SuggestedTimesFragment target);

    void inject(UserProfileActivity target);

    void inject(ChargeAccountActivity target);
}
