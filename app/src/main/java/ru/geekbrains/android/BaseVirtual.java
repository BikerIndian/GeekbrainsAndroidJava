package ru.geekbrains.android;

import android.util.ArrayMap;

import java.util.Map;

public final class BaseVirtual {


    private static final BaseVirtual instance = new BaseVirtual();
    private static final Object syncObj = new Object();

    private static boolean windSpeed;
    private static boolean pressure;
    private static int temperature;
    private static String city;
    private static final Map<String,WeatherCity> selectListCity = new ArrayMap();

    public BaseVirtual(){
        temperature = 0;
    }

    public static BaseVirtual getInstance(){
        synchronized (syncObj) {
            return instance;
        }
    }

    public static boolean isWindSpeed() {
        return windSpeed;
    }

    public static void setWindSpeed(boolean windSpeed) {
        BaseVirtual.windSpeed = windSpeed;
    }

    public static boolean isPressure() {
        return pressure;
    }

    public static void setPressure(boolean pressure) {
        BaseVirtual.pressure = pressure;
    }

    public static int getTemperature() {
        return temperature;
    }

    public static void setTemperature(int temperature) {
        BaseVirtual.temperature = temperature;
    }

    public static String getCity() {
        return city;
    }

    public static Map<String, WeatherCity> getSelectListCity() {
        return selectListCity;
    }


    public void setCity(String city, float temperature) {
        BaseVirtual.city = city;
        selectListCity.put(city,new WeatherCity(city,temperature));
    }
    public class WeatherCity {
        private String cityName;
        private float temperature;
        private int num_city;

        public WeatherCity(String cityName, float temperature) {
            this.temperature = temperature;
            this.cityName = cityName;
        }

        public String getCityName() {
            return cityName;
        }

        public void setCityName(String cityName) {
            this.cityName = cityName;
        }

        public float getTemperature() {
            return temperature;
        }

        public void setTemperature(float temperature) {
            this.temperature = temperature;
        }

        public int getNum_city() {
            return num_city;
        }

        public void setNum_city(int num_city) {
            this.num_city = num_city;
        }
    }
}
