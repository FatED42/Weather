package com.example.weather.rest;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OpenWeather {
    private static OpenWeather singleton = null;
    private IOpenWeather API;

    private OpenWeather() {
        API = createAdapter();
    }

    public static OpenWeather getSingleton() {
        if (singleton == null) {
            singleton = new OpenWeather();
        }

        return singleton;
    }

    public IOpenWeather getAPI() {
        return API;
    }

    private IOpenWeather createAdapter() {
        Retrofit adapter = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return adapter.create(IOpenWeather.class);
    }

}
