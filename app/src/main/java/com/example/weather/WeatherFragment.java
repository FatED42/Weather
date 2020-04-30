package com.example.weather;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class WeatherFragment extends Fragment {

    private TextView tempTodayTextView, feelsLikeTextView, humidityTextView, overcastTextView,
                    cityTextView, todayDateTextView;
    private ImageButton wikiInfoBtn;
    private int humidityValue;
    private double tempTodayValue, tempFeelsLikeValue;
    private String cityName, overcastValue;
    private final String HUMIDITY_VALUE_KEY = "HUMIDITY_VALUE_KEY",
            OVERCAST_VALUE_KEY = "OVERCAST_VALUE_KEY", TEMP_TODAY_KEY = "TEMP_TODAY_KEY",
            TEMP_FEELS_LIKE_KEY = "TEMP_FEELS_LIKE_KEY", CITY_NAME_KEY = "CITY_NAME_KEY";

    private final Handler handler = new Handler();
    private static final String LOG_TAG = "WeatherFragment";

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            humidityValue = savedInstanceState.getInt(HUMIDITY_VALUE_KEY);
            overcastValue = savedInstanceState.getString(OVERCAST_VALUE_KEY);
            tempTodayValue = savedInstanceState.getDouble(TEMP_TODAY_KEY);
            tempFeelsLikeValue = savedInstanceState.getDouble(TEMP_FEELS_LIKE_KEY);
            cityName = savedInstanceState.getString(CITY_NAME_KEY);

            setValues();
        }
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_weather, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        setTodayDate();
        onWikiInfoBtnClicked();
        setHasOptionsMenu(true);
    }

    private void initViews(View view) {
        tempTodayTextView = view.findViewById(R.id.temperatureValue);
        feelsLikeTextView = view.findViewById(R.id.feelsLikeTempValue);
        humidityTextView = view.findViewById(R.id.humidityValue);
        overcastTextView = view.findViewById(R.id.overcastValue);
        cityTextView = view.findViewById(R.id.cityTextView);
        wikiInfoBtn = view.findViewById(R.id.toWikiBtn);
        todayDateTextView = view.findViewById(R.id.todayDate);
    }

    private void setTodayDate() {
        Date todayDate;
        Calendar calendar = Calendar.getInstance();
        todayDate = calendar.getTime();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        todayDateTextView.setText(new SimpleDateFormat("dd MMM yyyy EEEE HH:mm",
                Locale.getDefault()).format(todayDate));
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        savedInstanceState.putInt(HUMIDITY_VALUE_KEY, humidityValue);
        savedInstanceState.putString(OVERCAST_VALUE_KEY, overcastValue);;
        savedInstanceState.putDouble(TEMP_TODAY_KEY, tempTodayValue);
        savedInstanceState.putDouble(TEMP_FEELS_LIKE_KEY, tempFeelsLikeValue);
        savedInstanceState.putString(CITY_NAME_KEY, cityName);
        super.onSaveInstanceState(savedInstanceState);
    }

    private void setValues() {
        cityTextView.setText(cityName);
        tempTodayTextView.setText(String.valueOf(tempTodayValue));
        overcastTextView.setText(overcastValue);
        humidityTextView.setText(String.valueOf(humidityValue));
        feelsLikeTextView.setText(String.valueOf(tempFeelsLikeValue));
    }

    void updateWeather() {
        if (getArguments() != null) {
            CitiesContainer citiesContainer = (CitiesContainer) getArguments().getSerializable(CitiesFragment.CITY_KEY);
            if (citiesContainer != null) {
                cityName = citiesContainer.cityName;
                updateWeatherData(cityName);
            }
        }
    }

    private void updateWeatherData(final String cityName) {
        new Thread() {
            public void run() {
                final JSONObject json = WeatherDataLoader.
                        getJSONData(Objects.requireNonNull(getActivity()), cityName);
                if (json == null) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(),getString(R.string.city_not_found),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            renderWeather(json);
                        }
                    });
                }
            }
        }.start();
    }

    private void renderWeather(JSONObject json) {
        Log.d(LOG_TAG, "json " + json.toString());
        try {
            cityName = json.getString("name");
            JSONObject details = json.getJSONArray("weather").getJSONObject(0);
            JSONObject main = json.getJSONObject("main");
            tempTodayValue = main.getDouble("temp");
            humidityValue = main.getInt("humidity");
            overcastValue = details.getString("main");
            setValues();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onWikiInfoBtnClicked() {
        wikiInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textUrl;
                if (cityName == null) {
                    Toast.makeText(getContext(), getString(R.string.choose_city).concat("!"),
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    textUrl = String.format("https://www.wikipedia.org/wiki/%s", cityName);
                    Uri uri = Uri.parse(textUrl);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            }
        });
    }

    static WeatherFragment newInstance() {
        Bundle args = new Bundle();
        WeatherFragment fragment = new WeatherFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        updateWeather();
        super.onAttach(context);
    }
}
