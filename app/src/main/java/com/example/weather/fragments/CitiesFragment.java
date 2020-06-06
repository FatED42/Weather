package com.example.weather.fragments;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weather.CityCard;
import com.example.weather.CityPreference;
import com.example.weather.R;
import com.example.weather.WeatherDataLoader;
import com.example.weather.activity.WeatherActivity;
import com.example.weather.adapters.CitiesListRecyclerViewAdapter;
import com.example.weather.callBackInterfaces.IAdapterCallbacks;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class CitiesFragment extends Fragment implements IAdapterCallbacks {

    private RecyclerView recyclerView;
    private CitiesListRecyclerViewAdapter adapter;
    private boolean isTempScreenExists;
    private String cityName;
    private ArrayList<CityCard> list;
    private CityPreference cityPreference;
    private final Handler handler = new Handler();
    private static final int REQUEST_CODE = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_cities, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        cityPreference = new CityPreference(requireActivity());
        initList();
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
        outState.putString("city", cityName);
        if (cityPreference != null) {
            cityPreference.setList(list);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        if (cityPreference != null) {
            cityPreference.setList(list);
        }
        super.onDestroy();
    }

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
    }

    private void initList() {
        list =cityPreference.getList();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        adapter = new CitiesListRecyclerViewAdapter(list, this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }


    private void showWeather(String cityName) {
        String units = cityPreference.getUnits();
        int pressure = cityPreference.getPressure();
        if (isTempScreenExists) {
            WeatherFragment detail = (WeatherFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.temp_screen);
            if (detail == null || !detail.getCityName().equals(cityName)) {
                detail = WeatherFragment.newInstance(cityName, units, pressure);
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
            intent.putExtra("units", units);
            intent.putExtra("pressure", pressure);
            startActivity(intent);
        }
    }

    @Override
    public void startWeatherFragment(String cityName) {
        cityPreference.setCity(cityName);
        showWeather(cityName);
    }

    private void addCityToList(String city) {
        city = city.substring(0, 1).toUpperCase() + city.substring(1);
        if(!city.isEmpty() && !adapter.checkIsItemInData(city)) {
            CityCard cityCard = new CityCard(city);
            adapter.addItem(cityCard);
            if (cityPreference != null) {
                cityPreference.setList(list);
            }
            recyclerView.scrollToPosition(0);
            Snackbar.make(recyclerView, R.string.city_added, Snackbar.LENGTH_LONG)
                    .setAction(R.string.cancel, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            deleteCityFromList();
                        }
                    }).show();
        }
    }

    private void deleteCityFromList() {
        adapter.removeItem();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.main_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.add_city_btn) {
            DialogBuilderFragment dialFrag = new DialogBuilderFragment();
            dialFrag.setTargetFragment(this, REQUEST_CODE);
            dialFrag.show(getParentFragmentManager(), "dialog");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateWeatherData(final String city, final String units) {
        if (!city.isEmpty()) {
            new Thread() {
                public void run() {
                    final JSONObject json = WeatherDataLoader.getJSONData(requireContext(),
                            city, units, Locale.getDefault().getCountry());
                    if (json == null) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(), getString(R.string.city_not_found),
                                        Toast.LENGTH_LONG).show();
                            }
                        });
                    } else handler.post(new Runnable() {
                        @Override
                        public void run() {
                            addCityToList(city) ;
                        }
                    });
                }
            }.start();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                assert data != null;
                String city = data.getStringExtra(DialogBuilderFragment.CITY_ADDED);
                updateWeatherData(city, "metric");
            }
        }
    }
}