package com.example.weather;

import android.app.Activity;
import android.content.SharedPreferences;

public class CityPreference {

    private static final String KEY = "city";
    private static final String MOSCOW = "Moscow";
    private SharedPreferences userPreference;

    public CityPreference(Activity activity) {
        userPreference = activity.getPreferences(Activity.MODE_PRIVATE);
    }

    public String getCity() {
        return userPreference.getString(KEY, MOSCOW);
    }

    public void setCity(String city) {
        userPreference.edit().putString(KEY, city).apply();
    }
}
