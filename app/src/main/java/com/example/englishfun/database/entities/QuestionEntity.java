package com.example.englishfun.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "question")
public class QuestionEntity {
    @PrimaryKey
    @ColumnInfo(name = "question_id")
    public int question_id;
    
    @ColumnInfo(name = "test_id")
    public int test_id;
    
    @ColumnInfo(name = "questionText")
    public String questionText;
} 