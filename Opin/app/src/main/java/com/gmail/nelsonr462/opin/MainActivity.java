package com.gmail.nelsonr462.opin;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.parse.ConfigCallback;
import com.parse.ParseConfig;
import com.parse.ParseException;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements SurveyFragment.OnSurveyFragmentClick, AddClassFragment.OnClassFragmentInteraction {
    public static ParseConfig mParseConfig;



@Bind(R.id.toolbar_title) TextView mToolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Courgette-Regular.ttf");
        mToolbarTitle.setTypeface(typeface);

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.tool_bar);
        toolbar.setVisibility(View.VISIBLE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        MainFragmentPagerAdapter adapter =  new MainFragmentPagerAdapter(getSupportFragmentManager(), MainActivity.this);

        // Assigning ViewPager View and setting the adapter
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

        // Assiging the Sliding Tab Layout View
        SlidingTabLayout tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);
        pager.setCurrentItem(0);
    }

    @Override
    protected void onPause() {
        super.onPause();
        ParseConstants.isAppActive = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        ParseConstants.isAppActive = true;
    }

    @Override
    public void onSurveyFragmentClick(String id) {

    }

    @Override
    public void onClassFragmentInteraction(Uri uri) {

    }
}
