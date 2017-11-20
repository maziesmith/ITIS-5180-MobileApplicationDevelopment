package com.mad.weathernow;

import java.io.Serializable;

/**
 * Homework 05
 * FavoriteWeather.java
 * Sanket Patil
 * Atul Banwar
 */

public class FavoriteWeather implements Serializable {

    private String city;
    private String state;
    private String temperature;
    private String date;

    public FavoriteWeather(String city, String state, String temperature, String date) {
        this.city = city;
        this.state = state;
        this.temperature = temperature;
        this.date = date;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
