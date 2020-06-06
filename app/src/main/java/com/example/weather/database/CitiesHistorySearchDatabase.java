package com.example.weather.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.weather.CityCard;
import com.example.weather.dao.ICitiesDao;

@Database(entities = {CityCard.class}, version = 7)
public abstract class CitiesHistorySearchDatabase extends RoomDatabase {

    public abstract ICitiesDao getCitiesDao();

}
