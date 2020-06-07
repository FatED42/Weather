package com.example.weather;

import android.content.Context;
import android.util.Log;

import com.example.weather.rest.models.WeatherModel;

import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

public class WeatherDataParser {

    private static final String LOG_TAG = "WeatherDataParser";

    static void parseDataToCard(CityCard cityCard, WeatherModel weatherModel, Context context) {
        Log.d(LOG_TAG, "starting parsing for " + cityCard.getCityName() + " with retrofit");
        assert weatherModel != null;
        cityCard.setTemp(weatherModel.getMain().getTemp());
        cityCard.setTempMax(weatherModel.getMain().getTempMax());
        cityCard.setTempMin(weatherModel.getMain().getTempMin());
        cityCard.setFeelsTemp(weatherModel.getMain().getFeelsLike());
        cityCard.setIcon(getWeatherIcon(
                weatherModel.getWeather().get(0).getId(),
                weatherModel.getSys().getSunrise(),
                weatherModel.getSys().getSunset(),
                context));
        cityCard.setHumidity(weatherModel.getMain().getHumidity());
        cityCard.setPressure(weatherModel.getMain().getPressure());
        cityCard.setCityName(weatherModel.getName());
        cityCard.setCountry(weatherModel.getSys().getCountry());
        cityCard.setDescription(weatherModel.getWeather().get(0).getDescription());
        cityCard.setUpdateOn(getUpdateTime(weatherModel.getDt()));
        cityCard.setWind(weatherModel.getWind().getSpeed());
    }

    static void parseDataToCard(CityCard cityCard, JSONObject json, Context context) {
        Log.d(LOG_TAG, "json " + json.toString());
        try {
            //up
            String cityName = json.getString("name");
            //sys
            String country = json.getJSONObject("sys").getString("country");
            long sunrise = json.getJSONObject("sys").getLong("sunrise") * 1000;
            long sunset = json.getJSONObject("sys").getLong("sunset") * 1000;
            //weather
            JSONObject weather = json.getJSONArray("weather").getJSONObject(0);
            String overcastValue = weather.getString("description");
            int id = weather.getInt("id");
            //main
            JSONObject main = json.getJSONObject("main");
            double tempToday = main.getDouble("temp");
            double tempFeelsLike = main.getDouble("feels_like");
            double tempMin = main.getDouble("temp_min");
            double tempMax = main.getDouble("temp_max");
            int pressureValue = main.getInt("pressure");
            int humidityValue = main.getInt("humidity");
            //wind
            int windValue = json.getJSONObject("wind").getInt("speed");
            //date
            String updateOn = getUpdateTime(json.getInt("dt"));
            String icon = getWeatherIcon(id, sunrise, sunset, context);

            cityCard.setTemp(tempToday);
            cityCard.setTempMax(tempMax);
            cityCard.setTempMin(tempMin);
            cityCard.setFeelsTemp(tempFeelsLike);
            cityCard.setIcon(icon);
            cityCard.setHumidity(humidityValue);
            cityCard.setPressure(pressureValue);
            cityCard.setCityName(cityName);
            cityCard.setCountry(country);
            cityCard.setDescription(overcastValue);
            cityCard.setUpdateOn(updateOn);
            cityCard.setWind(windValue);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "One or more fields not found in the JSON data");
        }
    }

    private static String getUpdateTime(long dt) {
        return DateFormat.getDateTimeInstance(DateFormat.SHORT,
                DateFormat.SHORT, Locale.getDefault()).format(new Date(dt * 1000));
    }

    private static String getWeatherIcon(int actualId, long sunrise, long sunset, Context context) {
        int id = actualId / 100;
        String icon = "";

        if (actualId == 800) {
            long currentTime = new Date().getTime();
            if (currentTime >= sunrise && currentTime < sunset) {
                icon = context.getString(R.string.weather_sunny);
            } else {
                icon = context.getString(R.string.weather_clear_night);
            }
        } else {
            switch (id) {
                case 2: {
                    icon = context.getString(R.string.weather_thunder);
                    break;
                }
                case 3: {
                    icon = context.getString(R.string.weather_drizzle);
                    break;
                }
                case 5: {
                    icon = context.getString(R.string.weather_rainy);
                    break;
                }
                case 6: {
                    icon = context.getString(R.string.weather_snowy);
                    break;
                }
                case 7: {
                    icon = context.getString(R.string.weather_foggy);
                    break;
                }
                case 8: {
                    icon = context.getString(R.string.weather_cloudy);
                    break;
                }
            }
        }
        return icon;
    }

}
