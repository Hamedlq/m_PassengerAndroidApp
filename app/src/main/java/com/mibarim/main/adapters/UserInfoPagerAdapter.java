

package com.mibarim.main.adapters;


import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.mibarim.main.R;
import com.mibarim.main.ui.fragments.userInfoFragments.CarInfoFragment;
import com.mibarim.main.ui.fragments.userInfoFragments.LicenseFragment;
import com.mibarim.main.ui.fragments.userInfoFragments.UserPersonFragment;

/**
 * Pager adapter
 */
public class UserInfoPagerAdapter extends FragmentPagerAdapter {

    private final Resources resources;

    /**
     * Create pager adapter
     *
     * @param resources
     * @param fragmentManager
     */
    public UserInfoPagerAdapter(final Resources resources, final FragmentManager fragmentManager) {
        super(fragmentManager);
        this.resources = resources;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Fragment getItem(final int position) {
        final Fragment result;
        switch (position) {
            case 0:
                result = new CarInfoFragment();
                break;
            case 1:
                result = new LicenseFragment();
                break;
            case 2:
                result = new UserPersonFragment();
                break;
            /*case 3:
                result = new NewRouteMapFragment();
                break;*/
            default:
                result = null;
                break;
        }
        if (result != null) {
            result.setArguments(new Bundle()); //TODO do we need this?
        }
        return result;
    }

    @Override
    public CharSequence getPageTitle(final int position) {
        switch (position) {
            case 0:
                return resources.getString(R.string.car_info);
            case 1:
                return resources.getString(R.string.license_info);
            case 2:
                return resources.getString(R.string.user_info);
/*
            case 3:

                return resources.getString(R.string.new_route);
*/
            default:
                return null;
        }
    }
}
