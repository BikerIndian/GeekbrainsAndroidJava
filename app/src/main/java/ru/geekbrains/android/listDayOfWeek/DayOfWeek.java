package ru.geekbrains.android.listDayOfWeek;

public class DayOfWeek {
        private  String nameDayOfWeek;
        private  int imageId;
        private  String humidity;
        private  String temperature;


    public DayOfWeek(String nameDayOfWeek, int imageId, String humidity, String temperature) {
        this.nameDayOfWeek = nameDayOfWeek;
        this.imageId = imageId;
        this.humidity = humidity;
        this.temperature = temperature;
    }

    public String getNameDayOfWeek() {
        return nameDayOfWeek;
    }

    public int getImageId() {
        return imageId;
    }

    public String getHumidity() {
        return humidity;
    }

    public String getTemperature() {
        return temperature;
    }
}
