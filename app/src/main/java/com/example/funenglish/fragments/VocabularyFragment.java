package com.example.funenglish.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.funenglish.R;
import com.example.funenglish.adapters.VocabularyAdapter;
import com.example.funenglish.database.DataRepository;
import com.example.funenglish.models.Word;
import java.util.ArrayList;
import java.util.List;

public class VocabularyFragment extends Fragment implements VocabularyAdapter.OnItemClick {
    private RecyclerView recyclerView;
    private Spinner groupSpinner;
    private VocabularyAdapter adapter;
    private DataRepository repository;
    private List<Word> words = new ArrayList<>();
    private Button btnTest;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vocabulary, container, false);
        
        recyclerView = view.findViewById(R.id.rvWords);
        groupSpinner = view.findViewById(R.id.spinnerGroups);
        btnTest = view.findViewById(R.id.btnTestVocab);
        
        repository = DataRepository.getInstance(requireContext());
        
        setupRecyclerView();
        setupSpinner();
        setupButton();
        
        return view;
    }

    private void setupRecyclerView() {
        adapter = new VocabularyAdapter(words, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    private void setupSpinner() {
        List<String> groups = repository.getWordGroupNames();
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                groups
        );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        groupSpinner.setAdapter(spinnerAdapter);
        
        groupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedGroup = groups.get(position);
                words.clear();
                words.addAll(repository.getWords(selectedGroup));
                adapter.updateWords(words);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    private void setupButton() {
        btnTest.setOnClickListener(view -> {
            // TODO: Implement vocabulary test
        });
    }

    @Override
    public void onClick(Word word) {
        // Show word details
    }
}