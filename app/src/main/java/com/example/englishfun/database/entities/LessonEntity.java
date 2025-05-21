package com.example.englishfun.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "lesson")
public class LessonEntity {
    @PrimaryKey
    @ColumnInfo(name = "lesson_id")
    public int lesson_id;
    @ColumnInfo(name = "title")
    public String title;
    @ColumnInfo(name = "content")
    public String content;
    @ColumnInfo(name = "level")
    public String level;

}
