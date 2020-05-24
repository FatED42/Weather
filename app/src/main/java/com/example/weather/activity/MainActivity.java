package com.example.weather.activity;


import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import com.example.weather.R;
import com.example.weather.WeatherDataLoader;
import com.example.weather.callBackInterfaces.IAddCityCallback;
import com.example.weather.fragments.CitiesFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONObject;

import java.util.Locale;
import java.util.Objects;

public class MainActivity extends BaseActivity {

    IAddCityCallback addCityCallback;
    private final Handler handler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void showInputDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.choose_city);
        final TextInputLayout layout = new TextInputLayout(this);
        final TextInputEditText editText = new TextInputEditText(this);
        layout.addView(editText);
        layout.setHint(getString(R.string.choose_city));
        layout.setPadding(64, 32, 64, 32);
        editText.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(layout);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                addCityCallback = (CitiesFragment) getSupportFragmentManager().findFragmentById(R.id.cities);
                String city = Objects.requireNonNull(Objects.requireNonNull(editText.getText()).toString());
                updateWeatherData(city, "metric");
            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                closeContextMenu();
            }
        });
        builder.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        menu.findItem(R.id.dark_theme).setChecked(isDarkTheme());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_main_settings) {
            return true;
        }
        if (id == R.id.dark_theme) {
            if (item.isChecked()) {
                item.setChecked(false);
            }
            else {
                item.setChecked(true);
            }
            setDarkTheme(item.isChecked());
            recreate();
            return true;
        }
        if (id == R.id.add_city_btn) {
            showInputDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateWeatherData(final String city, final String units) {
        if (!city.isEmpty()) {
            new Thread() {
                public void run() {
                    final JSONObject json = WeatherDataLoader.getJSONData(Objects.requireNonNull(getApplicationContext()), city, units, Locale.getDefault().getCountry());
                    if (json == null) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), getString(R.string.city_not_found),
                                        Toast.LENGTH_LONG).show();
                            }
                        });
                    } else handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Objects.requireNonNull(addCityCallback).addCityToList(city);
                        }
                    });
                }
            }.start();
        }
    }
}