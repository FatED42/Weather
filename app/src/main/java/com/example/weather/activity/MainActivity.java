package com.example.weather.activity;


import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import com.example.weather.R;
import com.example.weather.callBackInterfaces.IAddCityCallback;
import com.example.weather.fragments.CitiesFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class MainActivity extends BaseActivity {

    IAddCityCallback addCityCallback;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setFloatingBtn();
    }

    private void setFloatingBtn() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInputDialog(v);
            }
        });
    }

    private void showInputDialog(final View view) {
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
                if (Objects.requireNonNull(addCityCallback).addCityToList(city)) {
                    Snackbar.make(view, R.string.city_added, Snackbar.LENGTH_LONG)
                            .setAction(R.string.cancel, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    addCityCallback.deleteCityFromList();
                                }
                            }).show();
                }
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
        return super.onOptionsItemSelected(item);
    }
}