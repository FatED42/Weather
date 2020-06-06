package com.example.weather.event;

import com.example.weather.CityCard;

public class AddCityToListEvent {

    private CityCard cityCard;

    public AddCityToListEvent(CityCard cityCard) {
        this.cityCard = cityCard;
    }

    public CityCard getCityCard() {
        return cityCard;
    }

}
