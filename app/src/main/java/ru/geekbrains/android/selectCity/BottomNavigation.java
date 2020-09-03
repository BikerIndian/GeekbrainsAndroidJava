package ru.geekbrains.android.selectCity;

import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import ru.geekbrains.android.R;

public class BottomNavigation implements BottomNavigationView.OnNavigationItemSelectedListener {

    AppCompatActivity activity;
    public BottomNavigation(AppCompatActivity appCompatActivity) {
        activity = appCompatActivity;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_ok:
                activity.finish();
                return true;
        }
        return false;
    }
  }

