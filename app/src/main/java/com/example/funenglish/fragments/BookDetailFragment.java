package com.example.funenglish.fragments;

import android.os.Bundle; import android.view.*; import androidx.fragment.app.Fragment; import android.widget.TextView;
public class BookDetailFragment extends Fragment {
    static final String ARG="id"; int id;
    public static BookDetailFragment newInstance(int i){BookDetailFragment f=new BookDetailFragment();Bundle a=new Bundle();a.putInt(ARG,i);f.setArguments(a);return f;}
    @Override public void onCreate(Bundle b){super.onCreate(b);id=getArguments().getInt(ARG);}
    @Override public View onCreateView(LayoutInflater i,ViewGroup c,Bundle b){
        View v=i.inflate(R.layout.fragment_book_detail,c,false);
        ((TextView)v.findViewById(R.id.tvBookContent)).setText(DataRepository.getBook(id).text);
        v.findViewById(R.id.btnCloseBook).setOnClickListener(x->getParentFragmentManager().popBackStack());
        return v;
    }
}