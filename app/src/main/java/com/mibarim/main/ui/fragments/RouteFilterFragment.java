package com.mibarim.main.ui.fragments;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.Gson;
import com.mibarim.main.BootstrapApplication;
import com.mibarim.main.R;
import com.mibarim.main.adapters.PassengerRouteRecyclerAdapter;
import com.mibarim.main.adapters.RouteFilterAdapter;
import com.mibarim.main.core.Constants;
import com.mibarim.main.models.ApiResponse;
import com.mibarim.main.models.FilterModel;
import com.mibarim.main.models.Plus.PassRouteModel;
import com.mibarim.main.models.RouteResponse;
import com.mibarim.main.services.RouteRequestService;
import com.mibarim.main.services.RouteResponseService;
import com.mibarim.main.ui.ThrowableLoader;
import com.mibarim.main.ui.activities.MainActivity;
import com.mibarim.main.ui.activities.MainCardActivity;
import com.mibarim.main.ui.activities.UserInfoDetailActivity;
//import com.mibarim.main.ui.activities.MainCardActivity;


import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
//import com.mibarim.main.adapters.RouteFilterAdapter;

public class RouteFilterFragment extends Fragment implements AbsListView.OnItemClickListener, LoaderManager.LoaderCallbacks<List<FilterModel>> {

    // declarations

    List<FilterModel> items;
    List<FilterModel> latest;
    private RouteResponse routeResponse;
    private ApiResponse suggestRouteResponse;
    RouteFilterAdapter mAdapter;
    ListView listView;
    TextView mEmptyView;
    Toolbar toolbar;
    ImageView invite_btn;
    ImageView upload_btn;

    SwipeRefreshLayout swipeRefreshLayout;

    // injections
    @Inject
    protected RouteResponseService routeResponseService;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BootstrapApplication.component().inject(this);
        // Obtain the shared Tracker instance.
        BootstrapApplication application = (BootstrapApplication) getActivity().getApplication();



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.route_filter_fragment, container, false);


        listView = (ListView) view.findViewById(R.id.listView);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        mEmptyView = (TextView) view.findViewById(R.id.empty);

        toolbar = (Toolbar) view.findViewById(R.id.main_toolbar);
//        setSupportActionBar(toolbar);
        invite_btn = (ImageView) toolbar.findViewById(R.id.invite_btn);

        //initScreen();
        upload_btn = (ImageView) toolbar.findViewById(R.id.upload_btn);

        invite_btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    ((MainActivity) getActivity()).gotoInviteActivity();
                    return true;
                }
                return false;
            }
        });

        upload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).goToImageUploadActivity();
            }
        });


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            listView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    int c = 0;

                }
            });
        }


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });

        String[] strings = {"First Element", "Second Element", "Third Element"};

//        RouteFilterAdapter routeListAdapter = new RouteFilterAdapter(getActivity(), R.layout.list_item, strings);

//        listView.setAdapter(routeListAdapter);

//        listView.setOnItemClickListener(this);


        getLoaderManager().initLoader(0, null, this);

        return view;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String imageTransitionName = "";
        String textTransitionName = "";
        String textTransitionNameForDest = "";

        long selectedId = id;


//        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
        TextView srcText = (TextView) view.findViewById(R.id.source_name);
        TextView destName = (TextView) view.findViewById(R.id.destination_name);
        TextView suggestTime = (TextView) view.findViewById(R.id.suggest_time);

        long filterId = items.get(position).FilterId;

//        suggestTime.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ((MainActivity) getActivity()).goToSuggestTimes();
//            }
//        });

        ((MainActivity) getActivity()).chosenFilter(filterId);

        ImageView staticImage = (ImageView) getView().findViewById(R.id.imageView);

        RouteDetailsFragment routeDetailsFragment = new RouteDetailsFragment();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setSharedElementReturnTransition(TransitionInflater.from(
                    getActivity()).inflateTransition(R.transition.change_image_trans));
            setExitTransition(TransitionInflater.from(
                    getActivity()).inflateTransition(android.R.transition.fade));

            routeDetailsFragment.setSharedElementEnterTransition(TransitionInflater.from(
                    getActivity()).inflateTransition(R.transition.change_image_trans));
            routeDetailsFragment.setEnterTransition(TransitionInflater.from(
                    getActivity()).inflateTransition(android.R.transition.fade));

