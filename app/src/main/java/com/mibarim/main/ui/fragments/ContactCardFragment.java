package com.mibarim.main.ui.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.brnunes.swipeablerecyclerview.SwipeableRecyclerViewTouchListener;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.mibarim.main.BootstrapApplication;
import com.mibarim.main.R;
import com.mibarim.main.adapters.ContactListRecyclerAdapter;
import com.mibarim.main.adapters.RouteListRecyclerAdapter;
import com.mibarim.main.authenticator.LogoutService;
import com.mibarim.main.data.UserData;
import com.mibarim.main.models.ContactModel;
import com.mibarim.main.models.Route.RouteResponse;
import com.mibarim.main.ui.activities.MainActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

public class ContactCardFragment extends Fragment {

    @Inject
    UserData userData;
    @Inject
    protected LogoutService logoutService;
    private int RELOAD_REQUEST = 1234;
    List<ContactModel> items;
    private Tracker mTracker;

    private View mRecycler;
    private RecyclerView mRecyclerView;
    private TextView mEmptyView;
    private ContactListRecyclerAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private int selectedRow;
    ItemTouchListener itemTouchListener;


    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BootstrapApplication.component().inject(this);
        // Obtain the shared Tracker instance.
        BootstrapApplication application = (BootstrapApplication) getActivity().getApplication();
        mTracker = application.getDefaultTracker();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        mRecycler = inflater.inflate(R.layout.card_list, null);

        mRecyclerView = (RecyclerView) mRecycler.findViewById(android.R.id.list);
        mEmptyView = (TextView) mRecycler.findViewById(android.R.id.empty);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        items = loadData();

        if (items.size() == 0) {
            mEmptyView.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        } else {
            mEmptyView.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }

        itemTouchListener = new ItemTouchListener() {
            @Override
            public void onCardViewTap(View view, int position) {
                //Toaster.showLong(getActivity(), "tap"+position);
                ContactModel contactModel = ((ContactModel) items.get(position));
                ((MainActivity) getActivity()).goToMessaging(contactModel);
            }

            @Override
            public void onUserImageClick(View view, int position) {
                ContactModel contactModel = ((ContactModel) items.get(position));
                ((MainActivity) getActivity()).goToContactActivity(contactModel);

            }

        };

        // specify an adapter (see also next example)
        mAdapter = new ContactListRecyclerAdapter(getActivity(),items, itemTouchListener);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnItemTouchListener(new SwipeableRecyclerViewTouchListener(mRecyclerView,
                new SwipeableRecyclerViewTouchListener.SwipeListener() {
                    @Override
                    public boolean canSwipeLeft(int position) {
                        return false;
                    }

                    @Override
                    public boolean canSwipeRight(int position) {
                        return false;
                    }

                    @Override
                    public void onDismissedBySwipeLeft(RecyclerView recyclerView, int[] reverseSortedPositions) {
                        for (int position : reverseSortedPositions) {
                            selectedRow=position;
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setMessage("مسیر حذف شود").setPositiveButton("بله", dialogClickListener)
                                    .setNegativeButton("خیر", dialogClickListener).show();
                            /*items.remove(position);
                            mAdapter.notifyItemRemoved(position);*/
                        }
                        //mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {
                        for (int position : reverseSortedPositions) {
                            //((MainActivity) getActivity()).shareRoute(String.valueOf(items.get(position).RouteId));
                            //items.remove(position);
                            //mAdapter.notifyItemRemoved(position);
                        }
                        //mAdapter.notifyDataSetChanged();
                    }
                }));

        return mRecycler;
    }

    public void refresh() {
        items = loadData();

        if (items.size() == 0) {
            mEmptyView.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        } else {
            mEmptyView.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }
        mAdapter = new ContactListRecyclerAdapter(getActivity(),items, itemTouchListener);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mTracker.setScreenName("ContactCardFragment");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        mTracker.send(new HitBuilders.EventBuilder().setCategory("Fragment").setAction("ContactCardFragment").build());
        //setEmptyText(R.string.no_routes);
    }

    public List<ContactModel> loadData() {
        List<ContactModel> latest = new ArrayList<ContactModel>();
        try {
            latest = userData.contactModelListQuery();
            if (latest != null && latest.size() > 0) {
                return latest;
            } else {
                return Collections.emptyList();
            }
        } catch (final Exception e) {
            return Collections.emptyList();
        }
    }

    public interface ItemTouchListener {
        public void onCardViewTap(View view, int position);

        public void onUserImageClick(View view, int position);

    }

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    //((MainActivity) getActivity()).deleteRoute(String.valueOf(items.get(selectedRow).RouteId));
                    items.remove(selectedRow);
                    mAdapter.notifyItemRemoved(selectedRow);
                    mAdapter.notifyDataSetChanged();
            break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    break;
            }
        }
    };


}
