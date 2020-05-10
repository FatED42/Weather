package com.example.weather;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class CitiesFragment extends Fragment implements IAdapterCallbacks{

    private RecyclerView recyclerView;
    private Button addItemBtn, removeItemBtn;
    private CitiesListRecyclerViewAdapter adapter;
    private EditText enterCityEditText;
    private boolean isTempScreenExists;
    private String cityName = "Moscow";
    private ArrayList<CityCard> list;


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
        AddButtonListener();
        RemoveButtonListener();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isTempScreenExists = getResources().getConfiguration()
                .orientation == Configuration.ORIENTATION_LANDSCAPE;
        if (savedInstanceState != null) {
            cityName = savedInstanceState.getString("City", "Moscow");
        }
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
        addItemBtn = view.findViewById(R.id.addBtn);
        removeItemBtn = view.findViewById(R.id.removeBtn);
        enterCityEditText = view.findViewById(R.id.add_city_to_recycler);
    }

    private void initList(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            CityCard[] data = new CityCard[] {
                    new CityCard(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.moscow), getString(R.string.moscow)),
                    new CityCard(ContextCompat.getDrawable(getContext(), R.drawable.spb), getString(R.string.spb)),
                    new CityCard(ContextCompat.getDrawable(getContext(), R.drawable.zelenograd), getString(R.string.zelenograd)),
                    new CityCard(ContextCompat.getDrawable(getContext(), R.drawable.roslavl), getString(R.string.roslavl))
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
            assert getFragmentManager() != null;
            WeatherFragment detail = (WeatherFragment) Objects.requireNonNull(getFragmentManager().findFragmentById(R.id.temp_screen));
            if (!detail.getCityName().equals(cityName)) {
                detail = WeatherFragment.newInstance(cityName);
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.temp_screen, detail);
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                fragmentTransaction.addToBackStack("Some_key");
                fragmentTransaction.commit();
            }
        }
        else {
            Intent intent = new Intent();
            intent.setClass(Objects.requireNonNull(getActivity()), WeatherActivity.class);
            intent.putExtra("index", cityName);
            startActivity(intent);
        }
    }

    @Override
    public void startWeatherFragment(String cityName) {
        showWeather(cityName);
    }

    private void AddButtonListener() {
        addItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!enterCityEditText.getText().toString().isEmpty()) {
                    CityCard cityCard = new CityCard(ContextCompat.getDrawable
                            (Objects.requireNonNull(getContext()), R.drawable.city),
                            enterCityEditText.getText().toString());
                    adapter.addItem(cityCard);
                    recyclerView.scrollToPosition(0);
                }
            }
        });
    }

    private void RemoveButtonListener() {
        removeItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.removeItem();
            }
        });
    }
}