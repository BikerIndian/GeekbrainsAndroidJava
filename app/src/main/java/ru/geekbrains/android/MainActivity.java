package ru.geekbrains.android;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.io.IOException;
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

    private GoogleMaps googleMaps;
    private NotificationMessage notificationMessage;
    private AlertWeather alertWeather;

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
            String cityName = sharedPref.getString(KEY_SHARED_CITY_NAME, DEFAULT_CITY);
            retorfitUtil.getCityWeather(cityName);
            city.setText(cityName);
        }

        initRecyclerView();

        // База данных
        initDB();

        // регистрация ресивера
        registerReceiver();

        //FirebaseService
        initGetToken();

        googleMaps = new GoogleMaps();
        notificationMessage = new NotificationMessage();
        alertWeather = new AlertWeather();

    }

    // Firebase
    private void initGetToken() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.i("PushMessageAndroid", "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        Log.i("PushMessageAndroid", "Token: " + token);

                    }
                });
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
        onButtonClickedGetMap();
    }

    // Получение погоды по геоданным
    private void onButtonClickedGetMap() {
        Button button = findViewById(R.id.button_get_weather);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                googleMaps.getLocationOnce();
            }
        });
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
        historySearch.date = dateFormat.format(new Date());
        historySearch.temperature = temperature;
        historySearch.humidity = "" + humidity;

        educationSource.add(historySearch);

        // Сохраняем город в файл
        SharedPreferences sharedPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(KEY_SHARED_CITY_NAME, city.getText().toString());
        editor.commit();

        alertWeather.check(cityWeather);
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

    @Override
    protected void onPause() {
        super.onPause();
        googleMaps.removeUpdates();
    }

    public RetorfitUtil getRetorfitUtil() {
        return retorfitUtil;
    }

    public NotificationMessage getNotificationMessage() {
        return notificationMessage;
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
            openWeather.loadWeather(city + ",RU", BuildConfig.WEATHER_API_KEY)
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

        // Обновляем погоду по координатам
        //api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&appid={API key}
        public void getWeatherLatLon(double lat, double lon) {
            openWeather.loadWeatherLatLon(lat, lon, BuildConfig.WEATHER_API_KEY)
                    .enqueue(new Callback<WeatherRequest>() {
                        @Override
                        public void onResponse(Call<WeatherRequest> call, Response<WeatherRequest> response) {
                            if (response.body() != null) {
                                MainActivity.this.updateCityWeather(response.body());
                            }
                        }

                        @Override
                        public void onFailure(Call<WeatherRequest> call, Throwable t) {
                            Log.e("GoogleMaps", "Error getWeatherLatLon(double lat, double lon)");
                        }
                    });

        }
    }

    private class GoogleMaps {
        private static final int PERMISSION_REQUEST_CODE = 10;
        private String TAG = "GoogleMaps";
        private TextView city;

        // менеджер геолокаций
        private LocationManager locationManager;
        private LocationListener locationListener;


        // Запрос координат
        private void requestLocation() {
            Log.i(TAG, "Запрос координат.");
            // Если пермиссии все таки нет - то просто выйдем, приложение не имеет смысла
            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                return;
            // Получить менеджер геолокаций
            if (locationManager == null) {
                locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            }

            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_COARSE);

            // получим наиболее подходящий провайдер геолокации по критериям
            // Но можно и самому назначать какой провайдер использовать.
            // В основном это LocationManager.GPS_PROVIDER или LocationManager.NETWORK_PROVIDER
            // но может быть и LocationManager.PASSIVE_PROVIDER, это когда координаты уже кто-то недавно получил.
            String provider = locationManager.getBestProvider(criteria, true);
            //String provider = LocationManager.GPS_PROVIDER;
            Location myLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);


            if (provider != null) {
                // Будем получать геоположение
                locationListener = getLocationListener();
                locationManager.requestLocationUpdates(provider, 0, 0, locationListener);
            }
        }

        private LocationListener getLocationListener() {
            LocationListener locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    double lat = location.getLatitude();// Широта
                    String latitude = Double.toString(lat);
                    Log.i(TAG, "Latitude: " + latitude);
                    //** textLatitude.setText(latitude);

                    double lng = location.getLongitude();// Долгота
                    String longitude = Double.toString(lng);
                    Log.i(TAG, "Longitude: " + longitude);

                    // Обновляем погоду по координатам
                    MainActivity.this.getRetorfitUtil().getWeatherLatLon(lat, lng);
                    getAddress(new LatLng(lat, lng));
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                }

                @Override
                public void onProviderEnabled(String provider) {
                }

                @Override
                public void onProviderDisabled(String provider) {
                }
            };
            return locationListener;
        }

        // Запрос пермиссии для геолокации
        private void requestLocationPermissions() {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                    || !ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                // Запросим эти две пермиссии у пользователя
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION
                        },
                        PERMISSION_REQUEST_CODE);
            }
        }

        // Запрос пермиссий
        private void requestPemissions() {
            // Проверим на пермиссии, и если их нет, запросим у пользователя
            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                // запросим координаты
                requestLocation();
            } else {
                // пермиссии нет, будем запрашивать у пользователя
                requestLocationPermissions();
            }
        }

        // Получаем адрес по координатам
        private void getAddress(final LatLng location) {
            final Geocoder geocoder = new Geocoder(MainActivity.this);
            // Поскольку geocoder работает по интернету, создадим отдельный поток
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        final List<Address> addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1);
                        addresses.get(0).getAddressLine(0);
                        city = findViewById(R.id.main_city);
                        city.post(new Runnable() {
                            @Override
                            public void run() {
                                //String cityAddres = addresses.get(0).getAddressLine(0);
                                String cityAddres = addresses.get(0).getLocality();
                                city.setText(cityAddres);
                                Log.i(TAG, "Address: " + cityAddres);
                                Log.i(TAG, "addresses.get(0): " + addresses.get(0));
                            }
                        });
                        removeUpdates(); // удаляем листнер
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

        public void getLocationOnce() {
            requestPemissions();
        }

        public void removeUpdates() {
            if (locationManager != null && locationListener != null) {
                locationManager.removeUpdates(locationListener);
            }
        }
    } // end GoogleMaps

    // Вывод сообщений пользователю
    private class NotificationMessage {
        private final static String NOTIFICATION_ID = "2";
        private String channelName = getString(R.string.app_name);
        private Context context = MainActivity.this;

        public NotificationMessage() {
            init();
        }

        private void init() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                int importance = NotificationManager.IMPORTANCE_LOW;
                NotificationChannel channel = new NotificationChannel(NOTIFICATION_ID, channelName, importance);
                //channel.setDescription("My channel description");
                channel.enableLights(true);
                channel.setLightColor(Color.RED);
                channel.enableVibration(false);
                notificationManager.createNotificationChannel(channel);
            }
        }

        public void send(String mess) {

            // создать нотификацию
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "2")
                    .setSmallIcon(R.drawable.ic_stat_alert)
                    //.setContentTitle("Broadcast Receiver")
                    .setContentText(mess);
            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            //notificationManager.notify(messageId++, builder.build());
            notificationManager.notify(0, builder.build());
        }
    }

    // Отправить сообщение, при опасных погодных явлений
    private class AlertWeather {
        private String TAG = "AlertWeather";
        private String messageAlarm = "";
        private WeatherRequest cityWeather;

        // Оасные параметры погоды
        private float maxHourRain = 15f;        // mm осадков - Дождь
        private float maxSpeedWind = 14f;       // Ветер

        public void check(WeatherRequest cityWeather) {
            this.cityWeather = cityWeather;
            Log.i(TAG, "check");


            if (isMaxHourRain()) {
                String messRain = getString(R.string.alert_weather_max_hour_rain)+"\n";
                messageAlarm +=messRain;
            }

            if (isMaxSpeedWind()) {
                String messSpeedWind = getString(R.string.alert_weather_max_speed_wind)+"\n";
                messageAlarm+=messSpeedWind;
            }

            sendMess();
        }

        private void sendMess() {
            if (isMaxHourRain() || isMaxSpeedWind()) {
                getNotificationMessage().send(messageAlarm);
            }
        }

        private boolean isMaxHourRain() {
            float hourRain = 0;
            if (cityWeather.getRain() != null) {
                hourRain = cityWeather.getRain().getHour();
                Log.i(TAG, "hourRain = " + hourRain);
            }

            if (maxHourRain < hourRain) {
                return true;
            }
            return false;
        }

        private boolean isMaxSpeedWind() {
            float speedWind = 0;
            // wind скорость ветра > 25 метров /c
            if (cityWeather.getWind() != null) {
                speedWind = cityWeather.getWind().getSpeed();
                Log.i(TAG, "speedWind = " + speedWind);
            }
            if (maxSpeedWind < speedWind) {
                return true;
            }
            return false;
        }

    }

}