package ru.geekbrains.android.network;

import ru.geekbrains.android.MainActivity;

public class ApiService {
   private MainActivity mainActivity;
   // private static final String WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather?lat=55.75&lon=37.62&appid=";

   // final Handler handler = new Handler(); // Запоминаем основной поток

    public ApiService(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public void getCityWeather(String moscow) {

        Openweathermap openweathermap = new Openweathermap(mainActivity);
       // mainActivity.updateCityWeather(openweathermap.getCityWeather(moscow));
    }

}
