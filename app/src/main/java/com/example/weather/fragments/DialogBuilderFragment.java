package com.example.weather.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.TypedValue;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.weather.App;
import com.example.weather.CitiesHistorySource;
import com.example.weather.CityCard;
import com.example.weather.R;
import com.example.weather.dao.ICitiesDao;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;
import java.util.Objects;


public class DialogBuilderFragment extends DialogFragment {

    public static final String CITY_ADDED = "city added";
    private CitiesHistorySource citiesHistorySource;
    List<CityCard> cityCardsFromSQL;

   //public DialogBuilderFragment(CitiesHistorySource citiesHistorySource) {
      //  this.citiesHistorySource = citiesHistorySource;
    //}

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final android.app.AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.add_city);
        final TextInputLayout layout = new TextInputLayout(requireActivity());
        final TextInputEditText editText = new TextInputEditText(requireActivity());
        final ChipGroup chipGroup = new ChipGroup(requireContext());
        initSQLServices();
        cityCardsFromSQL = citiesHistorySource.getCityCardList();
        if (cityCardsFromSQL != null) {
            for (CityCard c : cityCardsFromSQL) {
                final Chip chip = new Chip(requireContext());
                String text = c.cityName;
                chip.setText(text);
                chipGroup.addView(chip);
                chip.setOnClickListener(v -> {
                    editText.setText(c.cityName);
                });
            }
        }
        TypedValue outValue = new TypedValue();
        requireActivity().getTheme().resolveAttribute(R.attr.themeName, outValue, true);
        if ("dark".contentEquals(outValue.string))
            editText.setTextColor(getResources().getColor(R.color.colorBackground));

        layout.addView(editText);
        layout.addView(chipGroup);
        layout.setHint(getString(R.string.enter_city_name));
        layout.setPadding(64, 32, 64, 32);
        editText.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(layout);
        builder.setPositiveButton("OK", (dialogInterface, i) -> {
            dismiss();
            String city = Objects.requireNonNull(editText.getText()).toString();
            Intent intent = new Intent();
            intent.putExtra(CITY_ADDED, city);
            Objects.requireNonNull(getTargetFragment()).onActivityResult(getTargetRequestCode(),
                    Activity.RESULT_OK, intent);
        });

        builder.setNegativeButton(R.string.cancel, (dialogInterface, i) -> dismiss());
        return builder.create();
    }

    private void initSQLServices() {
        ICitiesDao citiesDao = App.getInstance().getCitiesDao();
        citiesHistorySource = new CitiesHistorySource(citiesDao);
        cityCardsFromSQL = citiesHistorySource.getCityCardList();
    }
}
