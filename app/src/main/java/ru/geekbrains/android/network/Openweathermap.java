package ru.geekbrains.android.network;

import android.os.Handler;

import com.google.gson.Gson;

import ru.geekbrains.android.BuildConfig;
import ru.geekbrains.android.MainActivity;
import ru.geekbrains.android.network.model.WeatherRequest;

public class Openweathermap {
    private String city;
    private  WeatherRequest weatherRequest;

    private MainActivity mainActivity;

    //

    HttpUtil httpUtil;

    public Openweathermap(MainActivity mainActivity) {
            this.mainActivity = mainActivity;
    }


    public void getCityWeather(String cityIn) {
        httpUtil = new HttpUtil();
        this.city = cityIn;
        final Handler handler = new Handler(); // Запоминаем основной поток

        Thread t1 =  new Thread(new Runnable() {
            public void run() {
                String url = String.format("https://api.openweathermap.org/data/2.5/weather?q=%s,RU&appid=%s", city, BuildConfig.WEATHER_API_KEY);
                String result = httpUtil.getHttpsRequest(url);
                Gson gson = new Gson();
                weatherRequest = gson.fromJson(result, WeatherRequest.class);

                // Возвращаемся к основному потоку
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        mainActivity.updateCityWeather(weatherRequest);
                    }
                });

            }
        });
        t1.start();

    }
}
