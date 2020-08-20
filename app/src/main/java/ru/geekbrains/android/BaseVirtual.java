package ru.geekbrains.android;

public final class BaseVirtual {

    private static final BaseVirtual instance = new BaseVirtual();
    private static final Object syncObj = new Object();

    private static boolean windSpeed;
    private static boolean pressure;
    private static int temperature;
    private static String city;

    private BaseVirtual(){
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

    public static void setCity(String city) {
        BaseVirtual.city = city;
    }
}
