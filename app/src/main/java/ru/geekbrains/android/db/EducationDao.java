package ru.geekbrains.android.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

// В этом классе описывается, как будет происходить обработка данных
@Dao
public interface EducationDao {
    // Метод для добавления в базу данных
    // @Insert - признак добавления
    // onConflict - что делать, если такая запись уже есть
    // В данном случае просто заменим её
    @Insert(onConflict = OnConflictStrategy.REPLACE )
    void insert(HistorySearch historySearch);

    // Метод для замены
    @Update
    void update(HistorySearch historySearch);

    // Удалим одну запись
    @Delete
    void delete(HistorySearch historySearch);

    // Удалим данные
    @Query("DELETE FROM historySearch WHERE id = :id")
    void deteleById(long id);

    // Заберем данные
    @Query("SELECT * FROM historySearch")
    List<HistorySearch> getAll();

    //Получить количество записей в таблице
    @Query("SELECT COUNT() FROM historySearch")
    long getCount();

    // Очистить таблицу
    @Query("DELETE FROM historySearch")
    void clear();
}
