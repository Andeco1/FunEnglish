package com.example.funenglish.fragments;

import android.os.Bundle; import android.view.*; import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.*;
public class ReadingFragment extends Fragment implements BookAdapter.OnClick {
    RecyclerView rv; BookAdapter adapter;
    @Override public View onCreateView(LayoutInflater i,ViewGroup c,Bundle b){
        View v=i.inflate(R.layout.fragment_reading,c,false);
        rv=v.findViewById(R.id.rvBooks);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter=new BookAdapter(DataRepository.getBooks(),this);
        rv.setAdapter(adapter);
        return v;
    }
    @Override public void onClick(Book book){
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, BookDetailFragment.newInstance(book.id))
                .addToBackStack(null).commit();
    }
}