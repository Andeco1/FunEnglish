package com.example.funenglish.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.example.funenglish.database.entities.BookEntity;
import java.util.List;

@Dao
public interface BookDao {
    @Query("SELECT * FROM books")
    List<BookEntity> getAll();

    @Query("SELECT * FROM books WHERE id = :id")
    BookEntity getById(int id);

    @Query("SELECT * FROM books")
    LiveData<List<BookEntity>> getAllBooks();

    @Query("SELECT * FROM books WHERE id = :id")
    LiveData<BookEntity> getBook(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(BookEntity book);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<BookEntity> books);

    @Update
    void update(BookEntity book);

    @Delete
    void delete(BookEntity book);
} 