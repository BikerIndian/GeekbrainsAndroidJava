package ru.geekbrains.android.selectCity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import java.util.List;

import ru.geekbrains.android.R;
import ru.geekbrains.android.db.App;
import ru.geekbrains.android.db.EducationDao;
import ru.geekbrains.android.db.EducationSource;
import ru.geekbrains.android.db.HistorySearch;

// Фрагмент выбора города из списка
public class CitiesFragment extends Fragment {
    public static final String CITY_ID = "ru.geekbrains.android.selectCity.CitiesFragment.city";
    public static final String NUM_CITY_ID = "ru.geekbrains.android.selectCity.CitiesFragment.num";
    private String city = "";
    private int num_city = 0;
    private LinearLayout layoutView;
    private ConstraintLayout constraintLayout;
    private Button btmClear;

    private EducationSource educationSource; //DB

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
            city = savedInstanceState.getString(CITY_ID);
            num_city = savedInstanceState.getInt(NUM_CITY_ID);
        }

        // База данных
        initDB();

        constraintLayout = (ConstraintLayout) view;
        initBtm(constraintLayout);
        initList(constraintLayout);


    }

    private void initBtm(View view) {
        btmClear = view.findViewById(R.id.select_city_button_clear);
        btmClear.setVisibility(View.GONE);
        btmClear.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                educationSource.clear();
                updateHistory();
            }
        }));
    }

    private void initDB() {

        EducationDao educationDao = App
                .getInstance()
                .getEducationDao();
        educationSource = new EducationSource(educationDao);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {

        outState.putString(CITY_ID, city);
        outState.putInt(NUM_CITY_ID, num_city);
        super.onSaveInstanceState(outState);
    }

    // создаем список городов на экране из массива в ресурсах
    private void initList(View view) {
        layoutView = view.findViewById(R.id.select_city_list);
        //layoutViewCore = (LinearLayout) view;
        updateCityList("");
    }


    /**
     * Выводит список городов
     *
     * @param searchCity
     */
    public void updateCityList(String searchCity) {
        //Скрыть кнопку
        btmClear.setVisibility(View.GONE);

        //btmClear.setVisibility(View.VISIBLE);
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
            if (city.contains(searchCity) || searchCity.equals("")) {
                addCity(city, i);
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
        bundle.putInt(NUM_CITY_ID, num_city);
        setArguments(bundle);
    }


    public void updateHistory() {
        //Показать кнопку
        btmClear.setVisibility(View.VISIBLE);
        // очистка всех элементов
        layoutView.removeAllViews();

/*
        Map<String, BaseVirtual.WeatherCity> selectListCity =  BaseVirtual.getSelectListCity();
        for (Map.Entry<String, BaseVirtual.WeatherCity> entry : selectListCity.entrySet()) {

            TextView tv = new TextView(getContext());
            String temperature = String.format("%.0f°", entry.getValue().getTemperature());
            tv.setText(entry.getKey() + " " + temperature);
            tv.setTextSize(30);
            layoutView.addView(tv);
        }
*/

        List<HistorySearch> listHistory = educationSource.getHistorySearches();
        for (HistorySearch history : listHistory) {
            TextView tv = new TextView(getContext());
            tv.setText(String.format("%s %s %s°",history.date,history.city,history.temperature));
            tv.setTextSize(25);
            layoutView.addView(tv);
        }

    }
}
