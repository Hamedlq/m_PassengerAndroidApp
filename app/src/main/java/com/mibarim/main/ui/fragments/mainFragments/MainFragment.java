package com.mibarim.main.ui.fragments.mainFragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.mibarim.main.BootstrapApplication;
import com.mibarim.main.R;
import com.mibarim.main.adapters.MainPagerAdapter;
import com.mibarim.main.models.LocalRoute;
import com.mibarim.main.models.ScoreModel;
import com.mibarim.main.models.enums.MainTabs;
import com.mibarim.main.ui.activities.MainActivity;
import com.mibarim.main.ui.fragments.ContactCardFragment;
import com.mibarim.main.ui.fragments.RequestRideFragment;
import com.mibarim.main.ui.fragments.RouteCardFragment;
import com.mibarim.main.ui.fragments.UpdateMessageFragment;
import com.mibarim.main.ui.fragments.WeekRouteCardFragment;
import com.mibarim.main.ui.fragments.userInfoFragments.ProfileFragment;
import com.mibarim.main.ui.view.CustomViewPager;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabSelectedListener;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Hamed on 3/5/2016.
 */
public class MainFragment extends Fragment {

    private CoordinatorLayout layout;
    @Bind(R.id.cf_pages)
    protected CustomViewPager pager;
    @Bind(R.id.flag_layout)
    protected RelativeLayout flag_layout;
    @Bind(R.id.my_location)
    protected ImageView my_location;
    @Bind(R.id.go_to_trip)
    protected ImageView go_to_trip;
    @Bind(R.id.update_message_fragment)
    protected FrameLayout update_message_fragment;
    @Bind(R.id.ride_request_fragment)
    protected FrameLayout ride_request_fragment;

    public BottomBar bottomBar;
    private Animation blink;

    public MainFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BootstrapApplication.component().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = (CoordinatorLayout) inflater.inflate(R.layout.fragment_main, container, false);
        //initScreen();
        final FragmentManager fragmentManager = getChildFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.update_message_fragment, new UpdateMessageFragment())
                .commit();
        fragmentManager.beginTransaction()
                .replace(R.id.ride_request_fragment, new RequestRideFragment())
                .commit();
        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ButterKnife.bind(this, layout);
        hideUpdateMsg();
        pager.setAdapter(new MainPagerAdapter(getResources(), getChildFragmentManager()));
        pager.setPagingEnabled(false);
        if (savedInstanceState == null) {
            bottomBar = BottomBar.attach(getActivity(), savedInstanceState);
            bottomBar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            bottomBar.useOnlyStatusBarTopOffset();
            bottomBar.noNavBarGoodness();
            bottomBar.noTopOffset();
            bottomBar.noTabletGoodness();
            //final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            bottomBar.setItemsFromMenu(R.menu.bottom_menu, new OnMenuTabSelectedListener() {
                @Override
                public void onMenuItemSelected(int itemId) {
                    flag_layout.setVisibility(View.GONE);
                    my_location.setVisibility(View.GONE);
                    hideHeadBar();
                    switch (itemId) {
                        case R.id.profile_item:
                            pager.setCurrentItem(MainTabs.Profile.toInt());
                            //((MainActivity) getActivity()).showActionBar();
                            ((MainActivity) getActivity()).profileMenu();
                            //Snackbar.make(layout, R.string.profile_snack, Snackbar.LENGTH_LONG).show();
                            break;
                        case R.id.messages_item:
                            pager.setCurrentItem(MainTabs.Message.toInt());
                            //((MainActivity) getActivity()).showActionBar();
                            ((MainActivity) getActivity()).contactMenu();
                            //Snackbar.make(layout, R.string.message_snack, Snackbar.LENGTH_LONG).show();
                            break;
                        case R.id.map_item:
                            pager.setCurrentItem(MainTabs.Map.toInt());
                            //((MainActivity) getActivity()).showActionBar();
                            ((MainActivity) getActivity()).mapMenu();
                            flag_layout.setVisibility(View.VISIBLE);
                            my_location.setVisibility(View.VISIBLE);
                        /*fragmentManager.beginTransaction()
                                .replace(R.id.main_map_fragment, new MainMapFragment())
                                .commitAllowingStateLoss();*/
                            //Snackbar.make(layout, R.string.map_snack, Snackbar.LENGTH_LONG).show();
                            break;
                        /*case R.id.event_item:
                            pager.setCurrentItem(MainTabs.Event.toInt());
                            ((MainActivity) getActivity()).showActionBar();
                            ((MainActivity) getActivity()).hideMenu();
                            break;*/
                        case R.id.routes_item:
                            pager.setCurrentItem(MainTabs.Route.toInt());
                            //((MainActivity) getActivity()).showActionBar();
                            ((MainActivity) getActivity()).routeMenu();
                            //Snackbar.make(layout, R.string.route_snack, Snackbar.LENGTH_LONG).show();
                            break;
                    }
                }
            });
            // Set the color for the active tab. Ignored on mobile when there are more than three tabs.
            bottomBar.setActiveTabColor(getResources().getColor(R.color.background_selected));


            //bottomBar.setDrawingCacheBackgroundColor(getResources().getColor(R.color.colorPrimary));
