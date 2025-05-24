package com.example.englishfun.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "question_option")
public class QuestionOptionEntity {
    @PrimaryKey
    @ColumnInfo(name = "optionId")
    public int optionId;
    
    @ColumnInfo(name = "questionId")
    public int questionId;
    
    @ColumnInfo(name = "optionText")
    public String optionText;
    
    @ColumnInfo(name = "isCorrect")
    public boolean isCorrect;
} 