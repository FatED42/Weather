package com.example.weather;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.strictmode.WebViewMethodCalledOnWrongThreadViolation;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class WeatherFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView tempTodayTextView, humidityTextView, overcastTextView,
                    cityTextView, todayDateTextView;
    private ImageButton wikiInfoBtn, backArrowBtn;
    private int humidityValue;
    private double tempTodayValue;
    private String cityName, overcastValue, yesterdayDateString,
            twoDaysAgoDateString, threeDaysAgoDateString;
    private final String HUMIDITY_VALUE_KEY = "HUMIDITY_VALUE_KEY",
            OVERCAST_VALUE_KEY = "OVERCAST_VALUE_KEY", TEMP_TODAY_KEY = "TEMP_TODAY_KEY",
            CITY_NAME_KEY = "CITY_NAME_KEY";

    private final Handler handler = new Handler();
    private static final String LOG_TAG = "WeatherFragment";
    private NestedScrollView nestedScrollView;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            humidityValue = savedInstanceState.getInt(HUMIDITY_VALUE_KEY);
            overcastValue = savedInstanceState.getString(OVERCAST_VALUE_KEY);
            tempTodayValue = savedInstanceState.getDouble(TEMP_TODAY_KEY);
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
        setDates();
        initRecyclerView();
        onWikiInfoBtnClicked();
        showBackButton();
        onBackArrowBtnClicked();
        setHasOptionsMenu(true);

        if (nestedScrollView.getParent() != null) {
            nestedScrollView.getParent().requestChildFocus(nestedScrollView,nestedScrollView);
        }
    }

    private void initViews(View view) {
        tempTodayTextView = view.findViewById(R.id.temperatureValue);
        humidityTextView = view.findViewById(R.id.humidityValue);
        overcastTextView = view.findViewById(R.id.overcastValue);
        cityTextView = view.findViewById(R.id.cityTextView);
        wikiInfoBtn = view.findViewById(R.id.toWikiBtn);
        todayDateTextView = view.findViewById(R.id.todayDate);
        recyclerView = view.findViewById(R.id.recycleViewTempHistory);
        nestedScrollView = view.findViewById(R.id.ScrollView);
        backArrowBtn = view.findViewById(R.id.back_arrow);
    }

    private void setDates() {
        DateFormat dateFormat = new SimpleDateFormat("dd-MMMM-yyyy", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        Date todayDate = calendar.getTime();
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        Date yesterdayDate = calendar.getTime();
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        Date twoDaysAgoDate = calendar.getTime();
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        Date threeDaysAgoDate = calendar.getTime();
        calendar.add(Calendar.DAY_OF_YEAR, -1);

        yesterdayDateString = dateFormat.format(yesterdayDate);
        twoDaysAgoDateString = dateFormat.format(twoDaysAgoDate);
        threeDaysAgoDateString = dateFormat.format(threeDaysAgoDate);

        String todayDateString = dateFormat.format(todayDate);
        todayDateTextView.setText(todayDateString);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        savedInstanceState.putInt(HUMIDITY_VALUE_KEY, humidityValue);
        savedInstanceState.putString(OVERCAST_VALUE_KEY, overcastValue);
        savedInstanceState.putDouble(TEMP_TODAY_KEY, tempTodayValue);
        savedInstanceState.putString(CITY_NAME_KEY, cityName);
        super.onSaveInstanceState(savedInstanceState);
    }

    private void setValues() {
        cityTextView.setText(cityName);
        tempTodayTextView.setText(String.valueOf(tempTodayValue));
        overcastTextView.setText(overcastValue);
        humidityTextView.setText(String.valueOf(humidityValue));
    }

    private void updateWeather() {
        if (getArguments() != null) {
            updateWeatherData(getArguments().getString("index"));
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

    static WeatherFragment newInstance(String cityName) {
        Bundle args = new Bundle();
        WeatherFragment fragment = new WeatherFragment();
        args.putString("index", cityName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        updateWeather();
        super.onAttach(context);
    }

    String getCityName() {
        assert getArguments() != null;
        cityName = Objects.requireNonNull(getArguments().getString("index"));
        try {
            return cityName;
        } catch (Exception e) {
            return "";
        }
    }

    private void initRecyclerView() {
        TempHistoryCard[] data = new TempHistoryCard[] {
                new TempHistoryCard(yesterdayDateString, 0),
                new TempHistoryCard(twoDaysAgoDateString, 1),
                new TempHistoryCard(threeDaysAgoDateString, 2),
        };
        ArrayList<TempHistoryCard> list = new ArrayList<>(data.length);
        list.addAll(Arrays.asList(data));

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        TempHistoryRecyclerViewAdapter adapter = new TempHistoryRecyclerViewAdapter(list);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(adapter);
    }

    private void showBackButton() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            backArrowBtn.setVisibility(View.INVISIBLE);
        }
        else {
            backArrowBtn.setVisibility(View.VISIBLE);
        }
    }

    private void onBackArrowBtnClicked() {
        backArrowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                    getActivity().finish();
                }
            }
        });
    }
}
