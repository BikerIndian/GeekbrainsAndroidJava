package ru.geekbrains.android;

import java.io.Serializable;

public class SelectCity implements Serializable {
    private boolean windSpeed;
    private boolean pressure;
    private String city;

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
}
