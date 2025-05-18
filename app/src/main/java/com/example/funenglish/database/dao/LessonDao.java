package com.example.funenglish.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.example.funenglish.database.entities.LessonEntity;
import java.util.List;

@Dao
public interface LessonDao {
    @Query("SELECT * FROM lessons")
    List<LessonEntity> getAll();

    @Query("SELECT * FROM lessons WHERE id = :id")
    LessonEntity getById(int id);

    @Query("SELECT * FROM lessons")
    LiveData<List<LessonEntity>> getAllLessons();

    @Query("SELECT * FROM lessons WHERE id = :id")
    LiveData<LessonEntity> getLesson(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(LessonEntity lesson);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<LessonEntity> lessons);

    @Update
    void update(LessonEntity lesson);

    @Delete
    void delete(LessonEntity lesson);
} 