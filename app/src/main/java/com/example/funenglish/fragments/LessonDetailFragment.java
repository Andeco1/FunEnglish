package com.example.funenglish.fragments;

import android.os.Bundle; import android.view.*; import androidx.fragment.app.Fragment;
import android.widget.TextView;
public class LessonDetailFragment extends Fragment {
    static final String ARG="id"; int id;
    public static LessonDetailFragment newInstance(int i){LessonDetailFragment f=new LessonDetailFragment();Bundle a=new Bundle();a.putInt(ARG,i);f.setArguments(a);return f;}
    @Override public void onCreate(Bundle b){super.onCreate(b);id=getArguments().getInt(ARG);}
    @Override public View onCreateView(LayoutInflater i,ViewGroup c,Bundle b){
        View v=i.inflate(R.layout.fragment_lesson_detail,c,false);
        TextView tv=v.findViewById(R.id.tvLessonContent);
        tv.setText(DataRepository.getLesson(id).content);
        v.findViewById(R.id.btnCloseLesson).setOnClickListener(x->getParentFragmentManager().popBackStack());
        return v;
    }
}