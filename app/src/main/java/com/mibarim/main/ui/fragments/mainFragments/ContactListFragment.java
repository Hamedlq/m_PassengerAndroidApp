package com.mibarim.main.ui.fragments.mainFragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.ListView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;
import com.mibarim.main.BootstrapApplication;
import com.mibarim.main.BootstrapServiceProvider;
import com.mibarim.main.R;
import com.mibarim.main.data.UserData;
import com.mibarim.main.ui.activities.MessagingActivity;
import com.mibarim.main.adapters.ContactListAdapter;
import com.mibarim.main.authenticator.LogoutService;
import com.mibarim.main.models.ApiResponse;
import com.mibarim.main.models.ContactModel;
import com.mibarim.main.services.ContactService;
import com.mibarim.main.ui.HandleApiMessages;
import com.mibarim.main.ui.ItemListFragment;
import com.mibarim.main.ui.ThrowableLoader;
import com.mibarim.main.ui.activities.MainActivity;
import com.mibarim.main.ui.activities.UserContactActivity;
import com.mibarim.main.util.SingleTypeAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import retrofit.RetrofitError;

public class ContactListFragment extends ItemListFragment<ContactModel> {

    @Inject
    UserData userData;
    @Inject
    protected LogoutService logoutService;
    private int RELOAD_CONTACT = 8585;
    private Tracker mTracker;
    List<ContactModel> latest;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BootstrapApplication.component().inject(this);
        // Obtain the shared Tracker instance.
        BootstrapApplication application = (BootstrapApplication) getActivity().getApplication();
        mTracker = application.getDefaultTracker();
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getView().setBackgroundColor(Color.parseColor("#99ffffff"));
        setEmptyText(R.string.no_contacts);
        mTracker.setScreenName("ContactListFragment");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        mTracker.send(new HitBuilders.EventBuilder().setCategory("Fragment").setAction("ContactListFragment").build());
        /*if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).showActionBar();
        }*/
    }

    @Override
    protected void configureList(final Activity activity, final ListView listView) {
        super.configureList(activity, listView);

        listView.setFastScrollEnabled(true);
        listView.setDividerHeight(0);

        /*getListAdapter().addHeader(activity.getLayoutInflater()
                .inflate(R.layout.event_list_item_labels, null));*/
    }

    @Override
    protected LogoutService getLogoutService() {
        return logoutService;
    }

    @Override
    public Loader<List<ContactModel>> onCreateLoader(final int id, final Bundle args) {
        final List<ContactModel> initialItems = items;
        return new ThrowableLoader<List<ContactModel>>(getActivity(), items) {
            @Override
            public List<ContactModel> loadData() throws Exception {
                Gson gson = new Gson();
                try {
                    ApiResponse res = new ApiResponse();
                    latest = new ArrayList<ContactModel>();
                    latest = userData.contactModelListQuery();
                    if (latest != null && latest.size()>0) {
//                        ((RouteListActivity) getActivity()).setRouteList(latest);
//                        ((RouteListActivity) getActivity()).showAnnouncingMsg();
                        return latest;
                    } else {
                        return Collections.emptyList();
                    }
                } catch (final Exception e) {
                    //return initialItems;
                    return Collections.emptyList();
                }
            }
        };

    }

    public void onListItemClick(final ListView l, final View v, final int position, final long id) {
        final ContactModel contactModel = ((ContactModel) l.getItemAtPosition(position));
        ((MainActivity)getActivity()).goToContactActivity(contactModel);

    }

    @Override
    public void onLoadFinished(final Loader<List<ContactModel>> loader, final List<ContactModel> items) {
        super.onLoadFinished(loader, items);
    }

    @Override
    protected int getErrorMessage(final Exception exception) {
        if (!(exception instanceof RetrofitError)) {
            return R.string.error_loading_contacts;
        }
        return 0;
    }

    @Override
    protected SingleTypeAdapter<ContactModel> createAdapter(final List<ContactModel> items) {
        return new ContactListAdapter(getActivity().getLayoutInflater(), items,getActivity());
    }

/*
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getActivity() != null && getActivity() instanceof MainActivity) {
            if (isVisibleToUser) {
                ((MainActivity)getActivity()).hideMenu();
                refresh();
            } else {
                ((MainActivity)getActivity()).showMenu();
            }
        }
    }
*/

}
