package com.gmail.nelsonr462.opin;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.nineoldandroids.animation.Animator;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SurveyWebViewActivity extends AppCompatActivity {
    private String mSurveyUrl;

    @Bind(R.id.surveyWebView) WebView mSurveyWebView;
    @Bind(R.id.webViewProgressBar) ProgressBar mProgressBar;
    @Bind(R.id.surveyWebViewRelativeLayout) RelativeLayout mRelativeLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_web_view);
        ButterKnife.bind(this);

        mProgressBar.setVisibility(View.VISIBLE);
        mSurveyUrl = getIntent().getStringExtra("surveyUrl");

        mSurveyWebView.getSettings().setJavaScriptEnabled(true);
        mSurveyWebView.setVisibility(View.INVISIBLE);
        mSurveyWebView.setBackgroundColor(Color.rgb(
                ParseConstants.COLOR_LOADER_BACKGROUND[0],
                ParseConstants.COLOR_LOADER_BACKGROUND[1],
                ParseConstants.COLOR_LOADER_BACKGROUND[2]
                ));
        mSurveyWebView.loadUrl(mSurveyUrl);

        mSurveyWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                mSurveyWebView.setVisibility(View.INVISIBLE);
                YoYo.with(Techniques.SlideOutDown)
                        .duration(300)
                        .withListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                SurveyWebViewActivity.this.finish();
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }
                        })
                        .playOn(mRelativeLayout);
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                mProgressBar.setVisibility(View.INVISIBLE);
                mSurveyWebView.setVisibility(View.VISIBLE);
                YoYo.with(Techniques.SlideInUp)
                        .duration(300)
                        .playOn(mSurveyWebView);
            }
        });

    }


}
