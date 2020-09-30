package ru.geekbrains.android.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {HistorySearch.class }, version = 1 )
public abstract class EducationDatabase extends RoomDatabase {
    public abstract EducationDao getEducationDao();
}
