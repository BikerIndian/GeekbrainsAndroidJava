package ru.geekbrains.android.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index(value = {HistorySearch.CITY, HistorySearch.TEMPERATURE})})
public class HistorySearch {
    public final static String ID = "id";
    public final static String CITY = "city";
    public final static String TEMPERATURE = "temperature";
    //сохраняйте там город, число и температуру, выводите эти данные как элемент списка
    //в истории поиска.
    /*
    Используйте SharedPreferences для хранения последнего запроса погоды и загружайте погоду
по последнему запросу, когда пользователь открывает приложение.
     */

    // @PrimaryKey - указывает на ключевую запись,
    // autoGenerate = true - автоматическая генерация ключа
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID)
    public long id;

    // Город
    // @ColumnInfo - позволяет задавать параметры колонки в БД
    // name = "first_name" - такое будет имя колонки
    @ColumnInfo(name = CITY)
    public String city;

    // температура
    @ColumnInfo(name = TEMPERATURE)
    public int temperature;

    public String date;
    public String humidity;

}
