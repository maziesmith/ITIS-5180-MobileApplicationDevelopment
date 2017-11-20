package com.mad.weathernow;

import android.os.AsyncTask;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Homework 05
 * GetHourlyForcastData.java
 * Sanket Patil
 * Atul Banwar
 */

public class GetHourlyForcastData extends AsyncTask<String, Void, ArrayList<Weather>> {

    IData activity;

    public GetHourlyForcastData(IData activity) {
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        activity.startHourlyForcastDataFetch();
    }

    @Override
    protected ArrayList<Weather> doInBackground(String... params) {
        BufferedReader reader = null;
        try {
            URL url = new URL(params[0]);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.connect();
            int statusCode = con.getResponseCode();
            if (statusCode == HttpURLConnection.HTTP_OK) {
                reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                StringBuilder sb = new StringBuilder();

                String line = reader.readLine();

                while (line != null) {
                    sb.append(line);
                    line = reader.readLine();
                }
                return WeatherJSONParser.pasrseWeather(sb.toString());
            }
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;

    }

    @Override
    protected void onPostExecute(ArrayList<Weather> s) {
        super.onPostExecute(s);
        activity.setupData(s);
    }

    static public interface IData {
        public void startHourlyForcastDataFetch();

        public void setupData(ArrayList<Weather> result);
    }
}
