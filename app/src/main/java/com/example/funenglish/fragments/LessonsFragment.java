package com.example.funenglish.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.funenglish.R;
import com.example.funenglish.adapters.LessonAdapter;
import com.example.funenglish.database.DataRepository;
import com.example.funenglish.database.entities.LessonEntity;
import com.example.funenglish.models.Lesson;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LessonsFragment extends Fragment implements LessonAdapter.OnItemClick {
    private RecyclerView recyclerView;
    private LessonAdapter adapter;
    private DataRepository repository;
    private ProgressBar progressBar;
    private TextView tvLessonsTitle;
    private List<Lesson> lessons = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lessons, container, false);
        
        // Initialize views
        repository = DataRepository.getInstance(requireContext());
        recyclerView = view.findViewById(R.id.recyclerView);
        progressBar = view.findViewById(R.id.progressBar);
        tvLessonsTitle = view.findViewById(R.id.tvLessonsTitle);
        
        // Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new LessonAdapter(lessons, this);
        recyclerView.setAdapter(adapter);
        
        // Load lessons from server
        loadLessonsFromServer();
        
        return view;
    }

    private void loadLessonsFromServer() {
        // Show loading state
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        
        // Make API call
        repository.getApiService().fetchLessons().enqueue(new Callback<List<LessonEntity>>() {
            @Override
            public void onResponse(Call<List<LessonEntity>> call, Response<List<LessonEntity>> response) {
                progressBar.setVisibility(View.GONE);
                
                if (response.isSuccessful() && response.body() != null) {
                    // Convert entities to models
                    lessons.clear();
                    for (LessonEntity entity : response.body()) {
                        Lesson lesson = new Lesson();
                        lesson.id = entity.id;
                        lesson.title = entity.title;
                        lesson.content = entity.content;
                        lesson.level = entity.level;
                        lessons.add(lesson);
                    }
                    
                    // Update UI
                    adapter.notifyDataSetChanged();
                    recyclerView.setVisibility(View.VISIBLE);
                    
                    // Save to local database
                    repository.getDatabase().lessonDao().insertAll(response.body());
                } else {
                    // Handle error response
                    showError("Failed to load lessons");
                    // Try to load from local database
                    loadLessonsFromLocal();
                }
            }

            @Override
            public void onFailure(Call<List<LessonEntity>> call, Throwable t) {
                Log.d("d", t.getMessage());
                progressBar.setVisibility(View.GONE);
                showError("Network error: " + t.getMessage());
                // Try to load from local database
                loadLessonsFromLocal();
            }
        });
    }

    private void loadLessonsFromLocal() {
        List<Lesson> localLessons = repository.getLessons();
        if (!localLessons.isEmpty()) {
            lessons.clear();
            lessons.addAll(localLessons);
            adapter.notifyDataSetChanged();
            recyclerView.setVisibility(View.VISIBLE);
            Toast.makeText(getContext(), "Showing cached lessons", Toast.LENGTH_SHORT).show();
        } else {
            recyclerView.setVisibility(View.GONE);
            tvLessonsTitle.setText("No lessons available");
        }
    }

    private void showError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(Lesson lesson) {
        getParentFragmentManager().beginTransaction()
            .replace(R.id.fragment_container, LessonDetailFragment.newInstance(lesson.id))
            .addToBackStack(null)
            .commit();
    }
}