package com.gmail.nelsonr462.opin;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;

import com.parse.ParsePushBroadcastReceiver;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by nelson on 9/14/15.
 */
public class OpinPushBroadcastReceiver extends ParsePushBroadcastReceiver {
    public static final String PARSE_DATA_KEY = "com.parse.Data";


    @Override
    protected void onPushReceive(Context context, Intent intent) {

    }

    @Override
    protected void onPushOpen(Context context, Intent intent) {
        Intent newIntent = new Intent(context, SurveyWebViewActivity.class);
        newIntent.putExtras(intent.getExtras());
        newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(newIntent);
    }

    private JSONObject getDataFromIntent(Intent intent) {
        JSONObject data = null;
        try {
            data = new JSONObject(intent.getExtras().getString(PARSE_DATA_KEY));
        } catch (JSONException e) {
            // Json was not readable...
        }
        return data;
    }
}
