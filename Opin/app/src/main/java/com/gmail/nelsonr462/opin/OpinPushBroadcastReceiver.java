package com.gmail.nelsonr462.opin;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;

import com.parse.ParsePushBroadcastReceiver;
import com.parse.PushService;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by nelson on 9/14/15.
 */
public class OpinPushBroadcastReceiver extends ParsePushBroadcastReceiver {
    public static final String PARSE_DATA_KEY = "com.parse.Data";

    @Override
    protected void onPushReceive(Context context, Intent intent) {
        super.onPushReceive(context, intent);
    }

    @Override
    protected void onPushOpen(Context context, Intent intent) {
        super.onPushOpen(context, intent);
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
