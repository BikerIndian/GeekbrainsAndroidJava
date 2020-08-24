package ru.geekbrains.android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private  SelectCity selectCity;
    TextView city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        city = findViewById(R.id.main_city);
       // Log.d("myLogs", "onCreate");
        setDefaultTemp();
        setBtn();
        this.selectCity = new SelectCity();
    }


    @Override
    protected void onActivityResult( int requestCode, int resultCode, Intent data) {

        if (requestCode != Keys.SELECT_CITY_REQUEST ) {
            super .onActivityResult(requestCode, resultCode, data);
            return ;
        }

        if (resultCode == RESULT_OK) {
            this.selectCity =  (SelectCity) data.getSerializableExtra(Keys.SELECT_CITY);
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
                intent.putExtra(Keys.SELECT_CITY,selectCity);

                startActivityForResult(intent,Keys.SELECT_CITY_REQUEST);
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