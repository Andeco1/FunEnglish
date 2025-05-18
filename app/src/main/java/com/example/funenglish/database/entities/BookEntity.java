package com.example.funenglish.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

@Entity(tableName = "books")
public class BookEntity {
    @PrimaryKey
    @ColumnInfo(name = "id")
    public int id;

    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name = "author")
    public String author;

    @ColumnInfo(name = "text")
    public String text;

    @ColumnInfo(name = "level")
    public String level;
} 