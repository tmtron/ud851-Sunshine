package com.example.android.sunshine.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.example.android.sunshine.data.WeatherContract.WeatherEntry;
import com.example.android.sunshine.utilities.NetworkUtils;
import com.example.android.sunshine.utilities.OpenWeatherJsonUtils;

import java.net.URL;

//  DONE (1) Create a class called SunshineSyncTask
public class SunshineSyncTask {

    //  DONE (2) Within SunshineSyncTask, create a synchronized public static void method called syncWeather
    synchronized public static void syncWeather(Context context) {
//      DONE (3) Within syncWeather, fetch new weather data
        try {
            final URL url = NetworkUtils.getUrl(context);
            final String responseFromHttpUrl = NetworkUtils.getResponseFromHttpUrl(url);
            //      DONE (4) If we have valid results, delete the old data and insert the new
            if (responseFromHttpUrl == null || responseFromHttpUrl.length() == 0) return;

            ContentValues[] values = OpenWeatherJsonUtils
                    .getWeatherContentValuesFromJson(context, responseFromHttpUrl);
            if (values == null || values.length == 0) return;

            final ContentResolver contentResolver = context.getContentResolver();
            contentResolver.delete(WeatherEntry.CONTENT_URI, null, null);
            contentResolver.bulkInsert(WeatherEntry.CONTENT_URI, values);
        } catch (Exception e) {
            Log.e(SunshineSyncTask.class.getName(), "failed to sync weather: ", e);
            e.printStackTrace();
        }
    }

}