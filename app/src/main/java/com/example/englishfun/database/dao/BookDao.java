package com.example.englishfun.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.englishfun.database.entities.BookEntity;

import java.util.List;

@Dao
public interface BookDao {
    @Query("SELECT * FROM BookEntity")
    LiveData<List<BookEntity>> getAllBooks();

    @Insert
    void insert(BookEntity bookEntity);

    @Query("DELETE FROM BookEntity WHERE id = :id")
    void deleteById(int id);
} 