package com.example.englishfun.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.englishfun.database.entities.QuestionOptionEntity;

import java.util.List;

@Dao
public interface QuestionOptionDao {
    @Query("SELECT * FROM question_option")
    List<QuestionOptionEntity> getAll();

    @Query("SELECT * FROM question_option WHERE question_id = :questionId")
    List<QuestionOptionEntity> getByQuestionId(int questionId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<QuestionOptionEntity> options);
} 