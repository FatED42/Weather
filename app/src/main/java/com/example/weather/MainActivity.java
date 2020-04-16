package com.example.weather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String WIND_STATE = "wind state";
    private static final String PRESSURE_STATE = "pressure state";
    private static final String LIFECYCLE = "LIFE_CYCLE";

    private CheckBox wind_checkBox;
    private CheckBox pressure_checkBox;
    private TextView windSpeed_textView;
    private TextView pressure_textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String instanceState;
        if (savedInstanceState == null) {
            instanceState = "Первый запуск";
        }
        else {
            instanceState = "Повторный запуск";
        }
        makeToast(instanceState + " - onCreate()");

        initViews();

    }

    @Override
    protected void onStart() {
        super.onStart();
        makeToast("onStart()");
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        makeToast("Повторный запуск onRestoreInstanceState");

        wind_checkBox.setChecked(savedInstanceState.getBoolean(WIND_STATE));
        pressure_checkBox.setChecked(savedInstanceState.getBoolean(PRESSURE_STATE));
    }

    @Override
    protected void onResume() {
        super.onResume();
        makeToast("onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        makeToast("onPause()");
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        makeToast("onSaveInstanceState()");

        outState.putBoolean(WIND_STATE, wind_checkBox.isChecked());
        outState.putBoolean(PRESSURE_STATE, pressure_checkBox.isChecked());
    }

    @Override
    protected void onStop() {
        super.onStop();
        makeToast("onStop()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        makeToast("onRestart()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        makeToast("onDestroy()");
    }

    private void initViews() {
        wind_checkBox = findViewById(R.id.windSpeed_CB);
        pressure_checkBox = findViewById(R.id.pressure_CB);
        windSpeed_textView = findViewById(R.id.windSpeed_textView);
        pressure_textView = findViewById(R.id.pressure_textView);

        wind_checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (wind_checkBox.isChecked()) {
                    windSpeed_textView.setVisibility(View.VISIBLE);}
                else {
                    windSpeed_textView.setVisibility(View.INVISIBLE);
                }
            }
        });

        pressure_checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (pressure_checkBox.isChecked()) {
                    pressure_textView.setVisibility(View.VISIBLE);}
                else {
                    pressure_textView.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void makeToast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        Log.d(LIFECYCLE, message);
    }
}
