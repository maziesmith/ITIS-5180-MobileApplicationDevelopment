package com.mad.weathernow;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Homework 05
 * WeatherDetails.java
 * Sanket Patil
 * Atul Banwar
 */

public class WeatherDetails extends Activity {

    //UI Components:
    private TextView txtVwHeaderCityState, textViewTime, txtVwTemperature,
            txtVwCondition, txtVwMaxTemp, txtVwMinTemp, txtVwFeels,
            txtVwHumidity, txtVwDewpoint, txtVwPressure, txtVwClouds,
            txtVwWinds;
    private ImageView imgVwTemperatureIcon;

    //String
    private String temperature = "%s°F";
    private static final String FAHRENHEIT = " Fahrenheit";
    private String minMaxTemperature = "%s" + FAHRENHEIT;
    private String cityName;
    private String stateInitials;
    private String time;

    //Weather Object
    private Weather cityCurrentTimeWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_details);

        //UI component initialization
        txtVwHeaderCityState = (TextView) findViewById(R.id.textViewCityState);
        textViewTime = (TextView) findViewById(R.id.textViewTime);
        imgVwTemperatureIcon = (ImageView) findViewById(R.id.imageViewTempIcon);
        txtVwTemperature = (TextView) findViewById(R.id.textViewTemperature);
        txtVwCondition = (TextView) findViewById(R.id.textViewCondition);
        txtVwMaxTemp = (TextView) findViewById(R.id.textViewMaxTemp);
        txtVwMinTemp = (TextView) findViewById(R.id.textViewMinTemp);
        txtVwFeels = (TextView) findViewById(R.id.textViewFeels);
        txtVwHumidity = (TextView) findViewById(R.id.textViewHumidity);
        txtVwDewpoint = (TextView) findViewById(R.id.textViewDewpoint);
        txtVwPressure = (TextView) findViewById(R.id.textViewPressure);
        txtVwClouds = (TextView) findViewById(R.id.textViewClouds);
        txtVwWinds = (TextView) findViewById(R.id.textViewWinds);

        //Getting values from Other activity
        if (getIntent().getExtras() != null) {
            cityName = getIntent().getExtras().getString(MainActivity.CITY);
            stateInitials = getIntent().getExtras().getString(MainActivity.STATE);
            this.cityCurrentTimeWeather = (Weather) getIntent().getSerializableExtra(CityWeather.WEATHER_ITEM);

            txtVwHeaderCityState.setText(getResources().getString(R.string.text_view_current_location_value, cityName, stateInitials));
        }

        //Setting Time
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a Z MMM dd, yyyy");
        SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm a");

        try {
            String fullyQualifiedTime = cityCurrentTimeWeather.getTime();
            fullyQualifiedTime = fullyQualifiedTime.replace(" on", "");
            Date date = sdf.parse(fullyQualifiedTime);
            time = sdf1.format(date);
        } catch (ParseException e) {
            time = "";
        }

        textViewTime.setText("(" + time + ")");

        //Setting Temperature Icon
        Picasso.with(this)
                .load(cityCurrentTimeWeather.getIconUrl())
                .resize(300, 300)
                .centerCrop()
                .into(imgVwTemperatureIcon);

        //Setting Temperature
        txtVwTemperature.setText(String.format(temperature, cityCurrentTimeWeather.getTemperature()));

        //Setting Condition
        txtVwCondition.setText(cityCurrentTimeWeather.getClimateType());

        //Setting Min and Max Temperature
        txtVwMaxTemp.setText(String.format(minMaxTemperature, cityCurrentTimeWeather.getMaximumTemperature()));
        txtVwMinTemp.setText(String.format(minMaxTemperature, cityCurrentTimeWeather.getMinimumTemperature()));

        //Setting other weather details
        txtVwFeels.setText(cityCurrentTimeWeather.getFeelsLike() + FAHRENHEIT);
        txtVwHumidity.setText(cityCurrentTimeWeather.getHumidity() + "%");
        txtVwDewpoint.setText(cityCurrentTimeWeather.getDewPoint() + FAHRENHEIT);
        txtVwPressure.setText(cityCurrentTimeWeather.getPressure() + " hPa");
        txtVwClouds.setText(cityCurrentTimeWeather.getClouds());
        txtVwWinds.setText(cityCurrentTimeWeather.getWindSpeed() + ", " + cityCurrentTimeWeather.getWindDirection());
    }
}
