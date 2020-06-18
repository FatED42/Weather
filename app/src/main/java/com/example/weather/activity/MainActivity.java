package com.example.weather.activity;


import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.Menu;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.weather.ConnectionStateMonitor;
import com.example.weather.R;
import com.example.weather.event.ChangeThemeBtnClickedEvent;
import com.google.android.material.navigation.NavigationView;
import com.example.weather.EventBus;
import com.squareup.otto.Subscribe;

public class MainActivity extends BaseActivity {

    private AppBarConfiguration mAppBarConfiguration;
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setNavigationUI();
        ConnectionStateMonitor connectionStateMonitor = new ConnectionStateMonitor();
        connectionStateMonitor.enable(getApplicationContext());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_without_plus, menu);
        return true;
    }

    private void setNavigationUI() {
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_cities_list, R.id.nav_settings, R.id.nav_about)
                .setDrawerLayout(drawerLayout)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getBus().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getBus().unregister(this);
        super.onStop();
    }

    @Subscribe
    public void onChangeThemeBtnClickedEvent (ChangeThemeBtnClickedEvent event) {
        setDarkTheme(event.isChecked());
        recreate();
    }
}