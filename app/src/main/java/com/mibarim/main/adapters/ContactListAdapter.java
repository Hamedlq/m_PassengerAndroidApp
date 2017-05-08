package com.mibarim.main.adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;

import com.mibarim.main.R;
import com.mibarim.main.models.ContactModel;
import com.mibarim.main.models.EventResponse;
import com.mibarim.main.models.PersonalInfoModel;
import com.mibarim.main.ui.activities.MainActivity;
import com.mibarim.main.util.SingleTypeAdapter;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Adapter to display a list of traffic items
 */
public class ContactListAdapter extends SingleTypeAdapter<ContactModel> {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MMMM dd");
    private Activity activity;
    /**
     * @param inflater
     * @param items
     */
    public ContactListAdapter(final LayoutInflater inflater, final List<ContactModel> items) {
        super(inflater, R.layout.contact_list_item);

        setItems(items);
    }

    /**
     * @param inflater
     * @param items
     */
    public ContactListAdapter(final LayoutInflater inflater, final List<ContactModel> items,Activity activity) {
        super(inflater, R.layout.contact_list_item);
        this.activity=activity;
        setItems(items);
    }


    /**
     * @param inflater
     */
    public ContactListAdapter(final LayoutInflater inflater) {
        this(inflater, null);

    }

    @Override
    public long getItemId(final int position) {
        final String id = String.valueOf(getItem(position).ContactId);
        return !TextUtils.isEmpty(id) ? id.hashCode() : super
                .getItemId(position);
    }

    @Override
    protected int[] getChildViewIds() {
        return new int[]{R.id.userimage, R.id.username,R.id.last_msg,R.id.last_time};
    }

    @Override
    protected void update(final int position, final ContactModel contactModel) {

        /*if(contactModel.Base64UserPic!=null) {
            byte[] decodedString = Base64.decode(contactModel.Base64UserPic, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            bootstrapImageView(0).setImageBitmap(decodedByte);
        }*/
        ((MainActivity) activity).getContactImageFromServer(bootstrapImageView(0), contactModel.UserImageId);
        setText(1, contactModel.Name +" "+contactModel.Family);
        setText(2, contactModel.LastMsg);
        setText(3, contactModel.LastMsgTime);
/*
        setText(0, eventResponse.Name);
        setText(1, eventResponse.Address);
        setText(2, eventResponse.TimeString);
        setText(3, eventResponse.Conductor);
        setText(4, eventResponse.Description);

        setText(3, String.valueOf(eventResponse.AccompanyCount));
        setText(4, eventResponse.PricingString);
        setText(5, eventResponse.CarString);
*/
    }

}
