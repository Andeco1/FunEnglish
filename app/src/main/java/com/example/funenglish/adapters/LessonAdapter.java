package com.example.funenglish.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.funenglish.R;
import com.example.funenglish.models.Lesson;
import java.util.List;

public class LessonAdapter extends RecyclerView.Adapter<LessonAdapter.ViewHolder> {
    private List<Lesson> lessons;
    private OnItemClick listener;

    public interface OnItemClick {
        void onClick(Lesson lesson);
    }

    public LessonAdapter(List<Lesson> lessons, OnItemClick listener) {
        this.lessons = lessons;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_lesson, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Lesson lesson = lessons.get(position);
        holder.titleView.setText(lesson.title);
        holder.levelView.setText(lesson.level);
        holder.itemView.setOnClickListener(v -> listener.onClick(lesson));
    }

    @Override
    public int getItemCount() {
        return lessons.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleView;
        TextView levelView;

        ViewHolder(View view) {
            super(view);
            titleView = view.findViewById(R.id.tvLessonTitle);
            levelView = view.findViewById(R.id.tvLessonLevel);
        }
    }
} 