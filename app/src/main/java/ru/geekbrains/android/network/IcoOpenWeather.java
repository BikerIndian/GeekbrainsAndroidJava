package ru.geekbrains.android.network;

import java.util.HashMap;
import java.util.Map;

import ru.geekbrains.android.R;

public class IcoOpenWeather {
    public static  int getIco(String nameIco){

        Map<String,Integer> mapIco = new HashMap<>();
        mapIco.put("01d", R.mipmap.ico_weather_04);	//Чистое небо
        mapIco.put("02d", R.mipmap.ico_weather_01);	//Малооблачно
        mapIco.put("03d", R.mipmap.ico_weather_03);	//Рваная облачность
        mapIco.put("04d", R.mipmap.ico_weather_10);	//Облачно с прояснениями
        mapIco.put("09d", R.mipmap.ico_weather_02);	//Ливневый дождь
        mapIco.put("10d", R.mipmap.ico_weather_07);	//Дождь
        mapIco.put("11d", R.mipmap.ico_weather_09);	//Гроза
        mapIco.put("13d", R.mipmap.ico_weather_06);	//Снег
        mapIco.put("50d", R.mipmap.ico_weather_03);	//Туман

        mapIco.put("01n", R.mipmap.ico_weather_04);	//Чистое небо
        mapIco.put("02n", R.mipmap.ico_weather_01);	//Малооблачно
        mapIco.put("03n", R.mipmap.ico_weather_03);	//Рваная облачность
        mapIco.put("04n", R.mipmap.ico_weather_10);	//Облачно с прояснениями
        mapIco.put("09n", R.mipmap.ico_weather_02);	//Ливневый дождь
        mapIco.put("10n", R.mipmap.ico_weather_07);	//Дождь
        mapIco.put("11n", R.mipmap.ico_weather_09);	//Гроза
        mapIco.put("13n", R.mipmap.ico_weather_06);	//Снег
        mapIco.put("50n", R.mipmap.ico_weather_03);	//Туман

        if (nameIco == null) {
            return mapIco.get("04d");
        }
        return mapIco.get(nameIco);
    }
}
