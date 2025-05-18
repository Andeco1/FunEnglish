package com.example.funenglish.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

@Entity(tableName = "words")
public class WordEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "word")
    public String word;

    @ColumnInfo(name = "translation")
    public String translation;

    @ColumnInfo(name = "word_group")
    public String word_group;

    @ColumnInfo(name = "example")
    public String example;
} 