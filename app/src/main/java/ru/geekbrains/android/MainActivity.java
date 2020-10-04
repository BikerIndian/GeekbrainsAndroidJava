package ru.geekbrains.android;

import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.geekbrains.android.broadcast.BatteryInfoReceiver;
import ru.geekbrains.android.broadcast.NetworkInfoReceiver;
import ru.geekbrains.android.db.App;
import ru.geekbrains.android.db.EducationDao;
import ru.geekbrains.android.db.EducationSource;
import ru.geekbrains.android.db.HistorySearch;
import ru.geekbrains.android.listDayOfWeek.DataDayOfWeek;
import ru.geekbrains.android.listDayOfWeek.DayOfWeek;
import ru.geekbrains.android.listDayOfWeek.ListDayOfWeekAdapter;
import ru.geekbrains.android.network.IcoOpenWeather;
import ru.geekbrains.android.network.model.WeatherRequest;
import ru.geekbrains.android.network.picasso.ImageWeather;
import ru.geekbrains.android.network.retorfit.OpenWeather;
import ru.geekbrains.android.selectCity.SelectCity;
import ru.geekbrains.android.selectCity.SelectCityActivity;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "WEATHER";
    private static final String KEY_SHARED_CITY_NAME = "city";
    private String DEFAULT_CITY = "Moscow";
    private SelectCity selectCity;
    private TextView city;
    private TextView textTemp;
    private TextView textHumidity;
    private ImageView imageWeatherCity;
    private RetorfitUtil retorfitUtil;

    private EducationSource educationSource; //DB

    // Адаптер для списка погоды на неделю
    final ListDayOfWeekAdapter adapter = new ListDayOfWeekAdapter();

    // Сообщение об уровне
    // заряда батареи в вашем приложении.
    private BroadcastReceiver batteryInfoReceiver = new BatteryInfoReceiver();
    private BroadcastReceiver networkInfoReceiver = new NetworkInfoReceiver();

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        city = findViewById(R.id.main_city);
        textTemp = findViewById(R.id.text_temp);
        textHumidity = findViewById(R.id.main_text_humidity);
        imageWeatherCity = findViewById(R.id.mine_image_temp);
        Toolbar toolbar = findViewById(R.id.main_activity_toolbar);
        setSupportActionBar(toolbar);
        // Log.d("myLogs", "onCreate");

        setBtn();
        this.selectCity = new SelectCity();

        // Получаем данные о погоде с сервера
        // apiServiceWeather = new Openweathermap(this);
        retorfitUtil = new RetorfitUtil();

        if (savedInstanceState == null) {
            // Получаем город из файла
            SharedPreferences sharedPref = getPreferences(MODE_PRIVATE);
            String cityName = sharedPref.getString(KEY_SHARED_CITY_NAME,DEFAULT_CITY);
            retorfitUtil.getCityWeather(cityName);
            city.setText(cityName);
        }

        initRecyclerView();

        // База данных
        initDB();

        // регистрация ресивера
       registerReceiver();
    }

    private void registerReceiver() {
        // контроль заряда батареи
        registerReceiver(batteryInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_LOW));

        // контроль подключения к сети
        registerReceiver(networkInfoReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));

    }

    private void initDB() {
        EducationDao educationDao = App
                .getInstance()
                .getEducationDao();
        educationSource = new EducationSource(educationDao);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode != SelectCity.SELECT_CITY_REQUEST) {
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }

        if (resultCode == RESULT_OK) {
            this.selectCity = (SelectCity) data.getSerializableExtra(SelectCity.SELECT_CITY);
            this.city.setText(getResources().getStringArray(R.array.cities)[this.selectCity.getNum_city()]);
            Log.i("myLogs", "RESULT_OK: " + this.selectCity.getNum_city());
            String cityENG = new CityENG(this).get(this.selectCity.getNum_city());
            //apiServiceWeather.getCityWeather(cityENG);
            retorfitUtil.getCityWeather(cityENG);
        }

    }

    private void setBtn() {
        onButtonClickedSelectCity();
    }

    //SelectCity
    private void onButtonClickedSelectCity() {
        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SelectCityActivity.class);
                intent.putExtra(SelectCity.SELECT_CITY, selectCity);

                startActivityForResult(intent, SelectCity.SELECT_CITY_REQUEST);
            }
        });

    }


    public void updateCityWeather(WeatherRequest cityWeather) {
        // Обновить фон
        new ImageWeather().getFon((ImageView) findViewById(R.id.main_image_fon));

        if (cityWeather == null) {
            showAlertDialog();
            return;
        }
        int temperature = Math.round(cityWeather.getMain().getTemp());
        int humidity = cityWeather.getMain().getHumidity();

        textTemp.setText(String.format("%d°", temperature));
        textHumidity.setText(humidity + "%");
        imageWeatherCity.setImageResource(IcoOpenWeather.getIco(cityWeather.getWeather()[0].getIcon()));
        // Сохронение данных по городу
        /*
        Map<String, BaseVirtual.WeatherCity> selectListCity = BaseVirtual.getSelectListCity();

        if (selectListCity.containsKey(city.getText())) {
            BaseVirtual.WeatherCity weatherCity = selectListCity.get(city.getText());
            weatherCity.setTemperature(cityWeather.getMain().getTemp());
        } else {
            new BaseVirtual().setCity(city.getText().toString(), cityWeather.getMain().getTemp());
        }
        */
        //db
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM");

        HistorySearch historySearch = new HistorySearch();
        historySearch.city = city.getText().toString();
        historySearch.date  =  dateFormat.format(new Date());
        historySearch.temperature = temperature;
        historySearch.humidity = ""+humidity;

        educationSource.add(historySearch);

        // Сохраняем город в файл
        SharedPreferences sharedPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(KEY_SHARED_CITY_NAME, city.getText().toString());
        editor.commit();
    }




    /**
     * Предупреждение!
     * Не удалось получить данные с сервера!
     */
    private void showAlertDialog() {
        int title = R.string.alert_dialog_title;
        int message = R.string.alert_dialog_message;
        int icon = R.mipmap.ic_alert_dialog;
        int button = R.string.alert_dialog_button;

        // Создаем билдер и передаем контекст приложения
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        // в билдере указываем заголовок окна
        builder.setTitle(title)
                // указываем сообщение в окне
                .setMessage(message)
                // можно указать и пиктограмму
                .setIcon(icon)
                // из этого окна нельзя выйти кнопкой back
                .setCancelable(false)
                // устанавливаем кнопку
                .setPositiveButton(button,
                        // обработка нажатий
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //   Toast.makeText(MainActivity.this, "Кнопка нажата", Toast.LENGTH_SHORT).show();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(batteryInfoReceiver);
        unregisterReceiver(networkInfoReceiver);

    }

    public class RetorfitUtil {
        private OpenWeather openWeather;

        public RetorfitUtil() {
            initRetorfit();
        }
        private void initRetorfit() {
            Retrofit retrofit;
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.openweathermap.org/") //Базовая часть адреса
                    .addConverterFactory(GsonConverterFactory.create()) //Конвертер, необходимый для преобразования JSON'а в объекты
                    .build();
            openWeather = retrofit.create(OpenWeather.class); //Создаем объект, при помощи которого будем выполнять запросы
        }

        public void getCityWeather(String city) {
            openWeather.loadWeather(city+",RU", BuildConfig.WEATHER_API_KEY)
                    .enqueue(new Callback<WeatherRequest>() {
                        @Override
                        public void onResponse(Call<WeatherRequest> call, Response<WeatherRequest> response) {
                            if (response.body() != null) {
                                    MainActivity.this.updateCityWeather(response.body());
                            }
                        }

                        @Override
                        public void onFailure(Call<WeatherRequest> call, Throwable t) {
                            //  Log.e("WEATHER", "Error");
                        }
                    });

        }
    }
}