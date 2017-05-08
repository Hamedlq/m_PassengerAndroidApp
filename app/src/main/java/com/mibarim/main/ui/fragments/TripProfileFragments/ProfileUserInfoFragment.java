package com.mibarim.main.ui.fragments.TripProfileFragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapCircleThumbnail;
import com.mibarim.main.BootstrapApplication;
import com.mibarim.main.R;
import com.mibarim.main.core.ImageUtils;
import com.mibarim.main.models.UserInfo.UserRouteModel;
import com.mibarim.main.ui.activities.TripActivity;
import com.mibarim.main.ui.activities.TripProfileActivity;
import com.mibarim.main.ui.activities.UserContactActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.nekocode.badge.BadgeDrawable;

/**
 * Created by Hamed on 3/4/2016.
 */
public class ProfileUserInfoFragment extends Fragment {

    private Context context;
    private LinearLayout layout;

    @Bind(R.id.picture_profile)
    protected BootstrapCircleThumbnail picture_profile;

    @Bind(R.id.name_profile)
    protected TextView name_profile;

    /*@Bind(R.id.age_profile)
    protected TextView age_profile;*/

    @Bind(R.id.about_me)
    protected TextView about_me;

    @Bind(R.id.rating)
    protected RatingBar rating;

    @Bind(R.id.user_rate)
    protected TextView user_rate;

    public ProfileUserInfoFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BootstrapApplication.component().inject(this);
        context = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.fragment_trip_profile_user_info, container, false);
        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ButterKnife.bind(this, getView());
        setValues();
    }

    public void setValues() {
        UserRouteModel v = ((TripProfileActivity) getActivity()).getProfileTripInfo();
        Bitmap up = ((TripProfileActivity) getActivity()).getImageById(v.UserImageId, R.drawable.camera);
        if (v.UserImageId != null) {
            picture_profile.setImageBitmap(up);
        } else {
            //ImageUtils.pad(up,30,30);
            picture_profile.setImageBitmap(up);
        }


        if (v.IsVerified) {
            BadgeDrawable drawableBadge =
                    new BadgeDrawable.Builder()
                            .type(BadgeDrawable.TYPE_ONLY_ONE_TEXT)
                            .badgeColor(0xFF77FF00)
                            .text1("✔")
                            .build();
            SpannableString spannableString =
                    new SpannableString(TextUtils.concat(
                            v.Name,
                            " ",
                            v.Family,
                            " ",
                            drawableBadge.toSpannable()
                    ));
            name_profile.setText(spannableString);
        } else {
            name_profile.setText(v.Name + " " + v.Family);
        }
        if (v.UserAboutme != null) {
            about_me.setVisibility(View.VISIBLE);
            about_me.setText(v.UserAboutme);
        }
        rating.setRating(v.UserRating);
        user_rate.setText(v.UserRating + " امتیاز ");

    }
}
