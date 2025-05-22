package com.example.englishfun.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "test")
public class TestEntity {
    @PrimaryKey
    @ColumnInfo(name = "test_id")
    public int test_id;
    @ColumnInfo(name = "title")
    public String title;
    @ColumnInfo(name = "levelCode")
    public String level;
    @ColumnInfo(name = "score")
    public int score;
    @ColumnInfo(name = "passedAt")
    public String passedAt;

}
