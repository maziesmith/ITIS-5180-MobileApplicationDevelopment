package com.mad.weathernow;

import android.content.Context;
import android.test.suitebuilder.TestMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Homework 05
 * WeatherAdapter.java
 * Sanket Patil
 * Atul Banwar
 */

public class WeatherAdapter extends ArrayAdapter<Weather> {
    Context context;
    ArrayList<Weather> objects;

    public WeatherAdapter(Context context, ArrayList<Weather> objects) {
        super(context, R.layout.hourly_weather_list_item, objects);
        this.context = context;
        this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.hourly_weather_list_item, parent, false);
            holder = new ViewHolder();
            holder.thumbnail = (ImageView) convertView.findViewById(R.id.imageViewThumbnail);
            holder.time = (TextView) convertView.findViewById(R.id.textViewTime);
            holder.condition = (TextView) convertView.findViewById(R.id.textViewCondition);
            holder.temperature = (TextView) convertView.findViewById(R.id.textViewTemperature);
            convertView.setTag(holder);
        }

        holder = (ViewHolder) convertView.getTag();
        ImageView thumbnail = holder.thumbnail;
        TextView time = holder.time;
        TextView condition = holder.condition;
        TextView temperature = holder.temperature;

        Picasso.with(context).load(objects.get(position).getIconUrl()).into(thumbnail);
        time.setText(objects.get(position).getTime().substring(0, 8));
        condition.setText(objects.get(position).getClouds());
        temperature.setText(context.getResources().getString(R.string.text_view_temperature, objects.get(position).getTemperature()));

        return convertView;
    }

    static class ViewHolder {
        ImageView thumbnail;
        TextView time;
        TextView condition;
        TextView temperature;
    }
}