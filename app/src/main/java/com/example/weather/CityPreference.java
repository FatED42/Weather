package com.example.weather;

import android.app.Activity;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;

public class CityPreference {

    private static final String KEY = "city";
    private static final String MOSCOW = "Moscow";
    private static final String CITIES_LIST_KEY = "Cities list key";
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

    public void setList(ArrayList<CityCard> list){
        Gson gson = new Gson();
        String citiesList = gson.toJson(list);
        userPreference.edit().putString(CITIES_LIST_KEY, citiesList).apply();
    }

    public ArrayList<CityCard> getList(){
        Gson gson = new Gson();
        String json = userPreference.getString(CITIES_LIST_KEY, null);
        Type type = new TypeToken<ArrayList<CityCard>>() {}.getType();
        ArrayList<CityCard> array = gson.fromJson(json, type);
        if (array == null) {
            CityCard[] data = new CityCard[]{
                    new CityCard("Moscow"),
                    new CityCard("Saint Petersburg"),
                    new CityCard("Zelenograd"),
                    new CityCard("Roslavl")
            };
            array = new ArrayList<>(Arrays.asList(data));
            return array;
        }
        else return array;
    }
}
