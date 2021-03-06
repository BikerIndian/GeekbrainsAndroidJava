package ru.geekbrains.android.network.retorfit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.geekbrains.android.network.model.WeatherRequest;
public interface OpenWeather {
    @GET("data/2.5/weather")
    Call<WeatherRequest> loadWeather(@Query("q") String cityCountry, @Query("appid") String keyApi);

    //api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&appid={API key}
    @GET("data/2.5/weather")
    Call<WeatherRequest> loadWeatherLatLon(@Query("lat") double lat,@Query("lon") double lon, @Query("appid") String keyApi);
}
