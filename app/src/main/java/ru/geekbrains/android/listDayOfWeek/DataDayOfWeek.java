package ru.geekbrains.android.listDayOfWeek;

import java.util.ArrayList;
import java.util.List;

import ru.geekbrains.android.R;

public class DataDayOfWeek {
    List<DayOfWeek> listData = new ArrayList<>();

    public DataDayOfWeek() {
        init();
    }

    private void init() {

        listData.add(new DayOfWeek("пн",R.mipmap.ico_weather_01, "15%","24"));
        listData.add(new DayOfWeek("вт",R.mipmap.ico_weather_02, "10%","23"));
        listData.add(new DayOfWeek("ср",R.mipmap.ico_weather_03, "5%","20"));
        listData.add(new DayOfWeek("чт",R.mipmap.ico_weather_04, "20%","21"));
        listData.add(new DayOfWeek("пт",R.mipmap.ico_weather_07, "10%","25"));
        listData.add(new DayOfWeek("сб",R.mipmap.ico_weather_06, "15%","24"));


     }

    public List<DayOfWeek> getListData() {
        return listData;
    }
}
