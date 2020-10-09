package ru.geekbrains.android.network.model;

public class Wind {
    private float speed;    // Максимальная скорость порывов ветра
    private int deg;        // Направление ветра 360

    public int getDeg() {
        return deg;
    }

    public void setDeg(int deg) {
        this.deg = deg;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }
}
