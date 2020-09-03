package ru.geekbrains.android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class SelectCityActivity extends AppCompatActivity {

    CheckBox checkWindSpeed;
    CheckBox checkPressure;
    TextInputEditText editCity;

    SelectCity selectCity;
    CitiesFragment citiesFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        selectCity = new SelectCity();

        setContentView(R.layout.activity_select_city);

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

                save();

                // Передача данных в main
                Intent intentResult = new Intent();
                intentResult.putExtra(SelectCity.SELECT_CITY, selectCity);
                setResult(RESULT_OK, intentResult);

                finish();
            }
        }));
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

}
