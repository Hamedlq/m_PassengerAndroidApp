package com.mibarim.main.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.AwesomeTextView;
import com.mibarim.main.R;
import com.mibarim.main.models.Route.RouteResponse;
import com.mibarim.main.ui.fragments.RouteCardFragment;
import com.mibarim.main.ui.fragments.WeekRouteCardFragment;

import java.util.List;

import cn.nekocode.badge.BadgeDrawable;

/**
 * Created by Hamed on 10/16/2016.
 */
public class WeekRouteListRecyclerAdapter extends RecyclerView.Adapter<WeekRouteListRecyclerAdapter.ViewHolder> {
    private List<RouteResponse> items;
    private Context _context;
    //private Activity _activity;
    private WeekRouteCardFragment.ItemTouchListener onItemTouchListener;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView timing;
        //public TextView date;
        public TextView pricing;
        public TextView src_address;
        public TextView dst_address;
        public TextView sent_suggests;
        public LinearLayout share_btn;
        public LinearLayout delete_btn;
        public AwesomeTextView fa_trash;
        public AwesomeTextView fa_share;
        /*public TextView fa_cab;
        public TextView fa_male;*/
        public TextView sat;
        public TextView sun;
        public TextView mon;
        public TextView tue;
        public TextView wed;
        public TextView thu;
        public TextView fri;

