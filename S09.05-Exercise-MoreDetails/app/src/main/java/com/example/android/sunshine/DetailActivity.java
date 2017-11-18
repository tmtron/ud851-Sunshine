/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.sunshine;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.android.sunshine.utilities.SunshineDateUtils;
import com.example.android.sunshine.utilities.SunshineWeatherUtils;

import static com.example.android.sunshine.data.WeatherContract.WeatherEntry.*;

public class DetailActivity extends AppCompatActivity
//      DONE (21) Implement LoaderManager.LoaderCallbacks<Cursor>
    implements LoaderManager.LoaderCallbacks<Cursor> {

    /*
     * In this Activity, you can share the selected day's forecast. No social sharing is complete
     * without using a hashtag. #BeTogetherNotTheSame
     */
    private static final String FORECAST_SHARE_HASHTAG = " #SunshineApp";

//  DONE (18) Create a String array containing the names of the desired data columns from our ContentProvider
    private static final String[] sPROJECTION = new String[] {
        COLUMN_DATE
        , COLUMN_WEATHER_ID
        , COLUMN_MAX_TEMP
        , COLUMN_MIN_TEMP
        , COLUMN_HUMIDITY
        , COLUMN_WIND_SPEED
        , COLUMN_DEGREES
        , COLUMN_PRESSURE
    };
//  DONE (19) Create constant int values representing each column name's position above
    private static int INDEX_COLUMN_DATE = 0;
    private static int INDEX_COLUMN_WEATHER_ID = 1;
    private static int INDEX_COLUMN_TEMP_MAX = 2;
    private static int INDEX_COLUMN_TEMP_MIN = 3;
    private static int INDEX_COLUMN_HUMIDITY = 4;
    private static int INDEX_COLUMN_WIND_SPEED = 5;
    private static int INDEX_COLUMN_DEGREES = 6;
    private static int INDEX_COLUMN_PRESSURE = 7;

//  DONE (20) Create a constant int to identify our loader used in DetailActivity
    private static int LOADER_ID = 42;

    /* A summary of the forecast that can be shared by clicking the share button in the ActionBar */
    private String mForecastSummary;

//  DONE (15) Declare a private Uri field called mUri
    private Uri mUri;

//  DONE (10) Remove the mWeatherDisplay TextView declaration

//  DONE (11) Declare TextViews for the date, description, high, low, humidity, wind, and pressure
    private TextView mTvDate;
    private TextView mTvDescription;
    private TextView mTvTempMax;
    private TextView mTvTempMin;
    private TextView mTvHumidity;
    private TextView mTvWind;
    private TextView mTvPressure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
//      DONE (12) Remove mWeatherDisplay TextView

//      DONE (13) Find each of the TextViews by ID
        mTvDate = findViewById(R.id.tv_display_date);
        mTvDescription = findViewById(R.id.tv_display_description);
        mTvTempMax = findViewById(R.id.tv_display_temp_max);
        mTvTempMin = findViewById(R.id.tv_display_temp_min);
        mTvHumidity = findViewById(R.id.tv_display_humidity);
        mTvWind = findViewById(R.id.tv_display_wind);
        mTvPressure = findViewById(R.id.tv_display_pressure);

        Intent intentThatStartedThisActivity = getIntent();
        mUri = null;
        if (intentThatStartedThisActivity != null) {
//      DONE (14) Remove the code that checks for extra text
//      DONE (16) Use getData to get a reference to the URI passed with this Activity's Intent
            mUri = intentThatStartedThisActivity.getData();
        }

//      DONE (17) Throw a NullPointerException if that URI is null
        if (mUri == null) throw new NullPointerException("The intent does not cotain a URI for the item");

//      DONE (35) Initialize the loader for DetailActivity
        getSupportLoaderManager().initLoader(LOADER_ID, null, this);
    }


    /**
     * This is where we inflate and set up the menu for this Activity.
     *
     * @param menu The options menu in which you place your items.
     *
     * @return You must return true for the menu to be displayed;
     *         if you return false it will not be shown.
     *
     * @see #onPrepareOptionsMenu
     * @see #onOptionsItemSelected
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
        inflater.inflate(R.menu.detail, menu);
        /* Return true so that the menu is displayed in the Toolbar */
        return true;
    }

    /**
     * Callback invoked when a menu item was selected from this Activity's menu. Android will
     * automatically handle clicks on the "up" button for us so long as we have specified
     * DetailActivity's parent Activity in the AndroidManifest.
     *
     * @param item The menu item that was selected by the user
     *
     * @return true if you handle the menu click here, false otherwise
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /* Get the ID of the clicked item */
        int id = item.getItemId();

        /* Settings menu item clicked */
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        /* Share menu item clicked */
        if (id == R.id.action_share) {
            Intent shareIntent = createShareForecastIntent();
            startActivity(shareIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Uses the ShareCompat Intent builder to create our Forecast intent for sharing.  All we need
     * to do is set the type, text and the NEW_DOCUMENT flag so it treats our share as a new task.
     * See: http://developer.android.com/guide/components/tasks-and-back-stack.html for more info.
     *
     * @return the Intent to use to share our weather forecast
     */
    private Intent createShareForecastIntent() {
        Intent shareIntent = ShareCompat.IntentBuilder.from(this)
                .setType("text/plain")
                .setText(mForecastSummary + FORECAST_SHARE_HASHTAG)
                .getIntent();
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        return shareIntent;
    }

//  DONE (22) Override onCreateLoader
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
//          DONE (23) If the loader requested is our detail loader, return the appropriate CursorLoader
        if (id != LOADER_ID) throw new RuntimeException("Loader not implemented: "+id);
        return new CursorLoader(this, mUri, sPROJECTION, null, null, null);
    }

//  DONE(24) Override onLoadFinished
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
//      DONE (25) Check before doing anything that the Cursor has valid data
        if (data == null) return;
        if (!data.moveToFirst()) return;

//      DONE (26) Display a readable data string
        final long date = data.getLong(INDEX_COLUMN_DATE);
        final String friendlyDateString = SunshineDateUtils.getFriendlyDateString(this, date, true);
        mTvDate.setText(friendlyDateString);

//      DONE (27) Display the weather description (using SunshineWeatherUtils)
        final int weatherId = data.getInt(INDEX_COLUMN_WEATHER_ID);
        final String description = SunshineWeatherUtils.getStringForWeatherCondition(this, weatherId);
        mTvDescription.setText(description);

//      DONE (28) Display the high temperature
        final String tempMax = SunshineWeatherUtils.formatTemperature(this, data.getDouble(INDEX_COLUMN_TEMP_MAX));
        mTvTempMax.setText(tempMax);
//      DONE (29) Display the low temperature
        final String tempMin = SunshineWeatherUtils.formatTemperature(this, data.getDouble(INDEX_COLUMN_TEMP_MIN));
        mTvTempMin.setText(tempMin);
//      DONE (30) Display the humidity
        final String humidity = SunshineWeatherUtils.formatHumidity(this, data.getDouble(INDEX_COLUMN_TEMP_MIN));
        mTvHumidity.setText(humidity);
//      DONE (31) Display the wind speed and direction
        final float windSpeed = data.getFloat(INDEX_COLUMN_WIND_SPEED);
        final float windDegrees = data.getFloat(INDEX_COLUMN_DEGREES);
        final String windInfo = SunshineWeatherUtils.getFormattedWind(this, windSpeed, windDegrees);
        mTvWind.setText(windInfo);
//      DONE (32) Display the pressure
        final String pressure = SunshineWeatherUtils.formatPressure(this, data.getDouble(INDEX_COLUMN_PRESSURE));
        mTvPressure.setText(pressure);

//      DONE (33) Store a forecast summary in mForecastSummary
        mForecastSummary = friendlyDateString+" "+description +"("+tempMin+" to "+tempMax+")";
    }

    //  DONE (34) Override onLoaderReset, but don't do anything in it yet
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

}