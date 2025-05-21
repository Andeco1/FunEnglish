package com.example.englishfun.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.englishfun.R;
import com.example.englishfun.database.DataRepository;
import com.example.englishfun.databinding.FragmentHomeBinding;
import com.example.englishfun.database.models.Lesson;
import com.example.englishfun.ui.adapters.LessonAdapter;

import java.util.List;

public class HomeFragment extends Fragment implements LessonAdapter.OnLessonClickListener {

    private FragmentHomeBinding binding;
    private LessonAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        // Инфлейтим binding
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Настраиваем RecyclerView
        binding.recyclerViewLessons.setLayoutManager(
                new LinearLayoutManager(requireContext())
        );

        // Подготавливаем данные прямо во фрагменте
        List<Lesson> sampleLessons = DataRepository.getInstance(getContext()).getLessons();

        // ... при необходимости добавьте другие уроки

        // Создаём и устанавливаем адаптер
        adapter = new LessonAdapter(sampleLessons, this);
        binding.recyclerViewLessons.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        return root;
    }

    @Override
    public void onLessonClick(Lesson lesson) {
        Bundle args = new Bundle();
        args.putInt("lesson_id", lesson.lesson_id);
        Navigation.findNavController(requireView())
                .navigate(R.id.action_navigation_home_to_lessonDetailFragment, args);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
