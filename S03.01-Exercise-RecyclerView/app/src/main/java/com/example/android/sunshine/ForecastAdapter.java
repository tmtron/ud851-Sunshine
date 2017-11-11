package com.example.android.sunshine;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

// DONE (22) Extend RecyclerView.Adapter<ForecastAdapter.ForecastAdapterViewHolder>
public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastAdapterViewHolder> {

    // DONE (23) Create a private string array called mWeatherData
    private String[] mWeatherData;

    // DONE (47) Create the default constructor (we will pass in parameters in a later lesson)

    public ForecastAdapter() {
        mWeatherData = null;
    }

    // DONE (24) Override onCreateViewHolder
    @Override
    public ForecastAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // DONE (25) Within onCreateViewHolder, inflate the list item xml into a view
        final Context context = parent.getContext();
        final LayoutInflater layoutInflater = LayoutInflater.from(context);
        final View view = layoutInflater.inflate(R.layout.forecast_list_item, parent, false);
        // DONE (26) Within onCreateViewHolder, return a new ForecastAdapterViewHolder with the above view passed in as a parameter
        return new ForecastAdapterViewHolder(view);
    }

    // DONE (27) Override onBindViewHolder
    @Override
    public void onBindViewHolder(ForecastAdapterViewHolder holder, int position) {
        // DONE (28) Set the text of the TextView to the weather for this list item's position
        String weatherText = mWeatherData[position];
        holder.mWeatherTextView.setText(weatherText);
    }

    // DONE (29) Override getItemCount
    @Override
    public int getItemCount() {
        // DONE (30) Return 0 if mWeatherData is null, or the size of mWeatherData if it is not null
        if (mWeatherData == null) {
            return 0;
        }
        return mWeatherData.length;
    }

    // DONE (31) Create a setWeatherData method that saves the weatherData to mWeatherData
    public void setWeatherData(String [] weatherData) {
        mWeatherData = weatherData;
        // DONE (32) After you save mWeatherData, call notifyDataSetChanged
        notifyDataSetChanged();
    }

    // DONE (16) Create a class within ForecastAdapter called ForecastAdapterViewHolder
    // DONE (17) Extend RecyclerView.ViewHolder
    class ForecastAdapterViewHolder extends RecyclerView.ViewHolder {
        // Within ForecastAdapterViewHolder ///////////////////////////////////////////////////////////
        // DONE (18) Create a public final TextView variable called mWeatherTextView
        public final TextView mWeatherTextView;

        // DONE (19) Create a constructor for this class that accepts a View as a parameter
        public ForecastAdapterViewHolder(View itemView) {
            // DONE (20) Call super(view) within the constructor for ForecastAdapterViewHolder
            super(itemView);
            // DONE (21) Using view.findViewById, get a reference to this layout's TextView and save it to mWeatherTextView
            this.mWeatherTextView = itemView.findViewById(R.id.tv_weather_data);
        }

        // Within ForecastAdapterViewHolder ///////////////////////////////////////////////////////////
    }
}
