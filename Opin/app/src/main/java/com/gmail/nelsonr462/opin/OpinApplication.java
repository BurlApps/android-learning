package com.gmail.nelsonr462.opin;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.parse.ConfigCallback;
import com.parse.Parse;
import com.parse.ParseConfig;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.SaveCallback;

/**
 * Created by nelson on 9/14/15.
 */
public class OpinApplication extends Application {

    public static ParseInstallation mCurrentInstallation;
    public static ParseConfig mParseConfig;


    @Override
    public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "kZkm35gPYdZKT7dAAie6MYUhSQGiJLrzvdjuHjCi", "C8BrrahLYrYx4j4ijlk27PpjmPx1xDda1cPA4xHf");
        ParseInstallation.getCurrentInstallation().saveInBackground();

        ParseConfig.getInBackground(new ConfigCallback() {
            @Override
            public void done(ParseConfig parseConfig, ParseException e) {
                if (e == null) {
                    mParseConfig = parseConfig;
                } else {
                    mParseConfig = ParseConfig.getCurrentConfig();
                }
            }
        });

        mCurrentInstallation = ParseInstallation.getCurrentInstallation();

    }
}
