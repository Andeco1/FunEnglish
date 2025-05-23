package com.example.englishfun.ui.test;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.englishfun.R;
import com.example.englishfun.database.DataRepository;
import com.example.englishfun.database.entities.QuestionEntity;
import com.example.englishfun.database.entities.QuestionOptionEntity;
import com.example.englishfun.databinding.FragmentTestBinding;

import java.util.ArrayList;
import java.util.List;

public class TestFragment extends Fragment {
    private FragmentTestBinding binding;
    private DataRepository repository;
    private int testId;
    private List<QuestionEntity> questions;
    private List<QuestionOptionEntity> currentOptions;
    private int currentQuestionIndex = 0;
    private int correctAnswers = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            testId = getArguments().getInt("test_id");
        }
        repository = DataRepository.getInstance(requireContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentTestBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // Load questions for this test
        questions = repository.getDatabase().questionDao().getQuestionsByTestId(testId);
        if (questions.isEmpty()) {
            Toast.makeText(requireContext(), "No questions found for this test", Toast.LENGTH_SHORT).show();
            Navigation.findNavController(view).navigateUp();
            return;
        }

        setupQuestion();
        setupListeners();
    }

    private void setupQuestion() {
        if (currentQuestionIndex >= questions.size()) {
            showResults();
            return;
        }

        QuestionEntity currentQuestion = questions.get(currentQuestionIndex);
        binding.questionNumberText.setText(String.format("Question %d of %d", 
            currentQuestionIndex + 1, questions.size()));
        binding.questionText.setText(currentQuestion.questionText);

        // Load options for current question
        currentOptions = repository.getDatabase().questionOptionDao()
            .getByQuestionId(currentQuestion.question_id);

        // Clear previous selection
        binding.optionsRadioGroup.clearCheck();
        binding.feedbackText.setVisibility(View.GONE);
        binding.nextButton.setVisibility(View.GONE);
        binding.answerButton.setVisibility(View.VISIBLE);
        binding.answerButton.setText(currentQuestionIndex == questions.size() - 1 ? "Finish" : "Answer");

        // Set up radio buttons
        List<RadioButton> radioButtons = new ArrayList<>();
        radioButtons.add(binding.option1);
        radioButtons.add(binding.option2);
        radioButtons.add(binding.option3);

        for (int i = 0; i < currentOptions.size() && i < radioButtons.size(); i++) {
            QuestionOptionEntity option = currentOptions.get(i);
            RadioButton radioButton = radioButtons.get(i);
            radioButton.setText(option.optionText);
            radioButton.setTag(option.optionId);
            radioButton.setVisibility(View.VISIBLE);
        }

        // Hide unused radio buttons
        for (int i = currentOptions.size(); i < radioButtons.size(); i++) {
            radioButtons.get(i).setVisibility(View.GONE);
        }
    }

    private void setupListeners() {
        binding.answerButton.setOnClickListener(v -> {
            int selectedId = binding.optionsRadioGroup.getCheckedRadioButtonId();
            if (selectedId == -1) {
                Toast.makeText(requireContext(), "Please select an answer", Toast.LENGTH_SHORT).show();
                return;
            }

            RadioButton selectedButton = binding.getRoot().findViewById(selectedId);
            int selectedOptionId = (int) selectedButton.getTag();
            QuestionEntity currentQuestion = questions.get(currentQuestionIndex);

            // Show feedback
            binding.feedbackText.setVisibility(View.VISIBLE);

            // Disable radio buttons and show next button
            binding.optionsRadioGroup.setEnabled(false);
            binding.answerButton.setVisibility(View.GONE);
            binding.nextButton.setVisibility(View.VISIBLE);
        });

        binding.nextButton.setOnClickListener(v -> {
            currentQuestionIndex++;
            binding.optionsRadioGroup.setEnabled(true);
            setupQuestion();
        });
    }

    private String getCorrectOptionText(int correctOptionId) {
        for (QuestionOptionEntity option : currentOptions) {
            if (option.optionId == correctOptionId) {
                return option.optionText;
            }
        }
        return "";
    }

    private void showResults() {
        // Create and show results dialog
        TestResultsDialog dialog = TestResultsDialog.newInstance(
            correctAnswers,
            questions.size(),
            testId
        );
        dialog.show(getChildFragmentManager(), "TestResults");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
} 