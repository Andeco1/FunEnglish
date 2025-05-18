package com.example.funenglish.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.funenglish.R;
import com.example.funenglish.models.Book;
import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {
    private List<Book> books;
    private OnClick listener;

    public interface OnClick {
        void onClick(Book book);
    }

    public BookAdapter(List<Book> books, OnClick listener) {
        this.books = books;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_book, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Book book = books.get(position);
        holder.titleView.setText(book.title);
        holder.authorView.setText(book.author);
        holder.levelView.setText(book.level);
        holder.itemView.setOnClickListener(v -> listener.onClick(book));
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleView;
        TextView authorView;
        TextView levelView;

        ViewHolder(View view) {
            super(view);
            titleView = view.findViewById(R.id.tvBookTitle);
            authorView = view.findViewById(R.id.tvBookAuthor);
            levelView = view.findViewById(R.id.tvBookLevel);
        }
    }
} 