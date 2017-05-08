package com.mibarim.main.ui.fragments.userInfoFragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.AwesomeTextView;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapCircleThumbnail;
import com.mibarim.main.BootstrapApplication;
import com.mibarim.main.R;
import com.mibarim.main.models.ContactModel;
import com.mibarim.main.models.PersonalInfoModel;
import com.mibarim.main.models.ScoreModel;
import com.mibarim.main.ui.activities.MainActivity;
import com.mibarim.main.ui.activities.UserContactActivity;
import com.mibarim.main.ui.fragments.InfoMessageFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Hamed on 3/5/2016.
 */
public class UserContactFragment extends Fragment {

    private RelativeLayout layout;

    @Bind(R.id.user_image)
    protected BootstrapCircleThumbnail user_image;
    @Bind(R.id.similar_route)
    protected BootstrapButton similar_route;
    /*@Bind(R.id.driver_accept)
    protected BootstrapButton driver_accept;*/
    @Bind(R.id.name_family)
    protected TextView name_family;
    @Bind(R.id.chat_layout)
    protected LinearLayout chat_layout;
    @Bind(R.id.chat)
    protected AwesomeTextView chat;
    @Bind(R.id.credit_layout)
    protected LinearLayout credit_layout;
    /*@Bind(R.id.driver_accept_layout)
    protected LinearLayout driver_accept_layout;*/
    /*@Bind(R.id.credit_money)
    protected TextView credit_money;*/
    @Bind(R.id.about_user)
    protected TextView about_user;
    /*@Bind(R.id.charge_account_layout)
    protected LinearLayout charge_account_layout;*/

    /*@Bind(R.id.charge_account)
    protected BootstrapButton charge_account;*/


    @Bind(R.id.info_message_fragment)
    protected FrameLayout info_message_fragment;


    public UserContactFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BootstrapApplication.component().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = (RelativeLayout) inflater.inflate(R.layout.fragment_user_contact, container, false);
        initScreen();
        return layout;
    }

    private void initScreen() {
        final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.info_message_fragment, new InfoMessageFragment())
                .commit();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ButterKnife.bind(this, getView());
        hideMsg();
        ContactModel contactModel = ((UserContactActivity) getActivity()).getContactModel();
        SharedPreferences prefs = getActivity().getSharedPreferences(
                "com.mibarim.main", Context.MODE_PRIVATE);
        if (prefs.getInt("ContactHelpShown", 0) != 1) {
            prefs.edit().putInt("ContactHelpShown", 1).apply();
            //showHelpCaseView(contactModel);
        }
        name_family.setText(contactModel.Name + " " + contactModel.Family);
        credit_layout.setVisibility(View.GONE);
        /*if (contactModel.IsDriver > 0) {

            *//*if (contactModel.IsRideAccepted == 0) {
                driver_accept_layout.setVisibility(View.VISIBLE);
            } else {
                driver_accept_layout.setVisibility(View.GONE);
            }*//*
        } else {
            //credit_layout.setVisibility(View.VISIBLE);
            //driver_accept_layout.setVisibility(View.GONE);
        }*/
        about_user.setText(contactModel.AboutUser);

        if(contactModel.ContactId==0){
            similar_route.setVisibility(View.GONE);
            chat_layout.setVisibility(View.GONE);
        }

        if (contactModel.UserImageId != null) {
            Bitmap decodedByte =((UserContactActivity) getActivity()).getImageById(contactModel.UserImageId);
            /*byte[] decodedString = Base64.decode(contactModel.Base64UserPic, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);*/
            user_image.setImageBitmap(decodedByte);
        }
        similar_route.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    ((UserContactActivity) getActivity()).goToSimilarRoutes();
                    return true;
                }
                return false;
            }
        });
        chat_layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    ((UserContactActivity) getActivity()).goToMessaging();
                    return true;
                }
                return false;
            }
        });
        chat.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    ((UserContactActivity) getActivity()).goToMessaging();
                    return true;
                }
                return false;
            }
        });
        /*driver_accept.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    ((UserContactActivity) getActivity()).acceptRideShare();
                    return true;
                }
                return false;
            }
        });*/
        /*charge_account.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    showMsg(getString(R.string.charge_site));
                    return true;
                }
                return false;
            }
        });*/

    }

    /*private void showHelpCaseView(final ContactModel contactModel) {
        new ShowcaseView.Builder(getActivity())
                .setTarget(new ViewTarget(R.id.similar_route, getActivity()))
                .setContentTitle(R.string.similar_route)
                .setContentText(R.string.similar_route_desc)
                .hideOnTouchOutside()
                .setStyle(R.style.CustomShowcaseTheme)
                .replaceEndButton(R.layout.showcase_button)
                .setShowcaseEventListener(new SimpleShowcaseEventListener() {
                    @Override
                    public void onShowcaseViewDidHide(ShowcaseView showcaseView) {
                        new ShowcaseView.Builder(getActivity())
                                .setTarget(new ViewTarget(R.id.chat_layout, getActivity()))
                                .setContentTitle(R.string.chats)
                                .setContentText(R.string.chats_desc)
                                .setStyle(R.style.CustomShowcaseTheme)
                                .hideOnTouchOutside()
                                .replaceEndButton(R.layout.showcase_button)
                                .setShowcaseEventListener(new SimpleShowcaseEventListener() {
                                    @Override
                                    public void onShowcaseViewDidHide(ShowcaseView showcaseView) {
                                        if (contactModel.IsDriver > 0) {
                                            if (contactModel.IsRideAccepted == 0) {
                                                new ShowcaseView.Builder(getActivity())
                                                        .setTarget(new ViewTarget(R.id.driver_accept, getActivity()))
                                                        .setContentTitle(R.string.accept_rideshare)
                                                        .setStyle(R.style.CustomShowcaseTheme)
                                                        .setContentText(R.string.accept_rideshare_desc)
                                                        .hideOnTouchOutside()
                                                        .replaceEndButton(R.layout.showcase_button)
                                                        .build();
                                            }
                                        } else {
                                            new ShowcaseView.Builder(getActivity())
                                                    .setTarget(new ViewTarget(R.id.credit_layout, getActivity()))
                                                    .setContentTitle(R.string.charge_account)
                                                    .setStyle(R.style.CustomShowcaseTheme)
                                                    .setContentText(R.string.charge_account_desc)
                                                    .hideOnTouchOutside()
                                                    .replaceEndButton(R.layout.showcase_button)
                                                    .build();
                                        }
                                    }
                                })
                                .build();
                    }
                }).build();
    }*/

    public void hideMsg() {
        info_message_fragment.setVisibility(View.GONE);
    }

    public void showMsg(String msg) {
        info_message_fragment.setVisibility(View.VISIBLE);
        final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.info_message_fragment);
        ((InfoMessageFragment) fragment).setActionBtn(getString(R.string.ok));
        ((InfoMessageFragment) fragment).setMsg(msg);
    }

    public void setUserScores(ScoreModel scoreModel) {
        //credit_money.setText(scoreModel.CreditMoney);
    }

    public void hideDriverAcceptBtn() {
        //driver_accept_layout.setVisibility(View.GONE);
    }
}
