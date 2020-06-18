package com.example.weather.fragments;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.example.weather.App;
import com.example.weather.CitiesHistorySource;
import com.example.weather.CityCard;
import com.example.weather.CityPreference;
import com.example.weather.EventBus;
import com.example.weather.ICardList;
import com.example.weather.R;
import com.example.weather.WeatherDataLoader;
import com.example.weather.activity.WeatherActivity;
import com.example.weather.adapters.CitiesListRecyclerViewAdapter;
import com.example.weather.callBackInterfaces.IAdapterCallbacks;
import com.example.weather.dao.ICitiesDao;
import com.example.weather.event.AddCityToListEvent;
import com.example.weather.event.UpdateRecyclerListAfterParsingData;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.content.Context.MODE_PRIVATE;

public class CitiesFragment extends Fragment implements IAdapterCallbacks, ICardList {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.swipeLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.backgroundGifCitiesList)
    ImageView backgroundGif;

    private CitiesListRecyclerViewAdapter adapter;
    private boolean isTempScreenExists;
    private String cityName, units;
    private ArrayList<CityCard> list;
    private List<CityCard> cityCardsFromSQL;
    private CityPreference cityPreference;
    private static final int REQUEST_CODE = 1;
    private Unbinder unbinder;
    private CitiesHistorySource citiesHistorySource;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_cities, container, false);
        setHasOptionsMenu(true);
        unbinder = ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getBus().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getBus().unregister(this);
        cityPreference.setList(list);
    }

    @Subscribe
    public void onStopParsingDataToCardEvent(UpdateRecyclerListAfterParsingData event) {
        adapter.notifyItemChanged(event.getPosition());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initSQLServices();
        initPreferences();
        initList();
        renderWeather();
        setSwipeListener();
        setUpBackgroundGif();
        if (savedInstanceState == null) startWeatherFragment(cityPreference.getCity());
    }

    private void initSQLServices() {
        ICitiesDao citiesDao = App.getInstance().getCitiesDao();
        citiesHistorySource = new CitiesHistorySource(citiesDao);
        citiesHistorySource.getCityCardList(this);
    }

    private void setUpBackgroundGif() {
        String url;
        SharedPreferences sharedPref = requireContext().getSharedPreferences("LOGIN", MODE_PRIVATE);
        if (!sharedPref.getBoolean("IS_DARK_THEME", false))
            url = "https://media.giphy.com/media/xTiQyka2vyWF5EqPok/giphy.gif";
        else url = "https://media.giphy.com/media/3o7TKxvsYW7uTAChaw/giphy.gif";
        Glide.with(requireContext())
                .load(url)
                .centerCrop()
                .into(backgroundGif);
    }

    private void setSwipeListener() {
        swipeRefreshLayout.setOnRefreshListener(this::renderWeather);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isTempScreenExists = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
        cityName = cityPreference.getCity();
        if (isTempScreenExists) {
            showTempScreen(getCityCardByItsName(cityName));
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString("city", cityName);
        if (cityPreference != null) saveList();
        super.onSaveInstanceState(outState);
    }

    private void initPreferences() {
        cityPreference = new CityPreference(requireActivity());
        list = cityPreference.getList();
        units = cityPreference.getUnits();
    }

    private void initList() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        adapter = new CitiesListRecyclerViewAdapter(list, this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void startWeatherFragment(String city) {
        cityPreference.setCity(city);
        showTempScreen(getCityCardByItsName(city));
    }

    private void showTempScreen(CityCard cityCard) {
        if (cityCard != null) {
            String city = cityCard.getCityName();
            String units = cityPreference.getUnits();
            int pressure = cityPreference.getPressure();
            if (isTempScreenExists) {
                WeatherFragment detail = (WeatherFragment) requireActivity().getSupportFragmentManager()
                        .findFragmentById(R.id.temp_screen);
                if (detail == null || !detail.getCityName().equals(city)) {
                    detail = WeatherFragment.newInstance(cityCard, units, pressure);
                    FragmentTransaction fragmentTransaction = requireActivity()
                            .getSupportFragmentManager()
                            .beginTransaction();
                    fragmentTransaction.replace(R.id.temp_screen, detail);
                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    fragmentTransaction.addToBackStack("Some_key");
                    fragmentTransaction.commit();
                }
            } else {
                Intent intent = new Intent();
                intent.setClass(requireActivity(), WeatherActivity.class);
                intent.putExtra("index", city);
                intent.putExtra("units", units);
                intent.putExtra("pressure", pressure);
                intent.putExtra("cityCard", cityCard);
                startActivity(intent);
            }
        }
    }

    @Override
    public void onAdapterUpdate() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void saveList() {
        cityPreference.setList(list);
    }

    private void deleteCityFromList() {
        adapter.removeItem();
        saveList();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.main_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void renderWeather() {
        for (CityCard c : list) {
            c.setPosition(list.indexOf(c));
            WeatherDataLoader.getCurrentData(c, requireContext(), units);
        }
    }

    @Subscribe
    public void addCityToList(AddCityToListEvent event) {
        CityCard cityCard = event.getCityCard();
        if (cityCard.getCityName() != null && !list.contains(cityCard)) {
            if (!cityCardsFromSQL.contains(cityCard)) citiesHistorySource.addCity(cityCard);
            else {
                for (CityCard c : cityCardsFromSQL) {
                    if (c.equals(cityCard)) {
                        c.numberOfSearches++;
                        citiesHistorySource.updateCity(c);
                    }
                }
            }
            adapter.addItem(cityCard);
            cityCard.setPosition(list.indexOf(cityCard));
            if (cityPreference != null) saveList();
            recyclerView.scrollToPosition(0);
            Snackbar.make(recyclerView, R.string.city_added, Snackbar.LENGTH_LONG)
                    .setAction(R.string.cancel, view -> deleteCityFromList()).show();
            adapter.notifyDataSetChanged();
        }
    }

    private CityCard getCityCardByItsName(String city) {
        for (CityCard c : list) {
            if (c.getCityName().equals(city)) return c;
        }
        return null;
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                assert data != null;
                String city = data.getStringExtra(DialogBuilderFragment.CITY_ADDED);
                CityCard cityCard = new CityCard(city);
                if (!list.contains(cityCard)) {
                    WeatherDataLoader.getCurrentDataAndAddCity(cityCard,
                            requireContext(), units);
                }
            }
        }
    }

    @Override
    public void setCityCardList(List<CityCard> list) {
        cityCardsFromSQL = list;
    }
}