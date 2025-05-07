package com.example.funenglish.fragments;

import android.os.Bundle; import android.view.*; import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.*; import java.util.*;
public class TestsFragment extends Fragment implements TestAdapter.OnClick {
    RecyclerView rv; TestAdapter adapter;
    @Override public View onCreateView(LayoutInflater i,ViewGroup c,Bundle b){
        View v=i.inflate(R.layout.fragment_tests,c,false);
        rv=v.findViewById(R.id.rvTests);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter=new TestAdapter(DataRepository.getTests(),this);
        rv.setAdapter(adapter);
        return v;
    }
    @Override public void onClick(Test t){
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, TestDetailFragment.newInstance(t.id))
                .addToBackStack(null).commit();
    }
}
