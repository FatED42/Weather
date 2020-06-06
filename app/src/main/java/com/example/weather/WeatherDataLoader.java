package com.example.weather;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.weather.event.AddCityToListEvent;
import com.example.weather.rest.OpenWeather;
import com.example.weather.rest.models.WeatherModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherDataLoader {

    private static final String API = "http://api.openweathermap.org/";
    private static final String LOG_TAG = "WeatherDataLoader";

    public static void getCurrentData(final CityCard cityCard, final Context context, String units) {
        OpenWeather.getSingleton().getAPI().getCurrentWeatherData(cityCard.getCityName(),
                context.getString(R.string.open_weather_map_app_id),
                units,
                Locale.getDefault().getCountry())
                .enqueue(new Callback<WeatherModel>() {
                    @Override
                    public void onResponse(@NonNull Call<WeatherModel> call, @NonNull Response<WeatherModel> response) {
                        if (response.isSuccessful()) {
                            Log.d(LOG_TAG, "parse data to card call from retrofit for " + cityCard.getCityName());
                            WeatherDataParser.parseDataToCard(cityCard, Objects.requireNonNull(response.body()), context);
                            Log.d(LOG_TAG, "eventBus to update Recycler item from retrofit for " + cityCard.getCityName());
                        } else {
                            Toast.makeText(context, context.getString(R.string.city_not_found), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<WeatherModel> call, @NonNull Throwable t) {
                        Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public static void getCurrentDataAndAddCity(final CityCard cityCard, final Context context, String units) {
        OpenWeather.getSingleton().getAPI().getCurrentWeatherData(cityCard.getCityName(),
                context.getString(R.string.open_weather_map_app_id),
                units,
                Locale.getDefault().getCountry())
                .enqueue(new Callback<WeatherModel>() {
                    @Override
                    public void onResponse(@NonNull Call<WeatherModel> call, @NonNull Response<WeatherModel> response) {
                        if (response.isSuccessful()) {
                            Log.d(LOG_TAG, "parse data to card call from retrofit for " + cityCard.getCityName());
                            WeatherDataParser.parseDataToCard(cityCard, Objects.requireNonNull(response.body()), context);
                            Log.d(LOG_TAG, "eventBus to update Recycler item from retrofit for " + cityCard.getCityName());

                            EventBus.getBus().post(new AddCityToListEvent(cityCard));

                        } else {
                            Toast.makeText(context, context.getString(R.string.city_not_found), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<WeatherModel> call, @NonNull Throwable t) {
                        Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public static void getCurrentDataByGeo(double latitude, double longitude, final Context context, String units) {
        OpenWeather.getSingleton().getAPI().getCurrentWeatherDataByGeo(latitude,
                longitude,
                context.getString(R.string.open_weather_map_app_id),
                units,
                Locale.getDefault().getCountry())
                .enqueue(new Callback<WeatherModel>() {
                    @Override
                    public void onResponse(@NonNull Call<WeatherModel> call, @NonNull Response<WeatherModel> response) {
                        if (response.isSuccessful()) {
                            CityCard cityCard = new CityCard(Objects.requireNonNull(response.body()).name);
                            WeatherDataParser.parseDataToCard(cityCard, response.body(), context);
                            EventBus.getBus().post(new AddCityToListEvent(cityCard));
                        } else {
                            Toast.makeText(context, context.getString(R.string.city_not_found), Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<WeatherModel> call, @NonNull Throwable t) {
                        Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    public static void getForecast(String city, final Context context, String units) {
        OpenWeather.getSingleton().getAPI().getFiveDaysWeatherData(city,
                context.getString(R.string.open_weather_map_app_id),
                units,
                Locale.getDefault().getCountry())
                .enqueue(new Callback<WeatherModel>() {
                    @Override
                    public void onResponse(@NonNull Call<WeatherModel> call, @NonNull Response<WeatherModel> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                WeatherDataParser.parseForecast(response.body(), context);
                            }
                        } else {
                            Toast.makeText(context, context.getString(R.string.city_not_found), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<WeatherModel> call, @NonNull Throwable t) {
                        t.printStackTrace();
                        Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
