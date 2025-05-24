package com.example.englishfun.ui.notifications;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.englishfun.R;
import com.example.englishfun.data.AppDatabase;
import com.example.englishfun.data.entity.Book;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NotificationsFragment extends Fragment {
    private static final int PICK_TXT_FILE = 1;
    private List<Book> books = new ArrayList<>();
    private BookAdapter adapter;
    private ActivityResultLauncher<Intent> filePickerLauncher;
    private AppDatabase database;
    private ExecutorService executorService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        database = AppDatabase.getDatabase(requireContext());
        executorService = Executors.newSingleThreadExecutor();
        
        filePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri uri = result.getData().getData();
                    if (uri != null) {
                        saveFile(uri);
                    }
                }
            });

        // Load books from database
        loadBooks();
    }

    private void loadBooks() {
        LiveData<List<Book>> booksLiveData = database.bookDao().getAllBooks();
        booksLiveData.observe(this, booksList -> {
            books.clear();
            books.addAll(booksList);
            adapter.notifyDataSetChanged();
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        
        RecyclerView recyclerView = root.findViewById(R.id.books_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        
        adapter = new BookAdapter(books, this::openBook);
        recyclerView.setAdapter(adapter);
        
        FloatingActionButton fab = root.findViewById(R.id.fab_add_book);
        fab.setOnClickListener(v -> openFilePicker());
        
        return root;
    }

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/plain");
        filePickerLauncher.launch(intent);
    }

    private void saveFile(Uri uri) {
        try {
            File booksDir = new File(requireContext().getFilesDir(), "books");
            if (!booksDir.exists()) {
                booksDir.mkdirs();
            }

            String fileName = getFileName(uri);
            File destFile = new File(booksDir, fileName);

            try (InputStream is = requireContext().getContentResolver().openInputStream(uri);
                 FileOutputStream fos = new FileOutputStream(destFile)) {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, length);
                }
            }

            // Save to database
            Book book = new Book(fileName, destFile.getAbsolutePath());
            executorService.execute(() -> database.bookDao().insert(book));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            try (android.database.Cursor cursor = requireContext().getContentResolver()
                    .query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME));
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf(File.separator);
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    private void openBook(Book book) {
        ReadBookFragment fragment = ReadBookFragment.newInstance(book.getFilePath());
        requireActivity().getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.nav_host_fragment_activity_main, fragment)
            .addToBackStack(null)
            .commit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }

    private static class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {
        private List<Book> books;
        private OnBookClickListener listener;

        interface OnBookClickListener {
            void onBookClick(Book book);
        }

        BookAdapter(List<Book> books, OnBookClickListener listener) {
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
            holder.titleView.setText(book.getTitle());
            holder.itemView.setOnClickListener(v -> listener.onBookClick(book));
        }

        @Override
        public int getItemCount() {
            return books.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            TextView titleView;

            ViewHolder(View view) {
                super(view);
                titleView = view.findViewById(R.id.book_title);
            }
        }
    }
}