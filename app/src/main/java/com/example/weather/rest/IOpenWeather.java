package com.example.weather.rest;

import com.example.weather.rest.models.WeatherModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface IOpenWeather {

    @GET("data/2.5/weather?")
    Observable<WeatherModel> getCurrentWeatherData2(
            @Query("q") String city,
            @Query("APPID") String app_id,
            @Query("units") String units,
            @Query("lang") String lang);

    @GET("data/2.5/weather?")
    Call<WeatherModel> getCurrentWeatherData(
            @Query("q") String city,
            @Query("APPID") String app_id,
            @Query("units") String units,
            @Query("lang") String lang);
}
