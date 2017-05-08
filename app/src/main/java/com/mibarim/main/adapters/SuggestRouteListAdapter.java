package com.mibarim.main.adapters;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;

import com.mibarim.main.R;
import com.mibarim.main.ui.activities.SuggestRouteActivity;
import com.mibarim.main.models.Route.BriefRouteModel;
import com.mibarim.main.util.SingleTypeAdapter;
//import com.squareup.okhttp.Route;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Adapter to display a list of traffic items
 */
public class SuggestRouteListAdapter extends SingleTypeAdapter<BriefRouteModel> {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MMMM dd");

    private Activity activity;

    private int selectedValue;

    /**
     * @param inflater
     * @param items
     */
    public SuggestRouteListAdapter(final LayoutInflater inflater, final List<BriefRouteModel> items) {
        super(inflater, R.layout.suggest_route_list_item);

        setItems(items);
    }

    public SuggestRouteListAdapter(final LayoutInflater inflater, final List<BriefRouteModel> items, Activity activity) {
        super(inflater, R.layout.suggest_route_list_item);
        this.activity = activity;
        setItems(items);
    }


    /**
     * @param inflater
     */
    public SuggestRouteListAdapter(final LayoutInflater inflater) {
        this(inflater, null);

    }

    @Override
    public long getItemId(final int position) {
        final String id = String.valueOf(getItem(position).RouteId);
        return !TextUtils.isEmpty(id) ? id.hashCode() : super
                .getItemId(position);
    }

    @Override
    protected int[] getChildViewIds() {
        return new int[]{R.id.username, R.id.timing, R.id.pricing, R.id.carString,R.id.src_distance,R.id.dst_distance, R.id.userimage};
    }

    @Override
    protected void update(final int position, final BriefRouteModel routeResponse) {

        setText(0, routeResponse.Name + " " + routeResponse.Family);
        setText(1, routeResponse.TimingString);
        //setText(2, String.valueOf(routeResponse.AccompanyCount));
        setText(2, routeResponse.PricingString);
        setText(3, routeResponse.CarString);
        setText(4, routeResponse.SrcDistance);
        setText(5, routeResponse.DstDistance);
        if (activity instanceof SuggestRouteActivity) {
            bootstrapImageView(6).setImageBitmap(((SuggestRouteActivity) activity).getImageById(routeResponse.UserImageId,R.mipmap.ic_camera));
            //((SuggestRouteActivity) activity).getRouteImage(bootstrapImageView(6), routeResponse.RouteId);
        }
    }
}
