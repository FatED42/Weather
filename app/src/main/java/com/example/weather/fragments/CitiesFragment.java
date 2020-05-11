package com.example.weather.fragments;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weather.CityCard;
import com.example.weather.CityPreference;
import com.example.weather.R;
import com.example.weather.activity.WeatherActivity;
import com.example.weather.adapters.CitiesListRecyclerViewAdapter;
import com.example.weather.callBackInterfaces.IAdapterCallbacks;
import com.example.weather.callBackInterfaces.IAddCityCallback;

import java.util.ArrayList;
import java.util.Arrays;

public class CitiesFragment extends Fragment implements IAdapterCallbacks, IAddCityCallback {

    private RecyclerView recyclerView;
    private CitiesListRecyclerViewAdapter adapter;
    private boolean isTempScreenExists;
    private String cityName;
    private ArrayList<CityCard> list;
    private CityPreference cityPreference;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cities, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        initList(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isTempScreenExists = getResources().getConfiguration()
                .orientation == Configuration.ORIENTATION_LANDSCAPE;
        cityPreference = new CityPreference(requireActivity());
        cityName = cityPreference.getCity();
        if (isTempScreenExists) {
            showWeather(cityName);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString("City", cityName);
        outState.putParcelableArrayList("list", list);
        super.onSaveInstanceState(outState);
    }

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
    }

    private void initList(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            CityCard[] data = new CityCard[] {
                    new CityCard(getString(R.string.moscow)),
                    new CityCard(getString(R.string.spb)),
                    new CityCard(getString(R.string.zelenograd)),
                    new CityCard(getString(R.string.roslavl))
            };
            list = new ArrayList<>(data.length);
            list.addAll(Arrays.asList(data));
        }
        else {
            list = savedInstanceState.getParcelableArrayList("list");
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        adapter = new CitiesListRecyclerViewAdapter(list, this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }


    private void showWeather(String cityName) {
        if (isTempScreenExists) {
            WeatherFragment detail = (WeatherFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.temp_screen);
            assert detail != null;
            if (!detail.getCityName().equals(cityName)) {
                detail = WeatherFragment.newInstance(cityName);
                FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.temp_screen, detail);
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                fragmentTransaction.addToBackStack("Some_key");
                fragmentTransaction.commit();
            }
        }
        else {
            Intent intent = new Intent();
            intent.setClass(requireActivity(), WeatherActivity.class);
            intent.putExtra("index", cityName);
            startActivity(intent);
        }
    }

    @Override
    public void startWeatherFragment(String cityName) {
        cityPreference.setCity(cityName);
        showWeather(cityName);
    }

    @Override
    public boolean addCityToList(String city) {
        if (!city.isEmpty() && !adapter.checkIsItemInData(city)) {
            city = city.substring(0, 1).toUpperCase() + city.substring(1);
            CityCard cityCard = new CityCard(city);
            adapter.addItem(cityCard);
            recyclerView.scrollToPosition(0);
            return true;
        }
        return false;
    }

    @Override
    public void deleteCityFromList() {
        adapter.removeItem();
    }
}