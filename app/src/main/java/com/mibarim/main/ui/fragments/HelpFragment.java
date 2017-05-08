package com.mibarim.main.ui.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.mibarim.main.BootstrapApplication;
import com.mibarim.main.R;
import com.mibarim.main.models.StructNote;
import com.mibarim.main.ui.activities.HelpingActivity;
import com.mibarim.main.ui.fragments.helpFragments.FeedbackFragment;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Hamed on 3/4/2016.
 */
public class HelpFragment extends Fragment {


    @Bind(R.id.feedback_layout)
    protected LinearLayout feedback;

    @Bind(R.id.phone_layout)
    protected LinearLayout phone;

    @Bind(R.id.email_layout)
    protected LinearLayout email;

    @Bind(R.id.facebook_icon)
    protected ImageView facebook;

    @Bind(R.id.linkedin_icon)
    protected ImageView linkedin;

    @Bind(R.id.instagram_icon)
    protected ImageView instagram;

    @Bind(R.id.twitter_icon)
    protected ImageView twitter;

    @Bind(R.id.telegram_icon)
    protected ImageView telegram;

    @Bind(R.id.feedback_fragment)
    protected FrameLayout feedback_fragment;

    /*@Bind(R.id.list_item)
    protected ListView listView;*/

    @Bind(R.id.web_view)
    protected WebView webview;

    @Bind(R.id.progressBar)
    protected ProgressBar progressBar;


    public HelpFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BootstrapApplication.component().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.fragment_help, container, false);
        final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .replace(R.id.feedback_fragment, new FeedbackFragment())
                .commit();
        return layout;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ButterKnife.bind(this, getView());


        webview.loadUrl("http://mibarim.com/faq/");
        webview.getSettings().setDomStorageEnabled(true);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setWebViewClient(new WebViewClient() {

            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                webview.loadUrl("about:blank");
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
                webview.setMinimumHeight(2000);
            }
        });
        //ArrayList<StructNote> notes = new ArrayList<StructNote>();

/*
        AdapterNote adapter = new AdapterNote(notes, getActivity());
        listView.setAdapter(adapter);
        for (int i = 0; i < 20; i++) {
            StructNote note = new StructNote();
            note.title = "Title";
            note.description = "Description";
            notes.add(note);
        }
        adapter.notifyDataSetChanged();
*/


        feedback_fragment.setVisibility(View.GONE);

        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                feedback_fragment.setVisibility(View.VISIBLE);
                ((HelpingActivity) getActivity()).setFeedbackVisibility(true);
                // Toast.makeText(getActivity(), "Clicked", Toast.LENGTH_SHORT).show();

            }
        });

        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((HelpingActivity) getActivity()).call();

            }
        });

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ((HelpingActivity) getActivity()).email();

            }
        });


        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ((HelpingActivity) getActivity()).facebook();

            }
        });

        linkedin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ((HelpingActivity) getActivity()).linkedin();
            }
        });

        instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((HelpingActivity) getActivity()).instagram();
            }
        });

        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((HelpingActivity) getActivity()).twitter();
            }
        });


        telegram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ((HelpingActivity) getActivity()).telegram();

            }
        });

        //price_link.setMovementMethod(LinkMovementMethod.getInstance());
        //price_link.setText(Html.fromHtml("<p>"+getString(R.string.faq_1)+"</p><br/><a href=\"" + "http://taxi.tehran.ir/Default.aspx?tabid=312" + "\">" + "وبسایت تاکسیرانی تهران" + "</a>"));

    }


    public void hidefeedback() {

        feedback_fragment.setVisibility(View.GONE);

    }

    /*public void webview() {

        webview.setVisibility(View.VISIBLE);
    }*/


}
