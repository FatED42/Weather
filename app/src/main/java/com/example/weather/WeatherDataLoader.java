package com.example.weather;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.weather.event.AddCityToListEvent;
import com.example.weather.event.UpdateRecyclerListAfterParsingData;
import com.example.weather.rest.IOpenWeather;
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
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class WeatherDataLoader {

    private static final String OPEN_WEATHER_MAP_API = "http://api.openweathermap.org/data/2.5/weather?q=%s&units=%s&lang=%s";

    private static final String KEY = "x-api-key";
    private static final String RESPONSE = "cod";
    private static final String NEW_LINE = "\n";
    private static final int ALL_GOOD = 200;

    private static final String API = "http://api.openweathermap.org/";
    private static final String LOG_TAG = "WeatherDataLoader";

    public static void getJSONData(Context context, CityCard cityCard, String units, String language, Handler handler) {
        new Thread(() -> {
            try {
                String city = cityCard.getCityName();
                URL url = new URL(String.format(OPEN_WEATHER_MAP_API, city, units, language));
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.addRequestProperty(KEY, context.getString(R.string.open_weather_map_app_id));

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder rawData = new StringBuilder(1024);
                String tempVariable;
                while ((tempVariable = reader.readLine()) != null) {
                    rawData.append(tempVariable).append(NEW_LINE);
                }
                reader.close();

                JSONObject jsonObject = new JSONObject(rawData.toString());


                if (jsonObject.getInt(RESPONSE) != ALL_GOOD) {
                    Toast.makeText(context, context.getString(R.string.city_not_found),
                            Toast.LENGTH_LONG).show();
                } else {
                    WeatherDataParser.parseDataToCard(cityCard, jsonObject, context);
                }

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            } finally {
                Log.d(LOG_TAG, "eventBus to update Recycler item from getJsonData");
                handler.post(() -> EventBus.getBus().post(new UpdateRecyclerListAfterParsingData(cityCard.getPosition())));
            }

        }).start();
    }

    public static void getCurrentData(CityCard cityCard, Context context, String units, boolean isCityAdding) {
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
                            EventBus.getBus().post(new UpdateRecyclerListAfterParsingData(cityCard.getPosition()));
                            if (isCityAdding)
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

    public static void getCurrentData2(CityCard cityCard, Context context, String units) {
        Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        IOpenWeather service = retrofit.create(IOpenWeather.class);
        Observable<WeatherModel> call = service
                .getCurrentWeatherData2(
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
