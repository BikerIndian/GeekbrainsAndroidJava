package ru.geekbrains.android.selectCity;

import java.io.Serializable;

public class SelectCity implements Serializable {

    public final static String SELECT_CITY = "ru.geekbrains.android.SelectCityActivity";
    public final static int SELECT_CITY_REQUEST = 1 ;

    private boolean windSpeed;
    private boolean pressure;
    private String city;
    private int num_city;


    public boolean isWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(boolean windSpeed) {
        this.windSpeed = windSpeed;
    }

    public boolean isPressure() {
        return pressure;
    }

    public void setPressure(boolean pressure) {
        this.pressure = pressure;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getNum_city() {
        return num_city;
    }

    public void setNum_city(int num_city) {
        this.num_city = num_city;
    }
}
