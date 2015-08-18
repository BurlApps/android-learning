package com.test.nelson.stormy.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.test.nelson.stormy.R;
import com.test.nelson.stormy.weather.Current;
import com.test.nelson.stormy.weather.Day;
import com.test.nelson.stormy.weather.Forecast;
import com.test.nelson.stormy.weather.Hour;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    private Forecast mForecast;


    @Bind(R.id.temperatureLabel) TextView mTemperatureLabel;
    @Bind(R.id.timeLabel) TextView mTimeLabel;
    @Bind(R.id.humidityValue) TextView mHumidityValue;
    @Bind(R.id.precipValue) TextView mPrecipValue;
    @Bind(R.id.iconImageView) ImageView mIconImageView;
    @Bind(R.id.summaryLabel) TextView mSummaryLabel;
    @Bind(R.id.refreshImageView) ImageView mRefreshImageView;
    @Bind(R.id.progressBar) ProgressBar mProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        mProgressBar.setVisibility(View.INVISIBLE);

        final double latitude = 37.8267;
        final double longitude = -122.423;

        mRefreshImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getForecast(latitude, longitude);
            }
        });

        getForecast(latitude, longitude);



    }

    private void getForecast( double latitude, double longitude) {
        String apiKey = "ef0fe1d37b5300485589c845c96c18ae";

        String forecastURL = "https://api.forecast.io/forecast/" + apiKey +
                "/" + latitude + "," + longitude;

        if(isNetworkAvailable()) {

            toggleRefresh();

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(forecastURL)
                    .build();

            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toggleRefresh();
                        }
                    });
                    alertUserAboutError();
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toggleRefresh();
                        }
                    });
                    try {
                        String jsonData = response.body().string();
                        Log.v(TAG, response.body().string());
                        if (response.isSuccessful()) {
                            mForecast = parseForecastDetails(jsonData);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    updateDisplay();
                                }
                            });

                        } else {
                            alertUserAboutError();
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "Exception caught: ", e);
                    } catch (JSONException e) {
                        Log.e(TAG, "Exception caught: ", e);
                    }
                }
            });
        } else {
            Toast.makeText(this, R.string.network_unavailable, Toast.LENGTH_LONG).show();
        }
    }

    private void toggleRefresh() {

        if(mProgressBar.getVisibility() == View.INVISIBLE) {
            mProgressBar.setVisibility(View.VISIBLE);
            mRefreshImageView.setVisibility(View.INVISIBLE);
        } else {
            mProgressBar.setVisibility(View.INVISIBLE);
            mRefreshImageView.setVisibility(View.VISIBLE);
        }

    }

    private void updateDisplay() {
        Current current = mForecast.getCurrent();

        Drawable drawable = getResources().getDrawable(current.getIconId());

        mTemperatureLabel.setText(current.getTemperature() + "");
        mTimeLabel.setText("At "+ current.getFormattedTime()+" it will be");
        mHumidityValue.setText(current.getHumidity() + "");
        mPrecipValue.setText(current.getPrecipChance() + "%");
        mIconImageView.setImageDrawable(drawable);
        mSummaryLabel.setText(current.getSummary());
    }


    private Forecast parseForecastDetails(String jsonData) throws JSONException{
        Forecast forecast = new Forecast();
        forecast.setCurrent(getCurrentDetails(jsonData));
        forecast.setDailyForecast(getDailyForecast(jsonData));
        forecast.setHourlyForecast(getHourlyForecast(jsonData));


        return forecast;
    }

    private Day[] getDailyForecast(String jsonData) throws JSONException{
        JSONObject forecast = new JSONObject(jsonData);
        String timezone = forecast.getString("timezone");

        JSONObject daily = forecast.getJSONObject("daily");
        JSONArray data = daily.getJSONArray("data");

        Day[] dailyForecast = new Day[data.length()];

        for(int i = 0; i < data.length(); i++) {
            JSONObject hourData = data.getJSONObject(i);
            Day day = new Day();
            day.setTime(hourData.getLong("time"));
            day.setIcon(hourData.getString("icon"));
            day.setTemperatureMax(hourData.getDouble("temperatureMax"));
            day.setSummary(hourData.getString("summary"));
            day.setTimeZone(timezone);
            dailyForecast[i] = day;
        }

        return dailyForecast;
    }

    private Hour[] getHourlyForecast(String jsonData) throws JSONException{
        JSONObject forecast = new JSONObject(jsonData);
        String timezone = forecast.getString("timezone");

        JSONObject hourly = forecast.getJSONObject("hourly");
        JSONArray data = hourly.getJSONArray("data");

        Hour[] hourlyForecast = new Hour[data.length()];

        for(int i = 0; i < data.length(); i++) {
            JSONObject hourData = data.getJSONObject(i);
            Hour hour = new Hour();
            hour.setTime(hourData.getLong("time"));
            hour.setIcon(hourData.getString("icon"));
            hour.setTemperature(hourData.getDouble("temperature"));
            hour.setSummary(hourData.getString("summary"));
            hour.setTimeZone(timezone);
            hourlyForecast[i] = hour;
        }

        return hourlyForecast;

    }

    private Current getCurrentDetails(String jsonData) throws JSONException{
        JSONObject forecast = new JSONObject(jsonData);
        String timezone = forecast.getString("timezone");

        JSONObject currently = forecast.getJSONObject("currently");

        Current current = new Current();
        current.setHumidity(currently.getDouble("humidity"));
        current.setPrecipChance(currently.getDouble("precipProbability"));
        current.setSummary(currently.getString("summary"));
        current.setTemperature(currently.getDouble("temperature"));
        current.setTime(currently.getLong("time"));
        current.setIcon(currently.getString("icon"));
        current.setTimeZone(timezone);


        Log.d(TAG, current.getFormattedTime());

        Log.i(TAG, "From JSON: "+timezone);

        return current;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if(networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }
        return isAvailable;
    }

    private void alertUserAboutError() {
        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.show(getFragmentManager(), "error_dialog");
    }

    @OnClick (R.id.dailyButton)
    public void startDailyActivity(View view) {
        Intent intent = new Intent(this, DailyForecastActivity.class);
        startActivity(intent);
    }

}
