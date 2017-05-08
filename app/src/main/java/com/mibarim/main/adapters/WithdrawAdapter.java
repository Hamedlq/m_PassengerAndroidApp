package com.mibarim.main.adapters;

import android.text.TextUtils;
import android.view.LayoutInflater;

import com.mibarim.main.R;
import com.mibarim.main.models.UserInfo.DiscountModel;
import com.mibarim.main.models.UserInfo.WithdrawRequestModel;
import com.mibarim.main.util.SingleTypeAdapter;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Adapter to display a list of traffic items
 */
public class WithdrawAdapter extends SingleTypeAdapter<WithdrawRequestModel> {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MMMM dd");

    /**
     * @param inflater
     * @param items
     */
    public WithdrawAdapter(final LayoutInflater inflater, final List<WithdrawRequestModel> items) {
        super(inflater, R.layout.withdraw_list_item);

        setItems(items);
    }

    /**
     * @param inflater
     */
    public WithdrawAdapter(final LayoutInflater inflater) {
        this(inflater, null);

    }

    @Override
    public long getItemId(final int position) {
        final String id = String.valueOf(getItem(position));
        return !TextUtils.isEmpty(id) ? id.hashCode() : super
                .getItemId(position);
    }

    @Override
    protected int[] getChildViewIds() {
        return new int[]{R.id.withdraw_state,R.id.withdraw_date,R.id.withdraw_amount};
    }

    @Override
    protected void update(final int position, final WithdrawRequestModel withdrawRequestModel) {
        setText(0, withdrawRequestModel.WithdrawStateString);
        setText(1, withdrawRequestModel.WithdrawDate);
        setText(2, withdrawRequestModel.WithdrawAmount+ "ریال");
/*
        setText(0, eventResponse.Name);
        setText(1, eventResponse.Address);
        setText(2, eventResponse.TimeString);
        setText(3, eventResponse.Conductor);
        setText(4, eventResponse.Description);
*/
/*
        setText(3, String.valueOf(eventResponse.AccompanyCount));
        setText(4, eventResponse.PricingString);
        setText(5, eventResponse.CarString);
*/
    }

}
