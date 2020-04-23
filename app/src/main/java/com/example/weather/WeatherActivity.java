package com.example.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.Serializable;
import java.util.Objects;

public class WeatherActivity extends AppCompatActivity {

    private TextView windSpeed_textView;
    private TextView pressure_textView;
    private TextView city_textView;

    private static final String URL = "https://randstuff.ru/fact/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setValue();
    }

    private void initViews() {
        Button random_btn = findViewById(R.id.random_btn);
        city_textView = findViewById(R.id.cityName);
        windSpeed_textView = findViewById(R.id.speed_textView);
        pressure_textView = findViewById(R.id.pressure_textView);

        random_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(URL);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }

    public void setValue() {
        Serializable serializable = getIntent().getSerializableExtra(MainActivity.KEY_TO_DATA);
        Parcel parcel = (Parcel) serializable;
        String city = Objects.requireNonNull(parcel).city;
        city_textView.setText(city);
        windSpeed_textView.setVisibility(visibleView(Objects.requireNonNull(parcel).windSpeed));
        pressure_textView.setVisibility(visibleView(Objects.requireNonNull(parcel).pressure));
    }

    private int visibleView(Boolean visible) {
        return visible ? View.VISIBLE : View.INVISIBLE;
    }
}