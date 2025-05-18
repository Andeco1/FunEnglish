package com.example.funenglish.database;

import android.content.Context;
import androidx.room.Room;
import com.example.funenglish.database.entities.*;
import com.example.funenglish.models.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DataRepository {
    private static volatile DataRepository INSTANCE;
    private final AppDatabase db;
    private final ApiService api;

    private DataRepository(Context context) {
        db = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "english_app.db")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
        api = RetrofitClient.getInstance().create(ApiService.class);
    }

    public static DataRepository getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (DataRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new DataRepository(context);
                }
            }
        }
        return INSTANCE;
    }

    public ApiService getApiService() {
        return api;
    }

    public AppDatabase getDatabase() {
        return db;
    }

    public List<Lesson> getLessons() {
        List<LessonEntity> entities = db.lessonDao().getAll();
        return entities.stream()
                .map(entity -> {
                    Lesson lesson = new Lesson();
                    lesson.id = entity.id;
                    lesson.title = entity.title;
                    lesson.content = entity.content;
                    lesson.level = entity.level;
                    return lesson;
                })
                .collect(Collectors.toList());
    }

    public Lesson getLesson(int id) {
        LessonEntity entity = db.lessonDao().getById(id);
        if (entity != null) {
            Lesson lesson = new Lesson();
            lesson.id = entity.id;
            lesson.title = entity.title;
            lesson.content = entity.content;
            lesson.level = entity.level;
            return lesson;
        }
        return null;
    }

    public List<Test> getTests() {
        List<TestEntity> entities = db.testDao().getAll();
        return entities.stream()
                .map(entity -> {
                    Test test = new Test();
                    test.id = entity.id;
                    test.title = entity.title;
                    test.description = entity.description;
                    test.level = entity.level;
                    test.questionsCount = entity.questionsCount;
                    return test;
                })
                .collect(Collectors.toList());
    }

    public List<Book> getBooks() {
        List<BookEntity> entities = db.bookDao().getAll();
        return entities.stream()
                .map(entity -> {
                    Book book = new Book();
                    book.id = entity.id;
                    book.title = entity.title;
                    book.author = entity.author;
                    book.text = entity.text;
                    book.level = entity.level;
                    return book;
                })
                .collect(Collectors.toList());
    }

    public Book getBook(int id) {
        BookEntity entity = db.bookDao().getById(id);
        if (entity != null) {
            Book book = new Book();
            book.id = entity.id;
            book.title = entity.title;
            book.author = entity.author;
            book.text = entity.text;
            book.level = entity.level;
            return book;
        }
        return null;
    }

    public List<String> getWordGroupNames() {
        return db.wordDao().getGroups();
    }

    public List<Word> getWords(String group) {
        List<WordEntity> entities = db.wordDao().getByGroup(group);
        return entities.stream()
                .map(entity -> {
                    Word word = new Word();
                    word.id = entity.id;
                    word.word = entity.word;
                    word.translation = entity.translation;
                    word.word_group = entity.word_group;
                    word.example = entity.example;
                    return word;
                })
                .collect(Collectors.toList());
    }
}