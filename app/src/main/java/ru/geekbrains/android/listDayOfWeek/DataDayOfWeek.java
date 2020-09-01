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

        listData.add(new DayOfWeek("пн",R.drawable.ic_launcher_background, "15%","24"));
        listData.add(new DayOfWeek("вт",R.drawable.ic_launcher_background, "10%","23"));
        listData.add(new DayOfWeek("ср",R.drawable.ic_launcher_background, "5%","20"));
        listData.add(new DayOfWeek("чт",R.drawable.ic_launcher_background, "20%","21"));
        listData.add(new DayOfWeek("пт",R.drawable.ic_launcher_background, "10%","25"));
        listData.add(new DayOfWeek("сб",R.drawable.ic_launcher_background, "15%","24"));


     }

    public List<DayOfWeek> getListData() {
        return listData;
    }
}
