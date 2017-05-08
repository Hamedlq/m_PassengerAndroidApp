package com.mibarim.main.adapters;

import android.text.TextUtils;
import android.view.LayoutInflater;

import com.mibarim.main.R;
import com.mibarim.main.models.Address.Place;
import com.mibarim.main.util.SingleTypeAdapter;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Adapter to display a list of traffic items
 */
public class LocationAdapter extends SingleTypeAdapter<Place> {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MMMM dd");

    /**
     * @param inflater
     * @param items
     */
    public LocationAdapter(final LayoutInflater inflater, final List<Place> items) {
        super(inflater, R.layout.location_list_item);

        setItems(items);
    }

    /**
     * @param inflater
     */
    public LocationAdapter(final LayoutInflater inflater) {
        this(inflater, null);

    }

    @Override
    public long getItemId(final int position) {
        final String id = String.valueOf(getItem(position));
        return !TextUtils.isEmpty(id) ? id.hashCode() : super
                .getItemId(position);
    }

    @Override
    protected int[] getChildViewIds() {
        return new int[]{R.id.location_name};
    }

    @Override
    protected void update(final int position, final Place place) {
        setText(0, place.description);
/*
        setText(0, eventResponse.Name);
        setText(1, eventResponse.Address);
        setText(2, eventResponse.TimeString);
        setText(3, eventResponse.Conductor);
        setText(4, eventResponse.Description);
*/
/*
        setText(3, String.valueOf(eventResponse.AccompanyCount));
        setText(4, eventResponse.PricingString);
        setText(5, eventResponse.CarString);
*/
    }

}
