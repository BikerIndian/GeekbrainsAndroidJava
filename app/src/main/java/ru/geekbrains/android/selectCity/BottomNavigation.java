package ru.geekbrains.android.selectCity;

import android.view.MenuItem;

import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import ru.geekbrains.android.R;

public class BottomNavigation implements BottomNavigationView.OnNavigationItemSelectedListener {

    SelectCityActivity activity;
    public BottomNavigation(SelectCityActivity activity) {
        this.activity = activity;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_ok:
                activity.returnMain();
                activity.finish();
                return true;
        }
        return false;
    }
  }

