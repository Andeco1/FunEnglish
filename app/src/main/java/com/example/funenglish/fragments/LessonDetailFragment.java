package com.example.funenglish.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.example.funenglish.R;
import com.example.funenglish.database.DataRepository;

public class LessonDetailFragment extends Fragment {
    private int id;
    private DataRepository repository;

    public static LessonDetailFragment newInstance(int id) {
        LessonDetailFragment fragment = new LessonDetailFragment();
        fragment.id = id;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater i, ViewGroup c, Bundle b) {
        View v = i.inflate(R.layout.fragment_lesson_detail, c, false);
        repository = DataRepository.getInstance(requireContext());
        TextView tv = v.findViewById(R.id.tvLessonContent);
        tv.setText(repository.getLesson(id).content);
        v.findViewById(R.id.btnCloseLesson).setOnClickListener(x -> getParentFragmentManager().popBackStack());
        return v;
    }
}