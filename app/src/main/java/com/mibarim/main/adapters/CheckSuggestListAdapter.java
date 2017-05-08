package com.mibarim.main.adapters;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;

import com.mibarim.main.R;
import com.mibarim.main.models.Route.SuggestBriefRouteModel;
import com.mibarim.main.ui.activities.CheckSuggestRouteActivity;
import com.mibarim.main.util.SingleTypeAdapter;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Adapter to display a list of traffic items
 */
public class CheckSuggestListAdapter extends SingleTypeAdapter<SuggestBriefRouteModel> {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MMMM dd");

    private Activity activity;

    private int selectedValue;

    /**
     * @param inflater
     * @param items
     */
    public CheckSuggestListAdapter(final LayoutInflater inflater, final List<SuggestBriefRouteModel> items) {
        super(inflater, R.layout.suggest_route_list_item);

        setItems(items);
    }

    public CheckSuggestListAdapter(final LayoutInflater inflater, final List<SuggestBriefRouteModel> items, Activity activity) {
        super(inflater, R.layout.suggest_route_list_item);
        this.activity = activity;
        setItems(items);
    }


    /**
     * @param inflater
     */
    public CheckSuggestListAdapter(final LayoutInflater inflater) {
        this(inflater, null);

    }

    @Override
    public long getItemId(final int position) {
        final String id = String.valueOf(getItem(position).SelfRouteModel.RouteId);
        return !TextUtils.isEmpty(id) ? id.hashCode() : super
                .getItemId(position);
    }

    @Override
    protected int[] getChildViewIds() {
        return new int[]{R.id.username, R.id.timing, R.id.pricing, R.id.carString, R.id.src_distance, R.id.dst_distance, R.id.userimage};
    }

    @Override
    protected void update(final int position, final SuggestBriefRouteModel routeResponse) {

        setText(0, routeResponse.SuggestRouteModel.Name + " " + routeResponse.SuggestRouteModel.Family);
        setText(1, routeResponse.SuggestRouteModel.TimingString);
        //setText(2, String.valueOf(routeResponse.AccompanyCount));
        setText(2, routeResponse.SuggestRouteModel.PricingString);
        setText(3, routeResponse.SuggestRouteModel.CarString);
        setText(4, routeResponse.SuggestRouteModel.SrcDistance);
        setText(5, routeResponse.SuggestRouteModel.DstDistance);

        ((CheckSuggestRouteActivity) activity).getRouteImage(bootstrapImageView(6), routeResponse.SuggestRouteModel.RouteId);
    }
}
