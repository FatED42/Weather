package com.example.weather.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.weather.CityPreference;
import com.example.weather.EventBus;
import com.example.weather.R;
import com.example.weather.event.ChangeThemeBtnClickedEvent;

public class SettingsFragment extends Fragment {

    private CheckedTextView darkThemeCheckBox;
    private RadioGroup degreeRadGrp, pressureRadGrp;
    private RadioButton radBtnCelsius, radBtnFahrenheit, radBtnHPa, radBtnMmHg;
    private CityPreference cityPreference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        cityPreference = new CityPreference(requireActivity());
        darkThemeCheckBoxListener();
        degreeRadBtnListener();
        pressureRadBtnListener();
    }

    private void pressureRadBtnListener() {
        if (cityPreference.getPressure()==0) radBtnMmHg.setChecked(true);
        else radBtnHPa.setChecked(true);
        pressureRadGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.radBtnMmHg) {
                    cityPreference.setPressure(0);
                } else {
                    cityPreference.setPressure(1);
                }
            }
        });
    }

    private void degreeRadBtnListener() {
        if (cityPreference.getUnits().equals("metric")) radBtnCelsius.setChecked(true);
        else radBtnFahrenheit.setChecked(true);
        degreeRadGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.radBtnCelsius) {
                    cityPreference.setUnits("metric");
                } else {
                    cityPreference.setUnits("imperial");
                }
            }
        });
    }


    private void darkThemeCheckBoxListener() {
        darkThemeCheckBox.setChecked(cityPreference.getThemeCheckBox());
        darkThemeCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (darkThemeCheckBox.isChecked()) {
                    darkThemeCheckBox.setChecked(false);
                } else darkThemeCheckBox.setChecked(true);
                cityPreference.setThemeCheckBox(darkThemeCheckBox.isChecked());
                EventBus.getBus().post(new ChangeThemeBtnClickedEvent(darkThemeCheckBox.isChecked()));
            }
        });
    }

    private void initViews(View view) {
        darkThemeCheckBox = view.findViewById(R.id.Dark_theme_checkbox);
        radBtnCelsius = view.findViewById(R.id.radBtnCelsius);
        radBtnFahrenheit = view.findViewById(R.id.radBtnFahrenheit);
        radBtnHPa = view.findViewById(R.id.radBtnHPa);
        radBtnMmHg = view.findViewById(R.id.radBtnMmHg);
        degreeRadGrp = view.findViewById(R.id.radGrpDegree);
        pressureRadGrp = view.findViewById(R.id.radGrpPressure);
    }
}
