package ru.geekbrains.android.network.retorfit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.geekbrains.android.BuildConfig;
import ru.geekbrains.android.MainActivity;
import ru.geekbrains.android.network.model.WeatherRequest;

public class RetorfitUtil {
    private OpenWeather openWeather;
    private MainActivity mainActivity;
    public RetorfitUtil(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        initRetorfit();
    }
    private void initRetorfit() {
        Retrofit retrofit;
        retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/") //Базовая часть адреса
                .addConverterFactory(GsonConverterFactory.create()) //Конвертер, необходимый для преобразования JSON'а в объекты
                .build();
        openWeather = retrofit.create(OpenWeather.class); //Создаем объект, при помощи которого будем выполнять запросы
    }

    public void getCityWeather(String city) {
        openWeather.loadWeather(city+",RU", BuildConfig.WEATHER_API_KEY)
                .enqueue(new Callback<WeatherRequest>() {
                    @Override
                    public void onResponse(Call<WeatherRequest> call, Response<WeatherRequest> response) {
                        if (response.body() != null) {
                            // Проверка На тот случай, если Activity будет уничтожена.
                            if (mainActivity != null) {
                                mainActivity.updateCityWeather(response.body());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<WeatherRequest> call, Throwable t) {
                      //  Log.e("WEATHER", "Error");
                    }
                });

    }
}
