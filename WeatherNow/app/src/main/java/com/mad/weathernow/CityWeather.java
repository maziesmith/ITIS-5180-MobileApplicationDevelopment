package com.mad.weathernow;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Homework 05
 * CityWeather.java
 * Sanket Patil
 * Atul Banwar
 */

public class CityWeather extends Activity implements GetHourlyForcastData.IData, WaitFor5Seconds.IData {
    private String cityName;
    private String stateInitials;
    private String hourlyForcaseURL;
    private ArrayList<Weather> weatherHourlyItems;

    private TextView txtViewCurrentLocationValue;
    private ListView lstViewHourlyData;
    private ProgressDialog progressDialog;

    public static String WEATHER_ITEM = "WEATHER_ITEM";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_weather);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.msg_progress_dialog_loading));
        progressDialog.setIndeterminate(true);
        weatherHourlyItems = new ArrayList<>();

        txtViewCurrentLocationValue = (TextView) findViewById(R.id.textViewCurrentLocationValue);
        lstViewHourlyData = (ListView) findViewById(R.id.listViewHourlyData);

        if (getIntent().getExtras() != null) {
            cityName = getIntent().getExtras().getString(MainActivity.CITY);
            stateInitials = getIntent().getExtras().getString(MainActivity.STATE);
            hourlyForcaseURL = getIntent().getExtras().getString(MainActivity.CITY_WEATHER_URL);

            txtViewCurrentLocationValue.setText(getResources().getString(R.string.text_view_current_location_value, cityName, stateInitials));
            new GetHourlyForcastData(this).execute(hourlyForcaseURL);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflator= getMenuInflater();
        menuInflator.inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_favorites && weatherHourlyItems.size() > 0) {
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yy", Locale.US);

            String currentTemperature = "";
            String currentDate = dateFormat.format(calendar.getTime());
            dateFormat = new SimpleDateFormat("h:'00' a", Locale.US);
            String currentTime = dateFormat.format(calendar.getTime());

            // Fetching current temperature
            for (Weather wt : weatherHourlyItems){
                dateFormat = new SimpleDateFormat("hh:mm a Z MMM dd, yyyy", Locale.US);
                try {
                    String fullyQualifiedTime = wt.getTime();
                    fullyQualifiedTime =  fullyQualifiedTime.replace(" on", "");
                    Date date = dateFormat.parse(fullyQualifiedTime);
                    dateFormat = new SimpleDateFormat("h:'00' a", Locale.US);
                    String weatherItemTime = dateFormat.format(date);

                    if (currentTime.equals(weatherItemTime)) {
                        currentTemperature = wt.getTemperature();
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            ArrayList<FavoriteWeather> favoriteWeathers = new ArrayList<>();
            FavoriteWeather favoriteWeather = new FavoriteWeather(cityName, stateInitials, currentTemperature, currentDate);

            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();

            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            String favouriteItemsStr = sharedPreferences.getString(MainActivity.FAVOURITE_WEATHER_ITEMS, "");
            if (!favouriteItemsStr.isEmpty()) {
                favoriteWeathers = gson.fromJson(favouriteItemsStr, new TypeToken<ArrayList<FavoriteWeather>>() {}.getType());
            }

            if (favoriteWeathers.size() == 0) {
                Toast.makeText(this, getResources().getString(R.string.msg_added_to_favourite), Toast.LENGTH_SHORT).show();
                favoriteWeathers.add(favoriteWeather);
            } else {
                Boolean isPresent = false;

                for (FavoriteWeather fw : favoriteWeathers) {
                    if (fw.getCity().equals(cityName) && fw.getState().equals(stateInitials)) {
                        fw.setDate(currentDate);
                        fw.setTemperature(currentTemperature);
                        isPresent = true;
                        Toast.makeText(this, getResources().getString(R.string.msg_updated_favourite), Toast.LENGTH_SHORT).show();
                    }
                }

                if (!isPresent) {
                    Toast.makeText(this, getResources().getString(R.string.msg_added_to_favourite), Toast.LENGTH_SHORT).show();
                    favoriteWeathers.add(favoriteWeather);
                }
            }

            SharedPreferences.Editor editor = sharedPreferences.edit();
            favouriteItemsStr = gson.toJson(favoriteWeathers);
            editor.putString(MainActivity.FAVOURITE_WEATHER_ITEMS, favouriteItemsStr);
            editor.apply();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void startHourlyForcastDataFetch() {
        progressDialog.show();
    }

    @Override
    public void setupData(ArrayList<Weather> result) {
        progressDialog.dismiss();

        if (result.size() > 0) {
            weatherHourlyItems = result;

            WeatherAdapter weatherAdapter = new WeatherAdapter(this, weatherHourlyItems);
            weatherAdapter.setNotifyOnChange(true);
            lstViewHourlyData.setAdapter(weatherAdapter);

            lstViewHourlyData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(CityWeather.this, WeatherDetails.class);
                    intent.putExtra(WEATHER_ITEM, weatherHourlyItems.get(position));
                    intent.putExtra(MainActivity.CITY,cityName );
                    intent.putExtra(MainActivity.STATE, stateInitials);
                    startActivity(intent);
                }
            });
        } else {
            Toast.makeText(CityWeather.this, getResources().getString(R.string.msg_no_matches), Toast.LENGTH_SHORT).show();
            new WaitFor5Seconds(this).execute("");
        }
    }

    @Override
    public void finishActivity(ArrayList<Weather> result) {
        finish();
    }
}
