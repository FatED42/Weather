package com.example.weather.callBackInterfaces;

public interface IAdapterCallbacks {
    void startWeatherFragment(String city);
    void onAdapterUpdate();
    void saveList();
}
