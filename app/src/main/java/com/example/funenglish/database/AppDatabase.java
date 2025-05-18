package com.example.funenglish.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import com.example.funenglish.database.dao.*;
import com.example.funenglish.database.entities.*;

@Database(entities = {LessonEntity.class, TestEntity.class, BookEntity.class, WordEntity.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract LessonDao lessonDao();
    public abstract TestDao testDao();
    public abstract BookDao bookDao();
    public abstract WordDao wordDao();
}