package ru.geekbrains.android;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

// Фрагмент выбора города из списка
public class CitiesFragment extends Fragment {
    public static final String CITY_ID = "ru.geekbrains.android.CitiesFragment.city";
    public static final String NUM_CITY_ID = "ru.geekbrains.android.CitiesFragment.num";
    private String city="";
    private int num_city=0;

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
        LinearLayout layoutView = (LinearLayout) view;
        String[] cities = getResources().getStringArray(R.array.cities);

        // В этом цикле создаем элемент TextView,
        // заполняем его значениями,
        // и добавляем на экран.
        // Кроме того, создаем обработку касания на элемент
        for (int i = 0; i < cities.length; i++) {
            String city = cities[i];
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
                    editCity.setText(CitiesFragment.this.city);
                    save();

                }
            });
        }
    }

    private void save() {
        Bundle bundle = new Bundle();
        bundle.putString(CITY_ID, city);
        bundle.putInt(NUM_CITY_ID,num_city);
        setArguments(bundle);
    }


}
