package com.gmail.nelsonr462.opin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SurveyWebViewActivity extends AppCompatActivity {
    private String mSurveyUrl;

    @Bind(R.id.surveyWebView) WebView mSurveyWebView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_web_view);
        ButterKnife.bind(this);

        mSurveyUrl = getIntent().getStringExtra("surveyUrl");

        mSurveyWebView.getSettings().setJavaScriptEnabled(true);
        mSurveyWebView.loadUrl(mSurveyUrl);

        mSurveyWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                SurveyWebViewActivity.this.finish();
                return false;

            }
        });

    }


}
