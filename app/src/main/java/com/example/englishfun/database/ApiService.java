package com.example.englishfun.database;



import com.example.englishfun.database.entities.LessonEntity;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

import java.util.List;

public interface ApiService {
    @GET("api/lessons")
    Call<List<LessonEntity>> fetchLessons(
            @Header("Authorization") String basicAuthHeader
    );
}
