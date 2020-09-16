package ru.geekbrains.android.selectCity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Map;

import ru.geekbrains.android.BaseVirtual;
import ru.geekbrains.android.R;

// Фрагмент выбора города из списка
public class CitiesFragment extends Fragment {
    public static final String CITY_ID = "ru.geekbrains.android.selectCity.CitiesFragment.city";
    public static final String NUM_CITY_ID = "ru.geekbrains.android.selectCity.CitiesFragment.num";
    private String city="";
    private int num_city=0;
    private LinearLayout layoutView;

    // При создании фрагмента укажем его макет
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState != null) {
            city =  savedInstanceState.getString(CITY_ID);
            num_city =  savedInstanceState.getInt(NUM_CITY_ID);
        }

        initList(view);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {

        outState.putString(CITY_ID, city);
        outState.putInt(NUM_CITY_ID,num_city);
        super.onSaveInstanceState(outState);
    }

    // создаем список городов на экране из массива в ресурсах
    private void initList(View view) {
        layoutView = (LinearLayout) view;
        updateCityList("");
    }


    /**
     * Выводит список городов
     * @param searchCity
     */
    public void updateCityList(String searchCity) {
        // очистка всех элементов
        layoutView.removeAllViews();
        String[] cities = getResources().getStringArray(R.array.cities);

        // В этом цикле создаем элемент TextView,
        // заполняем его значениями,
        // и добавляем на экран.
        // Кроме того, создаем обработку касания на элемент
        for (int i = 0; i < cities.length; i++) {
            String city = cities[i];
            // Добавление города в список
            if (city.contains(searchCity) || searchCity.equals("") ) {
                addCity(city,i);
            }

        }

    }

    private void addCity(String city, int i) {

        TextView tv = new TextView(getContext());
        tv.setText(city);
        tv.setTextSize(30);
        layoutView.addView(tv);
        final int fi = i;

        tv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                TextView editCity = v.getRootView().findViewById(R.id.select_city_editText);
                CitiesFragment.this.city = getResources().getStringArray(R.array.cities)[fi];
                CitiesFragment.this.num_city = fi;
                editCity.setText(CitiesFragment.this.city);
                save();

            }
        });
    }

    private void save() {
        Bundle bundle = new Bundle();
        bundle.putString(CITY_ID, city);
        bundle.putInt(NUM_CITY_ID,num_city);
        setArguments(bundle);
    }


    public void updateHistory() {
        // очистка всех элементов
        layoutView.removeAllViews();

        Map<String, BaseVirtual.WeatherCity> selectListCity =  BaseVirtual.getSelectListCity();
        for (Map.Entry<String, BaseVirtual.WeatherCity> entry : selectListCity.entrySet()) {

            TextView tv = new TextView(getContext());
            String temperature = String.format("%.0f°", entry.getValue().getTemperature());
            tv.setText(entry.getKey() + " " + temperature);
            tv.setTextSize(30);
            layoutView.addView(tv);
        }


    }
}
