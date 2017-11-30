package com.mibarim.main.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mibarim.main.R;
import com.mibarim.main.models.FilterModel;
import com.mibarim.main.models.FilterTimeModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alireza on 11/22/2017.
 */

public class SuggestedTimesAdapter extends ArrayAdapter<FilterTimeModel> {

    Context myContext;
    List<FilterTimeModel> items;

    public SuggestedTimesAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<FilterTimeModel> objects) {
        super(context, resource, objects);
        myContext = context;

        items = new ArrayList<>();
        items = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.suggested_times_list_item, null);
        }


        TextView priceStringTextview = (TextView) view.findViewById(R.id.price_string_textview);
        TextView timeHourTextview = (TextView) view.findViewById(R.id.time_hour_textview);
        TextView timeMinuteTextview = (TextView) view.findViewById(R.id.time_minute_textview);
        TextView pairPassengersTextview = (TextView) view.findViewById(R.id.pair_passengers);


        String hour = Integer.toString(items.get(position).TimeHour);
        String min = Integer.toString(items.get(position).TimeMinute);
        String pairPass = Integer.toString(items.get(position).PairPassengers);


        priceStringTextview.setText(items.get(position).PriceString);
        timeHourTextview.setText(hour);
        timeMinuteTextview.setText(min);
        pairPassengersTextview.setText(pairPass);



        return view;

    }
}
