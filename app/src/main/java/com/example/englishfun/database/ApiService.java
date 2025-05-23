package com.example.englishfun.database;

import com.example.englishfun.database.entities.LessonEntity;
import com.example.englishfun.database.entities.QuestionEntity;
import com.example.englishfun.database.entities.QuestionOptionEntity;
import com.example.englishfun.database.entities.TestEntity;
import com.example.englishfun.database.models.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

import java.util.List;

public interface ApiService {
    @GET("api/lessons")
    Call<List<LessonEntity>> fetchLessons(
            @Header("Authorization") String basicAuthHeader
    );
    @GET("api/progress/user/{userId}")
    Call<List<TestEntity>> fetchTests(
            @Path("userId") long userId,
            @Header("Authorization") String basicAuthHeader

    );

    @GET("api/questions")
    Call<List<QuestionEntity>> fetchQuestions(
            @Header("Authorization") String basicAuthHeader
    );

    @GET("api/question-options")
    Call<List<QuestionOptionEntity>> fetchQuestionOptions(
            @Header("Authorization") String basicAuthHeader
    );

    @Headers("Content-Type: application/json")
    @POST("/api/users/register")
    Call<Void> registerUser(@Body User user);
}
