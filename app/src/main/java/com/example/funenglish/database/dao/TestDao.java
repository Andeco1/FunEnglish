package com.example.funenglish.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.example.funenglish.database.entities.TestEntity;
import java.util.List;

@Dao
public interface TestDao {
    @Query("SELECT * FROM tests")
    List<TestEntity> getAll();

    @Query("SELECT * FROM tests WHERE id = :id")
    TestEntity getById(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<TestEntity> tests);

    @Query("SELECT * FROM tests")
    LiveData<List<TestEntity>> getAllTests();

    @Query("SELECT * FROM tests WHERE id = :id")
    LiveData<TestEntity> getTest(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(TestEntity test);

    @Update
    void update(TestEntity test);

    @Delete
    void delete(TestEntity test);
} 