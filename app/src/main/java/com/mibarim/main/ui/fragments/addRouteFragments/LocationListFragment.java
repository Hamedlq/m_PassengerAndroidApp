package com.mibarim.main.ui.fragments.addRouteFragments;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.ListView;

import com.google.gson.Gson;
import com.mibarim.main.BootstrapApplication;
import com.mibarim.main.R;
import com.mibarim.main.adapters.LocationAdapter;
import com.mibarim.main.authenticator.LogoutService;
import com.mibarim.main.models.Address.AutoCompleteResult;
import com.mibarim.main.models.Address.Place;
import com.mibarim.main.services.AddressService;
import com.mibarim.main.ui.ItemListFragment;
import com.mibarim.main.ui.ThrowableLoader;
import com.mibarim.main.ui.activities.LocationSearchActivity;
import com.mibarim.main.util.SingleTypeAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import retrofit.RetrofitError;

public class LocationListFragment extends ItemListFragment<Place> {

    @Inject
    protected AddressService addressService;
    @Inject
    protected LogoutService logoutService;
    private int RELOAD_REQUEST = 1234;
    List<Place> latest;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BootstrapApplication.component().inject(this);
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getView().setBackgroundColor(Color.parseColor("#99ffffff"));
        setEmptyText(R.string.empty_string);
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
    public Loader<List<Place>> onCreateLoader(final int id, final Bundle args) {
        final List<Place> initialItems = items;
        return new ThrowableLoader<List<Place>>(getActivity(), items) {
            @Override
            public List<Place> loadData() throws Exception {
                Gson gson = new Gson();
                try {
                    AutoCompleteResult res = new AutoCompleteResult();
                    latest = new ArrayList<Place>();
                    if (getActivity() != null) {
                        String term = ((LocationSearchActivity) getActivity()).getSearchTerm();
                        if (!term.equals("")) {
                            res = addressService.getAutocomplete(term);
                            if (res != null) {
                                for (Place placeJson : res.predictions) {
                                    latest.add(placeJson);
                                }
                            }
                        }
                    }
                    if (latest != null) {
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
        final Place response = ((Place) l.getItemAtPosition(position));
        ((LocationSearchActivity) getActivity()).setSearchResult(response.place_id);
//        Intent intent = new Intent(getActivity(), RouteActivity.class);
//        intent.putExtra("EventResponse", latest.get(position - 1));
        //((AddMapActivity)getActivity()).selectEvent(eventResponse);
//        ((RouteListActivity)getActivity()).startActivityForResult(intent, RELOAD_REQUEST);
    }

    @Override
    public void onLoadFinished(final Loader<List<Place>> loader, final List<Place> items) {
        super.onLoadFinished(loader, items);
    }

    @Override
    protected int getErrorMessage(final Exception exception) {
        if (!(exception instanceof RetrofitError)) {
            return R.string.error_loading_addresses;
        }
        return 0;
    }

    @Override
    protected SingleTypeAdapter<Place> createAdapter(final List<Place> items) {
        return new LocationAdapter(getActivity().getLayoutInflater(), items);
    }
}
