package com.example.weather;

import com.example.weather.dao.ICitiesDao;

import java.util.List;

public class CitiesHistorySource {

    private final ICitiesDao citiesDao;
    private List<CityCard> cityCardList;

    public CitiesHistorySource(ICitiesDao citiesDao) {
        this.citiesDao = citiesDao;
    }

    public void getCityCardList(ICardList fragment) {
        new Thread(() -> {
            cityCardList = citiesDao.getAllCityCards();
            fragment.setCityCardList(cityCardList);
        }).start();
    }

    public void deleteAllCityCards() {
        new Thread(() -> {
            citiesDao.deleteAllCityCards();
            cityCardList = citiesDao.getAllCityCards();
        }).start();
    }

    public void loadCities() {
        new Thread(() -> cityCardList = citiesDao.getAllCityCards()).start();
    }

    public void addCity(CityCard cityCard) {
        new Thread(() -> {
            citiesDao.insertCity(cityCard);
            cityCardList = citiesDao.getAllCityCards();
        }).start();

    }

    public void updateCity(CityCard cityCard) {
        new Thread(() -> {
            citiesDao.updateCity(cityCard);
            cityCardList = citiesDao.getAllCityCards();
        }).start();
    }

    public void removeCity(CityCard cityCard) {
        new Thread(() -> {
            citiesDao.deleteCity(cityCard);
            cityCardList = citiesDao.getAllCityCards();
        }).start();
    }
}