        public ViewHolder(View v) {
            super(v);
            timing = (TextView) v.findViewById(R.id.timing);
            //date = (TextView) v.findViewById(R.id.date);
            pricing = (TextView) v.findViewById(R.id.pricing);
            src_address = (TextView) v.findViewById(R.id.src_address);
            dst_address = (TextView) v.findViewById(R.id.dst_address);
            sent_suggests = (TextView) v.findViewById(R.id.sent_suggests);
            share_btn = (LinearLayout) v.findViewById(R.id.share_btn);
            delete_btn = (LinearLayout) v.findViewById(R.id.delete_btn);
            fa_trash = (AwesomeTextView) v.findViewById(R.id.fa_trash);
            fa_share = (AwesomeTextView) v.findViewById(R.id.fa_share);
            /*fa_cab = (TextView) v.findViewById(R.id.fa_cab);
            fa_male = (TextView) v.findViewById(R.id.fa_male);*/
            sat = (TextView) v.findViewById(R.id.sat);
            sun = (TextView) v.findViewById(R.id.sun);
            mon = (TextView) v.findViewById(R.id.mon);
            tue = (TextView) v.findViewById(R.id.tue);
            wed = (TextView) v.findViewById(R.id.wed);
            thu = (TextView) v.findViewById(R.id.thu);
            fri = (TextView) v.findViewById(R.id.fri);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemTouchListener.onCardViewTap(v, getPosition());
                }
            });
            share_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemTouchListener.onShareBtnClick(v, getPosition());
                }
            });
            fa_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemTouchListener.onShareBtnClick(v, getPosition());
                }
            });
            delete_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemTouchListener.onDeleteBtnClick(v, getPosition());
                }
            });
            fa_trash.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemTouchListener.onDeleteBtnClick(v, getPosition());
                }
            });

            /*pricing.setText(_pricing);
            src_address.setText(_src_address);
            dst_address.setText(_dst_address);
            sent_suggests.setText(_sent_suggests);*/
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
/*    public WeekRouteListRecyclerAdapter(List<RouteResponse> list, WeekRouteCardFragment.ItemTouchListener onItemTouchListener) {
        items = list;
        this.onItemTouchListener = onItemTouchListener;
    }*/

    public WeekRouteListRecyclerAdapter(List<RouteResponse> list, WeekRouteCardFragment.ItemTouchListener onItemTouchListener, Context context) {
        items = list;
        this.onItemTouchListener = onItemTouchListener;
        this._context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public WeekRouteListRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                      int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.week_card_list_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        if(items.get(position).NewSuggestCount>0) {
            BadgeDrawable drawableBadge =
                    new BadgeDrawable.Builder()
                            .type(BadgeDrawable.TYPE_WITH_TWO_TEXT_COMPLEMENTARY)
                            .badgeColor(0xFFFF0004)
                            .text1("جدید")
                            .text2(" " + items.get(position).NewSuggestCount + " ")
                            .build();
            SpannableString passString =
                    new SpannableString(TextUtils.concat(
                            String.valueOf(items.get(position).SuggestCount) + " " + _context.getString(R.string.passenger),
                            "  ",
                            drawableBadge.toSpannable()
                    ));
            SpannableString driverString =
                    new SpannableString(TextUtils.concat(
                            String.valueOf(items.get(position).SuggestCount) + " " + _context.getString(R.string.driver),
                            "  ",
                            drawableBadge.toSpannable()
                    ));
            if (items.get(position).IsDrive) {
            /*holder.fa_male.setVisibility(View.VISIBLE);
            holder.fa_cab.setVisibility(View.GONE);*/
                holder.sent_suggests.setText(driverString);
            } else {
            /*holder.fa_male.setVisibility(View.GONE);
            holder.fa_cab.setVisibility(View.VISIBLE);*/
                holder.sent_suggests.setText(passString);
            }
        }else {
            if (items.get(position).IsDrive) {
            /*holder.fa_male.setVisibility(View.VISIBLE);
            holder.fa_cab.setVisibility(View.GONE);*/
                holder.sent_suggests.setText(String.valueOf(items.get(position).SuggestCount) + " " + _context.getString(R.string.driver));
            } else {
            /*holder.fa_male.setVisibility(View.GONE);
            holder.fa_cab.setVisibility(View.VISIBLE);*/
                holder.sent_suggests.setText(String.valueOf(items.get(position).SuggestCount) + " " + _context.getString(R.string.passenger));
            }
        }
        holder.timing.setText(items.get(position).TimingString);
        //holder.date.setText(items.get(position).DateString);
        holder.pricing.setText(items.get(position).PricingString);
        holder.src_address.setText(items.get(position).SrcAddress);
        holder.dst_address.setText(items.get(position).DstAddress);

        if (items.get(position).Sat) {
            holder.sat.setBackgroundDrawable(getResource(R.drawable.roundedgraybutton));
        } else {
            holder.sat.setBackgroundDrawable(getResource(R.drawable.roundedwhitebutton));
        }
        if (items.get(position).Sun) {
            holder.sun.setBackgroundDrawable(getResource(R.drawable.roundedgraybutton));
        } else {
            holder.sun.setBackgroundDrawable(getResource(R.drawable.roundedwhitebutton));
        }
        if (items.get(position).Mon) {
            holder.mon.setBackgroundDrawable(getResource(R.drawable.roundedgraybutton));
        } else {
            holder.mon.setBackgroundDrawable(getResource(R.drawable.roundedwhitebutton));
        }
        if (items.get(position).Tue) {
            holder.tue.setBackgroundDrawable(getResource(R.drawable.roundedgraybutton));
        } else {
            holder.tue.setBackgroundDrawable(getResource(R.drawable.roundedwhitebutton));
        }
        if (items.get(position).Wed) {
            holder.wed.setBackgroundDrawable(getResource(R.drawable.roundedgraybutton));
        } else {
            holder.wed.setBackgroundDrawable(getResource(R.drawable.roundedwhitebutton));
        }
        if (items.get(position).Thu) {
            holder.thu.setBackgroundDrawable(getResource(R.drawable.roundedgraybutton));
        } else {
            holder.thu.setBackgroundDrawable(getResource(R.drawable.roundedwhitebutton));
        }
        if (items.get(position).Fri) {
            holder.fri.setBackgroundDrawable(getResource(R.drawable.roundedgraybutton));
        } else {
            holder.fri.setBackgroundDrawable(getResource(R.drawable.roundedwhitebutton));
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return items.size();
    }

    private Drawable getResource(int id) {
        return _context.getResources().getDrawable(id);
    }
}
