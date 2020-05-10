package com.example.weather;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class WeatherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp_screen);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            finish();
        }

        if (savedInstanceState == null) {
            WeatherFragment details = new WeatherFragment();
            details.setArguments(getIntent().getExtras());
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, details)
                    .commit();
        }
    }
}
