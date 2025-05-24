package com.example.englishfun.ui.login;

import android.content.Context;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.englishfun.MainActivity;
import com.example.englishfun.R;
import com.example.englishfun.database.DataRepository;
import com.example.englishfun.database.entities.LessonEntity;
import com.example.englishfun.database.entities.QuestionEntity;
import com.example.englishfun.database.entities.TestEntity;
import com.example.englishfun.database.entities.QuestionOptionEntity;
import com.example.englishfun.database.models.Lesson;
import com.example.englishfun.databinding.FragmentLoginBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment {
    private FragmentLoginBinding binding;
    private static final String LOGIN_URL = "http://192.168.0.105:9090/api/users/login"; // Replace with your actual server URL
    private static final String REGISTER_URL = "http://192.168.0.105:9090/api/users/register"; // Replace with your actual server URL
    private DataRepository repository;
    private List<Lesson> lessons = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        repository = DataRepository.getInstance(getContext());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainActivity mainActivity = (MainActivity) requireActivity();
        if (mainActivity.getAuthToken() != null) {
            Navigation.findNavController(view)
                    .navigate(R.id.action_navigation_login_to_navigation_home);
            return;
        }

        binding.loginButton.setOnClickListener(v -> {
            String username = binding.usernameEditText.getText().toString().trim();
            String password = binding.passwordEditText.getText().toString().trim();

            if (validateInput(username, password)) {
                performLogin(username, password);
            }
        });

        binding.registerButton.setOnClickListener(v ->{
            String username = binding.usernameEditText.getText().toString().trim();
            String password = binding.passwordEditText.getText().toString().trim();

            if (validateInput(username, password)) {
                performRegister(username, password);
            }
        });
    }

    private boolean validateInput(String username, String password) {
        if (username.isEmpty()) {
            binding.usernameLayout.setError("Требуется логин");
            return false;
        }
        binding.usernameLayout.setError(null);

        if (password.isEmpty()) {
            binding.passwordLayout.setError("Требуется пароль");
            return false;
        }
        binding.passwordLayout.setError(null);

        return true;
    }
    private void performRegister(String username, String password) {

        binding.registerButton.setEnabled(false);

        RequestQueue queue = Volley.newRequestQueue(requireContext());

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("login", username);
            requestBody.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
            binding.registerButton.setEnabled(true);
            showError("Не удалось сформировать запрос");
            return;
        }

        JsonObjectRequest jsonRequest = new JsonObjectRequest(
                Request.Method.POST,
                REGISTER_URL,
                requestBody,
                response -> {
                    binding.registerButton.setEnabled(true);
                    try {
                        boolean success = response.optBoolean("success", false);
                        String message = response.optString("message", "");
                        if (success) {
                            long userId = response.optLong("user_id");
                            Toast.makeText(getContext(), "Зарегистрирован: " + message, Toast.LENGTH_SHORT).show();

                            ((MainActivity) requireActivity()).saveUserInfo(userId);

                        } else {
                            showError("Регистрация не удалась: " + message);
                        }
                    } catch (Exception e) {
                        showError("Неверный ответ сервера");
                    }
                },
                error -> {
                    binding.registerButton.setEnabled(true);
                    showError("Ошибка сети: " + error.getMessage());
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {

                return Collections.emptyMap();
            }
        };

        queue.add(jsonRequest);
    }



    private void performLogin(String username, String password) {

        binding.loginButton.setEnabled(false);

        RequestQueue queue = Volley.newRequestQueue(requireContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, LOGIN_URL,
                response -> {
                    binding.loginButton.setEnabled(true);
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        if (jsonResponse.has("token")) {

                            String token = jsonResponse.getString("token");
                            MainActivity mainActivity = (MainActivity) requireActivity();
                            mainActivity.saveAuthToken(token);
                            Long userId = jsonResponse.getLong("user_id");
                            mainActivity.saveUserInfo(userId);

                            String credentials = username + ":" + password;
                            String auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                            loadLessonsFromServer(auth);
                            loadTestsFromServer(auth, userId);
                            loadQuestionsFromServer(auth);
                            loadQuestionOptionsFromServer(auth);

                            Navigation.findNavController(requireView())
                                    .navigate(R.id.action_navigation_login_to_navigation_home);
                        } else {
                            showError("Неверный ответ сервера");
                        }
                    } catch (JSONException e) {
                        showError("Ошибка распознования ответа с сервера");
                    }
                },
                error -> {
                    binding.loginButton.setEnabled(true);
                    showError("Login failed: " + error.getMessage());
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                String credentials = username + ":" + password;
                String auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                headers.put("Authorization", auth);
                return headers;
            }


        };

        queue.add(stringRequest);
    }

    private void loadLessonsFromServer(String auth) {
        repository.getApiService().fetchLessons(auth).enqueue(new Callback<List<LessonEntity>>() {
            @Override
            public void onResponse(Call<List<LessonEntity>> call, Response<List<LessonEntity>> response) {
                if(response.isSuccessful() && response.body() != null){
                    repository.getDatabase().lessonDao().insertAll(response.body());
                    Log.d("TESTTEST","lessons downloaded");
                    showError("всё полуилось");
                }
                else {
                    showError("не получилось скачать уроки");
                }
            }

            @Override
            public void onFailure(Call<List<LessonEntity>> call, Throwable t) {
                showError("Failure");
            }
        });

    }
    private void loadTestsFromServer(String auth, Long userId) {

        repository.getApiService().fetchTests(userId,auth).enqueue(new Callback<List<TestEntity>>() {
            @Override
            public void onResponse(Call<List<TestEntity>> call, Response<List<TestEntity>> response) {
                if(response.isSuccessful() && response.body() != null){
                    repository.getDatabase().testDao().insertAll(response.body());
                    showError("всё полуилось");
                }
                else {
                    showError("не получилось скачать уроки");
                }
            }

            @Override
            public void onFailure(Call<List<TestEntity>> call, Throwable t) {
                showError("Failure");
            }
        });

    }

    private void loadQuestionOptionsFromServer(String auth) {
        repository.getApiService().fetchQuestionOptions(auth).enqueue(new Callback<List<QuestionOptionEntity>>() {
            @Override
            public void onResponse(Call<List<QuestionOptionEntity>> call, Response<List<QuestionOptionEntity>> response) {
                if(response.isSuccessful() && response.body() != null) {
                    repository.getDatabase().questionOptionDao().insertAll(response.body());
                    Log.d("TESTTEST", response.toString());
                    Log.d("QuestionOptions", "варианты вопросов скачаны");
                }
            }

            @Override
            public void onFailure(Call<List<QuestionOptionEntity>> call, Throwable t) {
                showError("Ошибка: " + t.getMessage());
            }
        });
    }

    private void loadQuestionsFromServer(String auth) {
        repository.getApiService().fetchQuestions(auth).enqueue(new Callback<List<QuestionEntity>>() {
            @Override
            public void onResponse(Call<List<QuestionEntity>> call, Response<List<QuestionEntity>> response) {
                if(response.isSuccessful() && response.body() != null) {
                    repository.getDatabase().questionDao().insertAll(response.body());
                    Log.d("TESTTEST", response.body().toString());
                    Log.d("QuestionOptions", "Вопросы скачаны");
                }
            }

            @Override
            public void onFailure(Call<List<QuestionEntity>> call, Throwable t) {
                showError("Ошибка: " + t.getMessage());
            }
        });
    }

    private void showError(String message) {
        if (!isAdded()) return;

        Context ctx = getContext();
        if (ctx != null) {
            Toast.makeText(ctx, message, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
} 