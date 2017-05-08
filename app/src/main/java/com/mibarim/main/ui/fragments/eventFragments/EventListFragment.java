package com.mibarim.main.ui.fragments.eventFragments;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.text.Html;
import android.view.View;
import android.widget.ListView;

import com.google.gson.Gson;
import com.mibarim.main.BootstrapApplication;
import com.mibarim.main.BootstrapServiceProvider;
import com.mibarim.main.R;
import com.mibarim.main.adapters.EventListAdapter;
import com.mibarim.main.authenticator.LogoutService;
import com.mibarim.main.models.ApiResponse;
import com.mibarim.main.models.EventResponse;
import com.mibarim.main.services.EventService;
import com.mibarim.main.ui.HandleApiMessages;
import com.mibarim.main.ui.ItemListFragment;
import com.mibarim.main.ui.ThrowableLoader;
import com.mibarim.main.ui.activities.MainActivity;
import com.mibarim.main.util.SingleTypeAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import retrofit.RetrofitError;

public class EventListFragment extends ItemListFragment<EventResponse> {

    @Inject
    protected EventService eventService;
    @Inject
    protected BootstrapServiceProvider serviceProvider;
    @Inject
    protected LogoutService logoutService;
    private int RELOAD_REQUEST = 1234;
    List<EventResponse> latest;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BootstrapApplication.component().inject(this);
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getView().setBackgroundColor(Color.parseColor("#99ffffff"));
        setEmptyClickableText(Html.fromHtml("<p>"+getString(R.string.no_events)+"</p><br/><a href=\"" + "http://mibarim.com/mibarim-event/" + "\">" + "ثبت رویداد" + "</a>"));
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
    public Loader<List<EventResponse>> onCreateLoader(final int id, final Bundle args) {
        final List<EventResponse> initialItems = items;
        return new ThrowableLoader<List<EventResponse>>(getActivity(), items) {
            @Override
            public List<EventResponse> loadData() throws Exception {
                Gson gson = new Gson();
                try {
                    ApiResponse res = new ApiResponse();
                    latest = new ArrayList<EventResponse>();
                    if (getActivity() != null) {
                        res = eventService.GetEvents();
                        if (res != null) {
                            latest = new ArrayList<EventResponse>();
                            for (String eventJson : res.Messages) {
                                EventResponse event = gson.fromJson(eventJson, EventResponse.class);
                                latest.add(event);
                            }
                            new HandleApiMessages(getActivity(), res).showMessages();
                        }
                    }
                    if (latest != null) {
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
        final EventResponse eventResponse = ((EventResponse) l.getItemAtPosition(position));
//        Intent intent = new Intent(getActivity(), RouteActivity.class);
//        intent.putExtra("EventResponse", latest.get(position - 1));
//        ((AddMapActivity)getActivity()).selectEvent(eventResponse);
//        ((RouteListActivity)getActivity()).startActivityForResult(intent, RELOAD_REQUEST);
        ((MainActivity)getActivity()).gotoEventActivity(eventResponse);
    }

    @Override
    public void onLoadFinished(final Loader<List<EventResponse>> loader, final List<EventResponse> items) {
        super.onLoadFinished(loader, items);
    }

    @Override
    protected int getErrorMessage(final Exception exception) {
        if (!(exception instanceof RetrofitError)) {
            return R.string.error_loading_events;
        }
        return 0;
    }

    @Override
    protected SingleTypeAdapter<EventResponse> createAdapter(final List<EventResponse> items) {
        return new EventListAdapter(getActivity().getLayoutInflater(), items);
    }

/*
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getActivity() != null && getActivity() instanceof MainActivity) {
            if (isVisibleToUser) {
                ((MainActivity) getActivity()).showActionBar();
                ((MainActivity)getActivity()).hideMenu();
                refresh();
            } else {
                ((MainActivity)getActivity()).showMenu();
            }
        }
    }
*/

}
