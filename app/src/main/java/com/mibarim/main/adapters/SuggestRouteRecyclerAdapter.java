package com.mibarim.main.adapters;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.AwesomeTextView;
import com.beardedhen.androidbootstrap.BootstrapCircleThumbnail;
import com.mibarim.main.R;
import com.mibarim.main.models.Route.BriefRouteModel;
import com.mibarim.main.models.Route.RouteResponse;
import com.mibarim.main.ui.activities.SuggestRouteActivity;
import com.mibarim.main.ui.activities.SuggestRouteCardActivity;
import com.mibarim.main.ui.fragments.RouteCardFragment;
import com.mibarim.main.ui.fragments.routeFragments.SuggestRouteCardFragment;

import java.util.List;

import cn.nekocode.badge.BadgeDrawable;

/**
 * Created by Hamed on 10/16/2016.
 */
public class SuggestRouteRecyclerAdapter extends RecyclerView.Adapter<SuggestRouteRecyclerAdapter.ViewHolder> {
    private List<BriefRouteModel> items;
    private Activity _activity;
    private SuggestRouteCardFragment.ItemTouchListener onItemTouchListener;
    //private RelativeLayout lastLayout;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        //public RelativeLayout row_layout;
        public TextView username;
        public TextView timing;
        public TextView pricing;
        public TextView src_address;
        public TextView dst_address;
        //public TextView carString;
        /*public TextView src_distance;
        public TextView dst_distance;*/
        public BootstrapCircleThumbnail userimage;


        public ViewHolder(View v) {
            super(v);
            //row_layout = (RelativeLayout) v.findViewById(R.id.row_layout);
            username = (TextView) v.findViewById(R.id.username);
            timing = (TextView) v.findViewById(R.id.timing);
            pricing = (TextView) v.findViewById(R.id.pricing);
            src_address = (TextView) v.findViewById(R.id.src_address);
            dst_address = (TextView) v.findViewById(R.id.dst_address);
            /*carString = (TextView) v.findViewById(R.id.carString);
            src_distance = (TextView) v.findViewById(R.id.src_distance);
            dst_distance = (TextView) v.findViewById(R.id.dst_distance);*/
            userimage = (BootstrapCircleThumbnail) v.findViewById(R.id.userimage);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemTouchListener.onCardViewTap(v, getPosition());
                    /*if (lastLayout != null) {
                        lastLayout.setSelected(false);
                    }
                    row_layout.setSelected(true);
                    lastLayout = row_layout;*/
                }
            });
            userimage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemTouchListener.onUserImageClick(v, getPosition());
                }
            });
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public SuggestRouteRecyclerAdapter(Activity activity, List<BriefRouteModel> list, SuggestRouteCardFragment.ItemTouchListener onItemTouchListener) {
        _activity = activity;
        items = list;
        this.onItemTouchListener = onItemTouchListener;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public SuggestRouteRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                     int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.suggest_mat_card_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        if (items.get(position).IsSuggestSeen) {
            holder.timing.setText(items.get(position).TimingString);
        } else {
            BadgeDrawable drawableBadge =
                    new BadgeDrawable.Builder()
                            .type(BadgeDrawable.TYPE_ONLY_ONE_TEXT)
                            .badgeColor(0xFFFF0004)
                            .text1("جدید")
                            .build();
            SpannableString spannableString =
                    new SpannableString(TextUtils.concat(
                            items.get(position).TimingString,
                            " ",
                            drawableBadge.toSpannable()
                    ));

            holder.timing.setText(spannableString);
        }
        if (items.get(position).IsVerified) {
            BadgeDrawable drawableBadge =
                    new BadgeDrawable.Builder()
                            .type(BadgeDrawable.TYPE_ONLY_ONE_TEXT)
                            .badgeColor(0xFF77FF00)
                            .text1("✔")
                            .build();
            SpannableString spannableString =
                    new SpannableString(TextUtils.concat(
                            items.get(position).Name,
                            " ",
                            items.get(position).Family,
                            " ",
                            drawableBadge.toSpannable()
                    ));

            holder.username.setText(spannableString);
        } else {
            holder.username.setText(items.get(position).Name + " " + items.get(position).Family);
        }
        holder.userimage.setImageBitmap(((SuggestRouteCardActivity) _activity).getImageById(items.get(position).UserImageId, R.mipmap.ic_user_black));

        holder.src_address.setText(items.get(position).SrcAddress);
        holder.dst_address.setText(items.get(position).DstAddress);
        //holder.carString.setText(items.get(position).CarString);
        holder.pricing.setText(items.get(position).PricingString);
        /*holder.src_distance.setText(items.get(position).SrcDistance);
        holder.dst_distance.setText(items.get(position).DstDistance);*/

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return items.size();
    }
}
