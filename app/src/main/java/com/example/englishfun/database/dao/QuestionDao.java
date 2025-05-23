package com.example.englishfun.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.englishfun.database.entities.QuestionEntity;

import java.util.List;

@Dao
public interface QuestionDao {
    @Query("SELECT * FROM question WHERE test_id = :testId")
    List<QuestionEntity> getQuestionsByTestId(int testId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<QuestionEntity> questions);
} 