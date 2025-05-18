package com.example.funenglish.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.funenglish.R;
import com.example.funenglish.adapters.TestAdapter;
import com.example.funenglish.database.DataRepository;

public class TestsFragment extends Fragment {
    private RecyclerView recyclerView;
    private TestAdapter adapter;
    private DataRepository repository;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tests, container, false);
        repository = DataRepository.getInstance(requireContext());
        recyclerView = view.findViewById(R.id.rvTests);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new TestAdapter(repository.getTests(), (TestAdapter.OnClick) this);
        recyclerView.setAdapter(adapter);
        return view;
    }

    public void onTestClick(int testId) {
        // TODO: Implement test start logic
    }
}
