package com.example.englishfun.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.englishfun.database.dao.LessonDao;
import com.example.englishfun.database.dao.QuestionDao;
import com.example.englishfun.database.dao.QuestionOptionDao;
import com.example.englishfun.database.dao.TestDao;
import com.example.englishfun.database.entities.LessonEntity;
import com.example.englishfun.database.entities.QuestionEntity;
import com.example.englishfun.database.entities.QuestionOptionEntity;
import com.example.englishfun.database.entities.TestEntity;

@Database(entities = {LessonEntity.class, TestEntity.class, QuestionOptionEntity.class, QuestionEntity.class}, version = 4, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract LessonDao lessonDao();
    public abstract TestDao testDao();
    public abstract QuestionOptionDao questionOptionDao();
    public abstract QuestionDao questionDao();
}
