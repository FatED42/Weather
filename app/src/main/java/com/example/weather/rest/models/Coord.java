package com.example.weather.rest.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Coord {

    @SerializedName("lat")
    @Expose
    public float lat;
    @SerializedName("lon")
    @Expose
    public float lon;

}
