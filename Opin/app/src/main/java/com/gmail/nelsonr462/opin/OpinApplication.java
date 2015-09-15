package com.gmail.nelsonr462.opin;

import android.app.Application;
import android.content.Context;

import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseObject;

/**
 * Created by nelson on 9/14/15.
 */
public class OpinApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "kZkm35gPYdZKT7dAAie6MYUhSQGiJLrzvdjuHjCi", "C8BrrahLYrYx4j4ijlk27PpjmPx1xDda1cPA4xHf");
        ParseInstallation.getCurrentInstallation().saveInBackground();

    }
}
