package com.example.englishfun.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.englishfun.database.entities.LessonEntity;
import com.example.englishfun.database.entities.TestEntity;

import java.util.List;

@Dao
public interface TestDao {
    @Query("SELECT * FROM test")
    List<TestEntity> getAll();

    @Query("SELECT * FROM test WHERE test_id = :id")
    TestEntity getById(int id);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAll(List<TestEntity> tests);

    @Query("UPDATE test SET score = :score, passedAt = :passedAt WHERE test_id = :testId")
    void updateTestScore(int testId, int score, String passedAt);
}
