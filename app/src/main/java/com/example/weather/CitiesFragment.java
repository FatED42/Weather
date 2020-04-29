package com.example.weather;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Objects;

public class CitiesFragment extends Fragment {

    private ListView listView;
    private TextView emptyTextView;

    private int currentPosition;
    private CitiesContainer citiesContainer = new CitiesContainer();

    private WeatherFragment weatherFragment;
    final static String CITY_KEY = "CITY_KEY";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cities, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        initList();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            currentPosition = savedInstanceState.getInt("CurrentCity", 0);
        }
        sendCityNameToWeatherFragment(currentPosition);
        listView.setItemChecked(currentPosition, true);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt("CurrentCity", currentPosition);
        super.onSaveInstanceState(outState);
    }

    private void initViews(View view) {
        listView = view.findViewById(R.id.cities_list_view);
        emptyTextView = view.findViewById(R.id.cities_list_empty_view);
    }

    private void initList() {
        ArrayAdapter adapter =
                ArrayAdapter.createFromResource(Objects.requireNonNull(getActivity()), R.array.cities,
                        android.R.layout.simple_list_item_activated_1);
        listView.setAdapter(adapter);
        listView.setEmptyView(emptyTextView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentPosition = position;
                sendCityNameToWeatherFragment(currentPosition);
                listView.setItemChecked(currentPosition, true);
                weatherFragment.updateWeather();
                showWeather();
            }
        });
    }

    private void sendCityNameToWeatherFragment(int currentPosition) {
        String[] cities = getResources().getStringArray(R.array.cities);
        citiesContainer.cityName = cities[currentPosition];
        citiesContainer.position = currentPosition;
        Bundle args = new Bundle();
        args.putSerializable(CITY_KEY, citiesContainer);
        weatherFragment = (WeatherFragment) Objects.requireNonNull(getFragmentManager())
                .findFragmentByTag(MainActivity.WEATHER_FRAGMENT_KEY);
        if (weatherFragment != null) {
            weatherFragment.setArguments(args);
        }
    }

    private void showWeather() {
        if (getFragmentManager() != null) {
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.replace(R.id.fragment_container, weatherFragment).commit();
        }
    }

    static CitiesFragment newInstance() {
        Bundle args = new Bundle();
        CitiesFragment fragment = new CitiesFragment();
        fragment.setArguments(args);
        return fragment;
    }
}