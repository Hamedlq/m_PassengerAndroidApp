

package com.mibarim.main.adapters;


import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.mibarim.main.models.enums.MainTabs;
import com.mibarim.main.ui.fragments.ContactCardFragment;
import com.mibarim.main.ui.fragments.RouteCardFragment;
import com.mibarim.main.ui.fragments.WeekRouteCardFragment;
import com.mibarim.main.ui.fragments.mainFragments.MainMapFragment;
import com.mibarim.main.ui.fragments.userInfoFragments.MainProfileFragment;
import com.mibarim.main.ui.fragments.userInfoFragments.ProfileFragment;

/**
 * Pager adapter
 */
public class MainPagerAdapter extends FragmentPagerAdapter {

    private final Resources resources;

    /**
     * Create pager adapter
     *
     * @param resources
     * @param fragmentManager
     */
    public MainPagerAdapter(final Resources resources, final FragmentManager fragmentManager) {
        super(fragmentManager);
        this.resources = resources;
    }

    @Override
    public int getCount() {
        return MainTabs.values().length;
    }

    @Override
    public Fragment getItem(final int position) {
        final Fragment result;
        MainTabs tabEnum = MainTabs.values()[position];
        switch (tabEnum) {
            case Profile:
                result = new ProfileFragment();
                break;
            case Message:
                //result = new ContactListFragment();
                result = new ContactCardFragment();
                break;
            case Map:
                result = new MainMapFragment();
                break;
            /*case Event:
                result = new EventListFragment();
                break;*/
            case Route:
                //result = new RouteListFragment();
                result = new WeekRouteCardFragment();
                break;
            default:
                result = null;
                break;
        }
        if (result != null) {
            result.setArguments(new Bundle()); //TODO do we need this?
        }
        return result;
    }
/*
    @Override
    public CharSequence getPageTitle(final int position) {
        switch (position) {
            case 0:
                return resources.getString(R.string.car_info);
            case 1:
                return resources.getString(R.string.license_info);
            case 2:
                return resources.getString(R.string.user_info);
*//*
            case 3:

                return resources.getString(R.string.new_route);
*//*
            default:
                return null;
        }
    }*/
}
