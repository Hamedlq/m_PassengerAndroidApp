package com.mibarim.main.ui.fragments.routeFragments;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.ListView;

import com.google.gson.Gson;
import com.mibarim.main.BootstrapApplication;
import com.mibarim.main.BootstrapServiceProvider;
import com.mibarim.main.R;
import com.mibarim.main.adapters.CheckSuggestListAdapter;
import com.mibarim.main.adapters.SuggestRouteListAdapter;
import com.mibarim.main.authenticator.LogoutService;
import com.mibarim.main.models.ApiResponse;
import com.mibarim.main.models.ContactModel;
import com.mibarim.main.models.Route.BriefRouteModel;
import com.mibarim.main.models.Route.RouteGroupModel;
import com.mibarim.main.models.Route.RouteResponse;
import com.mibarim.main.models.Route.SuggestBriefRouteModel;
import com.mibarim.main.services.RouteResponseService;
import com.mibarim.main.ui.ItemListFragment;
import com.mibarim.main.ui.ThrowableLoader;
import com.mibarim.main.ui.activities.CheckSuggestRouteActivity;
import com.mibarim.main.ui.activities.SuggestRouteActivity;
import com.mibarim.main.util.SingleTypeAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import retrofit.RetrofitError;

/**
 * Created by Hamed on 3/5/2016.
 */
public class CheckSuggestListFragment extends ItemListFragment<SuggestBriefRouteModel> {

    @Inject
    protected BootstrapServiceProvider serviceProvider;
    @Inject
    protected RouteResponseService routeResponseService;
    @Inject
    protected LogoutService logoutService;
    private int RELOAD_REQUEST = 1234;

    List<SuggestBriefRouteModel> latest;

    private RouteResponse routeResponse;
    private ApiResponse suggestRouteResponse;
    private ContactModel contactModel;

    private int selectedRouteId;


    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        BootstrapApplication.component().inject(this);
    }

    @Override
    protected LogoutService getLogoutService() {
        return null;
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setEmptyText(R.string.no_suggest_route);
    }

    @Override
    protected void configureList(final Activity activity, final ListView listView) {
        super.configureList(activity, listView);

        listView.setFastScrollEnabled(true);
        listView.setDividerHeight(0);
//
//        getListAdapter().addHeader(activity.getLayoutInflater()
//                .inflate(R.layout.route_list_item_labels, null));
    }

    @Override
    public Loader<List<SuggestBriefRouteModel>> onCreateLoader(final int id, final Bundle args) {
        final List<SuggestBriefRouteModel> initialItems = items;
        return new ThrowableLoader<List<SuggestBriefRouteModel>>(getActivity(), items) {
            @Override
            public List<SuggestBriefRouteModel> loadData() throws Exception {
                latest = new ArrayList<SuggestBriefRouteModel>();
                Gson gson = new Gson();
                contactModel = ((CheckSuggestRouteActivity) getActivity()).getContactModel();
                String authToken = serviceProvider.getAuthToken(getActivity());
                if (getActivity() != null) {
                    suggestRouteResponse = routeResponseService.GetRouteSimilarSuggests(authToken, contactModel.ContactId);
                    if (suggestRouteResponse != null) {
                        for (String routeJson : suggestRouteResponse.Messages) {
                            SuggestBriefRouteModel route = gson.fromJson(routeJson, SuggestBriefRouteModel.class);
                            latest.add(route);
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

    private BriefRouteModel castToBriefRouteModel(RouteGroupModel routeGroupModel) {
        BriefRouteModel model = new BriefRouteModel();
        model.RouteId = routeGroupModel.RouteId;
        model.Name = routeGroupModel.Name;
        model.Family = routeGroupModel.Family;
        model.TimingString = routeGroupModel.TimingString;
        model.PricingString = routeGroupModel.PricingString;
        model.CarString = routeGroupModel.CarString;
        model.SrcDistance = routeGroupModel.SrcDistance;
        model.SrcLatitude = routeGroupModel.SrcLatitude;
        model.SrcLongitude = routeGroupModel.SrcLongitude;
        model.DstDistance = routeGroupModel.DstDistance;
        model.DstLatitude = routeGroupModel.DstLatitude;
        model.DstLongitude = routeGroupModel.DstLongitude;
        model.AccompanyCount = routeGroupModel.AccompanyCount;
        model.IsDrive = routeGroupModel.IsDrive;
        return model;
    }


    public void onListItemClick(final ListView l, final View v, final int position, final long id) {
        /*if (getActivity() instanceof RouteActivity) {
            Intent intent = new Intent(getActivity(), SuggestRouteActivity.class);
            intent.putExtra("SuggestRoutes", suggestRouteResponse);
            intent.putExtra("RouteResponse", routeResponse);
            getActivity().startActivityForResult(intent, RELOAD_REQUEST);

        } else if (getActivity() instanceof GroupActivity) {
            String srcLat = latest.get(position).SrcLatitude;
            String srcLng = latest.get(position).SrcLongitude;
            String dstLat = latest.get(position).DstLatitude;
            String dstLng = latest.get(position).DstLongitude;
            ((GroupActivity) getActivity()).setRouteSrcDst(srcLat, srcLng, dstLat, dstLng, position);
        } else*/
        refreshColors(l);
        SuggestBriefRouteModel selectedItem = ((SuggestBriefRouteModel) l.getItemAtPosition(position));
        ((CheckSuggestRouteActivity) getActivity()).setSelectedRoute(selectedItem);
        v.setBackgroundColor(Color.GREEN);
//            selectedRouteId = selectedItem.RouteId;
        ((CheckSuggestRouteActivity) getActivity()).showOnMap(selectedItem, position);
        /*} else if (getActivity() instanceof SuggestGroupActivity) {
            String srcLat = latest.get(position).SrcLatitude;
            String srcLng = latest.get(position).SrcLongitude;
            String dstLat = latest.get(position).DstLatitude;
            String dstLng = latest.get(position).DstLongitude;
            ((SuggestGroupActivity) getActivity()).setRouteSrcDst(srcLat, srcLng, dstLat, dstLng, position);
        }*/
    }


    @Override
    public void onLoadFinished(final Loader<List<SuggestBriefRouteModel>> loader, final List<SuggestBriefRouteModel> items) {
        super.onLoadFinished(loader, items);
    }

    @Override
    protected int getErrorMessage(final Exception exception) {
        if (!(exception instanceof RetrofitError)) {
            return R.string.error_loading_routes;
        }
        return 0;
    }

    @Override
    protected SingleTypeAdapter<SuggestBriefRouteModel> createAdapter(final List<SuggestBriefRouteModel> items) {
        return new CheckSuggestListAdapter(getActivity().getLayoutInflater(), items, getActivity());
    }

    private void refreshColors(ListView listView) {
        for (int i = 0; i < listView.getAdapter().getCount(); i++) {
            View v = listView.getChildAt(i);
            if (v != null) {
                v.setBackgroundColor(Color.WHITE);
            }
        }
    }

}
