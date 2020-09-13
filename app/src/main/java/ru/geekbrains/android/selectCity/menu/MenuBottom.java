package ru.geekbrains.android.selectCity.menu;

import android.view.MenuItem;

import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import ru.geekbrains.android.R;
import ru.geekbrains.android.selectCity.SelectCityActivity;

public class MenuBottom implements BottomNavigationView.OnNavigationItemSelectedListener {

    SelectCityActivity activity;
    public MenuBottom(SelectCityActivity activity) {
        this.activity = activity;
        init();
    }

    private void init() {
        BottomNavigationView navView = activity.findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(this);
    }

    // обработка событий menu Bottom
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

