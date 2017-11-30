package com.mibarim.main.adapters;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mibarim.main.R;
import com.mibarim.main.models.FilterModel;
import com.mibarim.main.ui.activities.MainActivity;
import com.mibarim.main.ui.fragments.RouteDetailsFragment;
import com.mibarim.main.ui.fragments.RouteFilterFragment;
import com.mibarim.main.ui.fragments.SuggestedTimesFragment;

import java.util.List;
import java.util.Random;

/**
 * Created by Alireza on 11/20/2017.
 */

public class RouteFilterAdapter extends ArrayAdapter<FilterModel> {
    Context myContext;


    public RouteFilterAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<FilterModel> objects) {
        super(context, resource, objects);
        myContext = context;

    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_item, null);
        }

        final FilterModel listItem = getItem(position);


        final TextView sourceText = (TextView) view.findViewById(R.id.source_name);
        final TextView destText = (TextView) view.findViewById(R.id.destination_name);

        TextView suggestTime = (TextView) view.findViewById(R.id.suggest_time);

        TextView hourTime = (TextView) view.findViewById(R.id.hour_time);
        TextView minuteTime = (TextView) view.findViewById(R.id.minute_time);

        TextView observeTrip = (TextView) view.findViewById(R.id.observe_trip);

        ImageView deleteRoute = (ImageView) view.findViewById(R.id.delete_route);

        deleteRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long filterId = listItem.FilterId;

                ((MainActivity) myContext).deleteTheRoute(filterId);

            }
        });

        if (listItem.IsActive) {
            observeTrip.setVisibility(View.VISIBLE);
        }

        observeTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((MainActivity) myContext).hideFloatingActionButton();

                RouteDetailsFragment routeDetailsFragment = new RouteDetailsFragment();

                Bundle bundle = new Bundle();

                bundle.putString("SOURCE_TEXT", sourceText.getText().toString());
                bundle.putString("DESTINATION_TEXT", destText.getText().toString());

                routeDetailsFragment.setArguments(bundle);


                FragmentManager fragmentManager = ((MainActivity) myContext).getSupportFragmentManager();
                ((MainActivity) myContext).chosenFilter(listItem.FilterId);
                fragmentManager.beginTransaction()
                        .replace(R.id.main_activity, routeDetailsFragment)
                        .addToBackStack("Payment")
//                        .addSharedElement(imageView, imageTransitionName)
//                        .addSharedElement(textView, textTransitionName)
                        .addSharedElement(sourceText, "textTransitionNameForDest")
                        .addSharedElement(destText, "whatsoever")
                        .commit();
            }
        });


        suggestTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Toast.makeText(getContext(), listItem.FilterId + "  sdfsdf", Toast.LENGTH_SHORT).show();


                if (!listItem.IsActive) {

                    ((MainActivity) myContext).hideFloatingActionButton();
                    SuggestedTimesFragment suggestedTimesFragment = new SuggestedTimesFragment();
//                Context context = parent.getContext();
                    FragmentManager fragmentManager = ((MainActivity) myContext).getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .addToBackStack("ad")
                            .replace(R.id.main_activity, suggestedTimesFragment)
                            .commit();

                    ((MainActivity) myContext).chosenFilter(listItem.FilterId);
                } else {
                    ((MainActivity) myContext).cancelTrip(listItem.FilterId);

                }


            }
        });


        sourceText.setText(listItem.SrcStation);
        destText.setText(listItem.DstStation);

        if (listItem.IsActive == true) {
            hourTime.setText(Integer.toString(listItem.TimeHour));
            minuteTime.setText(Integer.toString(listItem.TimeMinute));
            suggestTime.setText(R.string.cancel_trip);
            suggestTime.setBackgroundColor(myContext.getResources().getColor(R.color.defining_route_destination_color));
        } else {
            suggestTime.setText(R.string.request_trip);
            suggestTime.setBackgroundColor(myContext.getResources().getColor(R.color.defining_route_origin_color));
        }

//        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
//        imageView.setImageResource(getRandomImage());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            sourceText.setTransitionName("trans_text" + position);
//            imageView.setTransitionName("trans_image" + position);
            destText.setTransitionName("dest_trans" + position);


        }

        return view;
    }

    private int getRandomImage() {
        if (new Random().nextInt(2) == 1)
            return R.drawable.aa_logo_blue;
        return R.drawable.aa_logo_green;
    }
}

