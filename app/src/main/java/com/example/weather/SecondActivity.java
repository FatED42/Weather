package com.example.weather;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

    }

    private void initViews() {
        Spinner cities_spinner = findViewById(R.id.cities_spinner);

        ArrayAdapter<CharSequence> citiesAdapter = ArrayAdapter.createFromResource(
                this,R.array.cities, R.layout.spinner_layout);
        citiesAdapter.setDropDownViewResource(R.layout.spinner_layout);
        cities_spinner.setAdapter(citiesAdapter);
    }
}
