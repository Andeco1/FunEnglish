package com.example.funenglish.database;

import com.example.funenglish.database.entities.LessonEntity;
import com.example.funenglish.database.entities.TestEntity;
import com.example.funenglish.database.entities.BookEntity;
import com.example.funenglish.database.entities.WordEntity;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import java.util.List;

public interface ApiService {
    @GET("api/lessons")
    Call<List<LessonEntity>> fetchLessons();

    @GET("tests")
    Call<List<TestEntity>> fetchTests();

    @GET("books")
    Call<List<BookEntity>> fetchBooks();

    @GET("words/{group}")
    Call<List<WordEntity>> fetchWords(@Path("group") String group);
}
