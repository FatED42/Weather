package com.example.weather;

import java.io.Serializable;
import java.util.Objects;

public class ForecastCard implements Serializable {

    private String date;
    private String icon;
    private String name;
    private String country;
    private String description;

    public String getUpdateOn() {
        return updateOn;
    }

    void setUpdateOn(String updateOn) {
        this.updateOn = updateOn;
    }

    private String updateOn;
    private double temp;
    private double tempFeelsLike;
    private double tempMax;
    private double tempMin;
    private double tempNight;
    private int day;
    private int hour;
    private int humidity, pressure;

    ForecastCard(String date, String icon, double temp, int day, int hour) {
        this.date = date;
        this.icon = icon;
        this.temp = temp;
        this.day = day;
        this.hour = hour;
    }

    public double getTempNight() {
        return tempNight;
    }

    void setTempNight(double tempNight) {
        this.tempNight = tempNight;
    }

    int getDay() {
        return day;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    void setCountry(String country) {
        this.country = country;
    }

    public String getDescription() {
        return description;
    }

    void setDescription(String description) {
        this.description = description;
    }

    public double getTempFeelsLike() {
        return tempFeelsLike;
    }

    void setTempFeelsLike(double tempFeelsLike) {
        this.tempFeelsLike = tempFeelsLike;
    }

    public double getTempMax() {
        return tempMax;
    }

    void setTempMax(double tempMax) {
        this.tempMax = tempMax;
    }

    public double getTempMin() {
        return tempMin;
    }

    void setTempMin(double tempMin) {
        this.tempMin = tempMin;
    }

    public int getHumidity() {
        return humidity;
    }

    void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public int getPressure() {
        return pressure;
    }

    public void setPressure(int pressure) {
        this.pressure = pressure;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ForecastCard that = (ForecastCard) o;
        return Objects.equals(getDate(), that.getDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDate());
    }

    public String getDate() {
        return date;
    }

    public String getIcon() {
        return icon;
    }

    public double getTemp() {
        return temp;
    }
}
