package com.example.englishfun.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.englishfun.database.dao.LessonDao;
import com.example.englishfun.database.dao.TestDao;
import com.example.englishfun.database.entities.LessonEntity;
import com.example.englishfun.database.entities.TestEntity;

@Database(entities = {LessonEntity.class, TestEntity.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase{
    public abstract LessonDao lessonDao();
    public abstract TestDao testDao();
}
