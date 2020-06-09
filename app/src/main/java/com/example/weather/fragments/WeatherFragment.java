package com.example.weather.fragments;

import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.weather.CityCard;
import com.example.weather.R;
import com.example.weather.WeatherDataLoader;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class WeatherFragment extends Fragment {

    //TextView
    @BindView(R.id.temperatureValue)
    TextView tempTodayTextView;
    @BindView(R.id.temperatureFeelsLikeValue)
    TextView tempFeelsLikeTextView;
    @BindView(R.id.tempValueMax)
    TextView tempMaxTextView;
    @BindView(R.id.tempValueMin)
    TextView tempMinTextView;
    @BindView(R.id.humidityValue)
    TextView humidityTextView;
    @BindView(R.id.pressureValue)
    TextView pressureTextView;
    @BindView(R.id.windValue)
    TextView windTextView;
    @BindView(R.id.overcastValue)
    TextView overcastTextView;
    @BindView(R.id.cityTextView)
    TextView cityTextView;
    @BindView(R.id.todayDate)
    TextView todayDateTextView;
    @BindView(R.id.weatherIcon)
    TextView weatherIconTextView;
    //ImageButton
    @BindView(R.id.updateBtn)
    ImageButton updateBtn;
    @BindView(R.id.back_arrow)
    ImageButton backArrowBtn;
    @BindView(R.id.backgroundImageTempScreen)
    ImageView backgroundImage;
    private int humidityValue, pressureValue, windValue, pressure;
    private double tempTodayValue, tempFeelsLikeValue, tempMaxValue, tempMinValue;
    private String cityName, overcastValue, country, updateOn, icon, units;
    private final String HUMIDITY_VALUE_KEY = "HUMIDITY_VALUE_KEY",
            OVERCAST_VALUE_KEY = "OVERCAST_VALUE_KEY", TEMP_TODAY_KEY = "TEMP_TODAY_KEY",
            CITY_NAME_KEY = "CITY_NAME_KEY", COUNTRY_KEY = "COUNTRY_KEY",
            TEMP_FEELS_LIKE_KEY = "TEMP_FEELS_LIKE_KEY", TEMP_MAX_KEY = "TEMP_MAX_KEY",
            TEMP_MIN_KEY = "TEMP_MIN_KEY", PRESSURE_KEY = "PRESSURE_KEY",
            WIND_KEY = "WIND_KEY", ICON_KEY = "ICON_KEY";

    private Unbinder unbinder;
    private CityCard cityCard;



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            //Name
            cityName = savedInstanceState.getString(CITY_NAME_KEY);
            country = savedInstanceState.getString(COUNTRY_KEY);
            //Temp
            tempTodayValue = savedInstanceState.getDouble(TEMP_TODAY_KEY);
            tempFeelsLikeValue = savedInstanceState.getDouble(TEMP_FEELS_LIKE_KEY);
            tempMaxValue = savedInstanceState.getDouble(TEMP_MAX_KEY);
            tempMinValue = savedInstanceState.getDouble(TEMP_MIN_KEY);
            //Descriptions
            humidityValue = savedInstanceState.getInt(HUMIDITY_VALUE_KEY);
            overcastValue = savedInstanceState.getString(OVERCAST_VALUE_KEY);
            pressureValue = savedInstanceState.getInt(PRESSURE_KEY);
            windValue = savedInstanceState.getInt(WIND_KEY);
            icon = savedInstanceState.getString(ICON_KEY);

            setValues();
        }
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initFont();
        showBackButton();
        onBackArrowBtnClicked();
        onUpdateBtnClicked();
        updateWeatherOnScreen();
        setValues();
        setBackgroundImage();
    }

    private void initFont() {
        Typeface weatherFont = Typeface.createFromAsset(requireActivity().getAssets(),
                "fonts/weathericons.ttf");
        weatherIconTextView.setTypeface(weatherFont);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        savedInstanceState.putString(CITY_NAME_KEY, cityName);
        savedInstanceState.putString(COUNTRY_KEY, country);
        savedInstanceState.putDouble(TEMP_TODAY_KEY, tempTodayValue);
        savedInstanceState.putDouble(TEMP_FEELS_LIKE_KEY, tempFeelsLikeValue);
        savedInstanceState.putDouble(TEMP_MAX_KEY, tempMaxValue);
        savedInstanceState.putDouble(TEMP_MIN_KEY, tempMinValue);
        savedInstanceState.putInt(HUMIDITY_VALUE_KEY, humidityValue);
        savedInstanceState.putInt(WIND_KEY, windValue);
        savedInstanceState.putInt(PRESSURE_KEY, pressureValue);
        savedInstanceState.putString(OVERCAST_VALUE_KEY, overcastValue);
        savedInstanceState.putString(ICON_KEY, icon);
        super.onSaveInstanceState(savedInstanceState);
    }

    private void setValues() {
        String degree = chooseDegree();
        //city + country
        String cityText = cityName + ", " + country;
        cityTextView.setText(cityText);
        //date of update
        String updatedText = getString(R.string.last_update) + " " + updateOn;
        todayDateTextView.setText(updatedText);
        //temp today
        String tempTodayString = String.valueOf(Math.round(tempTodayValue)).concat(degree);
        tempTodayTextView.setText(tempTodayString);
        //temp feels like
        String tempFeelsLikeString = getString(R.string.feels_like) + " " + Math.round(tempFeelsLikeValue) + degree;
        tempFeelsLikeTextView.setText(tempFeelsLikeString);
        // temp min
        String tempMinString = "Min: " + String.valueOf(tempMinValue).concat(degree);
        tempMinTextView.setText(tempMinString);
        //temp max
        String tempMaxString = "Max: " + String.valueOf(tempMaxValue).concat(degree);
        tempMaxTextView.setText(tempMaxString);
        //Descriptions
        //overcast
        overcastTextView.setText(overcastValue);
        //humidity
        humidityTextView.setText(String.valueOf(humidityValue).concat("%"));
        //pressure
        pressureTextView.setText(choosePressure());
        //wind
        windTextView.setText(String.valueOf(windValue).concat(getString(R.string.ms)));
        //icon
        weatherIconTextView.setText(icon);
    }

    private void updateWeatherOnScreen(){
        if (getArguments()!= null) {
            units = getArguments().getString("units");
            pressure = getArguments().getInt("pressure");
            cityCard = (CityCard) getArguments().getSerializable("cityCard");
            if (cityCard != null) {
                cityName = cityCard.getCityName();
                country = cityCard.getCountry();
                updateOn = cityCard.getUpdateOn();
                tempTodayValue = cityCard.getTemp();
                tempFeelsLikeValue = cityCard.getFeelsTemp();
                tempMinValue = cityCard.getTempMin();
                tempMaxValue = cityCard.getTempMax();
                overcastValue = cityCard.getDescription();
                humidityValue = cityCard.getHumidity();
                pressureValue = cityCard.getPressure();
                windValue = cityCard.getWind();
                icon = cityCard.getIcon();
            }
        }
    }

    private void setBackgroundImage() {
        String icon = cityCard.getIcon();
        String url;
        if (icon != null) {
            if (icon.equals(getString(R.string.weather_sunny))) {
                url = "https://images.unsplash.com/photo-1576433438817-bdf226ef9f87?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1834&q=80";
            } else if (icon.equals(getString(R.string.weather_cloudy))) {
                url = "https://images.unsplash.com/photo-1507291877270-e37ad1ce08a4?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1350&q=80";
            } else if (icon.equals(getString(R.string.weather_foggy))) {
                url = "https://images.unsplash.com/photo-1575195372639-373ecc8590f9?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1350&q=80";
            } else if (icon.equals(getString(R.string.weather_snowy))) {
                url = "https://images.unsplash.com/photo-1517504734587-2890819debab?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=528&q=80";
            } else if (icon.equals(getString(R.string.weather_rainy))) {
                url = "https://images.unsplash.com/photo-1509635022432-0220ac12960b?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=634&q=80";
            } else if (icon.equals(getString(R.string.weather_drizzle))) {
                url = "https://images.unsplash.com/photo-1504350987704-6f027af335c5?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=634&q=80";
            } else if (icon.equals(getString(R.string.weather_thunder))) {
                url = "https://images.unsplash.com/photo-1495917171981-79ce0e0456de?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1002&q=80";
            } else {
                url = "https://images.unsplash.com/photo-1564572681888-6d02eed77008?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1489&q=80";
            }
            Glide.with(requireContext())
                    .load(url)
                    .centerCrop()
                    .into(backgroundImage);
        }
    }

    static WeatherFragment newInstance(CityCard cityCard, String units, int pressure) {
        Bundle args = new Bundle();
        WeatherFragment fragment = new WeatherFragment();
        args.putString("index", cityCard.getCityName());
        args.putString("units", units);
        args.putInt("pressure", pressure);
        args.putSerializable("cityCard", cityCard);
        fragment.setArguments(args);
        return fragment;
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
        backArrowBtn.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().finish();
            }
        });
    }

    private void onUpdateBtnClicked() {
        updateBtn.setOnClickListener(v -> {
            Toast.makeText(getContext(), getString(R.string.updating), Toast.LENGTH_SHORT).show();
            WeatherDataLoader.getCurrentData2(cityCard, requireContext(), units);
            setValues();
        });
    }

    String getCityName() {
        cityName = requireArguments().getString("index");
        try {
            return cityName;
        } catch (Exception e) {
            return "";
        }
    }

    private String chooseDegree() {
        if (units.equals("metric")) {
            return getString(R.string.celsius);
        } else {
            return getString(R.string.fahrenheit);
        }
    }

    private String choosePressure() {
        if (pressure == 0) {
            return String.valueOf(Math.round(pressureValue / 1.33322387415))
                    .concat(getString(R.string.mmHg));
        } else {
            return pressureValue + getString(R.string.hPa);
        }
    }
}
