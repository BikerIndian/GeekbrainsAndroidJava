package ru.geekbrains.android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.TextView;

public class SelectCityActivity extends AppCompatActivity {

    CheckBox checkWindSpeed;
    CheckBox checkPressure;
    String city="";
    TextView editCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_city);

        checkWindSpeed = findViewById(R.id.select_city_check_wind_speed);
        checkPressure = findViewById(R.id.select_city_check_pressure);
        editCity = findViewById(R.id.select_city_editText);


        SelectCity selectCity = (SelectCity) getIntent().getSerializableExtra(Keys.SELECT_CITY);

        if (selectCity != null) {
            checkWindSpeed.setChecked(selectCity.isWindSpeed());
            checkPressure.setChecked(selectCity.isPressure());
        }



        setButton();
        setSelectCity();
        //android:id="@+id/select_city_TextView_city5"

    }

    private void setSelectCity() {
        findViewById(R.id.select_city_TextView_city1).setOnClickListener(selectCityListener());
        findViewById(R.id.select_city_TextView_city2).setOnClickListener(selectCityListener());
        findViewById(R.id.select_city_TextView_city3).setOnClickListener(selectCityListener());
        findViewById(R.id.select_city_TextView_city4).setOnClickListener(selectCityListener());
        findViewById(R.id.select_city_TextView_city5).setOnClickListener(selectCityListener());
    }

    private OnClickListener selectCityListener(){

        OnClickListener selectCityListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView  = findViewById(v.getId());
                city = ""+textView.getText();
                editCity.setText(city);
            }
        };
        return selectCityListener;
    }



    private void setButton() {

        // OK
        findViewById(R.id.select_city_button_ok).setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SelectCity selectCity = new SelectCity();

                selectCity.setPressure(checkPressure.isChecked());
                selectCity.setWindSpeed(checkWindSpeed.isChecked());
                selectCity.setCity(city);


                Intent intentResult = new Intent();
                intentResult.putExtra(Keys.SELECT_CITY, selectCity);
                setResult(RESULT_OK , intentResult);
                finish();
            }
        }));
    }

}
