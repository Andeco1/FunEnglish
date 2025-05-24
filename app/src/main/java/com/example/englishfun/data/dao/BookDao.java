package com.example.englishfun.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.englishfun.data.entity.Book;

import java.util.List;

@Dao
public interface BookDao {
    @Query("SELECT * FROM books")
    LiveData<List<Book>> getAllBooks();

    @Insert
    void insert(Book book);

    @Query("DELETE FROM books WHERE id = :id")
    void deleteById(int id);
} 