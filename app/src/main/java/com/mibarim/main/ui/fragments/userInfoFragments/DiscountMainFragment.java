package com.mibarim.main.ui.fragments.userInfoFragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.mibarim.main.BootstrapApplication;
import com.mibarim.main.R;
import com.mibarim.main.ui.activities.LocationSearchActivity;
import com.mibarim.main.ui.activities.UserInfoDetailActivity;
import com.mibarim.main.ui.fragments.addRouteFragments.LocationListFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Hamed on 3/5/2016.
 */
public class DiscountMainFragment extends Fragment {

    private RelativeLayout layout;
    @Bind(R.id.discount_btn)
    protected BootstrapButton discount_btn;
    @Bind(R.id.discount_txt)
    protected EditText discount_txt;

    public DiscountMainFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BootstrapApplication.component().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = (RelativeLayout) inflater.inflate(R.layout.fragment_main_discount, container, false);
        initScreen();
        return layout;
    }

    private void initScreen() {
        final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.discount_list, new DiscountListFragment())
                .commit();
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ButterKnife.bind(this, getView());
        discount_txt.requestFocus();
        discount_btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    String discountCode = discount_txt.getText().toString();
                    ((UserInfoDetailActivity) getActivity()).submitDiscountCode(discountCode);
                    return true;
                }
                return false;
            }
        });
    }



    public void ClearCode() {
        discount_txt.setText("");
        final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.discount_list, new DiscountListFragment())
                .addToBackStack(null)
                .commit();
    }
}
