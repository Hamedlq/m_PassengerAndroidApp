package com.mibarim.main.ui.fragments.userInfoFragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.mibarim.main.BootstrapApplication;
import com.mibarim.main.R;
import com.mibarim.main.ui.activities.UserInfoDetailActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Hamed on 3/5/2016.
 */
public class WithdrawMainFragment extends Fragment {

    private RelativeLayout layout;
    @Bind(R.id.withdraw_btn)
    protected BootstrapButton withdraw_btn;
    @Bind(R.id.withdraw_txt)
    protected EditText withdraw_txt;

    public WithdrawMainFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BootstrapApplication.component().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = (RelativeLayout) inflater.inflate(R.layout.fragment_main_withdraw, container, false);
        initScreen();
        return layout;
    }

    private void initScreen() {
        final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.withdraw_list, new WithdrawListFragment())
                .commit();
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ButterKnife.bind(this, getView());
        withdraw_txt.requestFocus();
        withdraw_btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    String withdrawAmount = withdraw_txt.getText().toString();
                    ((UserInfoDetailActivity) getActivity()).submitWithdrawRequest(withdrawAmount);
                    return true;
                }
                return false;
            }
        });
    }



    public void ClearCode() {
        withdraw_txt.setText("");
        final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.withdraw_list, new WithdrawListFragment())
                .addToBackStack(null)
                .commit();
    }
}
