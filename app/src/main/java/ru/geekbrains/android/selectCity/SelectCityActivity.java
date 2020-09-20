package ru.geekbrains.android.selectCity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.textfield.TextInputEditText;

import ru.geekbrains.android.R;
import ru.geekbrains.android.selectCity.menu.MenuActionBar;
import ru.geekbrains.android.selectCity.menu.MenuBottom;
import ru.geekbrains.android.selectCity.menu.MenuDrawer;

public class SelectCityActivity extends AppCompatActivity {
    private static final String TAG = "WEATHER";
    private CheckBox checkWindSpeed;
    private CheckBox checkPressure;
    private TextInputEditText editCity;

    private SelectCity selectCity;
    private CitiesFragment citiesFragment;

    private MenuBottom menuBottom;
    private MenuActionBar menuActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        selectCity = new SelectCity();

        // setContentView(R.layout.activity_select_city);
        setContentView(R.layout.activity_select_city_drawer); // активити с drawer

        checkWindSpeed = findViewById(R.id.select_city_check_wind_speed);
        checkPressure = findViewById(R.id.select_city_check_pressure);
        editCity = findViewById(R.id.select_city_editText);

        SelectCity selectCity = (SelectCity) getIntent().getSerializableExtra(SelectCity.SELECT_CITY);

        if (selectCity != null) {
            checkWindSpeed.setChecked(selectCity.isWindSpeed());
            checkPressure.setChecked(selectCity.isPressure());
        }

        setButton();
        setSelectCity();

        // Обработчик BottomBar
        menuBottom = new MenuBottom(this);
        menuActionBar = new MenuActionBar(this);
        //Navigation Drawer — боковое навигационное меню приложения
        new MenuDrawer(this);
    }

    // Обработка события по ножатию кнопки возврата
    // Если Drawer открыт то закрыть
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Здесь определяем меню приложения (активити)
        menuActionBar.createMenu(menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Обработка выбора пункта меню приложения (активити)
        //menuActionBar.selected(item);
        return super.onOptionsItemSelected(item);
    }



    private void setSelectCity() {

        // Проверим, что фрагмент существует в activity
        if (getSupportFragmentManager().findFragmentById(R.id.select_city_frame) == null) {
            citiesFragment = new CitiesFragment();
            // Добавим фрагмент на activity
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.select_city_frame, citiesFragment)
                    .commit();

        } else {
            citiesFragment = (CitiesFragment) getSupportFragmentManager().findFragmentById(R.id.select_city_frame);
        }

    }


    private void setButton() {

        // OK
        findViewById(R.id.select_city_button_ok).setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnMain();

            }
        }));


        // убрать кнопку
        findViewById(R.id.select_city_button_ok).setVisibility(View.GONE);
    }

    public void returnMain() {
        save();

        // Передача данных в main
        Intent intentResult = new Intent();
        intentResult.putExtra(SelectCity.SELECT_CITY, selectCity);
        setResult(RESULT_OK, intentResult);

        finish();
    }

    private void save() {
        selectCity.setPressure(checkPressure.isChecked());
        selectCity.setWindSpeed(checkWindSpeed.isChecked());

        if (editCity.getText().length() > 0 && citiesFragment.getArguments() != null) {
            String city = citiesFragment.getArguments().getString(CitiesFragment.CITY_ID);
            selectCity.setCity(city);
            int num_city = citiesFragment.getArguments().getInt(CitiesFragment.NUM_CITY_ID);
            selectCity.setNum_city(num_city);
        }

    }

    @Override
    protected void onRestoreInstanceState(Bundle saveInstanceState) {
        super.onRestoreInstanceState(saveInstanceState);

        if (saveInstanceState != null && saveInstanceState.get(SelectCity.SELECT_CITY) instanceof SelectCity) {
            selectCity = (SelectCity) saveInstanceState.get(SelectCity.SELECT_CITY);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle saveInstanceState) {
        super.onSaveInstanceState(saveInstanceState);
        save();
        saveInstanceState.putSerializable(SelectCity.SELECT_CITY, selectCity);

    }

    public void updateSearch(String searchText) {
        Log.i(TAG, searchText);
        citiesFragment.updateCityList(searchText);
    }

    public void updateHistory() {
        citiesFragment.updateHistory();
    }
}
