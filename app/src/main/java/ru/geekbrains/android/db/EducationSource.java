package ru.geekbrains.android.db;

import java.util.List;

// Вспомогательный класс, развязывающий
// зависимость между Room и RecyclerView
public class EducationSource {

    private final EducationDao educationDao;

    // Буфер с данными, сюда будем подкачивать данные из БД
    private List<HistorySearch> historySearches;

    public EducationSource(EducationDao educationDao){
        this.educationDao = educationDao;
    }

    // Получить весь список
    public List<HistorySearch> getHistorySearches(){
        // Если объекты еще не загружены, то загружаем их.
        // Сделано для того, чтобы не делать запросы в БД каждый раз
        if (historySearches == null){
            load();
        }
        return historySearches;
    }

    // Загрузить в буфер
    public void load(){
        historySearches = educationDao.getAll();
    }

    // Получить количество записей
    public long getCount(){
        return educationDao.getCount();
    }

    // Добавить
    public void add(HistorySearch historySearch){
        educationDao.insert(historySearch);

        // После изменения БД надо перечитать буфер
        load();
    }



    // Заменить
    public void update(HistorySearch historySearch){
        educationDao.update(historySearch);
        load();
    }

    // Удалить из базы
    public void remove(long id){
        educationDao.deteleById(id);
        load();
    }

}
