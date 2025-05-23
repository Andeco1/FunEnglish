package com.example.englishfun.ui.test;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.Navigation;

import com.example.englishfun.R;
import com.example.englishfun.database.DataRepository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TestResultsDialog extends DialogFragment {
    private static final String ARG_CORRECT_ANSWERS = "correct_answers";
    private static final String ARG_TOTAL_QUESTIONS = "total_questions";
    private static final String ARG_TEST_ID = "test_id";

    public static TestResultsDialog newInstance(int correctAnswers, int totalQuestions, int testId) {
        TestResultsDialog dialog = new TestResultsDialog();
        Bundle args = new Bundle();
        args.putInt(ARG_CORRECT_ANSWERS, correctAnswers);
        args.putInt(ARG_TOTAL_QUESTIONS, totalQuestions);
        args.putInt(ARG_TEST_ID, testId);
        dialog.setArguments(args);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_test_results, container, false);

        Bundle args = getArguments();
        if (args != null) {
            int correctAnswers = args.getInt(ARG_CORRECT_ANSWERS);
            int totalQuestions = args.getInt(ARG_TOTAL_QUESTIONS);
            int testId = args.getInt(ARG_TEST_ID);

            TextView scoreText = view.findViewById(R.id.scoreText);
            TextView percentageText = view.findViewById(R.id.percentageText);
            Button finishButton = view.findViewById(R.id.finishButton);

            // Calculate percentage
            double percentage = (correctAnswers * 100.0) / totalQuestions;
            
            // Update UI
            scoreText.setText(String.format(Locale.getDefault(), 
                "You got %d out of %d questions correct", correctAnswers, totalQuestions));
            percentageText.setText(String.format(Locale.getDefault(), 
                "Score: %.1f%%", percentage));

            // Save test result
            saveTestResult(testId, correctAnswers, totalQuestions);

            // Set up finish button
            finishButton.setOnClickListener(v -> {
                dismiss();
                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main)
                    .navigate(R.id.navigation_dashboard);
            });
        }

        return view;
    }

    private void saveTestResult(int testId, int correctAnswers, int totalQuestions) {
        DataRepository repository = DataRepository.getInstance(requireContext());
        String currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            .format(new Date());
        
        // Update test entity with new score
        repository.getDatabase().testDao().updateTestScore(
            testId,
            correctAnswers,
            currentDate
        );
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            );
        }
    }
} 