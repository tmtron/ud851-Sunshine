package com.example.android.sunshine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    private static final String FORECAST_SHARE_HASHTAG = " #SunshineApp";

    private TextView mTvWeatherDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mTvWeatherDetails = findViewById(R.id.tv_weather_details);

        // DONE (2) Display the weather forecast that was passed from MainActivity
        final Intent intent = getIntent();
        if (intent.hasExtra(Intent.EXTRA_TEXT)) {
            final String weatherForDay = intent.getStringExtra(Intent.EXTRA_TEXT);
            mTvWeatherDetails.setText(weatherForDay);
        }
    }
}