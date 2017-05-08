package com.mibarim.main.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.AwesomeTextView;
import com.beardedhen.androidbootstrap.BootstrapCircleThumbnail;
import com.mibarim.main.R;
import com.mibarim.main.models.ContactModel;
import com.mibarim.main.models.Route.RouteResponse;
import com.mibarim.main.ui.activities.MainActivity;
import com.mibarim.main.ui.fragments.ContactCardFragment;
import com.mibarim.main.ui.fragments.RouteCardFragment;

import java.util.List;

import cn.nekocode.badge.BadgeDrawable;

/**
 * Created by Hamed on 10/16/2016.
 */
public class ContactListRecyclerAdapter extends RecyclerView.Adapter<ContactListRecyclerAdapter.ViewHolder> {
    private List<ContactModel> items;
    private Activity _activity;
    private ContactCardFragment.ItemTouchListener onItemTouchListener;


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public BootstrapCircleThumbnail userimage;

        public LinearLayout contact_card_layout;
        public TextView username;
        public TextView last_msg;
        public TextView last_time;

        public ViewHolder(View v) {
            super(v);
            userimage = (BootstrapCircleThumbnail) v.findViewById(R.id.userimage);
            contact_card_layout = (LinearLayout) v.findViewById(R.id.contact_card_layout);
            username = (TextView) v.findViewById(R.id.username);
            last_msg = (TextView) v.findViewById(R.id.last_msg);
            last_time = (TextView) v.findViewById(R.id.last_time);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemTouchListener.onCardViewTap(v, getPosition());
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
    public ContactListRecyclerAdapter(Activity activity,List<ContactModel> list, ContactCardFragment.ItemTouchListener onItemTouchListener) {
        items = list;
        this.onItemTouchListener=onItemTouchListener;
        _activity=activity;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ContactListRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                  int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_card_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }



    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        if(items.get(position).NewChats==0){
            holder.username.setText(items.get(position).Name+" "+items.get(position).Family);
        }else {
            BadgeDrawable drawableBadge =
                    new BadgeDrawable.Builder()
                            .type(BadgeDrawable.TYPE_NUMBER)
                            .number(items.get(position).NewChats)
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
        }
        if(items.get(position).IsRideAccepted==1 &&items.get(position).IsPassengerAccepted==1){
            holder.contact_card_layout.setBackgroundColor(_activity.getResources().getColor(R.color.active_trip));
        }
        else if(items.get(position).IsDriver==1 && items.get(position).IsRideAccepted==1){
            holder.contact_card_layout.setBackgroundColor(_activity.getResources().getColor(R.color.active_row));
        }
        else if(items.get(position).IsDriver==0 && items.get(position).IsPassengerAccepted==1){
            holder.contact_card_layout.setBackgroundColor(_activity.getResources().getColor(R.color.active_row));
        }
        holder.userimage.setImageBitmap(((MainActivity)_activity).getImageById(items.get(position).UserImageId,R.mipmap.ic_user_black));
        //holder.username.setText(items.get(position).Name+" "+items.get(position).Family);
        holder.last_msg.setText(items.get(position).LastMsg);
        holder.last_time.setText(items.get(position).LastMsgTime);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return items.size();
    }
}
