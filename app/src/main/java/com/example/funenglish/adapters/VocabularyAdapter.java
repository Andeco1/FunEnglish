package com.example.funenglish.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.funenglish.R;
import com.example.funenglish.models.Word;
import java.util.List;

public class VocabularyAdapter extends RecyclerView.Adapter<VocabularyAdapter.ViewHolder> {
    private List<Word> words;
    private OnItemClick listener;

    public interface OnItemClick {
        void onClick(Word word);
    }

    public VocabularyAdapter(List<Word> words, OnItemClick listener) {
        this.words = words;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_word, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Word word = words.get(position);
        holder.wordText.setText(word.word);
        holder.translationText.setText(word.translation);
        holder.exampleText.setText(word.example);
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onClick(word);
            }
        });
    }

    @Override
    public int getItemCount() {
        return words != null ? words.size() : 0;
    }

    public void updateWords(List<Word> newWords) {
        this.words = newWords;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView wordText;
        TextView translationText;
        TextView exampleText;

        ViewHolder(View itemView) {
            super(itemView);
            wordText = itemView.findViewById(R.id.wordText);
            translationText = itemView.findViewById(R.id.translationText);
            exampleText = itemView.findViewById(R.id.exampleText);
        }
    }
} 