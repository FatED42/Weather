package com.example.weather;

import android.app.Application;

import androidx.room.Room;

import com.example.weather.dao.ICitiesDao;
import com.example.weather.database.CitiesHistorySearchDatabase;

public class App extends Application {

    private static App instance;
    private CitiesHistorySearchDatabase db;

    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        db = Room.databaseBuilder(
                getApplicationContext(),
                CitiesHistorySearchDatabase.class,
                "cities_history_search_database")
                .fallbackToDestructiveMigration()
                .build();

    }

    public ICitiesDao getCitiesDao() {
        return db.getCitiesDao();
    }
}
