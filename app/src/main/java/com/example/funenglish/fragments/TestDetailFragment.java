package com.example.funenglish.fragments;

import android.os.Bundle; import android.view.*; import androidx.fragment.app.Fragment;
import android.widget.*;

import com.example.funenglish.R;

public class TestDetailFragment extends Fragment {
    static final String ARG="id"; int id;
    public static TestDetailFragment newInstance(int i){TestDetailFragment f=new TestDetailFragment();Bundle a=new Bundle();a.putInt(ARG,i);f.setArguments(a);return f;}
    @Override public void onCreate(Bundle b){super.onCreate(b);id=getArguments().getInt(ARG);}
    @Override public View onCreateView(LayoutInflater i,ViewGroup c,Bundle b){
        View v=i.inflate(R.layout.fragment_test_detail,c,false);
        // TODO: загрузка вопросов по id
        v.findViewById(R.id.btnCloseTest).setOnClickListener(x->getParentFragmentManager().popBackStack());
        return v;
    }
}