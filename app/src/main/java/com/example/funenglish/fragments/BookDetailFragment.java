package com.example.funenglish.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.example.funenglish.R;
import com.example.funenglish.database.DataRepository;

public class BookDetailFragment extends Fragment {
    private int id;
    private DataRepository repository;

    public static BookDetailFragment newInstance(int id) {
        BookDetailFragment fragment = new BookDetailFragment();
        fragment.id = id;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater i, ViewGroup c, Bundle b) {
        View v = i.inflate(R.layout.fragment_book_detail, c, false);
        repository = DataRepository.getInstance(requireContext());
        ((TextView)v.findViewById(R.id.tvBookContent)).setText(repository.getBook(id).text);
        v.findViewById(R.id.btnCloseBook).setOnClickListener(x -> getParentFragmentManager().popBackStack());
        return v;
    }
}