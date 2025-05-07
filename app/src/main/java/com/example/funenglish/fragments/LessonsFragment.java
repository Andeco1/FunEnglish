package com.example.funenglish.fragments;
;
import android.os.Bundle; import android.view.*; import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.*; import java.util.*;
public class LessonsFragment extends Fragment implements LessonAdapter.OnItemClick {
    RecyclerView rv; LessonAdapter adapter;
    @Override public View onCreateView(LayoutInflater i,ViewGroup c,Bundle b){
        View v=i.inflate(R.layout.fragment_lessons,c,false);
        rv=v.findViewById(R.id.rvLessons);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter=new LessonAdapter(DataRepository.getLessons(),this);
        rv.setAdapter(adapter);
        return v;
    }
    @Override public void onClick(Lesson l){
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, LessonDetailFragment.newInstance(l.id))
                .addToBackStack(null).commit();
    }
}