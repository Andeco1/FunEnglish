package com.example.englishfun.ui.lesson;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.englishfun.database.models.Lesson;
import com.example.englishfun.databinding.FragmentLessonDetailBinding;

public class LessonDetailFragment extends Fragment {
    private FragmentLessonDetailBinding binding;
    private static final String ARG_LESSON_ID = "lesson_id";

    public static LessonDetailFragment newInstance(int lessonId) {
        LessonDetailFragment fragment = new LessonDetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_LESSON_ID, lessonId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
        binding = FragmentLessonDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            int lessonId = getArguments().getInt(ARG_LESSON_ID);
            // Get lesson from repository
            Lesson lesson = com.example.englishfun.database.DataRepository.getInstance(requireContext())
                    .getLesson(lessonId);

            if (lesson != null) {
                binding.textViewLessonTitle.setText(lesson.getTitle());
                binding.textViewLessonLevel.setText(lesson.getLevel());
                binding.textViewLessonContent.setText(lesson.content);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
} 