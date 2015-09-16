package com.gmail.nelsonr462.opin;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;

import com.parse.ParseConfig;
import com.parse.ParseInstallation;
import com.parse.ParsePushBroadcastReceiver;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class OpinPushBroadcastReceiver extends ParsePushBroadcastReceiver {
    public static final String PARSE_DATA_KEY = "com.parse.Data";
    public static final String TAG = OpinPushBroadcastReceiver.class.getSimpleName();
    private ParseConfig mParseConfig;


    @Override
    protected void onPushReceive(Context context, Intent intent) {
        mParseConfig = ParseConfig.getCurrentConfig();
        ParseInstallation currentInstallation = OpinApplication.mCurrentInstallation;

        JSONObject data = getDataFromIntent(intent);

        String surveyObjectId = "";
        String text = "";
        String deviceObjectId = "";
        String url = "";

        try {
            surveyObjectId = data.getString("survey");
            text = data.getString("alert");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        deviceObjectId = currentInstallation.getObjectId();
        url = mParseConfig.getString(ParseConstants.KEY_HOST)+"/surveys/"+surveyObjectId+"/"+deviceObjectId;

        // Fetch colors for webview
        fetchWebViewColors();


        // Define intent

        Intent surveyIntent = new Intent(context, SurveyWebViewActivity.class);
        surveyIntent.putExtra("surveyUrl", url);

        // Check if app is active

        if (ParseConstants.isAppActive) {
            surveyIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(surveyIntent);
            return;
        }

        PendingIntent notifyPIntent = PendingIntent.getActivity(context, 0, surveyIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentTitle("Opin");
        builder.setContentText(text);
        builder.setSmallIcon(R.mipmap.ic_notification);
        builder.setContentIntent(notifyPIntent);
        builder.setAutoCancel(true);


        notificationManager.notify(TAG, 0, builder.build());

    }

    private void fetchWebViewColors() {
        JSONArray loadingBackground = mParseConfig.getJSONArray(ParseConstants.KEY_LOADER_BACKGROUND);
        JSONArray loaderPrimary = mParseConfig.getJSONArray(ParseConstants.KEY_LOADER_PRIMARY);

        ParseConstants.COLOR_LOADER_BACKGROUND = new int[loadingBackground.length()];
        ParseConstants.COLOR_LOADER_PRIMARY = new int[loaderPrimary.length()];

        for (int i = 0; i < loadingBackground.length(); i++) {
            ParseConstants.COLOR_LOADER_BACKGROUND[i] = loadingBackground.optInt(i);
            ParseConstants.COLOR_LOADER_PRIMARY[i] = loaderPrimary.optInt(i);
        }
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
