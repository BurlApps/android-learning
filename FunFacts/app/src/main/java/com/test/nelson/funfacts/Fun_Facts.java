package com.test.nelson.funfacts;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;


public class Fun_Facts extends Activity {
    public static final String TAG = Fun_Facts.class.getSimpleName();

    private FactBook mFactBook = new FactBook();
    private ColorWheel mcolorWheel = new ColorWheel();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fun__facts);

        // Declare View Variables and assign them the Views from the layout file
        final TextView factLabel = (TextView) findViewById(R.id.factTextView);
        final Button showFactButton = (Button) findViewById(R.id.showFactButton);

        final RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fact = mFactBook.getFact();
                // Update the label with our dynamic fact

                factLabel.setText(fact);

                int color = mcolorWheel.getColor();
                relativeLayout.setBackgroundColor(color);
                showFactButton.setTextColor(color);
            }
        };

        showFactButton.setOnClickListener(listener);

       // Toast.makeText(this, "Yay", Toast.LENGTH_LONG).show();
        Log.d(TAG, "Logging from onCreate!");
    }

}
