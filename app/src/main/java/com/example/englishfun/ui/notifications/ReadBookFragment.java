package com.example.englishfun.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.englishfun.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class ReadBookFragment extends Fragment {
    private static final String ARG_FILE_PATH = "file_path";

    public static ReadBookFragment newInstance(String filePath) {
        ReadBookFragment fragment = new ReadBookFragment();
        Bundle args = new Bundle();
        args.putString(ARG_FILE_PATH, filePath);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_read_book, container, false);
        TextView contentView = root.findViewById(R.id.book_content);

        if (getArguments() != null) {
            String filePath = getArguments().getString(ARG_FILE_PATH);
            if (filePath != null) {
                try {
                    String content = readFile(filePath);
                    contentView.setText(content);
                } catch (IOException e) {
                    contentView.setText("Error reading file: " + e.getMessage());
                }
            }
        }

        return root;
    }

    private String readFile(String filePath) throws IOException {
        StringBuilder content = new StringBuilder();
        File file = new File(filePath);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        return content.toString();
    }
} 