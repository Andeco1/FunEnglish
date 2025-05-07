package com.example.funenglish.fragments;

import android.os.Bundle; import android.view.*; import androidx.fragment.app.Fragment;
public class ProfileFragment extends Fragment {
    @Override public View onCreateView(LayoutInflater i,ViewGroup c,Bundle b){
        return i.inflate(R.layout.fragment_profile,c,false);
    }
}
