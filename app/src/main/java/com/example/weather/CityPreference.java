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
    private static final String CITIES_PREFS = "citiesPrefs";
    private SharedPreferences userPreference;

    public CityPreference(Activity activity) {
        userPreference = activity.getSharedPreferences(CITIES_PREFS, Activity.MODE_PRIVATE);
    }

    public CityCard getCityCard(String city) {
        ArrayList<CityCard> list = getList();
        for (CityCard c : list) {
            if (c.cityName.equals(city)) return c;
        }
        return null;
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
        if (array == null || array.get(0).getCityName() == null) {
            array = new ArrayList<>();
            CityCard moscow = new CityCard("Moscow");
            moscow.setPosition(0);
            CityCard spb = new CityCard("Saint Petersburg");
            spb.setPosition(1);
            array.add(moscow);
            array.add(spb);
        }
        return array;
    }

    public void setThemeCheckBox(boolean isChecked) {
        userPreference.edit().putBoolean("THEME", isChecked).apply();
    }

    public Boolean getThemeCheckBox() {
        return userPreference.getBoolean("THEME", false);
    }

    public void setUnits(String units) {
        userPreference.edit().putString("UNITS", units).apply();
    }

    public String getUnits() {
        return userPreference.getString("UNITS", "metric");
    }

    public void setPressure(int pressure){
        userPreference.edit().putInt("PRESSURE", pressure).apply();
    }

    public int getPressure(){
        return userPreference.getInt("PRESSURE", 0);
    }
}
