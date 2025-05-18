package com.example.funenglish.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

@Entity(tableName = "lessons")
public class LessonEntity {
    @PrimaryKey
    @ColumnInfo(name = "id")
    public int id;

    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name = "content")
    public String content;

    @ColumnInfo(name = "level")
    public String level;
} 