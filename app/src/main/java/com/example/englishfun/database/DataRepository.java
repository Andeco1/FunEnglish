package com.example.englishfun.database;

import android.content.Context;

import androidx.room.Room;

import com.example.englishfun.database.entities.LessonEntity;
import com.example.englishfun.database.models.Lesson;

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
                    lesson.lesson_id = entity.lesson_id;
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
            lesson.lesson_id = entity.lesson_id;
            lesson.title = entity.title;
            lesson.content = entity.content;
            lesson.level = entity.level;
            return lesson;
        }
        return null;
    }
}
