package com.example.englishfun.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.englishfun.database.DataRepository;
import com.example.englishfun.database.models.Test;
import com.example.englishfun.databinding.FragmentDashboardBinding;
import com.example.englishfun.ui.adapters.TestAdapter;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment implements TestAdapter.OnTestClickListener {

    private FragmentDashboardBinding binding;
    private DataRepository repository;
    private TestAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                           ViewGroup container,
                           Bundle savedInstanceState) {
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Set up RecyclerView
        binding.recyclerViewTests.setLayoutManager(
                new LinearLayoutManager(requireContext())
        );
        List<Test> sampleTests = DataRepository.getInstance(getContext()).getTests();

        // Create and set adapter

        adapter = new TestAdapter(sampleTests, this);
        binding.recyclerViewTests.setAdapter(adapter);

        return root;
    }



    @Override
    public void onTestClick(Test test) {
        // TODO: Implement navigation to test detail screen
        // This will be implemented when we create the test detail screen
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}