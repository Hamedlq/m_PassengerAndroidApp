package com.mibarim.main.ui.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.os.OperationCanceledException;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mibarim.main.BootstrapApplication;
import com.mibarim.main.R;
import com.mibarim.main.adapters.SuggestedTimesAdapter;
import com.mibarim.main.models.ApiResponse;
import com.mibarim.main.models.FilterModel;
import com.mibarim.main.models.FilterTimeModel;
import com.mibarim.main.models.Plus.PassRouteModel;
import com.mibarim.main.services.RouteRequestService;
import com.mibarim.main.services.RouteResponseService;
import com.mibarim.main.ui.HandleApiMessagesBySnackbar;
import com.mibarim.main.ui.activities.MainActivity;
import com.mibarim.main.ui.activities.SearchStationActivity;
import com.mibarim.main.util.SafeAsyncTask;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Alireza on 11/22/2017.
 */

public class SuggestedTimesFragment extends Fragment {

    @Inject
    RouteResponseService routeResponseService;
    @Inject
    RouteRequestService routeRequestService;


    private ApiResponse suggestedTimesResponse;
    String authToken;
    long filterId;
    List<FilterTimeModel> items;
    ListView suggestedTimesList;
    private ApiResponse setRes;
    String suggestedTimePrice;
    Button suggestTimeButton;
    FrameLayout footerView;
    SwipeRefreshLayout swipeRefreshLayout;

    NumberPicker hourPicker;
    NumberPicker minutePicker;

    View alertLayout;
    View titleLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BootstrapApplication.component().inject(this);

//        BootstrapApplication application = (BootstrapApplication) getApplication();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.suggested_times_fragment, container, false);


        alertLayout = inflater.inflate(R.layout.time_picker_dialog, null);
        titleLayout = inflater.inflate(R.layout.timepicker_dialog_custom_title,null);

        authToken = ((MainActivity) getActivity()).getAuthToken();
        filterId = ((MainActivity) getActivity()).getChosenFilter();

        suggestedTimesList = (ListView) view.findViewById(R.id.suggested_times_list);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);

        footerView = (FrameLayout) inflater.inflate(R.layout.button_under_suggested_times_fragment, null);
        suggestTimeButton = (Button) footerView.findViewById(R.id.suggest_time);

        final long filterId = ((MainActivity) getActivity()).getChosenFilter();


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getTimesFromServer();
            }
        });


        suggestTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).showSuggestTimeDialog(filterId);
            }
        });

        suggestedTimesList.addFooterView(footerView);

        items = new ArrayList<>();

        swipeRefreshLayout.setRefreshing(true);
        getTimesFromServer();


        suggestedTimesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FilterTimeModel model = items.get(position);


//                long filterId = model.FilterId;
                int hour = model.TimeHour;
                int min = model.TimeMinute;

                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(
                        "com.mibarim.main", Context.MODE_PRIVATE);
                sharedPreferences.edit().putInt("AllowBackButton", 1).apply();

                sendSuggestedFilterToServer(filterId, hour, min);


//                setRes = routeRequestService.setSuggestedFilter(authToken, model.FilterId, model.TimeHour, model.TimeMinute);
            }
        });


        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


    private void getTimesFromServer() {
        new SafeAsyncTask<Boolean>() {

            @Override
            public Boolean call() throws Exception {


                suggestedTimesResponse = routeResponseService.GetTimes(authToken, filterId);
                Gson gson = new Gson();

                if (suggestedTimesResponse != null) {
                    for (String json : suggestedTimesResponse.Messages) {
                        FilterTimeModel timeModel = gson.fromJson(json, FilterTimeModel.class);
                                /*if (route.IsBooked) {
                                    bookedTrip = route;
                                }*/
                        if (!timeModel.IsManual)
                            items.add(timeModel);
                        if (timeModel.IsManual)
                            suggestedTimePrice = timeModel.PriceString;

                    }
                }


                return false;
            }

            @Override
            protected void onException(final Exception e) throws RuntimeException {
                super.onException(e);
                if (e instanceof OperationCanceledException) {

                    swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(getActivity(), R.string.network_error , Toast.LENGTH_SHORT).show();
                    // User cancelled the authentication process (back button, etc).
                    // Since auth could not take place, lets finish this activity.
//                    finish();
                }
            }

            @Override
            protected void onSuccess(final Boolean hasAuthenticated) throws Exception {
                super.onSuccess(hasAuthenticated);
                //userHasAuthenticated = true;
//                initScreen();
//                sendRegistrationToServer();
                prepareTheSuggestedTimesList();
            }
        }.execute();
    }

    public void prepareTheSuggestedTimesList() {
        SuggestedTimesAdapter timesAdapter = new SuggestedTimesAdapter(getActivity(), R.layout.suggested_times_list_item, items);
        suggestedTimesList.setAdapter(timesAdapter);

        String text = (String) suggestTimeButton.getText();
        suggestTimeButton.setText(text + "(قیمت: " +suggestedTimePrice + ")");

        swipeRefreshLayout.setRefreshing(false);

    }


    public void sendSuggestedFilterToServer(final long id, final int hour, final int min) {

        new SafeAsyncTask<Boolean>() {
            @Override
            public Boolean call() throws Exception {

//                authToken = ((MainActivity) getActivity()).getAuthToken();
                setRes = routeRequestService.setSuggestedFilter(authToken, id, hour, min);


                return true;
            }

            @Override
            protected void onException(final Exception e) throws RuntimeException {
                super.onException(e);
//                progressDialog.hide();
//                Toast.makeText(SearchStationActivity.this, "خطا در انتخاب مسیر", Toast.LENGTH_LONG).show();
//                hideProgress();
            }

            @Override
            protected void onSuccess(final Boolean state) throws Exception {
                super.onSuccess(state);
//                hideProgress();
                ((MainActivity) getActivity()).removeSuggestedTimesFragment();
//                new HandleApiMessagesBySnackbar(parentLayout, setRes).showMessages();
//                Gson gson = new Gson();
//                FilterModel filterModel = new FilterModel();
//                for (String shareJson : setRes.Messages) {
//                    filterModel = gson.fromJson(shareJson, FilterModel.class);
//                }
//                if (filterModel.FilterId > 0) {
////                    progressDialog.hide();
////                    finishIt();
//                }


            }
        }.execute();


    }





    public void allowBackPressed(){

    }








}