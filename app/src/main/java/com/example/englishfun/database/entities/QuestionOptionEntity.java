package com.example.englishfun.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "question_option")
public class QuestionOptionEntity {
    @PrimaryKey
    @ColumnInfo(name = "option_id")
    public int optionId;
    
    @ColumnInfo(name = "question_id")
    public int questionId;
    
    @ColumnInfo(name = "option_text")
    public String optionText;
    
    @ColumnInfo(name = "is_correct")
    public boolean isCorrect;
} 