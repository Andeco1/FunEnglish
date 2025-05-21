package com.example.englishfun.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.englishfun.database.dao.LessonDao;
import com.example.englishfun.database.entities.LessonEntity;

@Database(entities = {LessonEntity.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase{
    public abstract LessonDao lessonDao();
}
