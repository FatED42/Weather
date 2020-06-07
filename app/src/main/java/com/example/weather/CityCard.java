package com.example.weather;

import java.io.Serializable;
import java.util.Objects;

public class CityCard implements Serializable {

    private int position;
    private int humidity;
    private int pressure;
    private int wind;
    private String cityName, icon, country, description, updateOn;
    private double temp, feelsTemp, tempMax, tempMin;

    public CityCard(String cityName) {
        this.cityName = cityName;
    }

    public String getCityName() {
        return cityName;
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

    public int getWind() {
        return wind;
    }

    void setWind(double wind) {
        this.wind = (int) wind;
    }

    void setCityName(String cityName) {
        this.cityName = cityName;
    }

    void setIcon(String icon) {
        this.icon = icon;
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

    public String getUpdateOn() {
        return updateOn;
    }

    void setUpdateOn(String updateOn) {
        this.updateOn = updateOn;
    }

    void setTemp(double temp) {
        this.temp = temp;
    }

    void setFeelsTemp(double feelsTemp) {
        this.feelsTemp = feelsTemp;
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

    public double getTemp(){
        return temp;
    }

    public double getFeelsTemp(){
        return feelsTemp;
    }

    public String getIcon(){
        return icon;
    }

    int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        CityCard cityCard = (CityCard) object;
        return this.cityName.equalsIgnoreCase(cityCard.cityName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cityName);
    }

}
