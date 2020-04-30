package com.example.weather;


import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    CitiesFragment citiesFragment;
    WeatherFragment weatherFragment;
    FragmentManager fragmentManager;

    public static final String WEATHER_FRAGMENT_KEY = "WEATHER_FRAGMENT",
                                CITIES_FRAGMENT_KEY = "CITIES_FRAGMENT";

    private String cityName = "Zelenograd";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);

        setToolbar();

        if (savedInstanceState == null) {
            addWeatherFragment();
            citiesFragment = CitiesFragment.newInstance();
        }
        else {
            weatherFragment = (WeatherFragment) getSupportFragmentManager()
                    .findFragmentByTag(WEATHER_FRAGMENT_KEY);
            citiesFragment = (CitiesFragment) getSupportFragmentManager()
                    .findFragmentByTag(CITIES_FRAGMENT_KEY);
        }
    }

    private void addWeatherFragment() {
        weatherFragment = WeatherFragment.newInstance();
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.add(R.id.fragment_container, weatherFragment, WEATHER_FRAGMENT_KEY).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void setToolbar() {
        this.setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!citiesFragment.isVisible()) {
                    replaceFragment(citiesFragment, CITIES_FRAGMENT_KEY);
                }
                else {
                    replaceFragment(weatherFragment, WEATHER_FRAGMENT_KEY);
                }
            }
        });

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.menu_main_update) {
                    Toast.makeText(MainActivity.this, getString(R.string.updating),
                            Toast.LENGTH_SHORT).show();
                    weatherFragment.updateWeather();
                }
                else if (item.getItemId() == R.id.menu_main_settings) {
                    Toast.makeText(MainActivity.this, getString(R.string.settings_screen_waiting),
                            Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
    }

    private void replaceFragment(Fragment fragment, String KEY) {
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.fragment_container, fragment, KEY).commit();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        savedInstanceState.putString("key1", cityName);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        cityName = savedInstanceState.getString("key1");
        super.onRestoreInstanceState(savedInstanceState);
    }
}