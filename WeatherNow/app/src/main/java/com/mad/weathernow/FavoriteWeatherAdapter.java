package com.mad.weathernow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Homework 05
 * FavoriteWeatherAdapter.java
 * Sanket Patil
 * Atul Banwar
 */
public class FavoriteWeatherAdapter extends ArrayAdapter<FavoriteWeather> {

    private Context context;
    private ArrayList<FavoriteWeather> objects;

    public FavoriteWeatherAdapter(Context context, ArrayList<FavoriteWeather> objects) {
        super(context, R.layout.favorite_weather_list, objects);
        this.context = context;
        this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.favorite_weather_list, parent, false);
            holder = new ViewHolder();
            holder.cityState = (TextView) convertView.findViewById(R.id.textViewCityStateFav);
            holder.temperature = (TextView) convertView.findViewById(R.id.textViewFaveTemp);
            holder.lastUpdate = (TextView) convertView.findViewById(R.id.textViewUpdatedDate);
            convertView.setTag(holder);
        }
        holder = (ViewHolder) convertView.getTag();
        TextView cityState = holder.cityState;
        TextView temperature = holder.temperature;
        TextView lastUpdated = holder.lastUpdate;

        FavoriteWeather favoriteWeather = objects.get(position);
        cityState.setText(favoriteWeather.getCity() + ", " + favoriteWeather.getState());
        temperature.setText(context.getResources().getString(R.string.text_view_temperature, favoriteWeather.getTemperature()));
        lastUpdated.setText(context.getResources().getString(R.string.text_view_updated_on, favoriteWeather.getDate()));
        return convertView;

    }

    static class ViewHolder {
        TextView cityState;
        TextView temperature;
        TextView lastUpdate;
    }
}
