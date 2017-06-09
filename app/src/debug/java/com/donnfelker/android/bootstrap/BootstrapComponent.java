package com.mibarim.main;

import com.mibarim.main.ui.activities.MainCardActivity;
import com.mibarim.main.ui.activities.MobileActivity;
import com.mibarim.main.ui.activities.RegisterActivity;
import com.mibarim.main.ui.activities.MobileValidationActivity;
import com.mibarim.main.ui.activities.RidingActivity;
import com.mibarim.main.ui.activities.SplashActivity;
import com.mibarim.main.ui.activities.SmsValidationActivity;
import com.mibarim.main.authenticator.AuthenticatorActivity;
import com.mibarim.main.authenticator.TokenRefreshActivity;
import com.mibarim.main.ui.BootstrapActivity;
import com.mibarim.main.ui.BootstrapFragmentActivity;

import com.mibarim.main.ui.fragments.MapFragment;
import com.mibarim.main.ui.fragments.PlusFragments.PassengerCardFragment;

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

    void inject(PassengerCardFragment target);

    void inject(MainCardActivity target);

    void inject(SmsValidationActivity target);


    void inject(MobileValidationActivity target);

    void inject(RegisterActivity target);

    void inject(SplashActivity target);

    void inject(BootstrapFragmentActivity target);

    void inject(MapFragment target);


    void inject(BootstrapActivity target);


}
