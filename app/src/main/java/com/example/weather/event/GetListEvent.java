package com.example.weather.event;

import com.example.weather.ForecastCard;

import java.util.ArrayList;

public class GetListEvent {

    private ArrayList<ForecastCard> list;

    public GetListEvent(ArrayList<ForecastCard> list) {
        this.list = list;
    }

    public ArrayList<ForecastCard> getList() {
        return list;
    }

}
