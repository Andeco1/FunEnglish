package com.example.englishfun.ui.dashboard;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.englishfun.R;
import com.example.englishfun.database.DataRepository;
import com.example.englishfun.database.models.Test;
import com.example.englishfun.databinding.FragmentDashboardBinding;
import com.example.englishfun.ui.adapters.TestAdapter;

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
        List<Test> tests = DataRepository.getInstance(getContext()).getTests();

        // Create and set adapter
        adapter = new TestAdapter(tests, this);
        binding.recyclerViewTests.setAdapter(adapter);

        return root;
    }

    @Override
    public void onTestClick(Test test) {
        Bundle args = new Bundle();
        args.putInt("test_id", test.test_id);
        Log.d("QuestionOptions", String.valueOf(test.test_id));
        Navigation.findNavController(requireView())
            .navigate(R.id.action_navigation_dashboard_to_testFragment, args);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}