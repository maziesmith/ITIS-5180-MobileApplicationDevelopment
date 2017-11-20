package com.mad.weathernow;

import android.os.AsyncTask;

import java.util.ArrayList;

/**
 * Homework 05
 * GetHourlyForcastData.java
 * Sanket Patil
 * Atul Banwar
 */

/**
 * If a city/state is does not exist, then as per requirement the application should wait for 5 seconds and go back to mainActivity
 * Implementing the wait logic in AsyncTask, because Thread.Sleep in CityWeather activity will block the activity for 5 seconds and
 * won't allow the user to go back manually, if he/she wants to.
 */

public class WaitFor5Seconds extends AsyncTask<String, Void, ArrayList<Weather>> {
    IData activity;

    public WaitFor5Seconds(IData activity) {
        this.activity = activity;
    }

    @Override
    protected ArrayList<Weather> doInBackground(String... params) {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Weather> s) {
        super.onPostExecute(s);
        activity.finishActivity(s);
    }

    static public interface IData {
        public void finishActivity(ArrayList<Weather> result);
    }
}
