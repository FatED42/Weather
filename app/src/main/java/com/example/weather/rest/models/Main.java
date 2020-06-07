package com.example.weather.rest.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Main {

    @SerializedName("temp")
    @Expose
    public double temp;
    @SerializedName("feels_like")
    @Expose
    public double feelsLike;
    @SerializedName("temp_min")
    @Expose
    public double tempMin;
    @SerializedName("temp_max")
    @Expose
    public double tempMax;
    @SerializedName("pressure")
    @Expose
    public int pressure;
    @SerializedName("humidity")
    @Expose
    public int humidity;
    @SerializedName("sea_level")
    @Expose
    public int seaLevel;
    @SerializedName("grnd_level")
    @Expose
    public int grndLevel;
    @SerializedName("temp_kf")
    @Expose
    public double tempKf;

}