//        bottomBar.mapColorForTab(0,getResources().getColor(R.color.colorPrimary) );

            int selectedTab = ((MainActivity) getActivity()).getSelectedTab();
            bottomBar.setDefaultTabPosition(selectedTab);
// Use custom text appearance in tab titles.
            //bottomBar.setTextAppearance(R.style.bottom_navigation);

// Use custom typeface that's located at the "/src/main/assets" directory. If using with
// custom text appearance, set the text appearance first.
            //bottomBar.setTypeFace("fonts/IRANSans(FaNum)_Light.ttf");
            // Make a Badge for the first tab, with red background color and a value of "4".
            //BottomBarBadge unreadMessages = bottomBar.makeBadgeForTabAt(1, "#E91E63", 1);

            // Control the badge's visibility
            //unreadMessages.show();
            //unreadMessages.hide();

            // Change the displayed count for this badge.
            //unreadMessages.setCount(4);

            // Change the show / hide animation duration.
            //unreadMessages.setAnimationDuration(200);
        }
        my_location.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    ((MainActivity) getActivity()).gotoMyLocation();
                    return true;
                }
                return false;
            }
        });
        go_to_trip.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    ((MainActivity) getActivity()).gotoTripActivity();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        bottomBar.onSaveInstanceState(outState);
    }

    public void hideUpdateMsg() {
        update_message_fragment.setVisibility(View.GONE);
    }

    public void setTripState(){
        if(((MainActivity)getActivity()).IsTripState()){
            go_to_trip.setVisibility(View.VISIBLE);
            blink = AnimationUtils.loadAnimation(getActivity(), R.anim.blink);
            go_to_trip.startAnimation(blink);
        }else {
            go_to_trip.setVisibility(View.GONE);
        }
    }

    public void showUpdateMsg() {
        update_message_fragment.setVisibility(View.VISIBLE);
    }

    public void AddLocalRoutes(List<LocalRoute> localRouteList) {
        final FragmentManager fragmentManager = getChildFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag("android:switcher:" + R.id.cf_pages + ":"+MainTabs.Map.toInt());
        if (fragment != null) {
            ((MainMapFragment) fragment).setMapLocalRoutes(localRouteList);
        }
    }

    public void MoveMap(String lat, String lng) {
        final FragmentManager fragmentManager = getChildFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag("android:switcher:" + R.id.cf_pages + ":"+MainTabs.Map.toInt());
        if (fragment != null) {
            ((MainMapFragment) fragment).MoveMap(lat, lng);
        }
    }

    public void setUserScores(ScoreModel scoreModel) {
        final FragmentManager fragmentManager = getChildFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag("android:switcher:" + R.id.cf_pages + ":"+MainTabs.Profile.toInt());
        if (fragment != null) {
            ((ProfileFragment) fragment).SetScores(scoreModel);
        }
    }

    public void reloadRouteList() {
        final FragmentManager fragmentManager = getChildFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag("android:switcher:" + R.id.cf_pages + ":"+MainTabs.Route.toInt());
        if (fragment != null) {
            //((RouteListFragment) fragment).refresh();
            ((WeekRouteCardFragment) fragment).refresh();
        }
    }

    public void reloadContactList() {
        final FragmentManager fragmentManager = getChildFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag("android:switcher:" + R.id.cf_pages + ":"+MainTabs.Message.toInt());
        if (fragment != null) {
            ((ContactCardFragment) fragment).refresh();
        }
    }

    public void reloadUserImage() {
        final FragmentManager fragmentManager = getChildFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag("android:switcher:" + R.id.cf_pages + ":"+MainTabs.Profile.toInt());
        if (fragment != null) {
            ((ProfileFragment) fragment).reloadUserImage();
        }
    }



    public void setCompanyImage(Bitmap decodedByte) {
        final FragmentManager fragmentManager = getChildFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag("android:switcher:" + R.id.cf_pages + ":"+MainTabs.Profile.toInt());
        if (fragment != null) {
            ((ProfileFragment) fragment).setCompanyImage(decodedByte);
        }
    }

    public void ShowHeadBar() {
        ride_request_fragment.setVisibility(View.VISIBLE);
        final FragmentManager fragmentManager = getChildFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.ride_request_fragment);
        if (fragment instanceof RequestRideFragment) {
            ((RequestRideFragment) fragment).ShowHeadBar();
        }
    }


    private void hideHeadBar() {
        ride_request_fragment.setVisibility(View.GONE);
        /*final FragmentManager fragmentManager = getChildFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.ride_request_fragment);
        if (fragment instanceof RequestRideFragment) {
            ((RequestRideFragment) fragment).HideHeadBar();
        }*/
    }


    public void ShowRouteInfo(LocalRoute localRoute) {
        final FragmentManager fragmentManager = getChildFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.ride_request_fragment);
        if (fragment instanceof RequestRideFragment) {
            ((RequestRideFragment) fragment).ShowRouteInfo(localRoute);
        }
    }

    public void ShowSnackBar(int resourceId) {
        Snackbar.make(layout, resourceId, Snackbar.LENGTH_LONG).show();
    }
}
