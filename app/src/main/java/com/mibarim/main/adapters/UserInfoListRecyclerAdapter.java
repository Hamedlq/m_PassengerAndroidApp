package com.mibarim.main.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.AwesomeTextView;
import com.mibarim.main.R;
import com.mibarim.main.models.Route.RouteResponse;
import com.mibarim.main.models.UserCardModel;
import com.mibarim.main.ui.fragments.RouteCardFragment;
import com.mibarim.main.ui.fragments.userInfoFragments.UserInfoCardFragment;

import java.util.List;

/**
 * Created by Hamed on 10/16/2016.
 */
public class UserInfoListRecyclerAdapter extends RecyclerView.Adapter<UserInfoListRecyclerAdapter.ViewHolder> {
    private List<UserCardModel> items;
    //private Activity _activity;
    private UserInfoCardFragment.ItemTouchListener onItemTouchListener;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView title;
        public TextView pricing;
        public ImageView icon;


        public ViewHolder(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.title);
            pricing = (TextView) v.findViewById(R.id.pricing);
            pricing = (TextView) v.findViewById(R.id.pricing);
            icon = (ImageView) v.findViewById(R.id.icon);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemTouchListener.onCardViewTap(v, getPosition());
                }
            });
/*
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
*/

            /*pricing.setText(_pricing);
            src_address.setText(_src_address);
            dst_address.setText(_dst_address);
            sent_suggests.setText(_sent_suggests);*/
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public UserInfoListRecyclerAdapter(List<UserCardModel> list, UserInfoCardFragment.ItemTouchListener onItemTouchListener) {
        items = list;
        this.onItemTouchListener=onItemTouchListener;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public UserInfoListRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                  int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_info_card_list_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }



    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.title.setText(items.get(position).CardTitle);
        holder.pricing.setText(items.get(position).CardDiscount);
        holder.icon.setImageDrawable(items.get(position).CardIcon);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return items.size();
    }
}
