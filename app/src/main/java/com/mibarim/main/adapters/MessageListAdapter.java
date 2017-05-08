package com.mibarim.main.adapters;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;

import com.mibarim.main.R;
import com.mibarim.main.ui.activities.MessagingActivity;
import com.mibarim.main.models.MessageModel;
import com.mibarim.main.util.SingleTypeTwoViewAdapter;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Adapter to display a list of traffic items
 */
public class MessageListAdapter extends SingleTypeTwoViewAdapter<MessageModel> {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MMMM dd");

    private Activity activity;

    /**
     * @param inflater
     * @param items
     */
    public MessageListAdapter(final LayoutInflater inflater, final List<MessageModel> items) {
        super(inflater, R.layout.message_left, R.layout.message_right, true);

        setItems(items);
    }
    public MessageListAdapter(final LayoutInflater inflater, final List<MessageModel> items,Activity activity) {
        super(inflater, R.layout.message_left, R.layout.message_right, true);
        this.activity=activity;
        setItems(items);
    }

    /**
     * @param inflater
     */
    public MessageListAdapter(final LayoutInflater inflater) {
        this(inflater, null);

    }

    @Override
    public long getItemId(final int position) {
        final String id = String.valueOf(getItem(position).CommentId);
        return !TextUtils.isEmpty(id) ? id.hashCode() : super
                .getItemId(position);
    }

    @Override
    protected int[] getChildViewIds() {
        return new int[]{R.id.imageView, R.id.txtMessage, R.id.txtSender, R.id.txtDate};
    }

    @Override
    protected void update(final int position, final MessageModel messageModel) {

        //((MessagingActivity) activity).getMesssageImage(bootstrapImageView(0), messageModel.CommentId );
        bootstrapImageView(0).setImageBitmap(((MessagingActivity) activity).getImageById(messageModel.UserImageId));
        setText(1, messageModel.Comment);
        setText(2, messageModel.NameFamily);
        setText(3, messageModel.TimingString);
    }

    @Override
    protected void setView(final MessageModel messageModel) {
        if(messageModel.IsDeletable){
            setLayoutSelect(true);
        }else {
            setLayoutSelect(false);
        }
    }

}
