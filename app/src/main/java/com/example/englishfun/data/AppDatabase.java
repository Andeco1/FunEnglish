package com.example.englishfun.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.englishfun.data.dao.BookDao;
import com.example.englishfun.data.entity.Book;

@Database(entities = {Book.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase INSTANCE;
    public abstract BookDao bookDao();

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "english_fun_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
} 