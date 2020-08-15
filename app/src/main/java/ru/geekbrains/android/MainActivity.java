package ru.geekbrains.android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setDefaultTemp();
        setBtn();

    }

    private void setBtn() {
        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonClickedSelectCity();
            }
        });
    }

    private void onButtonClickedSelectCity() {
        startActivity(new Intent(this, SelectCityActivity.class));
    }


    private void setDefaultTemp() {
        TextView textTemp = findViewById(R.id.text_temp);
        textTemp.setText("24");
    }
}