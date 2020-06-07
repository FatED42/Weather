package com.example.weather;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Objects;

@Entity(indices = {@Index(value = {"cityName", "number_of_searches"})})
public class CityCard implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "cityName")
    public String cityName;

    @Ignore
    public double temp;

    @ColumnInfo(name = "number_of_searches")
    public int numberOfSearches;

    @Ignore
    private int position;
    @Ignore
    private int humidity;
    @Ignore
    private int pressure;
    @Ignore
    private int wind;

    @Ignore
    private int dt;

    @Ignore
    private String icon, country, description, updateOn;

    @Ignore
    private double feelsTemp, tempMax, tempMin;

    public CityCard(String cityName) {
        this.cityName = cityName;
    }

    public String getCityName() {
        return cityName;
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

    void setWind(double wind) {
        this.wind = (int) wind;
    }

    void setCityName(String cityName) {
        this.cityName = cityName;
    }

    void setIcon(String icon) {
        this.icon = icon;
    }

    void setCountry(String country) {
        this.country = country;
    }

    void setDescription(String description) {
        this.description = description;
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

    void setTempMax(double tempMax) {
        this.tempMax = tempMax;
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
