package com.example.funenglish.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.example.funenglish.R;
import com.example.funenglish.database.DataRepository;

public class ProfileFragment extends Fragment {
    private DataRepository repository;
    private TextView tvName;
    private TextView tvLevel;
    private TextView tvProgress;
    private ProgressBar progressBar;
    private Button btnEditProfile;
    private Button btnSettings;
    private Button btnLogout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        repository = DataRepository.getInstance(requireContext());
        
        // Initialize views
        tvName = view.findViewById(R.id.tvName);
        tvLevel = view.findViewById(R.id.tvLevel);
        tvProgress = view.findViewById(R.id.tvProgress);
        progressBar = view.findViewById(R.id.progressBar);
        btnEditProfile = view.findViewById(R.id.btnEditProfile);
        btnSettings = view.findViewById(R.id.btnSettings);
        btnLogout = view.findViewById(R.id.btnLogout);

        // Set up click listeners
        btnEditProfile.setOnClickListener(v -> showEditProfileDialog());
        btnSettings.setOnClickListener(v -> showSettings());
        btnLogout.setOnClickListener(v -> logout());

        // Load user data
        loadUserData();

        return view;
    }

    private void loadUserData() {
        // TODO: Load user data from repository
        // For now, using placeholder data
        tvName.setText("John Doe");
        tvLevel.setText("Intermediate");
        tvProgress.setText("75% Complete");
        progressBar.setProgress(75);
    }

    private void showEditProfileDialog() {
        // TODO: Show edit profile dialog
    }

    private void showSettings() {
        // TODO: Navigate to settings
    }

    private void logout() {
        // TODO: Implement logout functionality
        requireActivity().finish();
    }
}
