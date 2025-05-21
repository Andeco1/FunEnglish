package com.example.englishfun.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.englishfun.R;
import com.example.englishfun.database.models.Lesson;

import java.util.List;

public class LessonAdapter extends RecyclerView.Adapter<LessonAdapter.LessonViewHolder> {

    public interface OnLessonClickListener {
        void onLessonClick(Lesson lesson);
    }

    private final List<Lesson> lessons;
    private final OnLessonClickListener listener;

    public LessonAdapter(List<Lesson> lessons, OnLessonClickListener listener) {
        this.lessons = lessons;
        this.listener = listener;
    }

    @NonNull
    @Override
    public LessonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_lesson, parent, false);
        return new LessonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LessonViewHolder holder, int position) {
        Lesson lesson = lessons.get(position);
        holder.title.setText(lesson.getTitle());
        holder.level.setText(lesson.getLevel());
        
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onLessonClick(lesson);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lessons.size();
    }

    static class LessonViewHolder extends RecyclerView.ViewHolder {
        TextView title, level;

        LessonViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.lessonTitle);
            level = itemView.findViewById(R.id.lessonLevel);
        }
    }
}