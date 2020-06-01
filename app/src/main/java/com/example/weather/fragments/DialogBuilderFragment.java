package com.example.weather.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.TypedValue;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.weather.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;


public class DialogBuilderFragment extends DialogFragment {

    public static final String CITY_ADDED = "city added";

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final android.app.AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.add_city);
        final TextInputLayout layout = new TextInputLayout(requireActivity());
        final TextInputEditText editText = new TextInputEditText(requireActivity());
        TypedValue outValue = new TypedValue();
        requireActivity().getTheme().resolveAttribute(R.attr.themeName, outValue, true);
        if ("dark".contentEquals(outValue.string))
            editText.setTextColor(getResources().getColor(R.color.colorBackground));

        layout.addView(editText);
        layout.setHint(getString(R.string.choose_city));
        layout.setPadding(64, 32, 64, 32);
        editText.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(layout);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dismiss();
                String city = Objects.requireNonNull(editText.getText()).toString();
                Intent intent = new Intent();
                intent.putExtra(CITY_ADDED, city);
                Objects.requireNonNull(getTargetFragment()).onActivityResult(getTargetRequestCode(),
                        Activity.RESULT_OK, intent);
            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dismiss();
            }
        });
        return builder.create();
    }
}
