package com.mibarim.main.ui.fragments.userInfoFragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.brnunes.swipeablerecyclerview.SwipeableRecyclerViewTouchListener;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.mibarim.main.BootstrapApplication;
import com.mibarim.main.R;
import com.mibarim.main.adapters.RouteListRecyclerAdapter;
import com.mibarim.main.adapters.UserInfoListRecyclerAdapter;
import com.mibarim.main.authenticator.LogoutService;
import com.mibarim.main.data.UserData;
import com.mibarim.main.models.Route.RouteResponse;
import com.mibarim.main.models.UserCardModel;
import com.mibarim.main.models.enums.UserCardTypes;
import com.mibarim.main.ui.activities.MainActivity;
import com.mibarim.main.ui.activities.UserInfoActivity;
import com.mibarim.main.util.Toaster;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

public class UserInfoCardFragment extends Fragment {

    @Inject
    UserData userData;
    @Inject
    protected LogoutService logoutService;
    private int RELOAD_REQUEST = 1234;
    List<UserCardModel> items;
    private Tracker mTracker;

    private View mRecycler;
    private RecyclerView mRecyclerView;
    private TextView mEmptyView;
    private UserInfoListRecyclerAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private int selectedRow;
    ItemTouchListener itemTouchListener;


    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BootstrapApplication.component().inject(this);
        // Obtain the shared Tracker instance.
        BootstrapApplication application = (BootstrapApplication) getActivity().getApplication();
        mTracker = application.getDefaultTracker();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        mRecycler = inflater.inflate(R.layout.card_list, null);

        mRecyclerView = (RecyclerView) mRecycler.findViewById(android.R.id.list);
        mEmptyView = (TextView) mRecycler.findViewById(android.R.id.empty);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        items = loadData();

        if (items.size() == 0) {
            mEmptyView.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        } else {
            mEmptyView.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }

        itemTouchListener = new ItemTouchListener() {
            @Override
            public void onCardViewTap(View view, int position) {
                UserCardModel userCardModel = ((UserCardModel) items.get(position));
                ((UserInfoActivity) getActivity()).goToUserDetailActivity(userCardModel.CardType);
            }

        };

        // specify an adapter (see also next example)
        mAdapter = new UserInfoListRecyclerAdapter(items, itemTouchListener);
        mRecyclerView.setAdapter(mAdapter);
        /*mRecyclerView.addOnItemTouchListener(new SwipeableRecyclerViewTouchListener(mRecyclerView,
                new SwipeableRecyclerViewTouchListener.SwipeListener() {
                    @Override
                    public boolean canSwipeLeft(int position) {
                        return true;
                    }

                    @Override
                    public boolean canSwipeRight(int position) {
                        return true;
                    }

                    @Override
                    public void onDismissedBySwipeLeft(RecyclerView recyclerView, int[] reverseSortedPositions) {
                        *//*for (int position : reverseSortedPositions) {
                            selectedRow=position;
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setMessage("مسیر حذف شود").setPositiveButton("بله", dialogClickListener)
                                    .setNegativeButton("خیر", dialogClickListener).show();
                            *//**//*items.remove(position);
                            mAdapter.notifyItemRemoved(position);*//**//*
                        }*//*
                        //mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {
                        *//*for (int position : reverseSortedPositions) {
                            ((MainActivity) getActivity()).shareRoute(String.valueOf(items.get(position).RouteId));
                            //items.remove(position);
                            //mAdapter.notifyItemRemoved(position);
                        }*//*
                        //mAdapter.notifyDataSetChanged();
                    }
                }));*/

        return mRecycler;
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mTracker.setScreenName("RouteListFragment");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        mTracker.send(new HitBuilders.EventBuilder().setCategory("Fragment").setAction("RouteListFragment").build());
        //setEmptyText(R.string.no_routes);
    }

    public interface ItemTouchListener {
        public void onCardViewTap(View view, int position);
    }

    /*DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    ((MainActivity) getActivity()).deleteRoute(String.valueOf(items.get(selectedRow).RouteId));
                    items.remove(selectedRow);
                    mAdapter.notifyItemRemoved(selectedRow);
                    mAdapter.notifyDataSetChanged();
            break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    break;
            }
        }
    };*/

    private List<UserCardModel> loadData() {
        List<UserCardModel> list=new ArrayList<>();
        UserCardModel c1 = new UserCardModel();
        c1.CardTitle=getResources().getString(R.string.invite_friends);
        c1.CardIcon=getResources().getDrawable(R.mipmap.ic_friend);
        c1.CardType= UserCardTypes.InviteFriend;
        //c1.CardDiscount="200 "+getResources().getString(R.string.rial);
        list.add(c1);
        UserCardModel c2 = new UserCardModel();
        c2.CardTitle=getResources().getString(R.string.discount_code);
        c2.CardIcon=getResources().getDrawable(R.mipmap.ic_discount);
        c2.CardType= UserCardTypes.DiscountCode;
        list.add(c2);
        UserCardModel c3 = new UserCardModel();
        c3.CardTitle=getResources().getString(R.string.about_me);
        c3.CardIcon=getResources().getDrawable(R.mipmap.ic_aboutme);
        c3.CardType= UserCardTypes.AboutMe;
        //c3.CardDiscount="10000 "+getResources().getString(R.string.gift_amount);
        list.add(c3);
        UserCardModel c4 = new UserCardModel();
        c4.CardTitle=getResources().getString(R.string.user_info);
        c4.CardIcon=getResources().getDrawable(R.mipmap.ic_info);
        c4.CardType= UserCardTypes.UserInfo;
        //c4.CardDiscount="5000 "+getResources().getString(R.string.gift_amount);
        list.add(c4);
        UserCardModel c5 = new UserCardModel();
        c5.CardTitle=getResources().getString(R.string.license_info);
        c5.CardIcon=getResources().getDrawable(R.mipmap.ic_license);
        c5.CardType= UserCardTypes.LicenseInfo;
        //c5.CardDiscount="5000 "+getResources().getString(R.string.gift_amount);
        list.add(c5);
        UserCardModel c6 = new UserCardModel();
        c6.CardTitle=getResources().getString(R.string.car_info);
        c6.CardIcon=getResources().getDrawable(R.mipmap.ic_car);
        c6.CardType= UserCardTypes.CarInfo;
        //c6.CardDiscount="10000 "+getResources().getString(R.string.gift_amount);
        list.add(c6);
        UserCardModel c7 = new UserCardModel();
        c7.CardTitle=getResources().getString(R.string.bank_info);
        c7.CardIcon=getResources().getDrawable(R.mipmap.ic_bank);
        c7.CardType= UserCardTypes.BankInfo;
        list.add(c7);
        UserCardModel c8 = new UserCardModel();
        c8.CardTitle=getResources().getString(R.string.money_request);
        c8.CardIcon=getResources().getDrawable(R.mipmap.ic_withdraw);
        c8.CardType= UserCardTypes.WithDraw;
        list.add(c8);
        UserCardModel c9 = new UserCardModel();
        c9.CardTitle=getResources().getString(R.string.logout_action);
        c9.CardIcon=getResources().getDrawable(R.mipmap.ic_logout);
        c9.CardType= UserCardTypes.Exit;
        list.add(c9);
        return list;
    }

}
