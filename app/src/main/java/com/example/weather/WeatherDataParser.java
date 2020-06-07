package com.example.weather;

import android.content.Context;
import android.util.Log;

import com.example.weather.event.GetListEvent;
import com.example.weather.event.UpdateRecyclerListAfterParsingData;
import com.example.weather.rest.models.TimeGap;
import com.example.weather.rest.models.WeatherModel;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class WeatherDataParser {

    private static final String LOG_TAG = "WeatherParser";

    static void parseDataToCard(CityCard cityCard, WeatherModel weatherModel, Context context) {
        Log.d(LOG_TAG, "starting parsing for " + cityCard.getCityName() + " with retrofit");
        assert weatherModel != null;
        cityCard.setTemp(weatherModel.main.temp);
        cityCard.setTempMax(weatherModel.main.tempMax);
        cityCard.setTempMin(weatherModel.main.tempMin);
        cityCard.setFeelsTemp(weatherModel.main.feelsLike);
        cityCard.setIcon(getWeatherIcon(
                weatherModel.weather.get(0).id,
                weatherModel.sys.sunrise,
                weatherModel.sys.sunset,
                context));
        cityCard.setHumidity(weatherModel.main.humidity);
        cityCard.setPressure(weatherModel.main.pressure);
        cityCard.setCityName(weatherModel.name);
        cityCard.setCountry(weatherModel.sys.country);
        cityCard.setDescription(weatherModel.weather.get(0).description);
        cityCard.setUpdateOn(getUpdateTime(weatherModel.dt));
        cityCard.setWind(weatherModel.wind.speed);
        EventBus.getBus().post(new UpdateRecyclerListAfterParsingData(cityCard.getPosition()));
    }

    static void parseForecast(WeatherModel weatherModel, Context context) {
        ArrayList<ForecastCard> cards = new ArrayList<>();
        List<TimeGap> timeGaps = weatherModel.list;
        int count = 0;
        for (int i = 0; i < timeGaps.size(); i++) {
            int day = getDay(timeGaps.get(i).dt);
            int hour = getHour(timeGaps.get(i).dt);
            int id = timeGaps.get(i).weather.get(0).id;
            long sunrise = weatherModel.city.sunrise;
            long sunset = weatherModel.city.sunset;
            String icon = getWeatherIcon(id, sunrise, sunset, context);
            if (cards.size() == 0) {
                ForecastCard card = new ForecastCard(context.getString(R.string.now), icon, timeGaps.get(i).main.temp, day, hour);
                card.setCountry(weatherModel.city.country);
                card.setName(weatherModel.city.name);
                card.setDescription(timeGaps.get(i).weather.get(0).description);
                card.setHumidity(timeGaps.get(i).main.humidity);
                card.setPressure(timeGaps.get(i).main.pressure);
                card.setTempFeelsLike(timeGaps.get(i).main.feelsLike);
                card.setTempMax(timeGaps.get(i).main.tempMax);
                card.setTempMin(timeGaps.get(i).main.tempMin);
                card.setUpdateOn(getUpdateTime(timeGaps.get(i).dt));
                cards.add(card);
                count++;
            } else if (day != cards.get(count - 1).getDay()
                    && hour == 12) {
                ForecastCard card = new ForecastCard(getDayOfWeek(timeGaps.get(i).dt, cards, context), icon, timeGaps.get(i).main.temp, day, hour);
                cards.add(card);
                count++;
            } else if (day != cards.get(count - 1).getDay()
                    && hour == 3) {
                cards.get(count - 1).setTempNight(timeGaps.get(i).main.temp);
            }

        }
        EventBus.getBus().post(new GetListEvent(cards));
    }


    private static String getUpdateTime(long dt) {
        return DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, Locale.getDefault()).format(new Date(dt * 1000));
    }

    private static int getDay(long dt) {
        Date date = new Date(dt * 1000);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    private static int getHour(long dt) {
        Date date = new Date(dt * 1000);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    private static String getDayOfWeek(long dt, ArrayList<ForecastCard> cards, Context context) {
        Date date = new Date(dt * 1000);
        switch (cards.size()) {
            case 0:
                return context.getString(R.string.now);
            case 1:
                return context.getString(R.string.tomorrow);
            default:
                return android.text.format.DateFormat.format("EEEE", date).toString();

        }
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
