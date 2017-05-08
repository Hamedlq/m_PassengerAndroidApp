package com.mibarim.main.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.mibarim.main.BootstrapApplication;
import com.mibarim.main.R;
import com.mibarim.main.ui.activities.CheckSuggestRouteActivity;
import com.mibarim.main.ui.activities.SuggestRouteActivity;
import com.mibarim.main.ui.fragments.routeFragments.CheckButtonsFragment;
import com.mibarim.main.ui.fragments.routeFragments.CheckSuggestListFragment;
import com.mibarim.main.ui.fragments.routeFragments.SuggestRouteButtonsFragment;
import com.mibarim.main.ui.fragments.routeFragments.SuggestRouteCardFragment;
import com.mibarim.main.ui.fragments.routeFragments.SuggestRouteListFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Hamed on 3/5/2016.
 */
public class MainGroupFragment extends Fragment {

    private LinearLayout layout;

    @Bind(R.id.check_button_fragment)
    protected FrameLayout check_button_fragment;
    @Bind(R.id.suggest_group_button_fragment)
    protected FrameLayout suggest_group_button_fragment;
    @Bind(R.id.suggest_route_button_fragment)
    protected FrameLayout suggest_route_button_fragment;


    @Bind(R.id.group_route_fragment)
    protected FrameLayout group_route_fragment;
    @Bind(R.id.check_route_fragment)
    protected FrameLayout check_route_fragment;


    public MainGroupFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BootstrapApplication.component().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = (LinearLayout) inflater.inflate(R.layout.fragment_main_group, container, false);
        initScreen();
        return layout;
    }

    private void initScreen() {
        final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .add(R.id.map_fragment, new MapFragment())
                .commit();

        if (getActivity() instanceof CheckSuggestRouteActivity) {
            fragmentManager.beginTransaction()
                    .add(R.id.check_route_fragment, new CheckSuggestListFragment())
                    .commit();
            fragmentManager.beginTransaction()
                    .add(R.id.check_button_fragment, new CheckButtonsFragment())
                    .commit();
        }
        /*if (getActivity() instanceof SuggestGroupActivity) {
            fragmentManager.beginTransaction()
                    .add(R.id.suggest_group_button_fragment, new SuggestGroupButtonsFragment())
                    .commit();
        }*/
        if (getActivity() instanceof SuggestRouteActivity) {
            fragmentManager.beginTransaction()
                    .add(R.id.group_route_fragment, new SuggestRouteCardFragment())
                    .commit();
            fragmentManager.beginTransaction()
                    .add(R.id.suggest_route_button_fragment, new SuggestRouteButtonsFragment())
                    .commit();
        }
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ButterKnife.bind(this, getView());
        suggest_group_button_fragment.setVisibility(View.GONE);
        suggest_route_button_fragment.setVisibility(View.GONE);
        check_button_fragment.setVisibility(View.GONE);
        check_route_fragment.setVisibility(View.GONE);
        group_route_fragment.setVisibility(View.GONE);

        if (getActivity() instanceof CheckSuggestRouteActivity) {
            check_button_fragment.setVisibility(View.VISIBLE);
            check_route_fragment.setVisibility(View.VISIBLE);
        } else if (getActivity() instanceof SuggestRouteActivity) {
            group_route_fragment.setVisibility(View.VISIBLE);
            suggest_route_button_fragment.setVisibility(View.VISIBLE);
        }
    }

/*    public void resetRouteListFragment() {
            final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.group_route_fragment, new SuggestRouteListFragment())
                    .addToBackStack(null)
                    .commitAllowingStateLoss();
            fragmentManager.executePendingTransactions();
    }*/
}
