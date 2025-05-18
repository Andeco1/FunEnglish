package com.example.funenglish.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.example.funenglish.database.entities.WordEntity;
import java.util.List;

@Dao
public interface WordDao {
    @Query("SELECT * FROM words")
    List<WordEntity> getAll();

    @Query("SELECT * FROM words WHERE word_group = :groupName")
    LiveData<List<WordEntity>> getWordsByGroup(String groupName);

    @Query("SELECT * FROM words WHERE id = :id")
    LiveData<WordEntity> getWord(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(WordEntity word);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<WordEntity> words);

    @Update
    void update(WordEntity word);

    @Delete
    void delete(WordEntity word);

    @Query("SELECT DISTINCT word_group FROM words ORDER BY word_group ASC")
    List<String> getGroups();

    @Query("SELECT * FROM words WHERE word_group = :groupName")
    List<WordEntity> getByGroup(String groupName);
} 