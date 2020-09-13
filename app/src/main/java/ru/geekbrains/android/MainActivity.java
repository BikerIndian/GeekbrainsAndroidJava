package ru.geekbrains.android;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Map;

import ru.geekbrains.android.listDayOfWeek.DataDayOfWeek;
import ru.geekbrains.android.listDayOfWeek.DayOfWeek;
import ru.geekbrains.android.listDayOfWeek.ListDayOfWeekAdapter;
import ru.geekbrains.android.network.Openweathermap;
import ru.geekbrains.android.network.model.WeatherRequest;
import ru.geekbrains.android.selectCity.SelectCity;
import ru.geekbrains.android.selectCity.SelectCityActivity;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "WEATHER";
    private String DEFAULT_CITY = "Moscow";
    private SelectCity selectCity;
    TextView city;
    private TextView textTemp;
    private Openweathermap apiServiceWeather;


    // Адаптер для списка погоды на неделю
    final ListDayOfWeekAdapter adapter = new ListDayOfWeekAdapter();

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        city = findViewById(R.id.main_city);
        textTemp = findViewById(R.id.text_temp);

       // Log.d("myLogs", "onCreate");

        setBtn();
        this.selectCity = new SelectCity();

        // Получаем данные о погоде с сервера
        apiServiceWeather = new Openweathermap(this);

        if (savedInstanceState == null) {
            apiServiceWeather.getCityWeather(DEFAULT_CITY);
        }

        initRecyclerView();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initRecyclerView() {
        final RecyclerView recyclerView = findViewById(R.id.recyclerView);
        // Эта установка служит для повышения производительности системы
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);

        updateCities(new DataDayOfWeek().getListData());

        // Декоратор
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        itemDecoration.setDrawable(getDrawable(R.drawable.separator));
        recyclerView.addItemDecoration(itemDecoration);

    }
    public void updateCities(List<DayOfWeek> listDayOfWeek) {
        adapter.update(listDayOfWeek);
    }


    @Override
    protected void onActivityResult( int requestCode, int resultCode, Intent data) {

        if (requestCode != SelectCity.SELECT_CITY_REQUEST ) {
            super .onActivityResult(requestCode, resultCode, data);
            return ;
        }

        if (resultCode == RESULT_OK) {
            this.selectCity =  (SelectCity) data.getSerializableExtra(SelectCity.SELECT_CITY);
            this.city.setText(this.selectCity.getCity());
            Log.i("myLogs", "RESULT_OK: "+this.selectCity.getNum_city());
            String cityENG = new CityENG(this).get(this.selectCity.getNum_city());
            apiServiceWeather.getCityWeather(cityENG);
        }

    }

    private void setBtn() {
        onButtonClickedSelectCity();
        onButtonClickedLifeCycle();
    }

    //SelectCity
    private void onButtonClickedSelectCity() {
        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SelectCityActivity.class);
                intent.putExtra(SelectCity.SELECT_CITY,selectCity);

                startActivityForResult(intent,SelectCity.SELECT_CITY_REQUEST);
            }
        });

    }

    //LifeCycle
    private void onButtonClickedLifeCycle() {
        findViewById(R.id.button_LifeCycle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LifeCycle.class));
            }
        });
        // убрать кнопку
        findViewById(R.id.button_LifeCycle).setVisibility(View.GONE);
    }


    public void updateCityWeather(WeatherRequest cityWeather) {

        if (cityWeather == null) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Данные с сервера не пришли", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
       textTemp.setText(String.format("%.0f°", cityWeather.getMain().getTemp()));

        // Сохронение данных по городу
        Map<String, BaseVirtual.WeatherCity> selectListCity =  BaseVirtual.getSelectListCity();

        if (selectListCity.containsKey(city.getText())) {
            BaseVirtual.WeatherCity weatherCity = selectListCity.get(city.getText());
            weatherCity.setTemperature(cityWeather.getMain().getTemp());
        } else {
            new BaseVirtual().setCity(city.getText().toString(),cityWeather.getMain().getTemp());
        }
       // new BaseVirtual().setCity(city,num_city);
    }
}