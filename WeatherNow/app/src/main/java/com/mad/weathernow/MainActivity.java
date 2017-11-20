package com.mad.weathernow;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

/**
 * Homework 05
 * MainActivity.java
 * Sanket Patil
 * Atul Banwar
 */

public class MainActivity extends Activity {

    //UI components
    private EditText edtTxtCity, edtTxtState;
    private ListView favoriteWeatherList;
    private RelativeLayout layout;
    private LinearLayout linearLayout;
    private TextView textViewNoFav, textViewFavorites;

    //URL setup
    public static final String API_KEY = "2a4297abeeb3af0e";
    public static final String HOURLY_FORECAST_URL = "http://api.wunderground.com/api/" + API_KEY + "/hourly/q/%s/%s.json";

    //Data Passing
    public static final String CITY_WEATHER_URL = "CITY_WEATHER_URL";
    public static final String CITY = "CITY";
    public static final String STATE = "STATE";
    public static final String FAVOURITE_WEATHER_PREFERENCE = "FAVOURITE_WEATHER_PREFERENCE";
    public static final String FAVOURITE_WEATHER_ITEMS = "FAVOURITE_WEATHER_ITEMS";
    public ArrayList<FavoriteWeather> favoriteWeathers;

    private FavoriteWeatherAdapter weatherAdapter;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtTxtCity = (EditText) findViewById(R.id.editTextCity);
        edtTxtState = (EditText) findViewById(R.id.editTextState);
        layout = (RelativeLayout) findViewById(R.id.rootLayout);
        favoriteWeatherList = (ListView) findViewById(R.id.listViewFavorites);
        RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        linearLayout = (LinearLayout) findViewById(R.id.linearLayoutSearch);
        params1.addRule(RelativeLayout.BELOW, linearLayout.getId());
        params1.setMargins(0, 150, 10, 10);
        params1.addRule(RelativeLayout.CENTER_IN_PARENT);
        textViewNoFav = new TextView(MainActivity.this);
        textViewNoFav.setText(getResources().getString(R.string.text_lable_no_favorites));
        textViewNoFav.setTextSize(20);
        layout.addView(textViewNoFav, params1);

        textViewFavorites = (TextView) findViewById(R.id.textViewFavorite);

        setFavouriteItems();

        favoriteWeatherList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                weatherAdapter.remove(favoriteWeathers.get(position));

                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();

                sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                String favouriteItemsStr = gson.toJson(favoriteWeathers);
                editor.putString(MainActivity.FAVOURITE_WEATHER_ITEMS, favouriteItemsStr);
                editor.apply();

                if (favoriteWeathers.size() != 0) {
                    textViewNoFav.setVisibility(View.INVISIBLE);
                    textViewFavorites.setVisibility(View.VISIBLE);
                } else {
                    textViewNoFav.setVisibility(View.VISIBLE);
                    textViewFavorites.setVisibility(View.INVISIBLE);
                }

                return false;
            }
        });
    }

    /**
     * On Click Handler for submit button
     *
     * @param view
     */
    public void clickSubmit(View view) {
        String city = edtTxtCity.getText().toString().trim();
        String state = edtTxtState.getText().toString().trim().toUpperCase();
        String urlCity;

        if (city.length() > 0 && state.length() > 0) {
            if (city.contains(" ")) {
                String cityName[] = city.split(" ");
                urlCity = makeCamelCase(cityName[0]) + "_" + makeCamelCase(cityName[1]);
                city = makeCamelCase(cityName[0]) + " " + makeCamelCase(cityName[1]);
            } else {
                urlCity = city;
            }
            String cityWeatherURL = String.format(HOURLY_FORECAST_URL, state, urlCity);
            
            if(isConnectedOnline()) {
                Intent nextActivity = new Intent(MainActivity.this, CityWeather.class);
                nextActivity.putExtra(CITY_WEATHER_URL, cityWeatherURL);
                nextActivity.putExtra(CITY, city);
                nextActivity.putExtra(STATE, state);
                startActivity(nextActivity);
            }else {
                Toast.makeText(MainActivity.this, getResources().getString(R.string.err_msg_no_internet), Toast.LENGTH_SHORT).show();
            }
        }else
        {
            Toast.makeText(MainActivity.this, getResources().getString(R.string.err_msg_select_city_state), Toast.LENGTH_SHORT).show();
        }
    }

    private String makeCamelCase(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        setFavouriteItems();
    }

    private void setFavouriteItems() {
        favoriteWeathers = new ArrayList<>();

        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String favouriteItemsStr = sharedPreferences.getString(MainActivity.FAVOURITE_WEATHER_ITEMS, "");

        if (!favouriteItemsStr.isEmpty() && !favouriteItemsStr.equals("[]")) {
            textViewNoFav.setVisibility(View.INVISIBLE);
            textViewFavorites.setVisibility(View.VISIBLE);
            favoriteWeathers = gson.fromJson(favouriteItemsStr, new TypeToken<ArrayList<FavoriteWeather>>() {
            }.getType());

            weatherAdapter = new FavoriteWeatherAdapter(this, favoriteWeathers);
            weatherAdapter.setNotifyOnChange(true);
            favoriteWeatherList.setAdapter(weatherAdapter);

        } else {
            textViewNoFav.setVisibility(View.VISIBLE);
            textViewFavorites.setVisibility(View.INVISIBLE);
        }
    }

    private boolean isConnectedOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }
}
