package com.example.englishfun.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.englishfun.R;
import com.example.englishfun.database.models.Test;

import java.util.List;

public class TestAdapter extends RecyclerView.Adapter<TestAdapter.TestViewHolder> {

    public interface OnTestClickListener {
        void onTestClick(Test test);
    }

    private final List<Test> tests;
    private final OnTestClickListener listener;

    public TestAdapter(List<Test> tests, OnTestClickListener listener) {
        this.tests = tests;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_test, parent, false);
        return new TestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TestViewHolder holder, int position) {
        Test test = tests.get(position);
        holder.title.setText(test.getTitle());
        holder.level.setText(test.getLevel());
        holder.questionCount.setText(String.format("%d score", test.getQuestionScore()));

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onTestClick(test);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tests.size();
    }

    static class TestViewHolder extends RecyclerView.ViewHolder {
        TextView title, level, questionCount;

        TestViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.testTitle);
            level = itemView.findViewById(R.id.testLevel);
            questionCount = itemView.findViewById(R.id.questionCount);
        }
    }
} 