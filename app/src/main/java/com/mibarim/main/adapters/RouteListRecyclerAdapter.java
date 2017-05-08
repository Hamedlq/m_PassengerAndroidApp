package com.mibarim.main.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.AwesomeTextView;
import com.mibarim.main.R;
import com.mibarim.main.models.Route.RouteResponse;
import com.mibarim.main.ui.activities.MainActivity;
import com.mibarim.main.ui.fragments.RouteCardFragment;
import com.mibarim.main.util.Strings;

import java.util.List;

/**
 * Created by Hamed on 10/16/2016.
 */
public class RouteListRecyclerAdapter extends RecyclerView.Adapter<RouteListRecyclerAdapter.ViewHolder> {
    private List<RouteResponse> items;
    //private Activity _activity;
    private RouteCardFragment.ItemTouchListener onItemTouchListener;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView timing;
        public TextView date;
        public TextView pricing;
        public TextView src_address;
        public TextView dst_address;
        public TextView sent_suggests;
        public LinearLayout share_btn;
        public LinearLayout delete_btn;
        public AwesomeTextView fa_trash;
        public AwesomeTextView fa_share;
        public AwesomeTextView fa_cab;
        public AwesomeTextView fa_male;



        public ViewHolder(View v) {
            super(v);
            timing = (TextView) v.findViewById(R.id.timing);
            date = (TextView) v.findViewById(R.id.date);
            pricing = (TextView) v.findViewById(R.id.pricing);
            src_address = (TextView) v.findViewById(R.id.src_address);
            dst_address = (TextView) v.findViewById(R.id.dst_address);
            sent_suggests = (TextView) v.findViewById(R.id.sent_suggests);
            share_btn = (LinearLayout) v.findViewById(R.id.share_btn);
            delete_btn = (LinearLayout) v.findViewById(R.id.delete_btn);
            fa_trash = (AwesomeTextView) v.findViewById(R.id.fa_trash);
            fa_share = (AwesomeTextView) v.findViewById(R.id.fa_share);
            fa_cab = (AwesomeTextView) v.findViewById(R.id.fa_cab);
            fa_male = (AwesomeTextView) v.findViewById(R.id.fa_male);

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
    public RouteListRecyclerAdapter(List<RouteResponse> list,RouteCardFragment.ItemTouchListener onItemTouchListener) {
        items = list;
        this.onItemTouchListener=onItemTouchListener;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RouteListRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                  int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_list_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }



    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.timing.setText(items.get(position).TimingString);
        holder.date.setText(items.get(position).DateString);
        holder.pricing.setText(items.get(position).PricingString);
        holder.src_address.setText(items.get(position).SrcAddress);
        holder.dst_address.setText(items.get(position).DstAddress);
        if(items.get(position).IsDrive){
            holder.fa_male.setVisibility(View.VISIBLE);
            holder.fa_cab.setVisibility(View.GONE);
            holder.sent_suggests.setText(String.valueOf(items.get(position).SuggestCount)+ "\r\n" + "هم مسیر");
        }else {
            holder.fa_male.setVisibility(View.GONE);
            holder.fa_cab.setVisibility(View.VISIBLE);
            holder.sent_suggests.setText(String.valueOf(items.get(position).SuggestCount)+ "\r\n" + "هم مسیر");
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return items.size();
    }
}
