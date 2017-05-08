package com.mibarim.main;

import com.mibarim.main.ui.activities.EventMapActivity;
import com.mibarim.main.ui.activities.HelpingActivity;
import com.mibarim.main.ui.activities.HomeWorkStepActivity;
import com.mibarim.main.ui.activities.MainCardActivity;
import com.mibarim.main.ui.activities.MobileActivity;
import com.mibarim.main.ui.activities.RegisterActivity;
import com.mibarim.main.ui.activities.CheckSuggestRouteActivity;
import com.mibarim.main.ui.activities.HelpActivity;
import com.mibarim.main.ui.activities.MessagingActivity;
import com.mibarim.main.ui.activities.MobileValidationActivity;
import com.mibarim.main.ui.activities.RouteStepActivity;
import com.mibarim.main.ui.activities.SplashActivity;
import com.mibarim.main.ui.activities.RideMainActivity;
import com.mibarim.main.ui.activities.RideRequestMapActivity;
import com.mibarim.main.ui.activities.SmsValidationActivity;
import com.mibarim.main.ui.activities.SuggestRouteActivity;
import com.mibarim.main.ui.activities.AddMainActivity;
import com.mibarim.main.ui.activities.AddMapActivity;
import com.mibarim.main.ui.activities.DriveActivity;
import com.mibarim.main.ui.activities.HomeWorkActivity;
import com.mibarim.main.ui.activities.LocationSearchActivity;
import com.mibarim.main.ui.activities.SuggestRouteCardActivity;
import com.mibarim.main.ui.activities.TripActivity;
import com.mibarim.main.ui.activities.TripProfileActivity;
import com.mibarim.main.ui.activities.UserContactActivity;
import com.mibarim.main.ui.activities.UserInfoActivity;
import com.mibarim.main.authenticator.AuthenticatorActivity;
import com.mibarim.main.authenticator.TokenRefreshActivity;
import com.mibarim.main.ui.BootstrapActivity;
import com.mibarim.main.ui.BootstrapFragmentActivity;
import com.mibarim.main.ui.activities.MainActivity;
import com.mibarim.main.ui.activities.UserInfoDetailActivity;
import com.mibarim.main.ui.activities.VerifyStepperActivity;
import com.mibarim.main.ui.activities.WeekTimeActivity;
import com.mibarim.main.ui.activities.WorkHomeActivity;
import com.mibarim.main.ui.fragments.HelpFragment;
import com.mibarim.main.ui.fragments.PlusFragments.PassengerCardFragment;
import com.mibarim.main.ui.fragments.TripProfileFragments.ProfileRouteInfoFragment;
import com.mibarim.main.ui.fragments.TripProfileFragments.ProfileScoreInfoFragment;
import com.mibarim.main.ui.fragments.TripProfileFragments.ProfileUserInfoFragment;
import com.mibarim.main.ui.fragments.TripProfileFragments.TripProfileMainFragment;
import com.mibarim.main.ui.fragments.WeekRouteCardFragment;
import com.mibarim.main.ui.fragments.addRouteStepper.MainRouteStepFragment;
import com.mibarim.main.ui.fragments.addRouteStepper.StepAddressFlagFragment;
import com.mibarim.main.ui.fragments.addRouteStepper.StepSrcDstFragment;
import com.mibarim.main.ui.fragments.ContactCardFragment;
import com.mibarim.main.ui.fragments.InfoMessageFragment;
import com.mibarim.main.ui.fragments.MainGroupFragment;
import com.mibarim.main.ui.fragments.RequestRideFragment;
import com.mibarim.main.ui.fragments.RouteCardFragment;
import com.mibarim.main.ui.fragments.addRouteFragments.AddressFlagFragment;
import com.mibarim.main.ui.fragments.addRouteFragments.DriveFragment;
import com.mibarim.main.ui.fragments.addRouteFragments.LocationListFragment;
import com.mibarim.main.ui.fragments.addRouteFragments.LocationSearchMainFragment;
import com.mibarim.main.ui.fragments.addRouteFragments.MainAddMapFragment;
import com.mibarim.main.ui.fragments.addRouteFragments.SrcDstFragment;
import com.mibarim.main.ui.fragments.addRouteFragments.WeekTimesFragment;
import com.mibarim.main.ui.fragments.eventFragments.EventMainFragment;
import com.mibarim.main.ui.fragments.helpFragments.FeedbackFragment;
import com.mibarim.main.ui.fragments.mainFragments.AddTimeFragment;
import com.mibarim.main.ui.fragments.mainFragments.ContactListFragment;
import com.mibarim.main.ui.fragments.eventFragments.EventListFragment;
import com.mibarim.main.ui.fragments.mainFragments.AddMainFragment;
import com.mibarim.main.ui.fragments.mainFragments.MainFragment;
import com.mibarim.main.ui.fragments.mainFragments.MainMapFragment;
import com.mibarim.main.ui.fragments.UpdateMessageFragment;
import com.mibarim.main.ui.fragments.addRouteFragments.AddMapFragment;
import com.mibarim.main.ui.fragments.ConfirmFragment;
import com.mibarim.main.ui.fragments.MapFragment;
import com.mibarim.main.ui.fragments.mainFragments.SplashFragment;
import com.mibarim.main.ui.fragments.messagingFragments.MainMessagingFragment;
import com.mibarim.main.ui.fragments.messagingFragments.MessageListFragment;
import com.mibarim.main.ui.fragments.messagingFragments.SendMessageFragment;
import com.mibarim.main.ui.fragments.messagingFragments.ToggleContactTripFragment;
import com.mibarim.main.ui.fragments.rideRequestFragment.RideFlagFragment;
import com.mibarim.main.ui.fragments.rideRequestFragment.RideMainFragment;
import com.mibarim.main.ui.fragments.rideRequestFragment.RideRequestMainFragment;
import com.mibarim.main.ui.fragments.rideRequestFragment.RideRequestMapFragment;
import com.mibarim.main.ui.fragments.routeFragments.CheckButtonsFragment;
import com.mibarim.main.ui.fragments.routeFragments.CheckSuggestListFragment;
import com.mibarim.main.ui.fragments.routeFragments.SuggestRouteButtonsFragment;
import com.mibarim.main.ui.fragments.routeFragments.SuggestRouteCardFragment;
import com.mibarim.main.ui.fragments.routeFragments.SuggestRouteListFragment;
import com.mibarim.main.ui.fragments.tripFragments.MainTripFragment;
import com.mibarim.main.ui.fragments.tripFragments.TripInfoFragment;
import com.mibarim.main.ui.fragments.tripFragments.TripMapFragment;
import com.mibarim.main.ui.fragments.userInfoFragments.AboutMeMainFragment;
import com.mibarim.main.ui.fragments.userInfoFragments.BankFragment;
import com.mibarim.main.ui.fragments.userInfoFragments.CarInfoFragment;
import com.mibarim.main.ui.fragments.userInfoFragments.CommentContactFragment;
import com.mibarim.main.ui.fragments.userInfoFragments.DiscountListFragment;
import com.mibarim.main.ui.fragments.userInfoFragments.DiscountMainFragment;
import com.mibarim.main.ui.fragments.userInfoFragments.InviteFragment;
import com.mibarim.main.ui.fragments.userInfoFragments.LicenseFragment;
import com.mibarim.main.ui.fragments.userInfoFragments.MainProfileFragment;
import com.mibarim.main.ui.fragments.userInfoFragments.OperationFragment;
import com.mibarim.main.ui.fragments.userInfoFragments.ProfileFragment;
import com.mibarim.main.ui.fragments.userInfoFragments.ScoresFragment;
import com.mibarim.main.ui.fragments.userInfoFragments.UserContactFragment;
import com.mibarim.main.ui.fragments.userInfoFragments.UserInfoCardFragment;
import com.mibarim.main.ui.fragments.userInfoFragments.UserInfoDetailMainFragment;
import com.mibarim.main.ui.fragments.userInfoFragments.UserInfoFragment;
import com.mibarim.main.ui.fragments.userInfoFragments.UserInfoMainFragment;
import com.mibarim.main.ui.fragments.userInfoFragments.UserPersonFragment;
import com.mibarim.main.ui.fragments.userInfoFragments.VerifyStepperFragment;
import com.mibarim.main.ui.fragments.userInfoFragments.WithdrawListFragment;
import com.mibarim.main.ui.fragments.userInfoFragments.WithdrawMainFragment;

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

    void inject(AddMainActivity target);

    void inject(TripProfileActivity target);

    void inject(MobileActivity target);

    void inject(ProfileUserInfoFragment target);

    void inject(ProfileRouteInfoFragment target);

    void inject(ProfileScoreInfoFragment target);

    void inject(TripProfileMainFragment target);

    void inject(RouteStepActivity target);

    void inject(HomeWorkStepActivity target);

    void inject(TripActivity target);

    void inject(MainTripFragment target);

    void inject(PassengerCardFragment target);

    void inject(MainCardActivity target);

    void inject(SplashFragment target);

    void inject(StepAddressFlagFragment target);

    void inject(OperationFragment target);

    void inject(StepSrcDstFragment target);

    void inject(ScoresFragment target);

    void inject(MainProfileFragment target);

    void inject(CommentContactFragment target);

    void inject(SuggestRouteCardFragment target);

    void inject(WeekTimesFragment target);

    void inject(ToggleContactTripFragment target);

    void inject(RideFlagFragment target);

    void inject(VerifyStepperFragment target);

    void inject(VerifyStepperActivity target);

    void inject(InviteFragment target);

    void inject(UserInfoFragment target);

    void inject(RouteCardFragment target);

    void inject(WeekRouteCardFragment target);

    void inject(ContactCardFragment target);

    void inject(AddTimeFragment target);

    void inject(UserInfoMainFragment target);

    void inject(BankFragment target);

    void inject(UserInfoCardFragment target);

    void inject(TripInfoFragment target);

    void inject(TripMapFragment target);


    void inject(RideMainFragment target);

    void inject(RideMainActivity target);

    void inject(CheckSuggestRouteActivity target);


    void inject(RequestRideFragment target);

    void inject(SmsValidationActivity target);

    void inject(MainActivity target);

    void inject(MainFragment target);

    void inject(MainMapFragment target);

    void inject(EventListFragment target);

    void inject(EventMainFragment target);

    void inject(UserInfoDetailMainFragment target);

    void inject(MobileValidationActivity target);

    void inject(MessagingActivity target);

    void inject(EventMapActivity target);

    void inject(UserContactActivity target);

    void inject(RideRequestMapActivity target);

    void inject(UserInfoDetailActivity target);

    void inject(RideRequestMainFragment target);

    void inject(RideRequestMapFragment target);

    void inject(MainMessagingFragment target);

    void inject(CheckSuggestListFragment target);

    void inject(UserContactFragment target);

    void inject(InfoMessageFragment target);

    void inject(UpdateMessageFragment target);

    void inject(MessageListFragment target);

    void inject(WeekTimeActivity target);

    void inject(SendMessageFragment target);

    void inject(RegisterActivity target);

    void inject(WorkHomeActivity target);

    void inject(AddMapActivity target);

    void inject(AddressFlagFragment target);

    void inject(SplashActivity target);

    void inject(ConfirmFragment target);

    void inject(ProfileFragment target);


    void inject(AddMapFragment target);


    void inject(HomeWorkActivity target);

    void inject(AddMainFragment target);

    void inject(SrcDstFragment target);

    void inject(MainRouteStepFragment target);

    void inject(DriveFragment target);

    void inject(DriveActivity target);

    void inject(LocationSearchActivity target);

    void inject(LocationSearchMainFragment target);

    void inject(LocationListFragment target);


    void inject(MainAddMapFragment target);

    void inject(MapFragment target);

    void inject(ContactListFragment target);


    void inject(HelpActivity target);

    void inject(HelpingActivity target);


    void inject(UserInfoActivity target);

    void inject(UserPersonFragment target);

    void inject(CarInfoFragment target);

    void inject(LicenseFragment target);


    void inject(SuggestRouteListFragment target);


    void inject(SuggestRouteActivity target);

    void inject(SuggestRouteCardActivity target);


    void inject(CheckButtonsFragment target);

    void inject(MainGroupFragment target);


    void inject(SuggestRouteButtonsFragment target);

    void inject(DiscountMainFragment target);

    void inject(WithdrawMainFragment target);

    void inject(DiscountListFragment target);

    void inject(WithdrawListFragment target);


    void inject(BootstrapFragmentActivity target);

    void inject(AboutMeMainFragment target);

    void inject(FeedbackFragment target);

    void inject(HelpFragment target);

    void inject(BootstrapActivity target);


}
