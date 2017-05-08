package com.mibarim.main.adapters;

import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;

import com.mibarim.main.R;
import com.mibarim.main.models.EventResponse;
import com.mibarim.main.util.SingleTypeAdapter;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Adapter to display a list of traffic items
 */
public class EventListAdapter extends SingleTypeAdapter<EventResponse> {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MMMM dd");

    /**
     * @param inflater
     * @param items
     */
    public EventListAdapter(final LayoutInflater inflater, final List<EventResponse> items) {
        super(inflater, R.layout.event_list_item);

        setItems(items);
    }

    /**
     * @param inflater
     */
    public EventListAdapter(final LayoutInflater inflater) {
        this(inflater, null);

    }

    @Override
    public long getItemId(final int position) {
        final String id = String.valueOf(getItem(position).EventId);
        return !TextUtils.isEmpty(id) ? id.hashCode() : super
                .getItemId(position);
    }

    @Override
    protected int[] getChildViewIds() {
        return new int[]{R.id.event_name, R.id.event_time,R.id.event_link,R.id.event_desc};
    }

    @Override
    protected void update(final int position, final EventResponse eventResponse) {

        String text = "<a href=\""+eventResponse.ExternalLink+"\">Link</a>";
        setText(0, eventResponse.Name);
        setText(1, eventResponse.StartTimeString);
        setText(2, eventResponse.ExternalLink);
        setText(3, eventResponse.Description);
/*
        setText(3, String.valueOf(eventResponse.AccompanyCount));
        setText(4, eventResponse.PricingString);
        setText(5, eventResponse.CarString);
*/
    }

}
