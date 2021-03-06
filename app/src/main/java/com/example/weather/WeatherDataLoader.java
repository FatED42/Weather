package com.example.weather;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.weather.event.AddCityToListEvent;
import com.example.weather.event.SetValuesOnTheTempFragmentEvent;
import com.example.weather.event.UpdateRecyclerListAfterParsingData;
import com.example.weather.rest.IOpenWeather;
import com.example.weather.rest.OpenWeather;
import com.example.weather.rest.models.WeatherModel;

import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class WeatherDataLoader {

    private static final String API = "http://api.openweathermap.org/";
    private static final String LOG_TAG = "WeatherDataLoader";

    public static void getCurrentData(CityCard cityCard, Context context, String units) {
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

    public static void getCurrentDataAndAddCity(CityCard cityCard, Context context, String units) {
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

    public static void getCurrentDataAndSetValues(CityCard cityCard, Context context, String units) {
        OpenWeather.getSingleton().getAPI().getCurrentWeatherData(cityCard.getCityName(),
                context.getString(R.string.open_weather_map_app_id),
                units,
                Locale.getDefault().getCountry())
                .enqueue(new Callback<WeatherModel>() {
                    @Override
                    public void onResponse(@NonNull Call<WeatherModel> call, @NonNull Response<WeatherModel> response) {
                        if (response.isSuccessful()) {
                            if (cityCard.getCityName() != null) {
                                Log.d(LOG_TAG, "parse data to card call from retrofit for " + cityCard.getCityName());
                                WeatherDataParser.parseDataToCard(cityCard, Objects.requireNonNull(response.body()), context);
                                Log.d(LOG_TAG, "eventBus to TempScreenFragment.setValues() " + cityCard.getCityName());
                                EventBus.getBus().post(new SetValuesOnTheTempFragmentEvent());
                            }
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

    public static void getCurrentDataByGeo(double latitude, double longitude, Context context, String units) {
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

    public static void getCurrentDataWithRx(CityCard cityCard, Context context, String units) {
        Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        IOpenWeather service = retrofit.create(IOpenWeather.class);
        Observable<WeatherModel> call = service.getCurrentWeatherDataWithRx(
                cityCard.getCityName(),
                context.getString(R.string.open_weather_map_app_id),
                units,
                Locale.getDefault().getCountry());
        call.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<WeatherModel>() {
                    @Override
                    public void onCompleted() {
                        Log.d(LOG_TAG, "eventBus to update Recycler item from retrofit for " + cityCard.getCityName());
                        EventBus.getBus().post(new UpdateRecyclerListAfterParsingData(cityCard.getPosition()));
                    }
                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                    @Override
                    public void onNext(WeatherModel weatherModel) {
                        Log.d(LOG_TAG, "parse data to card call from retrofit for " + cityCard.getCityName());
                        WeatherDataParser.parseDataToCard(cityCard, weatherModel, context);
                    }
                });
    }

}
