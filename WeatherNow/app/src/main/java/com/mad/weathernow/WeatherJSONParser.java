package com.mad.weathernow;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Homework 05
 * WeatherJSONParser.java
 * Sanket Patil
 * Atul Banwar
 */

public class WeatherJSONParser {
    static ArrayList<Weather> pasrseWeather(String in) throws JSONException {
        ArrayList<Weather> weatherList = new ArrayList<Weather>();

        JSONObject root = new JSONObject(in);

        if (root.has("hourly_forecast")) {
            JSONArray weatherArray = root.getJSONArray("hourly_forecast");

            for (int i = 0; i < weatherArray.length(); i++) {
                JSONObject weatherArrayJSONObject = weatherArray.getJSONObject(i);
                Weather weather = Weather.getWeather(weatherArrayJSONObject);
                weatherList.add(weather);
            }
        }

        // Calculating and setting max and min temperature
        if (weatherList.size() > 0) {
            int maxTemp = 0;
            int minTemp = 0;

            for (int j = 0; j < weatherList.size(); j++) {
                Weather weather = weatherList.get(j);
                int temp = Integer.parseInt(weather.getTemperature());

                if (temp > maxTemp) {
                    maxTemp = temp;
                }

                if (j == 0) {
                    minTemp = temp;
                }

                if (temp < minTemp) {
                    minTemp = temp;
                }
            }

            for (int k = 0; k < weatherList.size(); k++) {
                weatherList.get(k).setMinimumTemperature(String.valueOf(minTemp));
                weatherList.get(k).setMaximumTemperature(String.valueOf(maxTemp));
            }
        }

        return weatherList;
    }
}