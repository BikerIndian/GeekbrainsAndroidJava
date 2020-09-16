package ru.geekbrains.android.selectCity.menu;

import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import ru.geekbrains.android.R;
import ru.geekbrains.android.selectCity.SelectCityActivity;

public class MenuDrawer implements NavigationView.OnNavigationItemSelectedListener{
    SelectCityActivity activity;
    DrawerLayout drawer;
    public MenuDrawer(SelectCityActivity selectCityActivity) {
        activity = selectCityActivity;
        init();
    }

    private void init() {
        Toolbar toolbar = initToolbar();
        initDrawer(toolbar);
        //  initList();
    }
    private Toolbar initToolbar() {
        Toolbar toolbar = activity.findViewById(R.id.toolbar);
        activity.setSupportActionBar(toolbar);
        return toolbar;
    }

    private void initDrawer(Toolbar toolbar) {
        drawer = activity.findViewById (R.id.drawer_layout);
        NavigationView navigationView = activity.findViewById(R.id.nav_view_drawer);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                activity , drawer , toolbar , R.string.app_city_London ,
                R.string.app_city_Berlin );
        drawer.addDrawerListener (toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        // !!! switch не использовать так должен выполнится closeDrawer
        if (id == R.id.nav_list_cities) {
            activity.updateSearch("");
        }
        if (id == R.id.nav_history) {
            activity.updateHistory();
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
