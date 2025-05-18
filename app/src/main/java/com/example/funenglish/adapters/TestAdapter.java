package com.example.funenglish.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.funenglish.R;
import com.example.funenglish.models.Test;
import java.util.List;

public class TestAdapter extends RecyclerView.Adapter<TestAdapter.ViewHolder> {
    private List<Test> tests;
    private OnClick listener;

    public interface OnClick {
        void onClick(Test test);
    }

    public TestAdapter(List<Test> tests, OnClick listener) {
        this.tests = tests;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_test, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Test test = tests.get(position);
        holder.titleView.setText(test.title);
        holder.descriptionView.setText(test.description);
        holder.questionsView.setText(test.questionsCount + " questions");
        holder.itemView.setOnClickListener(v -> listener.onClick(test));
    }

    @Override
    public int getItemCount() {
        return tests.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleView;
        TextView descriptionView;
        TextView questionsView;

        ViewHolder(View view) {
            super(view);
            titleView = view.findViewById(R.id.tvTestTitle);
            descriptionView = view.findViewById(R.id.tvTestDescription);
            questionsView = view.findViewById(R.id.tvTestQuestions);
        }
    }
} 