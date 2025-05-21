package com.example.englishfun.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.englishfun.database.entities.LessonEntity;

import java.util.List;

@Dao
public interface LessonDao {
    @Query("SELECT * FROM lesson")
    List<LessonEntity> getAll();

    @Query("SELECT * FROM lesson WHERE lesson_id = :id")
    LessonEntity getById(int id);

    @Query("SELECT * FROM lesson")
    LiveData<List<LessonEntity>> getAllLessons();

    @Query("SELECT * FROM lesson WHERE lesson_id = :id")
    LiveData<LessonEntity> getLesson(int id);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAll(List<LessonEntity> lessons);

}
