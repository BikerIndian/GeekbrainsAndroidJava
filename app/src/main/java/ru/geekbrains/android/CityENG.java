package ru.geekbrains.android;

import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;

import java.util.Locale;

public class CityENG {
   private MainActivity mainActivit;
    public CityENG(MainActivity mainActivity) {
        this.mainActivit = mainActivity;
    }

    public String get(int num_city) {

        Resources res = mainActivit.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.setLocale(new Locale("en"));
        res.updateConfiguration(conf, dm);
        String[] cities = res.getStringArray(R.array.cities);

        conf.setLocale(Locale.getDefault());
        res.updateConfiguration(conf, dm);

        Log.i("myLogs", cities[num_city]);

        return cities[num_city];
    }
}