//            imageTransitionName = imageView.getTransitionName();
            textTransitionName = srcText.getTransitionName();
            textTransitionNameForDest = destName.getTransitionName();
        }

        Bundle bundle = new Bundle();
        bundle.putString("TRANS_NAME", imageTransitionName);
        bundle.putString("ACTION", srcText.getText().toString());
        bundle.putString("DEST_NAME", destName.getText().toString());
        bundle.putString("DEST_TRANS", textTransitionNameForDest);
        bundle.putString("TRANS_TEXT", textTransitionName);
//        bundle.putParcelable("IMAGE", ((BitmapDrawable) imageView.getDrawable()).getBitmap());
        routeDetailsFragment.setArguments(bundle);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.main_activity, routeDetailsFragment)
                .addToBackStack("Payment")
//                .addSharedElement(imageView, imageTransitionName)
                .addSharedElement(srcText, textTransitionName)
                .addSharedElement(destName, textTransitionNameForDest)
                .addSharedElement(staticImage, getString(R.string.fragment_image_trans))
                .commit();
    }

    @Override
    public Loader<List<FilterModel>> onCreateLoader(int id, Bundle args) {
//        mEmptyView.setVisibility(View.GONE);

        swipeRefreshLayout.setRefreshing(true);
        items = new ArrayList<>();
        return new ThrowableLoader<List<FilterModel>>(getActivity(), items) {
            @Override
            public List<FilterModel> loadData() throws Exception {
                latest = new ArrayList<FilterModel>();
                if (getActivity() instanceof MainActivity) {
                    Gson gson = new Gson();
                    PassRouteModel bookedTrip = null;
//                    routeResponse = ((MainActivity) getActivity()).getRoute();
                    String authToken = ((MainActivity) getActivity()).getAuthToken();
                    if (getActivity() != null && authToken != null) {

                        suggestRouteResponse = routeResponseService.GetFilters(authToken); // GetFilters in here
//                        suggestRouteResponse = routeResponseService.GetPassengerRoutes(authToken, 1);

                        if (suggestRouteResponse != null) {
                            for (String routeJson : suggestRouteResponse.Messages) {
                                FilterModel route = gson.fromJson(routeJson, FilterModel.class);
//                                if (route.IsBooked) {
//                                    bookedTrip = route;
//                                }
                                latest.add(route);
                            }
                            /*if (bookedTrip != null) {
                                if (bookedTrip.TripState == TripStates.InPreTripTime.toInt() ||
                                        bookedTrip.TripState == TripStates.InRiding.toInt() ||
                                        bookedTrip.TripState == TripStates.InTripTime.toInt()) {
                                    ((MainCardActivity) getActivity()).gotoRidingActivity(bookedTrip);
                                }else{
                                    ((MainCardActivity) getActivity()).showRidingActivity(bookedTrip);
                                }
                            }*/
                        }
                    }
                }
                if (latest != null) {
                    return latest;
                } else {
                    return Collections.emptyList();
                }
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<List<FilterModel>> loader, List<FilterModel> data) {
        items = data;
        if (items.size() == 0) {
            mEmptyView.setVisibility(View.VISIBLE);
        }
        // specify an adapter (see also next example)
//        mAdapter = new PassengerRouteRecyclerAdapter(getActivity(), items, itemTouchListener);
        mAdapter = new RouteFilterAdapter(getActivity(), R.layout.list_item, items); //
        listView.setAdapter(mAdapter);
        /*listView.addOnItemTouchListener(new SwipeableRecyclerViewTouchListener(mRecyclerView,
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
                    }

                    @Override
                    public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {
                    }
                }));*/
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onLoaderReset(Loader<List<FilterModel>> loader) {

    }

    public void refresh() {
        getLoaderManager().restartLoader(0, null, this);
        //showState(1);
//        mAdapter = new RouteFilterAdapter(getActivity(), R.layout.list_item, items);
//        listView.setAdapter(mAdapter);

        ((MainActivity) getActivity()).showFloatingActionButton();
    }


}

