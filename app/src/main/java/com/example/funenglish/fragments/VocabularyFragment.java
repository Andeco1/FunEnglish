package com.example.funenglish.fragments;

import android.os.Bundle; import android.view.*; import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.*; import android.widget.*; import java.util.*;
public class VocabularyFragment extends Fragment implements VocabularyAdapter.OnClick {
    Spinner spinner; RecyclerView rv; Button btnTest;
    List<String> groups;
    @Override public View onCreateView(LayoutInflater i,ViewGroup c,Bundle b){
        View v=i.inflate(R.layout.fragment_vocabulary,c,false);
        spinner=v.findViewById(R.id.spinnerGroups);
        groups=new ArrayList<>(DataRepository.getWordGroupNames());
        spinner.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, groups));
        rv=v.findViewById(R.id.rvWords);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?> p,View view,int pos,long id){
                rv.setAdapter(new VocabularyAdapter(DataRepository.getWords(groups.get(pos)), VocabularyFragment.this));
            }
            public void onNothingSelected(AdapterView<?> p){}
        });
        btnTest=v.findViewById(R.id.btnTestVocab);
        btnTest.setOnClickListener(x->{
            // TODO: открыть тест по текущей группе
        });
        return v;
    }
    @Override public void onClick(Word w){/*можно показать детали слова*/}
}