package com.mad.weathernow;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Homework 05
 * Weather.java
 * Sanket Patil
 * Atul Banwar
 */

public class Weather implements Serializable {
    private String time;
    private String temperature;
    private String dewPoint;
    private String clouds;
    private String iconUrl;
    private String windSpeed;
    private String windDirection;
    private String climateType;
    private String humidity;
    private String feelsLike;
    private String maximumTemperature;
    private String minimumTemperature;
    private String pressure;

    public static Weather getWeather(JSONObject obj) throws JSONException {
        Weather weather = new Weather();

        JSONObject timeObj = obj.getJSONObject("FCTTIME");
        weather.setTime(timeObj.getString("pretty"));

        JSONObject tempObj = obj.getJSONObject("temp");
        weather.setTemperature(tempObj.getString("english"));
        weather.setMaximumTemperature(tempObj.getString("english"));
        weather.setMinimumTemperature(tempObj.getString("english"));

        JSONObject dewPointObj = obj.getJSONObject("dewpoint");
        weather.setDewPoint(dewPointObj.getString("english"));

        weather.setClouds(obj.getString("condition"));
        weather.setIconUrl(obj.getString("icon_url"));

        JSONObject windSpeedObj = obj.getJSONObject("wspd");
        weather.setWindSpeed(windSpeedObj.getString("english") + " mph");

        JSONObject windDirObj = obj.getJSONObject("wdir");
        String direction = windDirObj.getString("dir");
        direction = getFullDirection(direction);
        weather.setWindDirection(windDirObj.getString("degrees") + "\u00B0 " + direction);

        weather.setClimateType(obj.getString("wx"));
        weather.setHumidity(obj.getString("humidity"));

        JSONObject feelsLikeObj = obj.getJSONObject("feelslike");
        weather.setFeelsLike(feelsLikeObj.getString("english"));

        JSONObject pressureObj = obj.getJSONObject("mslp");
        weather.setPressure(pressureObj.getString("metric"));

        return weather;
    }

    private static String getFullDirection(String direction) {
        if (direction.equals("E")) {
            direction = "East";
        } else if (direction.equals("W")) {
            direction = "West";
        } else if (direction.equals("S")) {
            direction = "South";
        } else if (direction.equals("N")) {
            direction = "North";
        } else if (direction.equals("ES")) {
            direction = "East South";
        } else if (direction.equals("EN")) {
            direction = "East North";
        } else if (direction.equals("WS")) {
            direction = "West South";
        } else if (direction.equals("WN")) {
            direction = "West North";
        } else if (direction.equals("SE")) {
            direction = "South East";
        } else if (direction.equals("SW")) {
            direction = "South West";
        } else if (direction.equals("NE")) {
            direction = "North East";
        } else if (direction.equals("NW")) {
            direction = "North West";
        }

        return direction;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getDewPoint() {
        return dewPoint;
    }

    public void setDewPoint(String dewPoint) {
        this.dewPoint = dewPoint;
    }

    public String getClouds() {
        return clouds;
    }

    public void setClouds(String clouds) {
        this.clouds = clouds;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(String windDirection) {
        this.windDirection = windDirection;
    }

    public String getClimateType() {
        return climateType;
    }

    public void setClimateType(String climateType) {
        this.climateType = climateType;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getFeelsLike() {
        return feelsLike;
    }

    public void setFeelsLike(String feelsLike) {
        this.feelsLike = feelsLike;
    }

    public String getMaximumTemperature() {
        return maximumTemperature;
    }

    public void setMaximumTemperature(String maximumTemperature) {
        this.maximumTemperature = maximumTemperature;
    }

    public String getMinimumTemperature() {
        return minimumTemperature;
    }

    public void setMinimumTemperature(String minimumTemperature) {
        this.minimumTemperature = minimumTemperature;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    @Override
    public String toString() {
        return "Weather{" +
                "time='" + time + '\'' +
                ", temperature='" + temperature + '\'' +
                ", dewPoint='" + dewPoint + '\'' +
                ", clouds='" + clouds + '\'' +
                ", iconUrl='" + iconUrl + '\'' +
                ", windSpeed='" + windSpeed + '\'' +
                ", windDirection='" + windDirection + '\'' +
                ", climateType='" + climateType + '\'' +
                ", humidity='" + humidity + '\'' +
                ", feelsLike='" + feelsLike + '\'' +
                ", maximumTemperature='" + maximumTemperature + '\'' +
                ", minimumTemperature='" + minimumTemperature + '\'' +
                ", pressure='" + pressure + '\'' +
                '}';
    }
}
