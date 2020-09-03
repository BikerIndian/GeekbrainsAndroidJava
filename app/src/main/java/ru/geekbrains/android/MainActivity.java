package ru.geekbrains.android;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.geekbrains.android.listDayOfWeek.DataDayOfWeek;
import ru.geekbrains.android.listDayOfWeek.DayOfWeek;
import ru.geekbrains.android.listDayOfWeek.ListDayOfWeekAdapter;
import ru.geekbrains.android.selectCity.SelectCity;
import ru.geekbrains.android.selectCity.SelectCityActivity;

public class MainActivity extends AppCompatActivity {

    private SelectCity selectCity;
    TextView city;

    // Адаптер для списка погоды на неделю
    final ListDayOfWeekAdapter adapter = new ListDayOfWeekAdapter();

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        city = findViewById(R.id.main_city);
       // Log.d("myLogs", "onCreate");
        setDefaultTemp();
        setBtn();
        this.selectCity = new SelectCity();

        initRecyclerView();

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initRecyclerView() {
        final RecyclerView recyclerView = findViewById(R.id.recyclerView);
        // Эта установка служит для повышения производительности системы
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);

        updateCities(new DataDayOfWeek().getListData());

        // Декоратор
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        itemDecoration.setDrawable(getDrawable(R.drawable.separator));
        recyclerView.addItemDecoration(itemDecoration);

    }
    public void updateCities(List<DayOfWeek> listDayOfWeek) {
        adapter.update(listDayOfWeek);
    }


    @Override
    protected void onActivityResult( int requestCode, int resultCode, Intent data) {

        if (requestCode != SelectCity.SELECT_CITY_REQUEST ) {
            super .onActivityResult(requestCode, resultCode, data);
            return ;
        }

        if (resultCode == RESULT_OK) {
            this.selectCity =  (SelectCity) data.getSerializableExtra(SelectCity.SELECT_CITY);
            this.city.setText(this.selectCity.getCity());
        }

    }

    private void setBtn() {
        onButtonClickedSelectCity();
        onButtonClickedLifeCycle();
    }

    //SelectCity
    private void onButtonClickedSelectCity() {
        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SelectCityActivity.class);
                intent.putExtra(SelectCity.SELECT_CITY,selectCity);

                startActivityForResult(intent,SelectCity.SELECT_CITY_REQUEST);
            }
        });

    }

    //LifeCycle
    private void onButtonClickedLifeCycle() {
        findViewById(R.id.button_LifeCycle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LifeCycle.class));
            }
        });
    }

    private void setDefaultTemp() {
        TextView textTemp = findViewById(R.id.text_temp);
        textTemp.setText("24");
    }

}