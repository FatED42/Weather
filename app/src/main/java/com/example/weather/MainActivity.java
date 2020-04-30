package com.example.weather;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    final static String KEY_TO_DATA = "KEY_TO_DATA";
    private final static String CITY_STATE = "cityState";
    private static final String WIND_STATE = "wind state";
    private static final String PRESSURE_STATE = "pressure state";
    private CheckBox wind_checkBox;
    private CheckBox pressure_checkBox;
    Spinner cities_spinner;
    Button show_weather_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cities_choise);

        initViews();

    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        cities_spinner.setSelection(savedInstanceState.getInt(CITY_STATE));
        wind_checkBox.setChecked(savedInstanceState.getBoolean(WIND_STATE));
        pressure_checkBox.setChecked(savedInstanceState.getBoolean(PRESSURE_STATE));
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CITY_STATE,cities_spinner.getSelectedItemPosition());
        outState.putBoolean(WIND_STATE, wind_checkBox.isChecked());
        outState.putBoolean(PRESSURE_STATE, pressure_checkBox.isChecked());
    }

    private void initViews() {
        cities_spinner = findViewById(R.id.cities_spinner);
        wind_checkBox = findViewById(R.id.windSpeed_CB);
        pressure_checkBox = findViewById(R.id.pressure_CB);
        show_weather_btn = findViewById(R.id.showWeather_btn);

        ArrayAdapter<CharSequence> citiesAdapter = ArrayAdapter.createFromResource(
                this,R.array.cities, R.layout.spinner_layout);
        citiesAdapter.setDropDownViewResource(R.layout.spinner_layout);
        cities_spinner.setAdapter(citiesAdapter);

        show_weather_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,WeatherActivity.class);
                ActivityInfo activityInfo =intent.resolveActivityInfo(getPackageManager(),
                        intent.getFlags());
                if (activityInfo != null) {
                    Parcel parcel = new Parcel();
                    parcel.city = cities_spinner.getSelectedItem().toString();
                    parcel.windSpeed = wind_checkBox.isChecked();
                    parcel.pressure = pressure_checkBox.isChecked();
                    intent.putExtra(KEY_TO_DATA,parcel);
                    startActivity(intent);
                }
            }
        });
    }
}