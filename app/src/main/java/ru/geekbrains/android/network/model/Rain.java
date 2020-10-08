package ru.geekbrains.android.network.model;


import com.google.gson.annotations.SerializedName;

public class Rain {
    @SerializedName("1h")
    float hour;

    public float getHour() {
        return hour;
    }
}
